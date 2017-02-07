import java.io._
import scala.collection.mutable.Map
import scala.tools.nsc._
import scala.tools.nsc.interpreter._

object mainGen {

  def main(args: Array[String]) {
    
    var options = Map[String, String]()
    var files = List[String]()

    args.foreach { a =>
      if ( a.startsWith("--") ) {
        val optKey = new StringBuilder(a.split('=')(0)).drop(2).toString
        val optVal = a.split('=')(1)
        options.put(optKey, optVal)
      } else {
        files = files :+ a
      }
    }

    val config = new StringBuilder

    config.append("object config { ")

    options.foreach{ case (k, v) =>
      config.append(s"""val ${k} = "${v}";""")
    }

    config.append("}")

    val settings = new Settings()

    settings.classpath.append("./build");
    settings.usejavacp.value = true

    val writer = new java.io.StringWriter()
    val imain = new IMain(settings, new java.io.PrintWriter(writer))

    files.foreach { f =>
      val content = scala.io.Source.fromFile(f).mkString
      var statusConf = imain.interpret(config.toString)
      if(statusConf.toString != "Success")
        println(writer.toString())
      var statusCont = imain.interpret(content)
      if(statusCont.toString != "Success")
        println(writer.toString())
    }
    
  }

}

