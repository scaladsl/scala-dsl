import dslg.gdsl._

def urlArgType(urlArg: UrlArgument): String = {
  val baseType = urlArg.datatype match {
    case s: StringDatatype => "java.lang.String"
    case i: IntDatatype => "java.lang.Integer"
    case f: FloatDatatype => "java.lang.Double"
    case b: BoolDatatype => "java.lang.Boolean"
    case u: UuidDatatype => "java.util.UUID"
    case d: DateDatatype => "java.lang.Date"
    case _ => urlArg.datatype
      if(urlArg.datatype != null && urlArg.datatype.isInstanceOf[Structure]){
        var list = urlArg("datatype").split("::")
        list = list.take(list.length -1) :+ urlArg.datatype.name.toPascal
        list.mkString(".")
      }
      else
        throw new IllegalArgumentException(s"Invalid datatype: ${urlArg("datatype")}")
  }
  baseType
}



def functionParams(f: Function): String = {
  var params = List[String]()
  f.urlArgs.foreach{arg => params = params ::: List(urlArgType(arg) + " " + arg.name.toCamel) }
  f.fields.foreach{f => params = params ::: List(jtype(f) + " " + f.name.toCamel) }
  params.mkString(", ")
}

def ftype(function: Function): String = {
  val baseType = function.datatype match {
    case s: StringDatatype => "java.lang.String"
    case i: IntDatatype => "java.lang.Integer"
    case f: FloatDatatype => "java.lang.Double"
    case b: BoolDatatype => "java.lang.Boolean"
    case u: UuidDatatype => "java.util.UUID"
    case v: VoidDatatype => "java.lang.Void"
    case d: DateDatatype => "java.lang.Date"

    case _ => function.datatype
      if(function.datatype != null && function.datatype.isInstanceOf[Structure]){
        var list = function("return-datatype").split("::")
        list = list.take(list.length -1) :+ function.datatype.name.toPascal
        list.mkString(".")
      }
      else
        throw new IllegalArgumentException(s"Invalid datatype: ${function.datatype}")
  }

  function.modifier match {
    case "required" => baseType
    case "optional" => s"java.util.Option<$baseType>"
    case "repeated" => s"java.collection.List<$baseType>"
    case x => throw new IllegalArgumentException(s"Invalid modifier: $x")
  }
}

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
        throw new IllegalArgumentException(s"Invalid datatype: {field.datatype}")
  }

  field.modifier match {
    case "required" => baseType
    case "optional" => s"java.util.Option<$baseType>"
    case "repeated" => s"java.collection.List<$baseType>"
    case x => throw new IllegalArgumentException(s"Invalid modifier: $x")
  }
}

generate {

  begin ALL { root =>
    options.blockStart = "{"
    options.blockEnd = "}"
    options.currentDir = config.output
  }

  begin SERVICE { service =>

    file(s"${service.name.toPascal}.java") {
      ln(s"package ${service.path};")
      block(s"public interface ${service.name.toPascal}") {
        service.functions.foreach {f =>
          ln(s"public ${ftype(f)} ${f.name.toCamel}(${functionParams(f)}) throws java.lang.Throwable;")
        }
      }
    }

  }
}
