package org.edsl

case class Identifier(symbol: Symbol) {

  def name() = symbol.name

  def toPascal(): String = name.split("_").map(_.capitalize).mkString("")

  def toCamel(): String = toPascal.substring(0, 1).toLowerCase + toPascal.substring(1)

  def toKebab() = name

  def namespace(body: => Unit) = {
    assert(Context.current.isInstanceOf[Namespace])

    Context.newNamespace(this) { ns =>
      body
    }
  }

  def struct(body: => Unit) = {
    assert(Context.current.isInstanceOf[Namespace])

    Context.newStructure(this) { st =>
      body
    }
  }

  def required(datatypeId: Identifier) = {
    Context.newField(this, Modifier.REQ, datatypeId)
  }

  /*def required(datatypeId: Array[Identifier]) = {
    val datatype = Context.current.global.findRelative(datatypeId)
    Context.newField(this, datatypeId, Modifier.REQ)
  }*/

  def optional(datatypeId: Identifier) = {
    Context.newField(this, Modifier.OPT, datatypeId)
  }

  def repeated(datatypeId: Identifier) = {
    Context.newField(this, Modifier.REP, datatypeId)
  }

  override def toString(): String =  name
}
