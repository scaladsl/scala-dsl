package org.edsl

import scala.language.implicitConversions

object DSL {

  implicit def symbolToIdentifier(symbol: Symbol): Identifier = Identifier(symbol)

  /**
    * TODO: 
    */
  implicit def stringToStringEx(string: String): StringEx = new StringEx(string)

}
