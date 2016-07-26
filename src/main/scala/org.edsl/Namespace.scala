package org.edsl

import scala.collection.immutable.List

class Namespace (name: String) extends Container(name) {

  def structures(): List[Structure] = filter[Structure]()
  def namespaces(): List[Namespace] = filter[Namespace]()

}
