package org.edsl

class SimpleType(name: String) extends Entity(name) {

  def primitive(): Boolean = true

  def composite(): Boolean = !primitive()
}
