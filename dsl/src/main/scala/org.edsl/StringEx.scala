package org.edsl

/**
 * The class provides more properties for String
 */
class StringEx(val s: String) {

  def unary_! : Unit = {
    Comment.append(s)
  }

}

/**
 * Provides property to write comments
 */
object Comment {
  private var value = ""

  def reset(): String = {
    val temp = value
    value = ""
    temp
  }

  def append(comment: String): Unit = {
    value += comment
  }
}
