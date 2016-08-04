package org.edsl

import scala.language.implicitConversions
object DSL {

  var comment = ""
  def resolve(name: String): SimpleType = {
    null
  }

  implicit def symbolToIdentifier(symbol: Symbol): Identifier = {
    val c = comment
    comment = ""
    Identifier(symbol, c)
  }

  implicit def stringToExString(string: String): StringEx = {
    new StringEx(string)
  }

  implicit def stringToUnary(com: String): Comment = {
    comment = com
    new Comment(com)
  }

}
