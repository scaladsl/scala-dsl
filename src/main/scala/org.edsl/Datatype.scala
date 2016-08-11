package org.edsl

class Datatype(name: String, parent: Entity) extends Entity(name, parent) {

  def primitive(): Boolean = true

  def composite(): Boolean = !primitive()
}
