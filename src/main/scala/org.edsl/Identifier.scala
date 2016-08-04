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

  def as(datatype: Symbol) = {
    assert(Context.current.isInstanceOf[Structure])

    var field = new Field(name.name, datatype.name, comment)
    Context.add(field)
  }
}
