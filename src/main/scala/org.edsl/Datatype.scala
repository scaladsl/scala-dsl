package org.edsl

/**
  * The class representing type defined in SDL. 
  */
abstract class Datatype(id: Identity) extends Entity(id)

// primitives
class Primitive(id: Identity) extends Datatype(id) { parent = Context.global }
object Integer extends Primitive(new Identity('int))
object Boolean extends Primitive(new Identity('bool))
object Float   extends Primitive(new Identity('float))
object String  extends Primitive(new Identity('string))
object Date    extends Primitive(new Identity('date))

class Composite(id: Identity) extends Datatype(id)

/**
  * The class representing a structure described in SDL.
  *
  * @param id     Structure identity.
  * @param parent Parent entity.
  */
class Structure(id: Identity) extends Composite(id) with Container {
  protected var items = List[Entity]()

  /**
   * Gets all the fileds of this structure.
   */
  def fields(): List[Field] = filter[Field]
}

/**
  * The class representing a constant within enumeration.
  */
class Constant(id: Identity, val value: Int) extends Entity(id)

/**
  * The class representing enumeration described in SDL.
  * 
  * @param id     Enumeration identity.
  * @param parent Parent entity.
  */
class Enumeration(id: Identity) extends Composite(id) with Container {
  protected var items = List[Entity]()
 
  /**
   * Gets all the values of this enumeration.
   */
  def constants(): List[Constant] = filter[Constant]
}
