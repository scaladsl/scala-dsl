package org.edsl

class SimpleType(name: String, comment: String) extends Entity(name, comment) {

  def primitive(): Boolean = true

  def composite(): Boolean = !primitive()
}
