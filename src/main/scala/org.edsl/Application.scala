package org.edsl

trait Primitive
object string extends Primitive
object int extends Primitive
object bool extends Primitive

case class Identifier(name: Symbol) {
  def struct(body: () => Unit) = {
    println(s"$name ::= {")
    body()
    println(s"}")
  }

  def as(primitive: Primitive) = {
    println(s"$name ::= $primitive")
  }
}

object Application {
  implicit def symbolToIdentifier(symbol: Symbol): Identifier = {
    println(s"casting Symbol($symbol) to Identifier($symbol)")
    Identifier(symbol)
  }

  def main(args: Array[String]) = {

    'user struct { () =>
      'username as string
      'full_name as string
      'age as int
      'sex as bool
    }

  }
}


