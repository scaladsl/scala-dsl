package org.edsl

import scala.collection.immutable.List

trait Container {

  protected var items: List[Entity]

  def :+ (child: Entity) = items = items :+ child

  def children() = items

  def filter[T <: Entity]()(implicit m: Manifest[T]): List[T] =
    items.filter(_.getClass == m.erasure.asInstanceOf[Class[T]]).asInstanceOf[List[T]]
}
