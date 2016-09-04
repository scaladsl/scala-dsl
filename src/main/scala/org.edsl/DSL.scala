package org.edsl

import scala.language.implicitConversions

object DSL {

  implicit def symbolToIdentity(symbol: Symbol): Identity = Identity(symbol)

  implicit def symbolToQualifiedIdentity(symbol: Symbol): QualifiedIdentity = new QualifiedIdentity(List(symbol))

  implicit def qualifiedIdentityToList(qualified: QualifiedIdentity): List[Identity] = qualified.identities.map(new Identity(_))

  /**
   * TODO:
   */
  implicit def stringToStringEx(string: String): StringEx = new StringEx(string)

  implicit def symbolToDatatype(symbol: Symbol): Datatype = Context.current.parent.resolve(symbol)
 
  implicit def qualifiedIdentityToDatatype(qualified: QualifiedIdentity): Datatype = Context.current.parent.findByQualifiedIdentity(qualified)  

}
