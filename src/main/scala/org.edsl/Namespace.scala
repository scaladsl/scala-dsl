package org.edsl

import scala.collection.immutable.List


/**
 * Represents a namespace node in abstract syntax tree.
 *
 * @param name Namespace name.
 */
class Namespace(name: String) extends Container(name) {

  /**
   * Gets all the structures within this namespace.
   */
  def structures(): List[Structure] = filter[Structure]()

  /**
   * Gets all the namespaces within this one.
   */
  def namespaces(): List[Namespace] = filter[Namespace]()
}
