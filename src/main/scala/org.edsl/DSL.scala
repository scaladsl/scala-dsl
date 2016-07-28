package org.edsl

object DSL {
  implicit def symbolToIdentifier(symbol: Symbol): Identifier = {
    Identifier(symbol)
  }
}
