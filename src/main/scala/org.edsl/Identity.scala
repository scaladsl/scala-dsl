package org.edsl

case class Identity(symbol: Symbol) {

  def name() = symbol.name

  def toPascal(): String = name.split("_").map(_.capitalize).mkString("")

  def toCamel(): String = toPascal.substring(0, 1).toLowerCase + toPascal.substring(1)

  def toKebab() = name

  def toUpper() = name.toUpperCase

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

  def enum(body: => Unit) = {
    assert(Context.current.isInstanceOf[Namespace])

    Context.newEnumeration(this) { en =>
      body
    }
  }

  def is(value: Int) = {
    Context.newConstant(this, value)
  }

  def required(datatypeId: Identity): Unit = {
    Context.newField(this, Modifier.REQ, datatypeId)
  }

  def required(datatypeId: List[Identity]): Unit = {
    
  }

  def optional(datatypeId: Identity): Unit = {
    Context.newField(this, Modifier.REQ, datatypeId)
  }

  def repeated(datatypeId: Identity): Unit = {
    Context.newField(this, Modifier.REP, datatypeId)
  }

  override def toString(): String = name
}
