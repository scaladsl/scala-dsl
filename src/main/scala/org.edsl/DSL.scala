package org.edsl

import scala.language.implicitConversions

object DSL {

  def resolve(name: String): SimpleType = {
    null
  }

  implicit def symbolToIdentifier(symbol: Symbol): Identifier = {
    Identifier(symbol)
  }
  /*
  implicit def stringToExString(string: String): StringEx = {
    StringEx(string)
  }

implicit def stringToUnary(comment: String): Comment = {
    new Comment(comment)
  }*/

}
