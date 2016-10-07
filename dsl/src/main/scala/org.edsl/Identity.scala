package org.edsl
import collection.mutable.Stack

case class Identity(symbol: Symbol) {

  val global = new Entry(new Identity('__global__))

  val Current: Stack[Entry]= Stack(global)
//  Current.push(global)

  def namespace(body: => Unit) = {
    val entry = new Entry(this)
    entry.name = "namespace"
    println("=>>>>>" +entry.name)
    entry.parent = Current.top
    Current.top.children ::: List(entry)
    Current.push(entry)
    block(s"""namespace "${symbol.name}"""") {
      body
    }
    Current.pop
  }

  def struct(body: => Unit) = {
    val structComment = Comment.reset()
    if (structComment != "")
      ln(s"""set comment "${structComment.replaceAll("!", "").replaceAll("\n", "")}"""")
    val entry = new Entry(this)
    entry.name = "structure"
    entry.parent = Current.top
    Current.top.children ::: List(entry)
    Current.push(entry)
    block(s"""structure "${symbol.name}"""") {
      body
    }
    Current.pop
  }

  def enum(body: => Unit) = {
    val entry = new Entry(this)
    entry.name = "enum"
    entry.parent = Current.top
    Current.top.children ::: List(entry)
    Current.push(entry)
    block(s"""enum "${symbol.name}"""") {
      body
    }
    Current.pop
  }

  def is(value: Int) = {
    ln(s"""${symbol.name} = "$value"""")
  }

  def required(datatype: Symbol) = {
    val fieldComment = Comment.reset()
    if (fieldComment != "")
      ln(s"""set comment "${fieldComment.replaceAll("!", "").replaceAll("\n", "")}"""")

    block(s"""field "${symbol.name}"""") {
      ln(s"""set type = "${datatype.name}"""")
      ln("""set modifier = "required"""")
    }
  }

  def required(datatype: List[Symbol]) = {
    val fieldComment = Comment.reset()
    if (fieldComment != "")
      ln(s"""set comment "${fieldComment.replaceAll("!", "").replaceAll("\n", "")}"""")
    val data = datatype.reverse.map(s => s.name).mkString(".")

    block(s"""field "${symbol.name}"""") {
      ln(s"""set type = "${data}"""")
      ln("""set modifier = "required"""")
    }
  }

  def optional(datatype: Symbol) = {
    val fieldComment = Comment.reset()
    if (fieldComment != "")

      ln(s"""set comment "${fieldComment.replaceAll("!", "").replaceAll("\n", "")}"""")

    block(s"""field "${symbol.name}"""") {
      ln(s""""set type = "${datatype.name}"""")
      ln("""set modifier = "optional"""")
    }
  }

  def optional(datatype: List[Symbol]) = {
    val fieldComment = Comment.reset()
    if (fieldComment != "")
      ln(s"""set comment "${fieldComment.replaceAll("!", "").replaceAll("\n", "")}"""")
    val data = datatype.reverse.map(s => s.name).mkString(".")

    block(s"""field "${symbol.name}"""") {
      ln(s""""set type = "${data}"""")
      ln("""set modifier = "optional"""")
    }
  }


  def repeated(datatype: Symbol) = {
    val fieldComment = Comment.reset()
    if (fieldComment != "")
      ln(s"""set comment "${fieldComment.replaceAll("!", "").replaceAll("\n", "")}"""")

    block(s"""field "${symbol.name}"""") {
      ln(s"""set type = "${datatype.name}"""")
      ln("""set modifier = "repeated"""")
    }
  }

  def repeated(datatype: List[Symbol]) = {
    val fieldComment = Comment.reset()
    if (fieldComment != "")
      ln(s"""set comment "${fieldComment.replaceAll("!", "").replaceAll("\n", "")}"""")
    val data = datatype.reverse.map(s => s.name).mkString(".")

    block(s"""field "${symbol.name}"""") {
      ln(s"""set type = "${data}"""")
      ln("""set modifier = "repeated"""")
    }
  }

  private val INDENT = "    "
  private var indentLevel = 0

  private def indentation: String = {
    INDENT * indentLevel    
  }

  def block(str: String)(body: => Unit): Unit = {
    print(indentation)
    println("begin " + str)

    indentLevel += 1
    body
    indentLevel -= 1

    print(indentation)
    println("end")
  }

  def ln(str: String): Unit = {
    print(indentation)
    println(str)
  }
}
