package org.edsl

class StringEx(val s: String) {

  def toPascal: String = s.split("_").map(_.capitalize).mkString("")

  def toCamel: String =
    if (s.length > 0)
      toPascal.substring(0, 1).toLowerCase + toPascal.substring(1)
    else
      s

  def unary_! : Unit = {
    Comment.append(s)
  }
}

object Comment {
  private var value = ""

  def reset(): String = {
    val temp = value
    value = null
    temp
  }

  def append(comment: String): Unit = {
    // TODO: check for null and join comments with new line
    value += comment
  }
}
