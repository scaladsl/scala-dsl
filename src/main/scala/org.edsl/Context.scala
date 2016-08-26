package org.edsl

object Context {
  val global = new Namespace(new Identity('__global__))
  global :+ Integer
  global :+ String
  global :+ Boolean
  global :+ Float

  var leaf: Container = global

  def current(): Container = leaf

  def newNamespace(id: Identity)(body: Namespace => Unit): Unit = {
    val namespace = new Namespace(id)
    namespace.parent = current

    current :+ namespace

    leaf = namespace
    body(namespace)
    leaf = namespace.parent
  }

  def newStructure(id: Identity)(body: Structure => Unit): Unit = {
    val structure = new Structure(id)
    structure.parent = current

    current :+ structure

    leaf = structure
    body(structure)
    leaf = structure.parent
  }

  def newEnumeration(id: Identity)(body: Enumeration => Unit): Unit = {
    val enumeration = new Enumeration(id)
    enumeration.parent = current

    current :+ enumeration

    leaf = enumeration
    body(enumeration)
    leaf = enumeration.parent
  }

  def newField(name: Identity, modifier: Modifier, id: Identity): Unit = {
    assert(current.isInstanceOf[Structure])
    val datatype = current.parent.resolve(id)
    val field = new Field(name, modifier, datatype)
    field.comment = Comment.reset()
    current :+ field
  }

  def newConstant(name: Identity, value: Int): Unit = {
    assert(current.isInstanceOf[Enumeration])
    val econst = new Constant(name, value)
    econst.comment = Comment.reset()
    current :+ econst
  }
}
