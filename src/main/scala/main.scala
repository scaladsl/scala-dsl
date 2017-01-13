import java.io._
import scala.io.Source._

object define {
    
    val writer = new java.io.PrintWriter(new java.io.File("./test/define.scala"))
    var requireSet = Set[String]()

    def resolveComment(line: String): Boolean = {
        var result = false 
        if(line.contains("//") || line.contains("///")){
            if(line.contains("//") && !line.contains("///")) writer.write("!\"\"\"" + line.substring(line.indexOf("//")+2,line.size) + "\"\"\"\n")
            result = true
        }
        result    
    }

    def resolveLine(line: String){
        val regex = ".*require\\(.*\\).*".r
        if((regex findFirstIn line) != None)
          io.Source.fromFile(line.split("\"")(1)).getLines.foreach{line =>
            if((regex findFirstIn line) == None){ if (!resolveComment(line)) writer.write(s"$line\n") } 
            else if(!requireSet.contains(line.split("\"")(1))){ requireSet += line.split("\"")(1) ; resolveLine(line) }
          }
    }
        
    def main(args: Array[String]){
        writer.write("import sgf.CDSL._\ndefine {\n")
        io.Source.fromFile("./test/def.scala").getLines.foreach{line => resolveLine(line)}
        writer.write("}\n")
        writer.close()
        sys.process.Process("scala -cp build -savecompiled ./test/define.scala").!
    }
}
