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
    
    val CPV = table(name("cpv"))
    val ID = field(name("cpv", "id"), classOf[UUID])
    val PARENT_ID = field(name("cpv", "parent_id"), classOf[UUID])
    val CODE = field(name("cpv", "code"), classOf[String])
    val NAME_HY = field(name("cpv", "name_hy"), classOf[String])
    val NAME_RU = field(name("cpv", "name_ru"), classOf[String])
    val NAME_EN = field(name("cpv", "name_en"), classOf[String])
    val ARMEPS_ID = field(name("cpv", "armeps_id"), classOf[Integer])
    val PATH = field(name("cpv", "path"), classOf[String])
    
    var connection = DriverManager.getConnection(DB_CONNECTION)
    
    try {
      
      connection.setAutoCommit(false)
      
      val create: DSLContext  = using(connection, SQLDialect.H2)
      
      create.createTable(CPV)
        .column(ID.getName(), SQLDataType.UUID)
        .column(PARENT_ID.getName(), SQLDataType.UUID)
        .column(CODE.getName(), SQLDataType.VARCHAR.length(256))
        .column(NAME_HY.getName(), SQLDataType.VARCHAR.length(256))
        .column(NAME_RU.getName(), SQLDataType.VARCHAR.length(256))
        .column(NAME_EN.getName(), SQLDataType.VARCHAR.length(256))
        .column(ARMEPS_ID.getName(), SQLDataType.INTEGER)
        .column(PATH.getName(), SQLDataType.VARCHAR.length(256))
        .execute()
      
      create.insertInto(CPV,
      ID,
      PARENT_ID,
      CODE,
      NAME_HY,
      NAME_RU,
      NAME_EN,
      ARMEPS_ID,
      PATH)
      .values(
      UUID.randomUUID(),
      UUID.randomUUID(),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      1,
      scala.util.Random.alphanumeric take 10 mkString(""))
      .values(
      UUID.randomUUID(),
      UUID.randomUUID(),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      1,
      scala.util.Random.alphanumeric take 10 mkString(""))
      .values(
      UUID.randomUUID(),
      UUID.randomUUID(),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      1,
      scala.util.Random.alphanumeric take 10 mkString(""))
      .values(
      UUID.randomUUID(),
      UUID.randomUUID(),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      1,
      scala.util.Random.alphanumeric take 10 mkString(""))
      .values(
      UUID.randomUUID(),
      UUID.randomUUID(),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      scala.util.Random.alphanumeric take 10 mkString(""),
      1,
      scala.util.Random.alphanumeric take 10 mkString(""))
      .execute()
      
      Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start()
      
      val result = create.select().from("cpv").fetch()
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
