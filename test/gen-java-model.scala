import dslg.gdsl._

def jtype(field: Field): String = {

  val baseType = field.datatype match {
    case s: StringDatatype => "java.lang.String"
    case i: IntDatatype => "java.lang.Integer"
    case f: FloatDatatype => "java.lang.Double"
    case b: BoolDatatype => "java.lang.Boolean"
    case u: UuidDatatype => "java.util.UUID"
    case d: DateDatatype => "java.util.Date"
    case _ => field.datatype
      if(field.datatype != null && field.datatype.isInstanceOf[Structure]){
        var list = field("datatype").split("::")
        list = list.take(list.length -1) :+ field.datatype.name.toPascal
        list.mkString(".")
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

def comment(e: Entity): Unit = { if(e.comment != "") ln(s"/** ${e.comment} */") }

generate {
  begin ALL { root =>
    options.blockStart = "{"
    options.blockEnd = "}"
    options.currentDir = config.output
  }

  begin STRUCTURE { structure =>

    file(s"${structure.name.toPascal}.java") {
      ln(s"package ${structure.path};")
      comment(structure)
      block(s"public class ${structure.name.toPascal}") {
        structure.fields.foreach { f =>
          comment(f)
          ln(s"public ${jtype(f)} ${f.name.toCamel};")
        }
        ln(s"""public ${structure.name.toPascal}() {}""")
        var consParam = structure.fields.map(f=> s"${jtype(f)} ${f.name.toCamel}").mkString(", ")
        block(s"""public ${structure.name.toPascal}($consParam) """){
          structure.fields.foreach{f=>
            ln(s"this.${f.name.toCamel} = ${f.name.toCamel};")
          }
        }
         //if(jtype(f) == "java.util.UUID" || jtype(f) == "java.util.Date")
        block(s"public String toString()") {
          var str = structure.fields.map(f => s""" \"${f.name}: '\" + this.${f.name.toCamel}""").mkString("\"'\" +")
          ln(s"return ${str};")
        }
      }
    }

  }

}
