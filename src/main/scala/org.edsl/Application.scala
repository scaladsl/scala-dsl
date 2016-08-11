package org.edsl

import java.io._
import org.edsl.DSL._

object Application {

  def main(args: Array[String]): Unit = {

    'foo namespace {
      !"""
      !my comment for Structur user1 
      !"""
      'user1 struct {
        !"""
        !my comment for Field username
        !"""
        'username repeated 'int
        'address repeated 'string
      }

      'bar namespace {
        'user2 struct {
          'username2 repeated 'string
          !"""
          !my comment for Field addressA
          !"""
          'address_a optional 'string
        }
      }
    }

    val root = Context.current.asInstanceOf[Namespace]
    val java = new Java("/home/anahitm/dev/dsl-output")

    root.namespaces().foreach { ns => java.generate(ns) }
    root.structures().foreach { st => java.generate(st) }
  }

  class Java(val outputPath: String) {

    private val NEWLINE = "\n"
    private val INDENT = "  "
    private var indentLevel = 0

    var currentPath = new File(outputPath)
    var currentPackage = List[String]()

    def generate(namespace: Namespace): Unit = {
      currentPath = new File(currentPath, namespace.name)
      currentPath.mkdir()
      currentPackage = currentPackage :+ namespace.name
      namespace.namespaces().foreach { generate }
      namespace.structures().foreach { generate }
      currentPath = currentPath.getParentFile
      currentPackage = currentPackage diff List(namespace.name)
    }

    def toJavaType(datatype: String): String = { 
      datatype match {
        case "int" => "int"
        case "long" => "long"
        case "bool" => "boolean"
        case "string" => "String"
        case "date" => "Date"
        case "float" => "double"
        case t => throw new IllegalArgumentException(s"Invalid DSL type: $t")
      }
    }

    var writer: PrintWriter = null
    def source(name: String)(body: => Unit): Unit = {
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
      source(structure.name) {
        ln(s"package ${currentPackage.mkString(".")};")
        val structName = structure.name.toPascal
        doc(structure.comment)
        block(s"public class " + structName) {
          structure.fields foreach { f =>
            var fieldName = f.name.toCamel
            ln(s"private ${toJavaType(f.datatype)} $fieldName;")
          }

          structure.fields foreach { f =>
            val aname = f.name.toCamel
            val fname = f.name.toPascal
            val ftype = toJavaType(f.datatype)

            doc(f.comment)
            block(s"public $ftype get$fname($ftype $aname)") {
              ln(s"return $aname;")
            }

            doc(f.comment)
            block(s"public void set$fname($ftype $aname)") {
              block(s"if ( $aname == null )") {
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
