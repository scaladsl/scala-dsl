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
          'user1 optional 'int 
          //'foo::'services::'user // X => Array[Identifier]
        }
      }
    }

    val root = Context.current.asInstanceOf[Namespace]
    val java = new Java("/home/anahitm/dev/dsl-output")
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

    /**
     *
     */
    def toJavaType(f: Field): String = {

      f.datatype.id.name() match {
        case "int" => f.modifier match {
          case  Modifier.REQ => "int"
          case  Modifier.REP => "List<Integer>"
          case  Modifier.OPT => "Integer"
        }
        case "string" => f.modifier match {
          case  Modifier.REQ => "String"
          case  Modifier.REP => "List<String>"
          case  Modifier.OPT => "String"
        }
        case "bool" => f.modifier match {
          case  Modifier.REQ => "boolean"
          case  Modifier.REP => "List<Boolean>"
          case  Modifier.OPT => "Boolean"
        }
        case "date" => f.modifier match {
          case  Modifier.REQ => "Date"
          case  Modifier.REP => "List<Date>"
          case  Modifier.OPT => "Date"
        }
        case "float" => f.modifier match {
          case  Modifier.REQ => "double"
          case  Modifier.REP => "List<Double>"
          case  Modifier.OPT => "Double"
        }
        case t => throw new IllegalArgumentException(s"Invalid DSL type: $t")
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
          structure.fields foreach { f =>
            var fieldName = f.id.toCamel()
            val ftype = toJavaType(f)
            ln(s"private $ftype $fieldName;")
          }

          structure.fields foreach { f =>
            val aname = f.id.toCamel
            val fname = f.id.toPascal
            val ftype = toJavaType(f)
            doc(f.comment)
            block(s"public $ftype get$fname($ftype $aname)") {
              ln(s"return $aname;")
            }

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

    private def indentation: String = {
      INDENT * indentLevel
    }
  }
}
