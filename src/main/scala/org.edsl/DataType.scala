package org.edsl

trait DataType {
  def isPrimitive(): Boolean

  def isComposite() = !isPrimitive
}

class Primitive extends DataType {
  def isPrimitive = true
}

class Composite extends DataType {
  def isPrimitive = false
}

case class TString() extends Primitive
case class TInt() extends Primitive
case class TBool() extends Primitive
case class TFloat() extends Primitive
