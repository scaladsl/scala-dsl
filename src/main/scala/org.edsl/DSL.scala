package org.edsl

import scala.language.implicitConversions

object DSL {

  def resolve(name: String): Datatype = {
    null
  }

  implicit def symbolToIdentifier(symbol: Symbol): Identifier = Identifier(symbol)

  /**
    * TODO: 
    */
  implicit def stringToStringEx(string: String): StringEx = new StringEx(string)

}
