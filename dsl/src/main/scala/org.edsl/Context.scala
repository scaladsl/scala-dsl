package org.edsl

object Context {

  val global = new Entry(new Identity('__global__))
/*global :+ Integer
  global :+ String
  global :+ Boolean
  global :+ Float
 */

  var leaf: Entry = global

  def current(): Entry = leaf

  def newNamespace(id: Identity)(body: Entry => Unit): Unit = {
   // block(s"""begin namespace "${id.name}"""") {
      val namespace = new Entry(id)
      namespace.name = "namespace"
      namespace.parent = current
      current.children :+ namespace
      leaf = namespace
      body(namespace)
      leaf = namespace.parent
    //}
  }

  def newStructure(id: Identity)(body: Entry => Unit): Unit = {
    val structure = new Entry(id)
    val comment = Comment.reset()
    // if (comment != "")
    //   ln(s"""set comment "${comment}"""")
    //   block(s"""begin structure "${id.name}"""") {
       structure.name = "structure"
       structure.parent = current
       current.children :+ structure
       leaf = structure
       body(structure)
       leaf = structure.parent
//    }
  }

  def newEnumeration(id: Identity)(body: Entry => Unit): Unit = {
  //    block(s"""enum "${id.name}"""") {
       val enumeration = new Entry(id)
       val comment = Comment.reset()
       enumeration.parent = current
       current :+ enumeration
       leaf = enumeration
       body(enumeration)
       leaf = enumeration.parent
    //}
  }

  def newField(name: Identity, modifier: Modifier, datatype: Datatype): Unit = {
    assert(current.isInstanceOf[Entry])
    val field = new Field(name, modifier, datatype)
   // field.comment = Comment.reset()

    // block(s"""field "${name.name()}"""") {
    //   if (field.comment != "")
    //     ln(s"""set comment "${field.comment}"""")
    //     ln(s"""set type = ${datatype.path.map(e => e.id).mkString(".")}""")
    //     ln(s"""set modifier = ${modifier.id.name}""")
    // }
    current :+ field
  }

  def newConstant(name: Identity, value: Int): Unit = {
    assert(current.isInstanceOf[Entry])

//    ln(s"""${name.name} = ${value}""")

    val econst = new Constant(name, value)
 //   econst.comment = Comment.reset()
    current :+ econst
  }

  // private val INDENT = "    "
  // private var indentLevel = 0

  // private def indentation: String = {
  //   INDENT * indentLevel
  // }

  // def block(str: String)(body: => Unit): Unit = {
  //   print(indentation)
  //   println("begin " + str)

  //   indentLevel += 1
  //   body
  //   indentLevel -= 1

  //   print(indentation)
  //   println("end")
  // }

  // def ln(str: String = ""): Unit = {
  //   print(indentation)
  //   println(str)
  // }
}
