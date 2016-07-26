package org.edsl

import scala.collection._

/**
 * Represents a Structure entity.
 *
 * @param name: Structure name.
 */
class Structure(name: String) extends Container(name) {

  /**
   * Filters children by the specified type.
   */
  def fields(): List[Field] = filter[Field]()

}
