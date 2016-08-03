package org.edsl

case class Identifier(name: Symbol) {
   

  def namespace(body: => Unit) = {
    assert(Context.current.isInstanceOf[Namespace])
 
    val ns = new Namespace(name.name, "")
    Context.add(ns)
    Context.enter(ns)
    body
    Context.leave
  }

  def struct(body: => Unit) = {
    assert(Context.current.isInstanceOf[Namespace])

    val struct = new Structure(name.name, "")
    Context.add(struct)
    Context.enter(struct)
    body
    Context.leave
  }

  def as(datatype: Symbol) = {
    assert(Context.current.isInstanceOf[Structure])

    var field = new Field(name.name, datatype.name, "")
    Context.add(field)
  }
}
