import dslg.gdsl._

def functionUrl(f: Function): String = {
  var list = List[String]()
  f.url.split("/").map { case s =>
    f.urlArgs.foreach { urlarg =>
      if(urlarg.name == s)
        list = list ::: List(":" + s)
    }
    if(!list.contains(":" + s))
      list = list ::: List(s)
  }
  list.mkString("/")
}

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

  f.urlArgs.foreach { f =>
    params = params ::: List( s"""UUID.fromString(req.params(":""" + f.name + """"))""")
  }

  f.fields.foreach{f =>
    params = params ::: List(s"""new Gson().fromJson(req.body(), am.iunetworks.ppcm.api.${f.path}.${f.datatype.name.toPascal}.class)""")
  }
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

def loggerData(service: Service, f: Function): String = {
  var result = s""""${f.method}: ${service.serviceUrl}""""
  var list = List[String]()
  f.url.split("/").map { case s =>
    f.urlArgs.foreach { urlarg =>
      if(urlarg.name == s)
        list = list ::: List(s""" + req.params(":$s")""")
    }
    if(!list.contains(s""" + req.params(":$s")"""))
      list = list ::: List(s)
  }
  result + list.mkString("""+"/"""")
}

 
generate {

  begin ALL { root =>
    options.blockStart = "{"
    options.blockEnd = "}"
    options.currentDir = config.output
  }

  begin SERVICE { service =>;
    file(s"iunetworks/spark/${service.name.toPascal}Api.java") {

      bigBlock(s"""package am.iunetworks.ppcm.api.spark;
        |
        |import java.util.UUID;
        |import javax.persistence.EntityNotFoundException;
        |import static spark.Spark.*;
        |import com.google.gson.Gson;
        |import am.iunetworks.ppcm.api.service.*;
        |import org.apache.log4j.Logger;
        |
        |""")

      block(s"public final class ${service.name.toPascal}Api") {

        block(s"""public static void register()"""){
          ln(s"""final Logger logger = Logger.getLogger(${service.name.toPascal}Api.class);\n""")
          service.functions.foreach { f =>
            ln(s"""${f.method}("${service.serviceUrl}${functionUrl(f)}", (req, res) -> {""")
            ln(s"""logger.info(${loggerData(service, f)});""")

            if(ftype(f) != "void")
              ln(s"""String jsonInString = "";""")
            block(s"try"){
              if(ftype(f) != "void"){
                f.modifier match {
                  case "required" =>
                    ln(s"jsonInString = new Gson().toJson(new ${service.name.toPascal}ServiceImpl().${f.name.toCamel}(${functionParams(f)}));")
                  case "repeated" =>
                    ln(s"jsonInString = new Gson().toJson(new ${service.name.toPascal}ServiceImpl().${f.name.toCamel}(${functionParams(f)}));")
                  case _ => ""
                }
              }
              else
                ln(s"new ${service.name.toPascal}Impl().${f.name.toCamel}(${functionParams(f)});")
            }
            block(s"catch (EntityNotFoundException e)"){
              ln(s"""halt(404, "{\\"message\\" : \\"not found\\"}");""")
            }
            block(s"catch (Throwable e)"){
              ln(s"""halt(500, "{\\"message\\" : \\"internal server error\\"}");""")
            }
            if(ftype(f) == "void")
              ln(s"""return "";""")
            else
              ln(s"return jsonInString;")
            ln(s"});\n")
          }
        }
      }
    }
  }
}


