package org.edsl

import scala.collection.mutable.ListBuffer
import scala.collection.immutable.List

/**
 * Represents a Container  entity.
 */
class Container(name: String) extends Entity(name) {

  private val _children = new ListBuffer[Entity]

  def addChild(child:Entity) = _children += child

  def removeChild(child: Entity) = _children -= child

  def filter[T <: Entity]()(implicit m: Manifest[T]): List[T] = {
    children.filter(_.getClass ==  m.erasure.asInstanceOf[Class[T]]).toList.asInstanceOf[List[T]]
  }

  def children(): List[Entity] = _children.toList
}
