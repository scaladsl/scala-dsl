import sgf.GDSL._
import sgf.GDSL.Lang._
import sgf.Java._

generate {

  on NAMESPACE { namespace =>

  }

  on STRUCTURE { structure =>
    source(s"./dsl-output/${structure.fullPathDirectory}", s"${structure.name.toPascal}.java") {
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
    // source(s"./dsl-output/${enumeration.fullPathDirectory}", s"${enumeration.name.toPascal}.java") {
    //   ln(s"package ${enumeration.fullPath};")

    //   block(s"public enum " + enumeration.name.toPascal) {
    //     ln(enumeration.constants.map(e => s"${e.name.toUpper}(${e.value})").mkString(", ") + ";")
    //     ln(s"private int val;")
    //     block(s"private PhoneNumber(int value)") {
    //       ln("val = value;")
    //     }

    //     block("public int value()") {
    //       ln("return val;")
    //     }

    //     block("static PhoneNumber fromValue(int value)") {
    //       block("switch ( value )") {
    //         enumeration.constants.foreach { e =>
    //             ln(s"case ${e.value}: ${e.name.toUpper}")
    //         }
    //       }
    //     }
    //   }
    // }
  }

  on SERVICE { service =>
    source(s"./dsl-output/${service.fullPathDirectory}", s"${service.name.toPascal}Service.java") {
      ln(s"package ${service.fullPath};")

      block(s"public interface ${service.name.toPascal}") {
        service.functions.foreach {f =>
          comment(f)
          ln(s"${f.functionTypeName} ${f.name.toCamel}(${f.params}) throws Throwable;")
        }
      }
    }

    source(s"./dsl-output/${service.fullPathDirectory}", s"Abstract${service.name.toPascal}Controller.java") {

      ln(s"package ${service.fullPath};")

      ln(s"""@RequestMapping("${service.serviceUrl}")""")
      ln(s"""@javax.annotation.Generated("Generated according to PPCM DSL")""")
      block(s"public class Abstract${service.name.toPascal}Controller extends AbstractController") {

        bigBlock(s"""
          |  @org.springframework.beans.factory.annotation.Autowired
          |
          |  private ${service.name.toPascal}Service service;
          |
          |  protected ${service.name.toPascal}Service getService() {
          |    return service;
          |  }
          |
          |  protected String doIndex(org.springframework.ui.ModelMap modelMap) throws Throwable {
          |    return "${service.serviceUrl}";
          |  }
          |
          |  @RequestMapping(value = "", method = RequestMethod.GET)
          |  public final String index(org.springframework.ui.ModelMap modelMap) {
          |    log.debug("{}: AbstractCpvController.index(...)", getCaller());
          |
          |    try {
          |      getSession().checkPermission("${service.serviceUrl}");
          |      return doIndex(modelMap);
          |    }
          |
          |    catch ( org.springframework.security.access.AccessDeniedException e ) {
          |      log.error("The Abstract${service.name.toPascal}Controller.index(...) has been failed: " + e.getMessage(), e);
          |      return "redirect:public/access-denied";
          |    }
          |
          |    catch ( Throwable e ) {
          |      log.error("The Abstract${service.name.toPascal}Controller.index(...) has been failed: " + e.getMessage(), e);
          |      return "redirect:public/internal-error";
          |    }
          |  }
          |
          |""")

        service.functions.foreach {f =>
          block(s"""public static class ${f.name.toPascal}Request"""){
            if(f.params != "")
              ln(s"""public ${f.params};""")
          }

          bigBlock(s"""
          |  @RequestMapping(value = "/${f.name}", method = RequestMethod.POST)
          |  @ResponseBody
          |  public final AjaxResult ${f.name}(@RequestBody ${f.name.toPascal}Request request) {
          |    log.debug("{}: Abstract${service.name.toPascal}Controller.${f.name}(...)", getCaller());
          |  }
          |
          |  try {
          |    getSession().checkPermission("${service.serviceUrl}");
          |    ${f.functionTypeName} result = service.${f.name}(request.${f.argParamName});
          |    return AjaxResult.ok(result);
          |  }
          |
          |  catch ( org.springframework.security.access.AccessDeniedException e ) {
          |    log.error("The Abstract${service.name.toPascal}Controller.${f.name}(...) has been failed: " + e.getMessage(), e);
          |    return AjaxResult.error(AjaxResult.STATUS_ACCESS_DENIED);
          |  }
          |
          |  catch ( Throwable e ) {
          |    log.error("The Abstract${service.name.toPascal}Controller.${f.name}(...) has been failed: " + e.getMessage(), e);
          |    return AjaxResult.error(AjaxResult.STATUS_INTERNAL_ERROR);
          |  }
          |""")
        }

      }
    }

  }

}
