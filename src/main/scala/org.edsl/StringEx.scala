package org.edsl

class StringEx(val s: String) {

  def unary_! : Unit = {
    Comment.append(s)
  }
}

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
