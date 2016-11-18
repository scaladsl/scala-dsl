package sgf

import sgf.GDSL._
import sgf.GDSL.Lang._

object Java {

  implicit class JavaField(field: Field) {
    def setterName(): String = s"set${field.name.toPascal}"

    def getterName(): String = s"get${field.name.toPascal}"

    def argName(): String = field.name.toCamel

    def typeName(): String = {

      val baseType = field.datatype match {
        case s: StringDatatype => "java.lang.String"
        case i: IntDatatype => "java.lang.Integer"
        case f: FloatDatatype => "java.lang.Double"
        case b: BoolDatatype => "java.lang.Boolean"
        case _ => field.datatype
          if(field.datatype != null && field.datatype.isInstanceOf[Structure]){
            // field.datatype.name.toPascal
            field("datatype").replace("::",".").toString
          }
          else
            throw new IllegalArgumentException(s"Invalid datatype: ${field.datatype}")
      }

      field.modifier match {
        case "required" => baseType
        case "optional" => s"java.util.Option<$baseType>"
        case "repeated" => s"java.collection.List<$baseType>"
        case x => throw new IllegalArgumentException(s"Invalid modifier: $x")
      }
    }
  }

  def comment(e: Entity): Unit = {
    if(e.comment != "") ln(s"/** ${e.comment} */")
  }
}
