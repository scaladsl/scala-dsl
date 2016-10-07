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

  var stackOfCurrent = new scala.collection.mutable.Stack[Container]
  stackOfCurrent.push(new Namespace(new Identity('__global__)))
  stackOfCurrent.top :+ Integer
  stackOfCurrent.top :+ String
  stackOfCurrent.top :+ Boolean
  stackOfCurrent.top :+ Float

/*
  var leaf: Container = global

  /**
   * @return current Entity
   */
  def current(): Container = leaf
*/
  /**
   * Creates namespace with specified id
   */
  def newNamespace(id: Identity)/*(body: Namespace => Unit)*/: Unit = {
    val namespace = new Namespace(id)
    namespace.parent = stackOfCurrent.top
    stackOfCurrent.top :+ namespace
    stackOfCurrent.push(namespace)

    //  body(namespace)
    //  leaf = namespace.parent
  }

  /**
   * Creates structure with specified id
   */
  def newStructure(id: Identity)/*(body: Structure => Unit)*/: Unit = {
    val structure = new Structure(id)
    structure.comment = Comment.reset()
    structure.parent = stackOfCurrent.top
    stackOfCurrent.top :+ structure
    stackOfCurrent.push(structure)

    //  leaf = structure
    //  body(structure)
    //  leaf = structure.parent
  }

  /**
   * Creates enumeration with specified id
   */
  def newEnumeration(id: Identity)/*(body: Enumeration => Unit)*/: Unit = {
      val enumeration = new Enumeration(id)
      enumeration.comment = Comment.reset()
      enumeration.parent = stackOfCurrent.top 

      stackOfCurrent.top :+ enumeration
      stackOfCurrent.push(enumeration)

//      leaf = enumeration
 //     body(enumeration)
 //     leaf = enumeration.parent
  }

  /**
   * Creates field with specified name, modifier and datatype
   */
  def newField(name: Identity, modifier: Modifier, datatype: Datatype): Unit = {
    assert(stackOfCurrent.top.isInstanceOf[Structure])

    val field = new Field(name, modifier, datatype)
    field.comment = Comment.reset()
    stackOfCurrent.top :+ field
  }

  /**
   * Creates constant with specified name and value
   */
  def newConstant(name: Identity, value: Int): Unit = {
    assert(stackOfCurrent.top.isInstanceOf[Enumeration])

    val econst = new Constant(name, value)
    econst.comment = Comment.reset()
    stackOfCurrent.top  :+ econst
  }

}

