package dslg

import java.text._
import java.io._
import scala.collection.JavaConversions
import scala.collection.mutable.ListBuffer
import scala.util.parsing.json._
import scala.collection.mutable.Stack
import scala.io.StdIn.readLine

object gdsl {

  object options {
    var newLine = "\n"
    var indentWith = "  "
    var indentLevel = 0
    var blockStart = " {"
    var blockEnd = "}"
    var currentDir = "./"
  }

  private var currentFile: PrintWriter = null
  private var root: List[Entity] = List()

  private var onBeginNamespace: Namespace => Unit = null
  private var onEndNamespace: Namespace => Unit = null
  private var onBeginStructure: Structure => Unit = null
  private var onEndStructure: Structure => Unit = null
  private var onBeginTable: Table => Unit = null
  private var onEndTable: Table => Unit = null
  private var onBeginEnumeration: Enumeration => Unit = null
  private var onEndEnumeration: Enumeration => Unit = null
  private var onBeginService: Service => Unit = null
  private var onEndService: Service => Unit = null
  private var onBeginAll: List[Entity] => Unit = null
  private var onEndAll: List[Entity] => Unit = null

  object begin {
    def NAMESPACE(generate: Namespace => Unit): Unit = onBeginNamespace  = generate
    def STRUCTURE(generate: Structure => Unit): Unit = onBeginStructure = generate
    def TABLE(generate: Table => Unit): Unit = onBeginTable = generate
    def ENUMERATION(generate: Enumeration => Unit): Unit = onBeginEnumeration = generate
    def SERVICE(generate: Service => Unit): Unit = onBeginService = generate
    def ALL(generate: List[Entity] => Unit): Unit = onBeginAll = generate
  }

  object end {
    def NAMESPACE(generate: Namespace => Unit): Unit = onEndNamespace  = generate
    def STRUCTURE(generate: Structure => Unit): Unit = onEndStructure = generate
    def TABLE(generate: Table => Unit): Unit = onEndTable = generate
    def ENUMERATION(generate: Enumeration => Unit): Unit = onEndEnumeration = generate
    def SERVICE(generate: Service => Unit): Unit = onEndService = generate
    def ALL(generate: List[Entity] => Unit): Unit = onEndAll = generate
  }

  def generate(block: => Unit) {
    block

    val sb = new StringBuilder
    var line = readLine
    while ( line != null ) {
      sb.append(line)
      line = readLine
    }

    val obj = JSON.parseFull(sb.toString)
    val list = obj.get.asInstanceOf[List[Any]]

    root = list.map(d => Entity.createEntity(d.asInstanceOf[ObjectT], null))

    if ( onBeginAll != null ) onBeginAll(root)
    root.foreach(traverse(_))
    if ( onEndAll != null ) onEndAll(root)
  }

  def openFile(filename: String) {
    if ( currentFile != null ) {
      closeFile();
    }

    val f = new File(options.currentDir, filename);

    f.getParentFile().mkdirs()

    currentFile = new PrintWriter(f)
  }

  def closeFile() {
    currentFile.close()
  }

  def file(filename: String)(body: => Unit): Unit = {
    openFile(filename)
    body
    closeFile()
  }

  def bigBlock(str: String): Unit = {
    currentFile.write(str.stripMargin('|'))
  }

  def block(str: String)(body: => Unit): Unit = {
    currentFile.write(options.indentWith * options.indentLevel)
    currentFile.write(str)
    currentFile.write(options.blockStart)
    currentFile.write(options.newLine)
    options.indentLevel += 1
    body
    options.indentLevel -= 1
    currentFile.write(options.indentWith * options.indentLevel)
    currentFile.write(options.blockEnd)
    currentFile.write(options.newLine)
  }

  def ln(str: String): Unit = {
    currentFile.write(options.indentWith * options.indentLevel)
    currentFile.write(str)
    currentFile.write(options.newLine)
  }

  implicit class Name(name: String) {
    def toPascal(): String = name.split("_").map(_.capitalize).mkString("")

    def toCamel(): String = toPascal.substring(0, 1).toLowerCase + toPascal.substring(1)

    def toUpper() = name.toUpperCase
  }

  type ObjectT = Map[String, Any]
  type ListT = List[ObjectT]

  object Entity {
    def createEntity(data: Map[String, Any], parent: Entity): Entity = {
      val (name, content) = (data.head._1, data.head._2.asInstanceOf[Map[String, Any]])
      content("prototype") match {
        case "namespace" => Namespace(name, content, parent)
        case "structure" => Structure(name, content, parent)
        case "table" => Table(name, content, parent)
        case "enumeration" => Enumeration(name,content, parent)
        case "field" => Field(name, content, parent)
        case "constant" => Constant(name, content, parent)
        case "service" => Service(name, content, parent)
        case "function" => Function(name, content, parent)
        case "url-argument" => UrlArgument(name, content, parent)
      }
    }

    private var path = Array[String]()
  }

  private def createDatatype(dt: String): Datatype = {

    var result: Datatype = null
    result = dt match {
      case "string" => StringDatatype()
      case "int"    => IntDatatype()
      case "float"  => FloatDatatype()
      case "bool"   => BoolDatatype()
      case "uuid"   => UuidDatatype()
      case "void"   => VoidDatatype()
      case "date"   => DateDatatype()
      case _ => null
    }

    if ( result == null ) {
      val parts = dt.split("::")
      result = root.find(_.name == parts(0)) match {
        case Some(e) =>
          if ( parts.size == 1 )
            e.asInstanceOf[Datatype]
          else
            e.find(parts.tail.mkString("::")).asInstanceOf[Datatype]
          
        case None => null
      }
    }

    if ( result == null )
      throw new RuntimeException(s"datatype not found: ${dt}")

    result
  }

  class Entity(val name: String, val data: ObjectT, val parent: Entity) {
    protected val attributes = (data filter { case (_, v) => v.isInstanceOf[String] }).asInstanceOf[Map[String, String]]
    val children = (data getOrElse ("children", List())).asInstanceOf[ListT].map(Entity.createEntity(_, this))

    val comment = this("comment", "")

    def apply(key: String): String = (attributes get key).get

    def apply(key: Symbol): String = apply(key.name)

    def apply(name: String, defaultVal: String): String = attributes getOrElse (name, defaultVal)

    def has(key: Symbol): Boolean = {
      has(key.name)
    }

    def has(key: String): Boolean = {
      attributes.contains(key)
    }

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

    def path: String = {
      if ( parent == null )
        name
      else
        parent.path //+ "::" + name
    }
  }

  case class Namespace(_id: String, _data: ObjectT, _parent: Entity) extends Entity(_id, _data, _parent) {
    def namespaces = children.filter(_.isInstanceOf[Namespace]).map(_.asInstanceOf[Namespace])
    def structures = children.filter(_.isInstanceOf[Structure]).map(_.asInstanceOf[Structure])
    def tables = children.filter(_.isInstanceOf[Table]).map(_.asInstanceOf[Table])
    def enumerations = children.filter(_.isInstanceOf[Enumeration]).map(_.asInstanceOf[Enumeration])
    def services = children.filter(_.isInstanceOf[Service]).map(_.asInstanceOf[Service])
  }

  abstract class Datatype(_id: String, _data: ObjectT, _parent: Entity) extends Entity(_id, _data, _parent)

  case class StringDatatype() extends Datatype("string", Map(), null)
  case class IntDatatype() extends Datatype("int", Map(), null)
  case class FloatDatatype() extends Datatype("float", Map(), null)
  case class BoolDatatype() extends Datatype("bool", Map(), null)
  case class UuidDatatype() extends Datatype("uuid", Map(), null)
  case class VoidDatatype() extends Datatype("void", Map(), null)
  case class DateDatatype() extends Datatype("date", Map(), null)
  
  case class Structure(_id: String, _data: ObjectT, _parent: Entity) extends Datatype(_id, _data, _parent) {
    def fields = children.filter(_.isInstanceOf[Field]).map(_.asInstanceOf[Field])
  }

  case class Table(_id: String, _data: ObjectT, _parent: Entity) extends Datatype(_id, _data, _parent) {
    def fields = children.filter(_.isInstanceOf[Field]).map(_.asInstanceOf[Field])
  }

  case class Service(_id: String, _data: ObjectT, _parent: Entity) extends Datatype(_id, _data, _parent) {
    def functions = children.filter(_.isInstanceOf[Function]).map(_.asInstanceOf[Function])
    def serviceUrl = this("service-url", "")
  }

  case class Function(_id: String, _data: ObjectT, _parent: Entity) extends Datatype(_id, _data, _parent) {
    def urlArgs = children.filter(_.isInstanceOf[UrlArgument]).map(_.asInstanceOf[UrlArgument])
    def fields = children.filter(_.isInstanceOf[Field]).map(_.asInstanceOf[Field])
    def modifier = this("return-modifier")
    def datatype: Datatype = createDatatype(this("return-datatype"))
    def method = this("method")
    def url = this("url")
  }

  case class UrlArgument(_id: String, _data: ObjectT, _parent: Entity) extends Datatype(_id, _data, _parent) {
    def datatype: Datatype = createDatatype(this("datatype"))
  }

  case class Enumeration(_id: String, _data: ObjectT, _parent: Entity) extends Datatype(_id, _data, _parent) {
    def constants = children.filter(_.isInstanceOf[Constant]).map(_.asInstanceOf[Constant])
  }

  case class Field(_id: String, _data: ObjectT, _parent: Entity) extends Entity(_id, _data, _parent) {
    def modifier = this("modifier")
    def datatype: Datatype = createDatatype(this("datatype"))
  }

  case class Constant(_id: String, _data: ObjectT, _parent: Entity) extends Entity(_id, _data, _parent) {
    val value = this("value")
  }

  private def traverse(current: Entity): Unit = {
    current match {
      case n: Namespace =>
        if ( onBeginNamespace != null ) onBeginNamespace(n)
        current.children.foreach(traverse(_))
        if ( onEndNamespace != null ) onEndNamespace(n)
        
      case s: Structure =>
        if ( onBeginStructure != null ) onBeginStructure(s)
        current.children.foreach(traverse(_))
        if ( onEndStructure != null ) onEndStructure(s)

      case t: Table =>
        if ( onBeginTable != null ) onBeginTable(t)
        current.children.foreach(traverse(_))
        if ( onEndTable != null ) onEndTable(t)  

      case e: Enumeration =>
        if ( onBeginEnumeration != null ) onBeginEnumeration(e)
        current.children.foreach(traverse(_))
        if ( onEndEnumeration != null ) onEndEnumeration(e)

      case c: Service =>
        if ( onBeginService != null ) onBeginService(c)
        current.children.foreach(traverse(_))
        if ( onEndService != null ) onEndService(c)

      case _ => // ignore
    }
  }

}
