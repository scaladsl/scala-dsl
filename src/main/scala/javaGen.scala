package sgf

import sgf.GDSL._
import sgf.GDSL.Lang._
import java.lang.Void._
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
        case u: UuidDatatype => "java.util.UUID"
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

  }

  def comment(e: Entity): Unit = {
    if(e.comment != "") ln(s"/** ${e.comment} */")
  }

  implicit class JavaFunction(function: Function) {

    var params: String = ""
    var urlArgMap = Map[String, String]()
    var argMap = Map[String, String]()
    var argParamName = ""

    def functionTypeName(): String = {
      val baseType = function.datatype match {
        case s: StringDatatype => "java.lang.String"
        case i: IntDatatype => "java.lang.Integer"
        case f: FloatDatatype => "java.lang.Double"
        case b: BoolDatatype => "java.lang.Boolean"
        case u: UuidDatatype => "java.util.UUID"
        case v: VoidDatatype => "java.lang.Void"
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

    function.urlArgs.foreach{arg => urlArgMap = urlArgMap + (arg.urlArgTypeName -> arg.name) }
    function.args.foreach{arg => argMap = argMap + (arg.argTypeName -> arg.name) }

    if(argMap.size > 1) {
      argMap.foreach { arg =>
        params = params + arg._1 + " " + arg._2 + ", "
        argParamName = argParamName + arg._2
      }
      params = params.substring(0, params.size-2)
    } else argMap.foreach { arg=>
        argParamName = argParamName + arg._2
        params = params + arg._1 + " " + arg._2
      }

    if(urlArgMap.size > 1) {
      urlArgMap.foreach { arg =>
        params = params + arg._1 + " " + arg._2 + ", "
        argParamName = argParamName + arg._2
      }
      params = params.substring(0, params.size-2)
    } else urlArgMap.foreach{arg=>
        argParamName = argParamName + arg._2
        params = params + arg._1 + " " + arg._2
      }
  }

  implicit class JavaUrlArg(urlArg: UrlArgument) {

    def urlArgTypeName(): String = {
      val baseType = urlArg.datatype match {
        case s: StringDatatype => "java.lang.String"
        case i: IntDatatype => "java.lang.Integer"
        case f: FloatDatatype => "java.lang.Double"
        case b: BoolDatatype => "java.lang.Boolean"
        case u: UuidDatatype => "java.util.UUID"
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

  }

  implicit class JavaArg(arg: Argument) {

    def argTypeName(): String = {
      val baseType = arg.datatype match {
        case s: StringDatatype => "java.lang.String"
        case i: IntDatatype => "java.lang.Integer"
        case f: FloatDatatype => "java.lang.Double"
        case b: BoolDatatype => "java.lang.Boolean"
        case u: UuidDatatype => "java.util.UUID"
        case _ => arg.datatype
          if(arg.datatype != null && arg.datatype.isInstanceOf[Structure]){
            var list = arg("datatype").split("::")
            list = list.take(list.length -1) :+ arg.datatype.name.toPascal
            list.mkString(".")
          }
          else
            throw new IllegalArgumentException(s"Invalid datatype: ${arg("datatype")}")
      }
      baseType
    }

  }

}
