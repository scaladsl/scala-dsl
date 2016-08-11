package org.edsl

import scala.collection.mutable.Stack


object Context {
  private val stack = Stack[Entity]()
  stack.push(new Namespace("__root__", null))

  def enter(container: Entity) = stack.push(container)
  def leave() = stack.pop
  def current() = stack.top
  def add(entity: Entity) = current.asInstanceOf[Container] :+ entity
}
