package org.edsl

import java.io._
import org.edsl.DSL._

object Application {

  def main(args: Array[String]): Unit = {

    'foo namespace {

      !"my comment for Structur1"
      'user1 struct {
        !"my comment for Field1 "
        'username as 'string
        'address as 'string
      }

      'bar namespace {
        'user2 struct {
          'username2 as 'string
          !"my comment for Field 4"
          'address_a as 'string
        }
      }
    }

    val root = Context.current.asInstanceOf[Namespace]
    val java = new Java("/home/anahitm/dev/dsl-output")

    root.namespaces().foreach { ns => java.generate(ns) }
    root.structures().foreach { st => java.generate(st) }
  }

  class Java(val outputPath: String) {

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

    def toStandartJavaType(datatype: String): String = {
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
      writer.write(str)
      writer.write("\n")
      body
    }

    def ln(str: String): Unit = {
      writer.write(str)
      writer.write("\n")
    }

    def generate(structure: Structure): Unit = {
      source(structure.name) {
        ln(s"package ${currentPackage.mkString(".")};")
        val structName = structure.name.toPascal
        if (structure.comment.length() > 0) ln("\n//" + structure.comment)
        block(s"\npublic class " + structName + " {\n") {
          structure.fields foreach { f =>
            var fieldName = f.name.toCamel
            if (f.comment.length() > 0) ln("//" + f.comment)
            ln(" private " + toStandartJavaType(f.datatype) + " " + fieldName + ";")
          }
          structure.fields foreach { f =>
            var fieldName = f.name.toCamel
            ln(s"\n public void set$structName (" + toStandartJavaType(f.datatype) + s" $fieldName){")
            ln(s"   this.$fieldName = $fieldName;\n }")
          }
          structure.fields foreach { f =>
            var fieldName = f.name.toCamel
            var fieldType = toStandartJavaType(f.datatype)
            ln(s"\n public $fieldType get$structName (" + fieldType + s" $fieldName){")
            ln(s"   return $fieldName;\n }")
          }
          ln("\n}")
        }
      }
    }
  }
}

class StringEx(val s: String) {

  // PascalCase
  def toPascal: String = s.split("_").map(_.capitalize).mkString("")

  // camelCase
  def toCamel: String =
    if (s.length > 0)
      toPascal.substring(0, 1).toLowerCase + toPascal.substring(1)
    else
      s

}

class Comment(comment: String) {

  def unary_! = new Comment(comment)
  override def toString = comment

}

