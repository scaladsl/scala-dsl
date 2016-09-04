package org.edsl

class QualifiedIdentity(val identities: List[Symbol]) {

  def ::(other: Symbol): QualifiedIdentity = {
    new QualifiedIdentity(identities :+ other)
  }

  override def toString(): String = {
    var result: String = ""
    val length = identities.length
    val list = identities.reverse  
    for(i <- 0 to length-2) result += Identity(list(i)).toCamel +'.'
    result +=  Identity(list(length-1)).toPascal
    result  
  }
}
