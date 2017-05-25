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
    
    val FORM = table(name("form"))
    val ID = field(name("form", "id"), classOf[UUID])
    val CODE = field(name("form", "code"), classOf[String])
    val NAME_HY = field(name("form", "name_hy"), classOf[String])
    val ABBR_HY = field(name("form", "abbr_hy"), classOf[String])
    val NAME_EN = field(name("form", "name_en"), classOf[String])
    val ABBR_EN = field(name("form", "abbr_en"), classOf[String])
    val NAME_RU = field(name("form", "name_ru"), classOf[String])
    val ABBR_RU = field(name("form", "abbr_ru"), classOf[String])
    val OBSOLETE = field(name("form", "obsolete"), classOf[Boolean])
    
    var connection = DriverManager.getConnection(DB_CONNECTION)
    
    try {
      
      connection.setAutoCommit(false)
      
      val create: DSLContext  = using(connection, SQLDialect.H2)
      
      create.createTable(FORM)
        .column(ID.getName(), SQLDataType.UUID)
        .column(CODE.getName(), SQLDataType.VARCHAR.length(256))
        .column(NAME_HY.getName(), SQLDataType.VARCHAR.length(256))
        .column(ABBR_HY.getName(), SQLDataType.VARCHAR.length(256))
        .column(NAME_EN.getName(), SQLDataType.VARCHAR.length(256))
        .column(ABBR_EN.getName(), SQLDataType.VARCHAR.length(256))
        .column(NAME_RU.getName(), SQLDataType.VARCHAR.length(256))
        .column(ABBR_RU.getName(), SQLDataType.VARCHAR.length(256))
        .column(OBSOLETE.getName(), SQLDataType.BOOLEAN)
        .execute()
      
      create.insertInto(FORM,
      ID,
      CODE,
      NAME_HY,
      ABBR_HY,
      NAME_EN,
      ABBR_EN,
      NAME_RU,
      ABBR_RU,
      OBSOLETE)
      .values(
      UUID.randomUUID(),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
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
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      true)
      .execute()
      
      Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start()
      
      val result = create.select().from("form").fetch()
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
