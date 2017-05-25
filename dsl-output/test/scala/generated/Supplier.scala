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
    
    val SUPPLIER = table(name("supplier"))
    val ID = field(name("supplier", "id"), classOf[UUID])
    val NAME = field(name("supplier", "name"), classOf[String])
    val EMAIL = field(name("supplier", "email"), classOf[String])
    val COUNTRY = field(name("supplier", "country"), classOf[String])
    val ARMEPS_ID = field(name("supplier", "armeps_id"), classOf[Integer])
    val TAXPAYER_ID = field(name("supplier", "taxpayer_id"), classOf[String])
    
    var connection = DriverManager.getConnection(DB_CONNECTION)
    
    try {
      
      connection.setAutoCommit(false)
      
      val create: DSLContext  = using(connection, SQLDialect.H2)
      
      create.createTable(SUPPLIER)
        .column(ID.getName(), SQLDataType.UUID)
        .column(NAME.getName(), SQLDataType.VARCHAR.length(256))
        .column(EMAIL.getName(), SQLDataType.VARCHAR.length(256))
        .column(COUNTRY.getName(), SQLDataType.VARCHAR.length(256))
        .column(ARMEPS_ID.getName(), SQLDataType.INTEGER)
        .column(TAXPAYER_ID.getName(), SQLDataType.VARCHAR.length(256))
        .execute()
      
      create.insertInto(SUPPLIER,
      ID,
      NAME,
      EMAIL,
      COUNTRY,
      ARMEPS_ID,
      TAXPAYER_ID)
      .values(
      UUID.randomUUID(),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      1,
      scala.util.Random.alphanumeric take 10 mkString(""))
      .values(
      UUID.randomUUID(),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      1,
      scala.util.Random.alphanumeric take 10 mkString(""))
      .values(
      UUID.randomUUID(),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      1,
      scala.util.Random.alphanumeric take 10 mkString(""))
      .values(
      UUID.randomUUID(),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      1,
      scala.util.Random.alphanumeric take 10 mkString(""))
      .values(
      UUID.randomUUID(),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      1,
      scala.util.Random.alphanumeric take 10 mkString(""))
      .execute()
      
      Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start()
      
      val result = create.select().from("supplier").fetch()
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
