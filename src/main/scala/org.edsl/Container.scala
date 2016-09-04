package org.edsl

import scala.collection.immutable.List

trait Container {

  var parent: Container

  protected var items: List[Entity]

  def :+(child: Entity) = items = items :+ child

  def children() = items

  def filter[T <: Entity]()(implicit m: Manifest[T]): List[T] =
    items.filter(_.getClass == m.erasure.asInstanceOf[Class[T]]).asInstanceOf[List[T]]

  def findById(id: Identity): Entity = {
    val result = items.filter(_.id == id).headOption
    result match {
      case Some(x) => x
      case None => null
    }
  }

  def resolve(id: Identity): Datatype = {
    var result = findById(id)
    if (result == null && parent != null) {
      result = parent.asInstanceOf[Namespace].resolve(id)
    }
      
    if (result == null)
      throw new RuntimeException(s"Unresolved type: $id");
    result.asInstanceOf[Datatype]
  }
    
  def resolve(id: Identity, qualifiedName: String): Datatype = {
    var result = findById(id)
    if (result == null && parent != null) {
      result = parent.asInstanceOf[Namespace].resolve(id, qualifiedName)
    }
    if(result != null && parent != null && result.path.map(e => if (e.isInstanceOf[Namespace]) e.id.toCamel else e.id.toPascal).mkString(".") != qualifiedName ){
        result = parent.asInstanceOf[Namespace].resolve(id, qualifiedName)
    }
    if (result == null)
      throw new RuntimeException(s"Unresolved type: $id");
    result.asInstanceOf[Datatype]
  }
    
  def findByQualifiedIdentity(qualified: QualifiedIdentity): Datatype = {
     var result: Datatype = null  
     val dataName = qualified.identities(0)
     val data = resolve(Identity(dataName), qualified.toString())    
     if(data != null ) result = data
     else throw new RuntimeException(s"Unresolved type: $qualified"); 
     result
  }

}
