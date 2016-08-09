package org.edsl

case class Identifier(name: Symbol, comment: String) {

  def namespace(body: => Unit) = {
    assert(Context.current.isInstanceOf[Namespace])

    val ns = new Namespace(name.name, comment)
    Context.add(ns)
    Context.enter(ns)
    body
    Context.leave
  }

  def struct(body: => Unit) = {
    assert(Context.current.isInstanceOf[Namespace])

    val struct = new Structure(name.name, comment)
    Context.add(struct)
    Context.enter(struct)
    body
    Context.leave
  }

  /*  def as(datatype: Symbol) = {
    assert(Context.current.isInstanceOf[Structure])

    var field = new Field(name.name, datatype.name, comment)
    Context.add(field)
  }*/

  def required(datatype: Symbol) = {
    assert(Context.current.isInstanceOf[Structure])
    datatype match {
      case 'int => "int"
      case 'long => "long"
      case 'bool => "boolean"
      case 'string => "String"
      case 'date => "Date"
      case 'float => "double"
      case t => throw new IllegalArgumentException(s"Invalid DSL type: $t")
    }
    var field = new Field(name.name, datatype.name, comment)
    Context.add(field)
  }

  def optional(datatype: Symbol) = {
    assert(Context.current.isInstanceOf[Structure])
    datatype match {
      case 'int => "Integer"
      case 'long => "Long"
      case 'bool => "Boolean"
      case 'string => "String"
      case 'date => "Date"
      case 'float => "Double"
      case t => throw new IllegalArgumentException(s"Invalid DSL type: $t")
    }
    var field = new Field(name.name, datatype.name, comment)
    Context.add(field)
  }

  def repeated(datatype: Symbol) = {
    assert(Context.current.isInstanceOf[Structure])
    datatype match {
      case 'int => "Integer"
      case 'long => "Long"
      case 'bool => "Boolean"
      case 'string => "String"
      case 'date => "Date"
      case 'float => "Double"
      case t => throw new IllegalArgumentException(s"Invalid DSL type: $t")
    }
    var field = new Field(name.name, s"List[$datatype.name]", comment)
    Context.add(field)
  }
}
