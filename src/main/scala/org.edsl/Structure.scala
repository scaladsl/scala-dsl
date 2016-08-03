package org.edsl

/**
 * Represents a Structure entity.
 *
 * @param name Structure name.
 */
class Structure(name: String, comment: String) extends Entity(name, comment) with Container {

  protected var items = List[Entity]()

  /**
   * Filters children by the specified type.
   */
  def fields(): List[Field] = filter[Field]
}
