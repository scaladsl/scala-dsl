package org.edsl

/**
 * Represents a field of structure.
 *
 * @param name     Field name.
 * @param dataType Field type.
 */
class Field(name: String, val dataType: DataType) extends Entity(name)
