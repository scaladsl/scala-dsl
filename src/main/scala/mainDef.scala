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
  
  def main(args: Array[String]){
    val settings = new Settings()
    settings.classpath.append("./build")
    settings.usejavacp.value = true
    val writer = new java.io.StringWriter()
    val imain = new IMain(settings, new java.io.PrintWriter(writer))
    if(args.length > 1) throw new IllegalArgumentException(s"There mast be only one filepath")
    content.append("import dslc.CDSL._ \ndefine {\n")
    scala.io.Source.fromFile(args(0)).getLines.foreach{line => resolveLine(line)}
    content.append("}")
    val status = imain.interpret(content.toString)
    if(status.toString != "Success")
      println(writer.toString())
  }

}
