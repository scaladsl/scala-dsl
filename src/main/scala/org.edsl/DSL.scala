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

  implicit def stringToStringEx(string: String): StringEx = {
    comment = string
    new StringEx(string)
  }

}
