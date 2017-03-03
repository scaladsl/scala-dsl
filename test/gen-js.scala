import dslg.gdsl._

def functionParams(f: Function): String = {
  var params = List[String]()
  f.urlArgs.foreach{ arg => params = params ::: List(arg.name) }
  f.fields.foreach{f => params = params ::: List(f.name) }
  params.mkString(", ")
}

def functionFields(f: Function): String = {
  var params = List[String]()
  f.fields.foreach{f => params = params ::: List(f.name) }
  params.mkString(", ")
}

def ftype(function: Function): String = {
  val baseType = function.datatype match {
    case s: StringDatatype => "java.lang.String"
    case i: IntDatatype => "java.lang.Integer"
    case f: FloatDatatype => "java.lang.Double"
    case b: BoolDatatype => "java.lang.Boolean"
    case u: UuidDatatype => "java.util.UUID"
    case v: VoidDatatype => "void"
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
    case "repeated" => s"java.util.List<$baseType>"
    case x => throw new IllegalArgumentException(s"Invalid modifier: $x")
  }
}

def functionUrl(f: Function): String = {
  var list = List[String]()
  f.url.split("/").map { case s =>
    f.urlArgs.foreach { urlarg =>
      if(urlarg.name == s)
        list = list ::: List("'+" + s + "+'")
    }
    if(!list.contains("'+" + s + "+'"))
      list = list ::: List(s)
  }
  list.mkString("/")

}

generate {

  begin ALL { root =>
    options.blockStart = "{"
    options.blockEnd = "}"
    options.currentDir = config.output
  }

  begin SERVICE { service =>

    file(s"${service.name.toPascal}.js") {

      ln(s"""angular.module("app", ['ngRoute']).service("${service.name.toPascal}", ['""" + """$""" + s"""http', ${service.name.toPascal}]);""")
      block(s"function ${service.name.toPascal}("+"""$""" + "http)"){
        service.functions.foreach { f =>
          block(s"this.${f.name.toCamel} = function(${functionParams(f)})"){
            var funcFieldsList = functionFields(f)
            if(!funcFieldsList.isEmpty)
              ln("return $" + s"http.${f.method}('${service.serviceUrl}${functionUrl(f)}', ${functionFields(f)})")
            else
              ln("return $" + s"http.${f.method}('${service.serviceUrl}${functionUrl(f)}')")
          }
        }
      }

    }

  }

}
