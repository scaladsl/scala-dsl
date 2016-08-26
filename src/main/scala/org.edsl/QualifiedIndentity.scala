package org.edsl

class QualifiedIdentity(val identities: List[Symbol]) {

  def ::(other: Symbol): QualifiedIdentity = {
    new QualifiedIdentity(identities :+ other)
  }
}
