package org.edsl

/**
  * The class representing type defined in SDL. 
  */
abstract class Datatype(id: Identifier) extends Entity(id)

// primitives
class Primitive(id: Identifier) extends Datatype(id) { parent = Context.global }
object Integer extends Primitive(new Identifier('int))
object Boolean extends Primitive(new Identifier('bool))
object Float   extends Primitive(new Identifier('float))
object String  extends Primitive(new Identifier('string))
object Date    extends Primitive(new Identifier('date))

class Composite(id: Identifier) extends Datatype(id)

/**
  * The class representing a structure described in SDL.
  *
  * @param id     Structure identity.
  * @param parent Parent entity.
  */
class Structure(id: Identifier) extends Composite(id) with Container {
  protected var items = List[Entity]()

  /**
   * Gets all the fileds of this structure.
   */
  def fields(): List[Field] = filter[Field]
}

/**
  * The class representing a constant within enumeration.
  */
class Constant(id: Identifier, val value: Int) extends Entity(id)

/**
  * The class representing enumeration described in SDL.
  * 
  * @param id     Enumeration identity.
  * @param parent Parent entity.
  */
class Enumeration(id: Identifier) extends Composite(id) with Container {
  protected var items = List[Entity]()
 
  /**
   * Gets all the values of this enumeration.
   */
  def constants(): List[Constant] = filter[Constant]
}


