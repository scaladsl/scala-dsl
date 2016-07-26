package org.edsl

trait FieldType
object string extends FieldType
object int extends FieldType
object bool extends FieldType

case class Identifier(name: Symbol) {

  def namespace( body: () => Unit)={
    println(s"namespace is $name {")
    body()
    println ("}")
  }

  def struct(body: () => Unit) = {
    println(s"$name ::= {")
    body()
    println(s"}")
  }

  def as(primitive: FieldType) = {
    println(s"$name ::= $primitive")
  }
}

object Application {
  implicit def symbolToIdentifier(symbol: Symbol): Identifier = {
    println(s"casting Symbol($symbol) to Identifier($symbol)")
    Identifier(symbol)
  }

  def main(args: Array[String]) = {

    'namespace namespace { ()=>
    'user struct { () =>
      'username as string
      'full_name as string
      'age as int
      'sex as bool
     }
    }
  }
}



