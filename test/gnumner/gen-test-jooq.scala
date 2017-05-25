import dslg.gdsl._

def scalaType(field: Field): String = {

  val baseType = field.datatype match {
    case s: StringDatatype => "String"
    case i: IntDatatype => "Integer"
    case f: FloatDatatype => "Double"
    case b: BoolDatatype => "Boolean"
    case u: UuidDatatype => "UUID"
    case d: DateDatatype => "Date"
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

def jooqType(field: Field): String = {

  val baseType = field.datatype match {
    case s: StringDatatype => "SQLDataType.VARCHAR.length(256)"
    case i: IntDatatype => "SQLDataType.INTEGER"
    case f: FloatDatatype => "SQLDataType.DOUBLE"
    case b: BoolDatatype => "SQLDataType.BOOLEAN"
    case u: UuidDatatype => "SQLDataType.UUID"
    case d: DateDatatype => "SQLDataType.Date"
    case x => throw new IllegalArgumentException(s"Invalid datatype: {field.datatype}")
  }

  baseType
}

def jooqTypeValue(field: Field): String = {

  val baseType = field.datatype match {
    case s: StringDatatype => """scala.util.Random.alphanumeric take 10 mkString("")"""
    case i: IntDatatype => "1"
    case f: FloatDatatype => "1.0"
    case b: BoolDatatype => "true"
    case u: UuidDatatype => "UUID.randomUUID()"
    case d: DateDatatype => "SQLDataType.Date"
    case x => throw new IllegalArgumentException(s"Invalid datatype: {field.datatype}")
  }

  baseType
}


generate {

  begin ALL { root =>
    options.blockStart = "{"
    options.blockEnd = "}"
    options.currentDir = config.output
  }

  begin STRUCTURE { structure =>

    file(s"test/scala/generated/${structure.name.toPascal}.scala") {
      ln("import java.sql.Connection")
      ln("import java.sql.DriverManager")
      ln("import java.sql.SQLException")
      ln("import java.util.UUID")
      ln("")
      ln("import org.h2.tools.Server")
      ln("")
      ln("import org.jooq._")
      ln("import org.jooq.impl._")
      ln("import org.jooq.impl.DSL._")
      ln("import org.jooq.impl.SQLDataType._")
      ln("")
      block(s"object ${structure.name.toCamel} ") {
        ln("")
        block("def create(DB_DRIVER: String, DB_CONNECTION: String) ") {
          ln("")
          ln(s"""val ${structure.name.toUpperCase()} = table(name("${structure.name}"))""")
          structure.fields.foreach { f =>
            ln(s"""val ${f.name.toUpperCase()} = field(name("${structure.name}", "${f.name}"), classOf[${scalaType(f)}])""")
          }
          ln("")
          ln("var connection = DriverManager.getConnection(DB_CONNECTION)")
          ln("")
          block("try ") {
            ln("")
            ln("connection.setAutoCommit(false)")
            ln("")
            ln("val create: DSLContext  = using(connection, SQLDialect.H2)")
            ln("")
            ln(s"create.createTable(${structure.name.toUpperCase()})")
            structure.fields.foreach { f =>
              ln(s"  .column(${f.name.toUpperCase()}.getName(), ${jooqType(f)})")
            }
            ln("  .execute()")
            ln("")
            ln(s"create.insertInto(${structure.name.toUpperCase()},")
            structure.fields.dropRight(1).foreach { f =>
              ln(s"${f.name.toUpperCase()},")
            }
            ln(s"${structure.fields.last.name.toUpperCase()})")
            1 to 5 foreach { _ =>
              ln(".values(")
              structure.fields.dropRight(1).foreach { f =>
                ln(s"""${jooqTypeValue(f)},""")
              }
              ln(s"${jooqTypeValue(structure.fields.last)})")
            }
            ln(".execute()")
            ln("")
            ln("""Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start()""")
            ln("")
            ln(s"""val result = create.select().from("${structure.name}").fetch()""")
            ln("result.forEach(println(_))")
            ln("")
            ln("connection.commit();")
            ln("")
          }
          block("catch ") {
            ln("")
            ln("""case s: SQLException => println("Exception Message " + s.getLocalizedMessage())""")
            ln("case e: Exception => e.printStackTrace()")
            ln("")
          }
          block("finally ") {
            ln("connection.close()")
          }
          ln("")
        }
        ln("")
      }
    }
  }
}
