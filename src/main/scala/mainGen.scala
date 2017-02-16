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
    settings.classpath.append("./build/generator");
    // settings.classpath.append("libs/scala-libs_2.11.6.jar")
    // settings.classpath.append("libs/akka-actor_2.11-2.3.4.jar:libs/config-1.2.1.jar:libs/jline-2.12.1.jar:libs/scala-actors-2.11.0.jar:libs/scala-actors-migration_2.11-1.1.0.jar:libs/scala-compiler.jar:libs/scala-continuations-library_2.11-1.0.2.jar:libs/scala-continuations-plugin_2.11.5-1.0.2.jar:libs/scala-library.jar:libs/scala-libs_2.11.6.jar:libs/scalap-2.11.6.jar:libs/scala-parser-combinators_2.11-1.0.3.jar:libs/scala-reflect.jar:libs/scala-swing_2.11-1.0.1.jar:libs/scala-xml_2.11-1.0.3.jar")
    // settings.classpath.append("libs/scala-2.12/jline-2.14.1.jar:libs/scala-2.12/scala-compiler.jar:libs/scala-2.12/scala-library.jar:libs/scala-2.12/scala-parser-combinators_2.12-1.0.4.jar:libs/scala-2.12/scala-reflect.jar:libs/scala-2.12/scala-swing_2.12-2.0.0-M2.jar:libs/scala-2.12/scala-xml_2.12-1.0.6.jar:libs/scala-2.12/scalap-2.12.1.jar:libs/scala-2.11/scala-parser-combinators-2.11.0-M4.jar")
    settings.classpath.append("libs/scala-parser-combinators_2.12-1.0.4.jar")
    // println("mainGen: " + settings.classpath)
    settings.usejavacp.value = true
    val writer = new java.io.StringWriter()
    val imain = new IMain(settings, new java.io.PrintWriter(writer))

    var statusConf = imain.interpret(config.toString)
    if(statusConf.toString != "Success")
      println(writer.toString())

    val content = scala.io.Source.fromFile(file).mkString
    var statusCont = imain.interpret(content)
    if(statusCont.toString != "Success")
      println(writer.toString())
    
  }

}

