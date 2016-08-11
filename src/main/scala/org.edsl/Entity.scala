package org.edsl

/**
 * Represents a class Entity.
 *
 * @param name: Entity name.
 */
class Entity(val name: String, val parent: Entity) {
  var comment: String = ""
}
