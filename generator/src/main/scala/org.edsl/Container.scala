package org.edsl

import scala.collection.immutable.List

trait Container {

  var parent: Container

  /**
   * List of Entities
   */
  protected var items: List[Entity]

  /**
   * Adds items of type entity
   */
  def :+(child: Entity) = {
    items = items :+ child
  }
  /**
   * @return items witch is list of entities
   */
  def children() = items

  /**
   * Filters entities by specified type
   */
  def filter[T <: Entity]()(implicit m: Manifest[T]): List[T] ={
    items.filter(_.getClass == m.erasure.asInstanceOf[Class[T]]).asInstanceOf[List[T]]
  }

  /**
   * Finds entity with specified id
   */
  def findById(id: Identity): Entity = {
    val result = items.filter(_.id == id).headOption
    result match {
      case Some(x) => x
      case None => null
    }
  }

  /**
   * @return entity with specified id
   * @exception unresolved type by specified id
   */
  def resolve(id: Identity): Datatype = {
    var result = findById(id)
    if (result == null &&  parent != null) {
      result = parent.asInstanceOf[Namespace].resolve(id)
    }
    if (result == null)
      throw new RuntimeException(s"Unresolved type: $id")
    result.asInstanceOf[Datatype]
  }

  /**
   * @return entity with specified id and qualified name
   * @exception unresolved type by specified id
   */
  def resolve(id: Identity, qualifiedName: String): Datatype = {
    var result = findById(id)
    if (result == null && parent != null) {
      result = parent.asInstanceOf[Namespace].resolve(id, qualifiedName)
    }
    val name = result.path.map(e => if (e.isInstanceOf[Namespace]) e.id.toCamel else e.id.toPascal).mkString(".")
    if (result != null && parent != null && name != qualifiedName) {
      result = parent.asInstanceOf[Namespace].resolve(id, qualifiedName)
    }
    if (result == null)
      throw new RuntimeException(s"Unresolved type: $id");
    result.asInstanceOf[Datatype]
  }

  /**
   * @return entity with specified qualified name
   * @exception unresolved type by qualified name
   */
  def findByQualifiedIdentity(qualified: QualifiedIdentity): Datatype = {
    var result: Datatype = null
    val dataName = qualified.identities(0)
    val data = resolve(Identity(dataName), qualified.toString())
    if (data != null) result = data
    else throw new RuntimeException(s"Unresolved type: $qualified");
    result
  }

}
