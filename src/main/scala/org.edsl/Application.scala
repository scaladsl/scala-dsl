package org.edsl

import java.io._
import org.edsl.DSL._

// > java -jar sdlc.jar -sdl /home/kikos/my-service.sdl -java /home/kikos/java.gen -out /home/kikos/output/java
// > java -jar sdlc.jar -sdl /home/kikos/my-service.sdl -java /home/kikos/ruby.gen -out /home/kikos/output/ruby

// > sdlp /home/kikos/my-service.sdl | sdlg -out /home/kikos/output/ruby /home/kikos/ruby.gen 

//IDL
// BeginNamespace "foo"
// BeginStruct "user"
// Field Required Int "age"
// EndStruct
// EndNamespace

// Compiler - SDLC
object Application {

  def main(args: Array[String]): Unit = {

    // SDL
    'services namespace {

      'user struct {
        'username repeated 'string
        'forename optional 'string
        'surname  optional 'string
      }

      'authentication namespace {

        'login_request struct {
          'username required 'string
          'password required 'string
        }
 
        'login_reply struct {
          'user1 optional 'login_request
          //'foo::'services::'user // X => Array[Identifier]
        }
      }
    }

    val root = Context.current.asInstanceOf[Namespace]
    val java = new Java("/home/nqq/dev/entity-dsl/temp")
    root.namespaces().foreach { ns => java.generate(ns) }
    root.structures().foreach { st => java.generate(st) }
  }

  // Generator
  class Java(val outputPath: String) {

    private val NEWLINE = "\n"
    private val INDENT = "  "
    private var indentLevel = 0

    var currentPath = new File(outputPath)
    var currentPackage = List[String]()

    def generate(namespace: Namespace): Unit = {
      currentPath = new File(currentPath, namespace.id.name())
      currentPath.mkdir()
      currentPackage = currentPackage :+ namespace.id.name()
      namespace.namespaces().foreach { generate }
      namespace.structures().foreach { generate }
      currentPath = currentPath.getParentFile
      currentPackage = currentPackage diff List(namespace.id.name())
    }

    var writer: PrintWriter = null
    def source(name: Identifier)(body: => Unit): Unit = {
      writer = new PrintWriter(new File(currentPath, (name.toPascal + ".java")))
      body
      writer.close
    }

    def block(str: String)(body: => Unit): Unit = {
      writer.write(indentation)
      writer.write(str)
      writer.write(" {")
      writer.write(NEWLINE)

      indentLevel += 1
      body
      indentLevel -= 1

      writer.write(indentation)
      writer.write("}")
      writer.write(NEWLINE)
    }

    def ln(str: String = ""): Unit = {
      writer.write(indentation)
      writer.write(str)
      writer.write(NEWLINE)
    }

    def asJava(datatype: Datatype): String = {
      if ( datatype == null )
        throw new IllegalArgumentException("Datatype is not specified.");
      datatype match {
        case Integer => "java.util.Integer"
        case String => "java.String"
        case Boolean => "java.util.Boolean"
        case Float => "java.util.Double"
        case Date => "java.util.Date"
        case _: Enumeration => datatype.path.map(e => if ( e.isInstanceOf[Namespace] ) e.id.toCamel else e.id.toPascal).mkString(".")
        case _: Structure => datatype.path.map(e => if ( e.isInstanceOf[Namespace] ) e.id.toCamel else e.id.toPascal).mkString(".")
        case _ => throw new IllegalArgumentException(s"Invalid datatype: ${datatype.getClass}");
      }
    }

    def asRequired(datatype: Datatype): String = asJava(datatype)
    def asOptional(datatype: Datatype): String = s"java.util.Optional<${asJava(datatype)}>"
    def asRepeated(datatype: Datatype): String = s"java.util.List<${asJava(datatype)}>"

    def fieldType(field: Field): String = {
      field.modifier match {
        case  Modifier.REQ => asRequired(field.datatype)
        case  Modifier.OPT => asOptional(field.datatype)
        case  Modifier.REP => asRepeated(field.datatype)
      }
    }

    def doc(comment: String): Unit = {
      ln()
      if (comment != null && comment.length() > 0) {
        var lines = comment.replaceAll("\\n\\s*!", "\n").split("\n")
        if (lines(0).trim == "")
          lines = lines.slice(1, lines.length)

        ln("/**")
        lines foreach { s => ln(" * " + s) }
        ln(" */")
      }
    }

    def generate(structure: Structure): Unit = {
      source(structure.id) {
        ln(s"package ${currentPackage.mkString(".")};")

        val structName = structure.id.toPascal
        doc(structure.comment)
        block(s"public class " + structName) {
          structure.fields.foreach(f =>
            ln(s"private ${fieldType(f)} ${f.id.toCamel()};"))

          structure.fields foreach { f =>
            val aname = f.id.toCamel
            val fname = f.id.toPascal
            val ftype = fieldType(f)
            doc(f.comment)
            block(s"public $ftype get$fname($ftype $aname)") {
              ln(s"return $aname;")
            }

            if ( !f.isRepeated ) {
              doc(f.comment)
              block(s"public void set$fname($ftype $aname)") {
                block(s"if ($aname == null)") {
                  ln(s"""throw new IllegalArgumentException("$aname");""")
                }
                ln(s"this.$aname = $aname;")
              }
            }
          }
        }
      }
    }

    private def indentation: String = {
      INDENT * indentLevel
    }
  }
}
