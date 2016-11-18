import sgf.GDSL._
import sgf.GDSL.Lang._
import sgf.Java._

generate {

  on NAMESPACE { namespace =>
  }

  on STRUCTURE { structure =>
    source(s"/home/anahitm/dev/dsl-output/${structure.fullPathDirectory}", s"${structure.name.toPascal}.java") {
      ln(s"package ${structure.fullPath};")

      comment(structure)
      block(s"public class ${structure.name.toPascal}") {

        structure.fields.foreach { f =>
          comment(f)
          ln(s"private ${f.typeName} ${f.argName};")
        }

        structure.fields.foreach { f =>
          block(s"public ${f.getterName}()") {
            ln(s"return ${f.argName};")
          }

          block(s"public void ${f.setterName}(${f.typeName} ${f.argName})") {
            ln(s"this.${f.argName} = ${f.argName};")
          }
        }
      }
    }
  }

  on ENUMERATION { enumeration =>
    source(s"/home/anahitm/dev/dsl-output/${enumeration.fullPathDirectory}", s"${enumeration.name.toPascal}.java") {
      ln(s"package ${enumeration.fullPath};")

      block(s"public enum " + enumeration.name.toPascal) {
        ln(enumeration.constants.map(e => s"${e.name.toUpper}(${e.value})").mkString(", ") + ";")
        ln(s"private int val;")
        block(s"private PhoneNumber(int value)") {
          ln("val = value;")
        }

        block("public int value()") {
          ln("return val;")
        }

        block("static PhoneNumber fromValue(int value)") {
          block("switch ( value )") {
            enumeration.constants.foreach { e =>
                ln(s"case ${e.value}: ${e.name.toUpper}")
            }
          }
        }
      }
    }
  }

}
