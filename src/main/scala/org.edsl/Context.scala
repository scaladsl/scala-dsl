package org.edsl

import scala.collection.mutable.Stack

object Context {
  private val stack = Stack[Container]()
  stack.push(new Namespace("__root__"))

  def enter(container: Container) = stack.push(container)
  def leave() = stack.pop
  def current() = stack.top
  def add(entity: Entity) = current.addChild(entity)
}
