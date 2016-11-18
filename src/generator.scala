package sgf

import java.text._
import java.io._
import scala.collection.mutable.ListBuffer
import scala.util.parsing.json._
import scala.collection.mutable.Stack

object GDSL {

  def generate(block: => Unit) {
    block

    val sb = new StringBuilder
    var line = readLine
    while(line != null) {
      sb.append(line)
      line = readLine
    }

    val obj = JSON.parseFull(sb.toString)
    val list = obj.get.asInstanceOf[List[Any]]

    on.ROOT = list.map(d => Entity.createEntity(d.asInstanceOf[ObjectT], null))
    on.ROOT.foreach(traverse(_))
  }

  type ObjectT = Map[String, Any]
  type ListT = List[ObjectT]

  object Entity {
    def createEntity(data: Map[String, Any], parent: Entity): Entity = {
      val (name, content) = (data.head._1, data.head._2.asInstanceOf[Map[String, Any]])
      content("prototype") match {
        case "namespace" => Namespace(name, content, parent)
        case "structure" => Structure(name, content, parent)
        case "enumeration" => Enumeration(name,content, parent)
        case "field" => Field(name, content, parent)
        case "constant" => Constant(name, content, parent)
      }
    }

    private var path = Array[String]()
  }

  class Entity(val name: String, val data: ObjectT, val parentEntity: Entity) {

    protected val attributes = (data filter { case (_, v) => !v.isInstanceOf[ObjectT] && !v.isInstanceOf[ListT] }).asInstanceOf[Map[String, String]]
    val children = (data getOrElse ("children", List())).asInstanceOf[ListT].map(Entity.createEntity(_, this))
    val comment = this("comment", "")
    private var parentsOfStruct = Array[String](name.capitalize)

    def apply(name: String): String = (attributes get name).get

    def apply(name: String, defaultVal: String): String = attributes getOrElse (name, defaultVal)

    def find(name: String): Entity = {
      val parts = name.split("::")
      children.find( _.name == parts(0) ) match {
        case None => null
        case Some(e) => {
          if ( parts.size == 1)
            e
          else
            e.find(parts.tail.mkString("::"))
        }
      }
    }

    def fullPath(): String = {
      Entity.path = Array[String]()
      findPackRecursively
      Entity.path.reverse.mkString(".")
    }

    def fullPathDirectory(): String = fullPath.replace(".", "/")

    def findPackRecursively(): Unit = {
      if(parentEntity.isInstanceOf[Namespace] && parentEntity!= null) {
        Entity.path = Entity.path :+ parentEntity.name
        parentEntity.findPackRecursively
      }
    }
  }

  case class Namespace(_id: String, _data: ObjectT, _parent: Entity) extends Entity(_id, _data, _parent) {
    def namespaces = children.filter(_.isInstanceOf[Namespace]).map(_.asInstanceOf[Namespace])
    def structures = children.filter(_.isInstanceOf[Structure]).map(_.asInstanceOf[Structure])
    def enumerations = children.filter(_.isInstanceOf[Enumeration]).map(_.asInstanceOf[Enumeration])
  }

  abstract class Datatype(_id: String, _data: ObjectT, _parent: Entity) extends Entity(_id, _data, _parent)

  case class StringDatatype() extends Datatype("string", Map(), null)
  case class IntDatatype() extends Datatype("int", Map(), null)
  case class FloatDatatype() extends Datatype("float", Map(), null)
  case class BoolDatatype() extends Datatype("bool", Map(), null)

  case class Structure(_id: String, _data: ObjectT, _parent: Entity) extends Datatype(_id, _data, _parent) {
    def fields = children.filter(_.isInstanceOf[Field]).map(_.asInstanceOf[Field])
  }

  case class Enumeration(_id: String, _data: ObjectT, _parent: Entity) extends Datatype(_id, _data, _parent) {
    def constants = children.filter(_.isInstanceOf[Constant]).map(_.asInstanceOf[Constant])
  }

  case class Field(_id: String, _data: ObjectT, _parent: Entity) extends Entity(_id, _data, _parent) {
    def modifier = this("modifier")

    def datatype: Datatype = {
      val dt = this("datatype")

      var result: Datatype = null

      result = dt match {
        case "string" => StringDatatype()
        case "int" => IntDatatype()
        case "float" => FloatDatatype()
        case "bool" => BoolDatatype()
        case _ => null
      }

      if ( result == null ) {
        val parts = this("datatype").split("::")
        result = on.ROOT.find(_.name == parts(0)) match {
          case Some(e) => 
            // e.find(parts.tail.mkString("::")).asInstanceOf[Datatype]
            if ( parts.size == 1 )
              e.asInstanceOf[Datatype]
            else
              e.find(parts.tail.mkString("::")).asInstanceOf[Datatype]
            
          case None => null
        }
      }

      if ( result == null )
        throw new RuntimeException(s"datatype not found: ${this("datatype")}");

      result
    }

  }

  case class Constant(_id: String, _data: ObjectT, _parent: Entity) extends Entity(_id, _data, _parent) {
    val value = this("value")
  }

  object on {
    var onNamespace: Namespace => Unit = null
    var onStructure: Structure => Unit = null
    var onEnumeration: Enumeration => Unit = null

    var ROOT: List[Entity] = List()

    def NAMESPACE(generate: Namespace => Unit): Unit = onNamespace  = generate

    def STRUCTURE(generate: Structure => Unit): Unit = onStructure = generate

    def ENUMERATION(generate: Enumeration => Unit): Unit = onEnumeration = generate
  }

  private def traverse(current: Entity): Unit = {
    current match {
      case n: Namespace => on.onNamespace(n)
      case s: Structure => on.onStructure(s)
      case e: Enumeration => on.onEnumeration(e)
      case _ => // ignore
    }
    current.children.foreach(traverse(_))
  }

  object Lang {
    val NEWLINE = "\n"
    val INDENT = "  "

    private var indentLevel = 0
    private var writer: PrintWriter = null

    def source(outputPath: String, name: String)(body: => Unit): Unit = {
      new File(outputPath).mkdir()
      writer = new PrintWriter(new File(outputPath, name))
      body
      writer.close()
    }

    def block(str: String)(body: => Unit): Unit = {
      writer.write(INDENT * indentLevel)
      writer.write(str)
      writer.write(" {")
      writer.write(NEWLINE)
      indentLevel += 1
      body
      indentLevel -= 1
      writer.write(INDENT * indentLevel)
      writer.write("}")
      writer.write(NEWLINE)
    }

    def ln(str: String): Unit = {
      writer.write(INDENT * indentLevel)
      writer.write(str)
      writer.write(NEWLINE)
    }

    implicit class Name(name: String) {
      def toPascal(): String = name.split("_").map(_.capitalize).mkString("")

      def toCamel(): String = toPascal.substring(0, 1).toLowerCase + toPascal.substring(1)

      def toUpper() = name.toUpperCase
    }
  }
}

