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
    val namespace = new Namespace(id)
    namespace.parent = current

    current :+ namespace

    leaf = namespace
    body(namespace)
    leaf = namespace.parent
  }

  /**
   * Creates structure with specified id
   */
  def newStructure(id: Identity)(body: Structure => Unit): Unit = {
    val structure = new Structure(id)
    structure.parent = current

    current :+ structure

    leaf = structure
    body(structure)
    leaf = structure.parent
  }

  /**
   * Creates enumeration with specified id
   */
  def newEnumeration(id: Identity)(body: Enumeration => Unit): Unit = {
    val enumeration = new Enumeration(id)
    enumeration.parent = current

    current :+ enumeration

    leaf = enumeration
    body(enumeration)
    leaf = enumeration.parent
  }

  /**
   * Creates field with specified name, modifier and datatype
   */
  def newField(name: Identity, modifier: Modifier, datatype: Datatype): Unit = {
    assert(current.isInstanceOf[Structure])
    val field = new Field(name, modifier, datatype)
    field.comment = Comment.reset()
    current :+ field
  }

  /**
   * Creates constant with specified name and value
   */
  def newConstant(name: Identity, value: Int): Unit = {
    assert(current.isInstanceOf[Enumeration])
    val econst = new Constant(name, value)
    econst.comment = Comment.reset()
    current :+ econst
  }
}
