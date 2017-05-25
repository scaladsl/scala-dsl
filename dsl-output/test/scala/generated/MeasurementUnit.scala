import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.UUID

import org.h2.tools.Server

import org.jooq._
import org.jooq.impl._
import org.jooq.impl.DSL._
import org.jooq.impl.SQLDataType._

object Form {
  
  def create(DB_DRIVER: String, DB_CONNECTION: String) {
    
    val MEASUREMENT_UNIT = table(name("measurement_unit"))
    val ID = field(name("measurement_unit", "id"), classOf[UUID])
    val CODE = field(name("measurement_unit", "code"), classOf[Integer])
    val NAME_HY = field(name("measurement_unit", "name_hy"), classOf[String])
    val NAME_EN = field(name("measurement_unit", "name_en"), classOf[String])
    val NAME_RU = field(name("measurement_unit", "name_ru"), classOf[String])
    
    var connection = DriverManager.getConnection(DB_CONNECTION)
    
    try {
      
      connection.setAutoCommit(false)
      
      val create: DSLContext  = using(connection, SQLDialect.H2)
      
      create.createTable(MEASUREMENT_UNIT)
        .column(ID.getName(), SQLDataType.UUID)
        .column(CODE.getName(), SQLDataType.INTEGER)
        .column(NAME_HY.getName(), SQLDataType.VARCHAR.length(256))
        .column(NAME_EN.getName(), SQLDataType.VARCHAR.length(256))
        .column(NAME_RU.getName(), SQLDataType.VARCHAR.length(256))
        .execute()
      
      create.insertInto(MEASUREMENT_UNIT,
      ID,
      CODE,
      NAME_HY,
      NAME_EN,
      NAME_RU)
      .values(
      UUID.randomUUID(),
      1,
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""))
      .values(
      UUID.randomUUID(),
      1,
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""))
      .values(
      UUID.randomUUID(),
      1,
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""))
      .values(
      UUID.randomUUID(),
      1,
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""))
      .values(
      UUID.randomUUID(),
      1,
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""))
      .execute()
      
      Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start()
      
      val result = create.select().from("measurement_unit").fetch()
      result.forEach(println(_))
      
      connection.commit();
      
    }
    catch {
      
      case s: SQLException => println("Exception Message " + s.getLocalizedMessage())
      case e: Exception => e.printStackTrace()
      
    }
    finally {
      connection.close()
    }
    
  }
  
}
