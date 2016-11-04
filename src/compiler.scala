package sgf

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Stack
import scala.util.parsing.json._

object CDSL {

  private var global = List[Any]()

  def define(body: => Unit): Unit = {
    body
    println(JSONArray(global).toString(format))
  }

  private def format(o: Any): String = {
    o match {
      case m: Map[_, _] =>
        val sb = new StringBuilder
        sb.append("{")
        sb.append(m.map(e => s"""${format(e._1)}:${format(e._2)}""").mkString(", "))
        sb.append("}")
        sb.toString()

      case l: List[Any] =>
        val sb = new StringBuilder
        sb.append("[")
        sb.append(l.map(e => s"""${format(e)}""").mkString(", "))
        sb.append("]")
        sb.toString()

      case x: Any =>
        s""""${x.toString()}""""
    }
  }

  implicit class Comment(val s: String) {
    def unary_! : Unit = Comment.append(s)
  }

  object Comment {
    private var value = ""

    def reset(): String = {
      val temp = value
      value = ""
      temp
    }

    def append(comment: String): Unit = {
      value += comment
    }
  }

  case class Node(val name: String, val prototype: String, val parent: Node) {
    val children: ListBuffer[Node] = ListBuffer[Node]()
    var attributes = Map[String, String]("comment" -> Comment.reset())

    def fullName(): String = {
      if ( parent == AST.root )
        s"$name"
      else {
        s"${parent.fullName}::$name"
      }
    }

    def find(name: String): Option[Node] = children.find(_.name == name)

    def toMap: Map[String, Any] = {
      var result = Map[String, Any]()
      result += ("prototype" -> prototype)
      attributes.filter(!_._2.isEmpty).foreach(result += _)
      if ( !children.isEmpty )
        result += "children" -> children.map(c => Map(c.name -> c.toMap)).toList
      result
    }
  }

  object AST {

    val root = createRoot()
    private val context = Stack[Node](root)

    def resolve(name: String): Node = {
      val path = name.split('.').toList

      val res = if ( path.size > 1 ) {
        findByPath(root, path)
      } else {
        findRecursively(context.top.parent, name)
      }

      if ( res == null )
        throw new IllegalArgumentException(s"unknown type name: $name")

      res
    }

    def findByPath(current: Node, path: List[String]): Node = {
      current.find(path.head) match {
        case Some(child) =>
          if ( path.size == 1 )
            child
          else
            findByPath(child, path.tail)

        case None =>
          null
      }
    }

    def current = context.top

    def push(node: Node): Unit = {
      context.top.children.append(node)
      context.push(node)
    }

    def pop(): Unit = {
      val node = context.pop()
      if ( context.top == root ) {
        global = global :+ Map(node.name -> node.toMap)
      }
    }

    def scope(name: String, prototype: String)(block: Node => Unit): Unit = {
      val node = Node(name, prototype, current)
      push(node)
      block(node)
      pop()
    }

    private def findRecursively(current: Node, name: String): Node = {
      if ( current != null ) {
        current.find(name) match {
          case Some(node) => node
          case None => findRecursively(current.parent, name)
        }
      } else {
        null
      }
    }

    private def createRoot(): Node = {
      val node = Node("__root__", "namespace", null)
      node.children.append(Node("string", "primitive", node))
      node.children.append(Node("bool", "primitive", node))
      node.children.append(Node("int", "primitive", node))
      node.children.append(Node("float", "primitive", node))
      node
    }
  }

  implicit class Identity(val id: Symbol) {

    def name = id.name

    def ::(other: Identity): Identity = new Identity(Symbol(s"${other.name}.${name}"))

    def namespace(block: => Unit): Unit = entity("namespace", block)
    def struct(block: => Unit): Unit = entity("structure", block)
    def enum(block: => Unit): Unit = entity("enumeration", block)
    def required(datatype: Identity): Unit = field(datatype, "required")
    def optional(datatype: Identity): Unit = field(datatype, "optional")
    def repeated(datatype: Identity): Unit = field(datatype, "repeated")
    def is(value: Int): Unit = AST.scope(name, "constant") { node => node.attributes += "value" -> value.toString }

    private def entity(prototype: String, block: => Unit): Unit = {
      AST.scope(name, prototype) { _ =>
        block
      }
    }

    private def field(datatype: Identity, modifier: String): Unit = {
      AST.scope(name, "field") { node =>
        val dtype = AST.resolve(datatype.name)
        node.attributes += "datatype" -> dtype.fullName
        node.attributes += "modifier" -> modifier
      }
    }
  }
}
