package org.edsl

import java.io._
import org.edsl.DSL._

object Application {

  def main(args: Array[String]): Unit = {

    var line = readLine()

    while(line != null){

      if(line.indexOf("end") != -1) { Symbol("end") end }
      println(s"generator: $line")


      if(line.indexOf("begin namespace") != -1){
        val name = Symbol(line.substring(line.indexOf("begin namespace") + "begin namespace".length + 2, line.length - 1))
        name namespace
      }

      if(line.indexOf("comment") != -1){
      val name = line.substring(line.indexOf("comment") + "comment".length + 2, line.length - 1)
        !name
        line = readLine()
      }

      if(line.indexOf("begin structure") != -1){
        val name = Symbol(line.substring(line.indexOf("begin structure") + "begin structure".length + 2, line.length - 1))
        name struct
      }

        if(line.indexOf("comment") != -1){
        val name = line.substring(line.indexOf("comment") + "comment".length + 2, line.length - 1)
          !name
          line = readLine()
        }

      if(line.indexOf("begin field") != -1){
        val fieldName = Symbol(line.substring(line.indexOf("begin field") + "begin field".length + 2, line.length - 1))
        line = readLine()
        if(line.indexOf("type") != -1){
          var fieldtype = Symbol(line.substring(line.indexOf("type") + "type".length + 4, line.length - 1))
          if(fieldtype.name.indexOf('.') != -1){
            var identities: List[Symbol] = Nil
            val a = fieldtype.name.split('.')
            for(i<-0 to a.length-1){
              identities = identities ::: List(Symbol(a(i)))
            }
            line = readLine()
            if(line.indexOf("modifier") != -1){
              val modifier = Symbol(line.substring(line.indexOf("modifier") + "modifier".length + 4, line.length - 1))
              if(modifier == 'required){
                fieldName required DSL.qualifiedIdentityToDatatype(new QualifiedIdentity(identities))
              }
              if(modifier == 'optional){
                fieldName optional DSL.qualifiedIdentityToDatatype(new QualifiedIdentity(identities))
              }
              if(modifier == 'repeated){
                fieldName repeated DSL.qualifiedIdentityToDatatype(new QualifiedIdentity(identities))
              }
            }
          }
          else{
            fieldtype = Symbol(line.substring(line.indexOf("type") + "type".length + 4, line.length - 1))
            line = readLine()
            if(line.indexOf("modifier") != -1){
              val modifier = Symbol(line.substring(line.indexOf("modifier") + "modifier".length + 4, line.length - 1))
              if(modifier == 'required){
                fieldName required fieldtype
              }
              if(modifier == 'optional){
                fieldName optional fieldtype
              }
              if(modifier == 'repeated){
                fieldName repeated fieldtype
              }
            }
          }
        }
       line = readLine()
      }

      if(line.indexOf("enum") != -1){
        val name = Symbol(line.substring(line.indexOf("enum") + "enum".length + 2, line.length - 1))
        name enum
      }

      if(line.indexOf("=") != -1){
        val ename = Symbol(line.substring(0, line.length-line.substring(line.indexOf("=") + "=".length, line.length).length-2))
        val value = line.substring(line.indexOf("=") + "=".length+2, line.length-1).toInt
        ename is value
      }
      
      line = readLine()
    }

     val root = Context.stackOfCurrent.top.asInstanceOf[Namespace]
     val java = new Java("/home/anahitm/dev/dsl-output")
     root.namespaces().foreach { ns => java.generate(ns) }
     root.structures().foreach { st => java.generate(st) }
     root.enumerations().foreach { en => java.generate(en) }

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
      namespace.enumerations().foreach { generate }
      currentPath = currentPath.getParentFile
      currentPackage = currentPackage diff List(namespace.id.name())
    }
    

    var writer: PrintWriter = null
    def source(name: Identity)(body: => Unit): Unit = {
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
      if (datatype == null)
        throw new IllegalArgumentException("Datatype is not specified.");
      datatype match {
        case Integer => "Integer"
        case String => "String"
        case Boolean => "Boolean"
        case Float => "Double"
        case Date => "Date"
        case _: Enumeration => datatype.path.map(e => if (e.isInstanceOf[Namespace]) e.id.toCamel else e.id.toPascal).mkString(".")
        case _: Structure => datatype.path.map(e => if (e.isInstanceOf[Namespace]) e.id.toCamel else e.id.toPascal).mkString(".")
        case _ => throw new IllegalArgumentException(s"Invalid datatype: ${datatype.getClass}");
      }
    }

    def asRequired(datatype: Datatype): String = asJava(datatype)
    def asOptional(datatype: Datatype): String = s"Optional<${asJava(datatype)}>"
    def asRepeated(datatype: Datatype): String = s"List<${asJava(datatype)}>"

    def fieldType(field: Field): String = {
      field.modifier match {
        case Modifier.REQ => asRequired(field.datatype)
        case Modifier.OPT => asOptional(field.datatype)
        case Modifier.REP => asRepeated(field.datatype)
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
        var mod: collection.immutable.Set[String] = Set()
        var types: collection.immutable.Set[String] = Set()
        structure.fields.foreach { f =>
          mod += f.modifier.id.name.capitalize
          if (f.datatype.isInstanceOf[Primitive]) types += asJava(f.datatype)
        }
        mod.foreach { f => if (f != "Required") ln(s"import java.util.$f") }
        types.foreach { f => if (f == "String") ln(s"import java.$f") else ln(s"import java.util.$f") }

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
            block(s"public ${ftype} get$fname($ftype $aname)") {
              ln(s"return $aname;")
            }

            if (!f.isRepeated) {
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

    def generate(enum: Enumeration): Unit = {
      source(enum.id) {
        ln(s"package ${currentPackage.mkString(".")};")

        val enumName = enum.id.toPascal
        doc(enum.comment)
        block(s"public enum " + enumName) {
          ln(enum.constants.map(e => s"${e.id.toUpper}(${e.value})").mkString(", ") + ";")
          ln(s"private int val;")
          ln()
          block(s"private PhoneNumber(int value)") {
            ln("val = value;")
          }
          ln()
          block("public int value()") {
            ln("return val;")
          }
          ln()
          block("static PhoneNumber fromValue(int value)") {
            block("switch ( value )") {
              enum.constants.foreach { e =>
                ln(s"case ${e.value}: ${e.id.toUpper}")
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
