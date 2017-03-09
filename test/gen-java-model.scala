import dslg.gdsl._

def jtype(field: Field): String = {

  val baseType = field.datatype match {
    case s: StringDatatype => "java.lang.String"
    case i: IntDatatype    => "java.lang.Integer"
    case f: FloatDatatype  => "java.lang.Double"
    case b: BoolDatatype   => "java.lang.Boolean"
    case u: UuidDatatype   => "java.util.UUID"
    case d: DateDatatype   => "java.util.Date"
    case _ => field.datatype
      if(field.datatype != null && field.datatype.isInstanceOf[Structure]){
        var list = field("datatype").split("::")
        list = list.take(list.length -1) :+ field.datatype.name.toPascal
        list.mkString(".")
      }
      else
        throw new IllegalArgumentException(s"Invalid datatype: {field.datatype}")
  }

  field.modifier match {
    case "required" => baseType
    case "optional" => s"java.util.Option<$baseType>"
    case "repeated" => s"java.util.List<$baseType>"
    case x => throw new IllegalArgumentException(s"Invalid modifier: $x")
  }
}

def comment(e: Entity): Unit = { if(e.comment != "") ln(s"/** ${e.comment} */") }

generate {

  begin ALL { root =>
    options.blockStart = "{"
    options.blockEnd = "}"
    options.currentDir = config.output
  }

  begin STRUCTURE { structure =>

    file(s"${structure.path}/${structure.name.toPascal}.java") {
      ln(s"package ${structure.path};")
      comment(structure)
      block(s"public class ${structure.name.toPascal}") {
        structure.fields.foreach { f =>
          comment(f)
          ln(s"private ${jtype(f)} ${f.name.toCamel};")
        }
        structure.fields.foreach { f =>
          ln(s"/** get method for ${f.name.toCamel} */")
          block(s"public ${jtype(f)} get${f.name.toPascal}()"){
            ln(s"return this.${f.name.toCamel};")
          }
          ln(s"/** set method for ${f.name.toCamel} */")
          block(s"public void set${f.name.toPascal}( ${jtype(f)} ${f.name.toCamel})"){
            ln(s"this.${f.name.toCamel} = ${f.name.toCamel};")
          }
        }
      }
    }
  }
}
