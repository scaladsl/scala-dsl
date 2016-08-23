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

  
  def path(): List[Entity] = {
    if ( parent == Context.global ) {
      List(this)
    } else {
      val p = parent.asInstanceOf[Entity]
      p.path :+ this
    }
  }
}
