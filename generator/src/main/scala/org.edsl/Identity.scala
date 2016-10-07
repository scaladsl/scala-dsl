package org.edsl

/**
 * The class represents identity for entity
 *
 * @param symbol the Identity Symbol
 */
case class Identity(symbol: Symbol) {
  /**
   * Converts symbol to string
   */
  def name() = symbol.name

  /**
   * Capitalize every word by spliting "_"
   */
  def toPascal(): String = name.split("_").map(_.capitalize).mkString("")

  /**
   * TODO:
   */
  def toCamel(): String = toPascal.substring(0, 1).toLowerCase + toPascal.substring(1)

  /**
   * Returns name without changes
   */
  def toKebab() = name

  /**
   * Changing every character to upper
   */
  def toUpper() = name.toUpperCase

  /**
   * Makes namespace with specified name
   */
  def namespace() = {
    assert(Context.stackOfCurrent.top.isInstanceOf[Namespace])
    Context.newNamespace(this)
  }

  /**
   * Makes structue with specified name
   */
  def struct() = {
   assert(Context.stackOfCurrent.top.isInstanceOf[Namespace])
    Context.newStructure(this)
  }

  /**
   * Makes enumeration with specified name
   */
  def enum() = {
    assert(Context.stackOfCurrent.top.isInstanceOf[Namespace])
    Context.newEnumeration(this) 
  }

  /**
   * Makes constatnt with specified name
   */
  def is(value: Int) = {
    Context.newConstant(this, value)
  }

  /**
   * Makes field with specified name and required modifier
   */
  def required(datatype: Datatype): Unit = {
    Context.newField(this, Modifier.REQ, datatype)
  }

  /**
   * Makes field with specified name and optional modifier
   */
  def optional(datatype: Datatype): Unit = {
    Context.newField(this, Modifier.REQ, datatype)
  }

  /**
   * Makes field with specified name and repeated modifier
   */
  def repeated(datatype: Datatype): Unit = {
    Context.newField(this, Modifier.REP, datatype)
  }

  def end(){
    Context.stackOfCurrent.pop
  }

  /**
   * @return name
   */
  override def toString(): String = name

}
