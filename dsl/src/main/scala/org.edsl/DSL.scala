 package org.edsl

 import scala.language.implicitConversions

object DSL {

 
  implicit def symbolToIdentity(symbol: Symbol): Identity = Identity(symbol)

  implicit def symbolToQualifiedIdentity(symbol: Symbol): QualifiedIdentity = new QualifiedIdentity(List(symbol))

//  implicit def qualifiedIdentityToList(qualified: QualifiedIdentity): List[Identity] = qualified.identities.map(new Identity(_))

  implicit def stringToStringEx(string: String): StringEx = new StringEx(string)


//  implicit def symbolToStringEx(symbol: Symbol): StringEx = new StringEx(symbol.name)

//  implicit def qualifiedIdentityToList(qualified: QualifiedIdentity): List[Identity] = qualified.identities.map(new Identity(_))

//  implicit def identityToSymbol(identity: Identity): Symbol = identity.symbol

//  implicit def qualifiedIdentityToDatatype(qualified: QualifiedIdentity): Datatype = Context.stackOfCurrent.top.parent.findByQualifiedIdentity(qualified)

}
