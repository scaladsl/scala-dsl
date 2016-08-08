package org.edsl

class StringEx(val s: String) {

  // PascalCase
  def toPascal: String = s.split("_").map(_.capitalize).mkString("")

  // camelCase
  def toCamel: String =
    if (s.length > 0)
      toPascal.substring(0, 1).toLowerCase + toPascal.substring(1)
    else
      s

  // unary operator
  def unary_! = new StringEx(s)

}
