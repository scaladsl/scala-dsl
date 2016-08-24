package org.edsl

import scala.collection.immutable.List

trait Container {

  var parent: Container

  protected var items: List[Entity]

  def :+(child: Entity) = items = items :+ child

  def children() = items

  def filter[T <: Entity]()(implicit m: Manifest[T]): List[T] =
    items.filter(_.getClass == m.erasure.asInstanceOf[Class[T]]).asInstanceOf[List[T]]

  def findById(id: Identifier): Entity = {
    val result = items.filter(_.id == id).headOption
    result match {
      case Some(x) => x
      case None => null
    }
  }

  def resolve(id: Identifier): Datatype = {
    var result = findById(id)
    if (result == null && parent != null) {
      result = parent.asInstanceOf[Namespace].resolve(id)
    }

    if (result == null)
      throw new RuntimeException(s"Unresolved type: $id");
    result.asInstanceOf[Datatype]
  }
}
