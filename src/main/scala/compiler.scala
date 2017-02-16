package dslc


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
        s""" "${x.toString()}" """
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

  case class Node(var name: String, val prototype: String, var parent: Node) {
    val children: ListBuffer[Node] = ListBuffer[Node]()
    var attributes = Map[String, String]("comment" -> Comment.reset())

    def fullName(): String = {
      if (parent == AST.root)
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
      if (!children.isEmpty)
        result += "children" -> children.map(c => Map(c.name -> c.toMap)).toList
      result
    }
  }

  object AST {
    val root = createRoot()
    private val context = Stack[Node](root)

    def resolve(name: String): Node = {
      var path = name.split("::").toList
      val res = if (path.size > 1) {
        findByPath(root, path)
      } else {
        findRecursively(context.top.parent, name)
      }

      if (res == null)
        throw new IllegalArgumentException(s"unknown type name: $name")
      res
    }

    def findByPath(current: Node, path: List[String]): Node = {
      current.find(path.head) match {
        case Some(child) =>
          if (path.size == 1)
            child
          else
            findByPath(child, path.tail)

        case None =>
          null
      }

    }

    private def findRecursively(current: Node, name: String): Node = {
      if (current != null) {
        current.find(name) match {
          case Some(node) => node
          case None => findRecursively(current.parent, name)
        }
      } else {
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
      if (context.top == root) {
        global = global :+ Map(node.name -> node.toMap)
      }
    }

    def scope(name: String, prototype: String)(block: Node => Unit): Unit = {
      val node = Node(name, prototype, current)
      push(node)
      block(node)
      pop()
    }

    private def createRoot(): Node = {
      val node = Node("__root__", "namespace", null)
      node.children.append(Node("string", "primitive", node))
      node.children.append(Node("bool", "primitive", node))
      node.children.append(Node("int", "primitive", node))
      node.children.append(Node("float", "primitive", node))
      node.children.append(Node("uuid", "primitive", node))
      node.children.append(Node("date", "primitive", node))
      node
    }
  }

  class Function(val url: String, val name: String, _body: => Unit) {
    def body = _body
  }

  def get(url: String)(body: => Unit): Function = new Function(url, "get", body)
  def post(url: String)(body: => Unit): Function = new Function(url, "post", body)
  def put(url: String)(body: => Unit): Function = new Function(url, "put", body)
  def delete(url: String)(body: => Unit): Function = new Function(url, "delete", body)
  def returns = 'returns
  def service(block: => Unit) = entity("service", block)
  def service(baseUrl: String)(block: => Unit) = entity("service", block, baseUrl)
  def namespace(block: => Unit): EntityTag = entity("namespace", block)
  def struct(block: => Unit): EntityTag = entity("structure", block)
  def enum(block: => Unit): EntityTag = entity("enumeration", block)

  class EntityTag(_node: Node, _block: => Unit) {
    var node = _node
    def block() = _block
  }

  private def entity(prototype: String, block: => Unit, baseUrl: String = ""): EntityTag = {
    var node = Node("", prototype, AST.current)
    if (baseUrl.length > 0) node.attributes += ("service-url" -> baseUrl)
    new EntityTag(node, block)
  }

  private var typeArgs = new scala.collection.mutable.ArrayBuffer[Tuple2[Symbol, Any]]()
  private var urlArgs = new scala.collection.mutable.ArrayBuffer[Tuple2[String, String]]()
  private var funcArgs = new scala.collection.mutable.ArrayBuffer[Tuple2[String, String]]()

  implicit class Identity(val id: Symbol) {

    def ::=(function: Function): Unit = {
      val node = Node(name, "function", AST.current)
      node.attributes += ("method" -> function.name)
      node.attributes += ("url" -> function.url)
      AST.push(node)
      function.body
      if (!node.attributes.contains("return-datatype")) {
        node.attributes += ("return-datatype" -> "void")
        node.attributes += ("return-modifier" -> "required")
      }
      if (!urlArgs.isEmpty) {
        urlArgs.foreach { f =>
          AST.scope(f._1, "url-argument") { node =>
            val dtype = AST.resolve(f._2)
            node.attributes += "datatype" -> dtype.fullName
          }
        }
        urlArgs.clear
      }
      if (!funcArgs.isEmpty) {
        funcArgs.foreach { f =>
          AST.scope(f._1, "argument") { node =>
            val dtype = AST.resolve(f._2)
            node.attributes += "datatype" -> dtype.fullName
          }
        }
        funcArgs.clear
      }
      AST.pop()
    }

    def ::=(tag: EntityTag): Unit = {
      if(name.contains("::")){
        val namespaces = name.split("::")
        for( i <- 0 to namespaces.size -2 ){ AST.push(Node(namespaces(i), "namespace", AST.current))}    
        tag.node.name = namespaces.last
        tag.node.parent = AST.current
        AST.push(tag.node)             
        tag.block
        namespaces.foreach{_ => AST.pop()}
      }
       else{
          tag.node.name = name
          AST.push(tag.node)             
          tag.block
          AST.pop()
       }
    }

    def name = id.name
    def ::(other: Identity): Identity = new Identity(Symbol(s"${other.name}::${name}"))
    def required(datatype: Identity): Unit = field(datatype, "required")
    def optional(datatype: Identity): Unit = field(datatype, "optional")
    def repeated(datatype: Identity): Unit = field(datatype, "repeated")
    def is(value: Int): Unit = AST.scope(name, "constant") { node => node.attributes += "value" -> value.toString }
    
    def as(datatype: Identity): String = {
      urlArgs += (name -> datatype.name)
      name
    }

    def arg(datatype: Identity): String = {
      funcArgs += (name -> datatype.name)
      name
    }

    def void() {
      if (name != "returns") {
        throw new IllegalArgumentException(s"Only return type can be void!")
      }
      AST.current.attributes = AST.current.attributes + ("return-datatype" -> "void")
      AST.current.attributes = AST.current.attributes + ("return-modifier" -> "required")
    }

    def apply(args: Tuple2[Symbol, Any]*): Identity = {
      AST.resolve(name)
      args.map(tuple => typeArgs += tuple)
      this
    }

    override def toString = name

    private def field(datatype: Identity, modifier: String): Unit = {
      name match {
        case "returns" => {
          if (typeArgs.size != 0) throw new IllegalArgumentException(s"Return type could not have attributes")
          AST.current.attributes += ("return-datatype" -> AST.resolve(datatype.name).fullName)
          AST.current.attributes += ("return-modifier" -> modifier)
        }
        case _ =>
          AST.scope(name, "field") { node =>
            val dtype = AST.resolve(datatype.name)
            node.attributes += "datatype" -> dtype.fullName
            node.attributes += "modifier" -> modifier
            typeAttr
          }
      } 
    }

    private def typeAttr(): Unit = {
      if (typeArgs.size != 0) {
        typeArgs.foreach { tuple =>
          AST.current.attributes = AST.current.attributes + (tuple._1.name -> tuple._2.toString)
        }
        typeArgs.clear
      }
    }

  }

}
