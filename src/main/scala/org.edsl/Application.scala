package org.edsl

import java.io._

import org.edsl.DSL._

object Application {

  // int, long, bool, string, date, float

  def main(args: Array[String]): Unit = {

    'foo namespace {
      'user1 struct {
        'username as 'string
        'address as 'string
        's as 'Long
        'af as 'int
      }
      'bar namespace {
        'user2 struct {
          'username as 'string
          'address as 'string
        }
      }
    }

    // kikos_poxos => KikosPoxos, kikosPoxos, getKikosPoxos()

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
      //println("Path start "+currentPath+" ")

      currentPackage = currentPackage :+ namespace.name
      //  println("Package start "+currentPackage+" ")
      namespace.namespaces().foreach { generate }
      namespace.structures().foreach { generate }

      currentPath = currentPath.getParentFile
      //    println("Path end "+currentPath+" ")
      currentPackage = currentPackage diff List(namespace.name)
      //      println("Package end "+currentPackage+" ")
    }

    def generate(structure: Structure): Unit = {
      val writer = new PrintWriter(new File(currentPath, (structure.name + ".java")))
      writer.write(s"package ${currentPackage.mkString(".")};\n")
      writer.write(s"\npublic class " + structure.name.capitalize + " {\n")
      for (f <- structure.fields()) {
        writer.write("      private " + f.datatype + " " + f.name.toString() + ";\n")
      }
      writer.close()
    }
  }
}
