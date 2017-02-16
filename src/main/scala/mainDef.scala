import scala.tools.nsc._
import scala.tools.nsc.interpreter._

object mainDef {

  var content = new StringBuilder
  var requireSet = Set[String]()

  def resolveComment(line: String): Boolean = {
    var result = false
    if(line.contains("//") || line.contains("///")){
      if(line.contains("//") && !line.contains("///")) content.append("!\"\"\"" + line.substring(line.indexOf("//")+2,line.size) + "\"\"\"\n")
      result = true
    }
    result
  }

  def resolveLine(line: String){
    val regex = ".*require\\(.*\\).*".r
    if((regex findFirstIn line) != None)
      scala.io.Source.fromFile(line.split("\"")(1)).getLines.foreach{line =>
        if((regex findFirstIn line) == None){ if (!resolveComment(line)) content.append(s"$line\n") }
        else if(!requireSet.contains(line.split("\"")(1))){ requireSet += line.split("\"")(1) ; resolveLine(line) }
      }
  }
  
  def main(args: Array[String]) {
   
    if(args.length > 1) throw new IllegalArgumentException(s"There mast be only one filepath")
    
    val settings = new Settings()
    settings.classpath.append("build/compiler")
    // settings.classpath.append("libs/scala-libs_2.11.6.jar")
    // settings.classpath.append("libs/scala-2.12/jline-2.14.1.jar:libs/scala-2.12/scala-compiler.jar:libs/scala-2.12/scala-library.jar:libs/scala-2.12/scala-parser-combinators_2.12-1.0.4.jar:libs/scala-2.12/scala-reflect.jar:libs/scala-2.12/scala-swing_2.12-2.0.0-M2.jar:libs/scala-2.12/scala-xml_2.12-1.0.6.jar:libs/scala-2.12/scalap-2.12.1.jar:libs/scala-2.11/scala-parser-combinators-2.11.0-M4.jar")
    settings.classpath.append("libs/scala-parser-combinators_2.12-1.0.4.jar")
    // println("mainDef: " + settings.classpath)
    settings.usejavacp.value = true    
    val writer = new java.io.StringWriter()
    val imain = new IMain(settings, new java.io.PrintWriter(writer))

    content.append("import dslc.CDSL._ \ndefine {\n")
    scala.io.Source.fromFile(args(0)).getLines.foreach{line => resolveLine(line)}
    content.append("}")
    val status = imain.interpret(content.toString)
    if(status.toString != "Success")
      println(writer.toString())
  
  }

}
