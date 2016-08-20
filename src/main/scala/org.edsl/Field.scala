package org.edsl

/**
 * Represents a field of structure.
 *
 * @param name     Field name.
 * @param datatype Field type.
 */
class Field(name: String, val datatype: String, comment: String) extends Entity(name, comment)
