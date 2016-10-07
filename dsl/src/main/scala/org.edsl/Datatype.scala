package org.edsl

/**
  * The class representing type defined in SDL.
  */
abstract class Datatype(id: Identity) extends Entry(id)

/**
  * The class represents primitive types
  *
  * @param id Identity
  */
class Primitive(id: Identity) extends Datatype(id) { val parent = Context.global }
object Integer extends Primitive(new Identity('int))
object Boolean extends Primitive(new Identity('bool))
object Float extends Primitive(new Identity('float))
object String extends Primitive(new Identity('string))
object Date extends Primitive(new Identity('date))

/**
  * The class represents user defined types
  *
  * @param id Identity
  */
class Composite(id: Identity) extends Datatype(id)
  
   /**
   * The class representing a structure described in SDL.
   *
   * @param id Structure identity.
   */
  /* class Structure(id: Identity) extends Composite(id) with Container {
   protected var items = List[Entity]()

   /**
   * Gets all the fileds of this structure.
   */
   def fields(): List[Field] = filter[Field]
   }

   /**
   * The class representing a constant within enumeration.
   *
   * @param id Identity
   * @param value
   */
   */
   class Constant(id: Identity, val value: Int) extends Entry(id)
/*
   /**
   * The class representing enumeration described in SDL.
   *
   * @param id Enumeration identity.
   */
   class Enumeration(id: Identity) extends Composite(id) with Container {

   /**
   * @param items Contains list of entities which are into this entity
   */
   protected var items = List[Entity]()

   /**
   * Gets all the values of this enumeration.
   */
   def constants(): List[Constant] = filter[Constant]
   }
   */
