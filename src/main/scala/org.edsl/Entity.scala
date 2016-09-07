package org.edsl

/**
 * The class representing a node in abstract syntax tree.
 *
 * @param id the Entity identity.
 */
abstract class Entity(val id: Identity) {

  /**
   * @param parent the points to parent of entity
   */
  var parent: Container = null

  /**
   * @param comment the value which contains comments written by user
   */
  var comment: String = ""

  /**
   * Solves path for entity
   */
  def path(): List[Entity] = {
    if (parent == Context.global) {
      List(this)
    } else {
      val p = parent.asInstanceOf[Entity]
      p.path :+ this
    }
  }
}
