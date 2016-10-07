package org.edsl

/**
 * Provides qualified names for that fields which have qualified identities
 */
class QualifiedIdentity(val identities: List[Symbol]) {

  def ::(other: Symbol): List[Symbol] = {
    identities :+ other
  }

/*
  override def toString(): String = {
    var result: String = ""
    val length = identities.length
    val list = identities.reverse
    for (i <- 0 to length - 2) result += Identity(list(i))
    result += Identity(list(length - 1))
    result
  }
 */
}
