package org.edsl

import scala.collection.immutable.List

/**
 * Represents a namespace node in abstract syntax tree.
 *
 */
class Namespace(id: Identity) extends Entity(id) with Container {

  protected var items = List[Entity]()

  /**
   * Gets all the structures within this namespace.
   */
  def structures(): List[Structure] = filter[Structure]

  /**
   * Gets all the enumerations within this one.
   */
  def enumerations(): List[Enumeration] = filter[Enumeration]

  /**
   * Gets all the namespaces within this one.
   */
  def namespaces(): List[Namespace] = filter[Namespace]
}
