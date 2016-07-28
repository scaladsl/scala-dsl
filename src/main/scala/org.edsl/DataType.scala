package org.edsl

/**
 * Represents a field data type.
 */
trait DataType {

  /** Indicates whether this type is primitive. */
  def isPrimitive(): Boolean

  /** Indicates whether this type is composite (not primitive). */
  def isComposite() = !isPrimitive
}

class Primitive extends DataType {
  def isPrimitive = true
}

class Composite extends DataType {
  def isPrimitive = false
}

object string extends Primitive
object int extends Primitive
object bool extends Primitive
object float extends Primitive
