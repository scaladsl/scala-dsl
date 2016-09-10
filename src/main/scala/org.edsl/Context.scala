package org.edsl

/**
 * TODO:
 */
object Context {

  val global = new Namespace(new Identity('__global__))
  global :+ Integer
  global :+ String
  global :+ Boolean
  global :+ Float

  var leaf: Container = global

  /**
   * @return current Entity
   */
  def current(): Container = leaf

  /**
   * Creates namespace with specified id
   */
  def newNamespace(id: Identity)(body: Namespace => Unit): Unit = {
    block(s"""namespace "${id.name}"""") {
      val namespace = new Namespace(id)
      namespace.parent = current

      current :+ namespace

      leaf = namespace
      body(namespace)
      leaf = namespace.parent
    }
  }

  /**
   * Creates structure with specified id
   */
  def newStructure(id: Identity)(body: Structure => Unit): Unit = {
    val structure = new Structure(id)
    structure.comment = Comment.reset()
    if (structure.comment != "")
      ln(s"""set comment "${structure.comment}"""")
      block(s"""structure "${id.name}"""") {
      structure.parent = current
      current :+ structure

      leaf = structure
      body(structure)
      leaf = structure.parent
    }
  }

  /**
   * Creates enumeration with specified id
   */
  def newEnumeration(id: Identity)(body: Enumeration => Unit): Unit = {
      block(s"""enum "${id.name}"""") {
      val enumeration = new Enumeration(id)
      enumeration.comment = Comment.reset()
      enumeration.parent = current

      current :+ enumeration

      leaf = enumeration
      body(enumeration)
      leaf = enumeration.parent
    }
  }

  /**
   * Creates field with specified name, modifier and datatype
   */
  def newField(name: Identity, modifier: Modifier, datatype: Datatype): Unit = {
    assert(current.isInstanceOf[Structure])
    val field = new Field(name, modifier, datatype)
    field.comment = Comment.reset()

    block(s"""field "${name.name()}"""") {
      if (field.comment != "")
        ln(s"""set comment "${field.comment}"""")
      ln(s"""set type = ${datatype.path.map(e => e.id).mkString(".")}""")
      ln(s"""set modifier = ${modifier.id.name}""")
    }

    current :+ field
  }

  /**
   * Creates constant with specified name and value
   */
  def newConstant(name: Identity, value: Int): Unit = {
    assert(current.isInstanceOf[Enumeration])

    ln(s"""${name.name} = ${value}""")

    val econst = new Constant(name, value)
    econst.comment = Comment.reset()
    current :+ econst
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

  def ln(str: String = ""): Unit = {
    print(indentation)
    println(str)
  }
}
