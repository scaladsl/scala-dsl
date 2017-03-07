import java.io._
import scala.collection.mutable.Map
import scala.tools.nsc._
import scala.tools.nsc.interpreter._

object mainGen {

  def main(args: Array[String]) {

    var options = Map[String, String]()
    var file = ""

    args.foreach { a =>
      if ( a.startsWith("--") ) {
        val optKey = new StringBuilder(a.split('=')(0)).drop(2).toString
        val optVal = a.split('=')(1)
        options.put(optKey, optVal)
      } else {
        if(file != "") throw new IllegalArgumentException(s"There mast be only one filepath")
          file = a
      }
    }

    val config = new StringBuilder
    config.append("object config { ")
    options.foreach{ case (k, v) =>
      val value = v.replaceAll("\"","""\\"""")
      config.append(s"""val ${k} = "${value}";""")
    }
    config.append("}")
    val settings = new Settings()
    settings.classpath.append("./build/generator")
    settings.classpath.append("libs/scala-parser-combinators_2.12-1.0.4.jar")
    settings.usejavacp.value = true
    val writer = new java.io.StringWriter()
    val imain = new IMain(settings, new java.io.PrintWriter(writer))
    val content = scala.io.Source.fromFile(file).mkString
    var statusConf = imain.interpret(config.toString)
    if(statusConf.toString != "Success")
      println(writer.toString())
    var statusCont = imain.interpret(content)
    if(statusCont.toString != "Success")
      println(writer.toString())
    
  }
}

