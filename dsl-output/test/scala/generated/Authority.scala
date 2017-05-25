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
    
    val AUTHORITY = table(name("authority"))
    val ID = field(name("authority", "id"), classOf[UUID])
    val CODE = field(name("authority", "code"), classOf[String])
    val NAME_HY = field(name("authority", "name_hy"), classOf[String])
    val NAME_EN = field(name("authority", "name_en"), classOf[String])
    val NAME_RU = field(name("authority", "name_ru"), classOf[String])
    val REGIONAL = field(name("authority", "regional"), classOf[Boolean])
    
    var connection = DriverManager.getConnection(DB_CONNECTION)
    
    try {
      
      connection.setAutoCommit(false)
      
      val create: DSLContext  = using(connection, SQLDialect.H2)
      
      create.createTable(AUTHORITY)
        .column(ID.getName(), SQLDataType.UUID)
        .column(CODE.getName(), SQLDataType.VARCHAR.length(256))
        .column(NAME_HY.getName(), SQLDataType.VARCHAR.length(256))
        .column(NAME_EN.getName(), SQLDataType.VARCHAR.length(256))
        .column(NAME_RU.getName(), SQLDataType.VARCHAR.length(256))
        .column(REGIONAL.getName(), SQLDataType.BOOLEAN)
        .execute()
      
      create.insertInto(AUTHORITY,
      ID,
      CODE,
      NAME_HY,
      NAME_EN,
      NAME_RU,
      REGIONAL)
      .values(
      UUID.randomUUID(),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      true)
      .values(
      UUID.randomUUID(),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      true)
      .values(
      UUID.randomUUID(),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      true)
      .values(
      UUID.randomUUID(),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      true)
      .values(
      UUID.randomUUID(),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      true)
      .execute()
      
      Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start()
      
      val result = create.select().from("authority").fetch()
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
