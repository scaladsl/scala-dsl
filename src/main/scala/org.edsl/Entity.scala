package org.edsl

/**
 * The class representing a node in abstract syntax tree.
 *
 * @param id Entity identity.
 */
abstract class Entity(val id: Identifier) {

  /**
    * 
    */
  var parent: Container = null

  var comment: String = ""

  def parents(): List[Entity] = {
    null // TODO
  }

  def qualifiedName(): List[Identifier] = parents.map(_.id) ::: List(id)
}
