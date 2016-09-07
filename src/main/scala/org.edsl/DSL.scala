package org.edsl

import scala.language.implicitConversions

object DSL {

  /**
   * Makes identity from symbol
   */
  implicit def symbolToIdentity(symbol: Symbol): Identity = Identity(symbol)

  /**
   * Makes qualified identity from symbol
   */
  implicit def symbolToQualifiedIdentity(symbol: Symbol): QualifiedIdentity = new QualifiedIdentity(List(symbol))

  /**
   * Makes list of identities from qualified identity
   */
  implicit def qualifiedIdentityToList(qualified: QualifiedIdentity): List[Identity] = qualified.identities.map(new Identity(_))

  /**
   * It converts String to StringEx which provides more possibilities 
   */
  implicit def stringToStringEx(string: String): StringEx = new StringEx(string)

  /**
   * It resolve Datatype by symbol 
   */
  implicit def symbolToDatatype(symbol: Symbol): Datatype = Context.current.parent.resolve(symbol)

  /**
   * @return Datatype by qualified identity
   */
  implicit def qualifiedIdentityToDatatype(qualified: QualifiedIdentity): Datatype = Context.current.parent.findByQualifiedIdentity(qualified)

}
