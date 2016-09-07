package org.edsl

class Modifier(val id: Symbol)

object Modifier {

  /**
   * A type modifier representing a required data.
   */
  val REQ = new Modifier('required)

  /**
   * A type modifier representing an optional/nullable data.
   */
  val OPT = new Modifier('optional)

  /**
   * A type modifier representing an repeated data (list/array).
   */
  val REP = new Modifier('repeated)
}

/**
 * The class representing a structure field.
 *
 * @param id the Field identity.
 * @param modifier the type modifier.
 * @param datatype the field type.
 */
class Field(id: Identity, val modifier: Modifier, val datatype: Datatype) extends Entity(id) {

  /**
   * Idicates whether this filed is required.
   */
  def isRequired(): Boolean = modifier == Modifier.REQ

  /**
   * Idicates whether this filed is optional.
   */
  def isOptional(): Boolean = modifier == Modifier.OPT

  /**
   * Idicates whether this filed is repeated.
   */
  def isRepeated(): Boolean = modifier == Modifier.REP

}
