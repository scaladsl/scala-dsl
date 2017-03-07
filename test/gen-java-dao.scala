import dslg.gdsl._

def jtype(field: Field): String = {

  val baseType = field.datatype match {
    case s: StringDatatype => "java.lang.String"
    case i: IntDatatype => "java.lang.Integer"
    case f: FloatDatatype => "java.lang.Double"
    case b: BoolDatatype => "java.lang.Boolean"
    case u: UuidDatatype => "java.util.UUID"
    case d: DateDatatype => "java.util.Date"
    case _ => field.datatype
      if(field.datatype != null && field.datatype.isInstanceOf[Structure]){
        var list = field("datatype").split("::")
        list = list.take(list.length -1) :+ field.datatype.name.toPascal
        list.mkString(".")
      }
      else
        throw new IllegalArgumentException(s"Invalid datatype: {field.datatype}")
  }

  field.modifier match {
    case "required" => baseType
    case "optional" => s"java.util.Option<$baseType>"
    case "repeated" => s"java.util.List<$baseType>"
    case x => throw new IllegalArgumentException(s"Invalid modifier: $x")
  }

}

generate {
  begin ALL { root =>
    options.blockStart = "{"
    options.blockEnd = "}"
    options.currentDir = config.output
  }


  begin STRUCTURE { structure =>
    file(s"Basic${structure.name.toPascal}Dao.java") {
      ln("package basicDao;")
      bigBlock(s"""
          |import java.sql.*;
          |import java.util.*;
          |import java.text.SimpleDateFormat;
		      |import java.lang.Throwable;
          |import com.dataSource.dbcp2.DataSource;
          |import model.${structure.name.toPascal};\n
          |""")
             
      block(s"public class Basic${structure.name.toPascal}Dao extends BaseDao"){

        block(s"protected Basic${structure.name.toPascal}Dao (DataSource ds)"){
          ln("super(ds);")
        }

        block(s"public void insert(${structure.name.toPascal} ${structure.name}) throws Throwable") {
          var sqlParam = structure.fields.map(f=> s"${f.name}").mkString(", ")
          var sqlValues = structure.fields.map(f=> s"?").mkString(", ")
          ln(s"""final String SQL_INSERT = "insert into ${structure.name}($sqlParam) values($sqlValues)";""")

          block(s"try( Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_INSERT))"){
            ln(s"""SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");""")
            structure.fields.foreach{f=>
              f.datatype match {
                case d: DateDatatype => ln(s"String stringDateISO = df.format(${structure.name}.get${f.name.toPascal}());")
                case _ => ""
              }
            }
            var i = 0;
            structure.fields.foreach{f=>
              i += 1
              f.datatype match {
                case u: UuidDatatype => ln(s"ps.setString($i, ${structure.name}.get${f.name.toPascal}().toString());")
                case d: DateDatatype => ln(s"ps.setString($i, stringDateISO);")
                case _ => ln(s"ps.setString($i, ${structure.name}.get${f.name.toPascal}());")
              }
            }
            ln(s"ps.executeUpdate();")
          }
        }

        block(s"public void update(${structure.name.toPascal} ${structure.name}) throws Throwable") {
          var sqlParam = ""
          var filtredFildsPkey = structure.fields.filter(_.has('pkey) == false).map(_.name.toCamel)
          filtredFildsPkey.foreach{ f =>
              sqlParam += s"${f} = ?, "
            }
          sqlParam = sqlParam + s"where id = ?"
          ln(s"""final String SQL_UPDATE = "update ${structure.name} set $sqlParam;";""")
          block(s"try( Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_UPDATE))"){
            ln(s"""SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");""")
            structure.fields.foreach{f=>
              f.datatype match { 
                case d: DateDatatype => ln(s"String stringDateISO = df.format(${structure.name}.get${f.name.toPascal}());")
                case _ => ""
              }
            }
            var i = 0
            structure.fields.foreach{f=>
              i += 1
              f.datatype match {
                case u: UuidDatatype => ln(s"ps.setString($i, ${structure.name}.get${f.name.toPascal}().toString());")
                case d: DateDatatype => ln(s"ps.setString($i, stringDateISO);")
                case _ => ln(s"ps.setString($i, ${structure.name}.get${f.name.toPascal}());")
              }
            }
            ln(s"ps.executeUpdate();")
          }
        }

        block(s"public void remove(UUID id) throws Throwable"){
          ln(s"""final String SQL_DELETE = "delete from ${structure.name} where id =?;";""")
          block(s"try( Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_DELETE))"){
            ln(s"ps.setString(1, id.toString());")
            ln(s"ps.executeUpdate();")
          }
        }

        block(s"public List<${structure.name.toPascal}> selectAll() throws Throwable"){
          ln(s"""final String SQL_SELECT_ALL = "Select * from ${structure.name}";""")
          block(s"try( Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL))"){
            ln(s"List<${structure.name.toPascal}> ${structure.name}List = new ArrayList<${structure.name.toPascal}>();")
            ln(s"""SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");""")
            ln(s"ResultSet rs = ps.executeQuery();")
            block(s"while (rs.next())"){
              ln(s"${structure.name.toPascal} ${structure.name} = new ${structure.name.toPascal}();")
              structure.fields.foreach{f=>
                f.datatype match {
                  case u: UuidDatatype => ln(s"""${structure.name}.set${f.name.toPascal}(${jtype(f)}.fromString(rs.getString("${f.name}")));""")
                  case d: DateDatatype => ln(s"""${structure.name}.set${f.name.toPascal}(formatter.parse(rs.getString("${f.name}")));""")
                  case _ => ln(s"""${structure.name}.set${f.name.toPascal}(rs.getString("${f.name}"));""")
                }
              }
              ln(s"${structure.name}List.add(${structure.name});")
            }
            ln(s"return ${structure.name}List;")
          }
        }

        var key = structure.fields.filter(_.has('pkey)).map(f=> jtype(f) + " " + f.name.toCamel).mkString(", ")
        block(s"public ${structure.name.toPascal} selectByKey($key) throws Throwable"){
          ln(s"""final String SQL_SELECT_BY_KEY = "Select * from ${structure.name} where id = ? ;";""")
          block(s"try( Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_KEY))"){
            ln(s"""SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");""")
            ln(s"${structure.name.toPascal} ${structure.name} = new ${structure.name.toPascal}();")
            structure.fields.filter(_.has('pkey)).map(f=>f).foreach{f=>
              f.datatype match {
                case u: UuidDatatype => ln(s"ps.setString(1, ${f.name.toCamel}.toString());")
                case _=> ""
              }
            }
            ln(s"ResultSet rs = ps.executeQuery();")
            block(s"while (rs.next())"){
              structure.fields.foreach{f=>
                f.datatype match {
                  case u: UuidDatatype => ln(s"""${structure.name}.set${f.name.toPascal}(${jtype(f)}.fromString(rs.getString("${f.name}")));""")
                  case d: DateDatatype => ln(s"""${structure.name}.set${f.name.toPascal}(formatter.parse(rs.getString("${f.name}")));""")
                  case _ => ln(s"""${structure.name}.set${f.name.toPascal}(rs.getString("${f.name}"));""")
                }
              }
            }
            ln(s"return ${structure.name};")
          }
          
        }

        if ( !structure.fields.filter(_.has('ref)).map(f=> f).isEmpty ) {
          var fkey = structure.fields.filter(_.has('ref)).map(f=> jtype(f) + " " + f.name.toCamel).mkString(", ")
          var fkeyforsql = structure.fields.filter(_.has('ref)).map(f=> f.name + " = ?").mkString(", ")
          var fkeyName = structure.fields.filter(_.has('ref)).map(f=> f.name.toPascal).mkString(" ")
          block(s"public List<${structure.name.toPascal}> selectBy$fkeyName($fkey) throws Throwable"){
            ln(s"""final String SQL_SELECT_BY_ARTICLE_ID = "Select * from ${structure.name} where $fkeyforsql ;";""")
            block(s"try( Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_ARTICLE_ID))"){
              ln(s"""SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");""")
              ln(s"List<${structure.name.toPascal}> ${structure.name}List = new ArrayList<${structure.name.toPascal}>();")
              structure.fields.filter(_.has('ref)).map(f=>f).foreach{f=>
                ln(s"ps.setString(1, ${f.name.toCamel}.toString());")
              }
              ln(s"ResultSet rs = ps.executeQuery();")
              block(s"while (rs.next())"){
                ln(s"${structure.name.toPascal} ${structure.name} = new ${structure.name.toPascal}();")
                structure.fields.foreach{f=>
                  f.datatype match {
                    case u: UuidDatatype => ln(s"""${structure.name}.set${f.name.toPascal}(${jtype(f)}.fromString(rs.getString("${f.name}")));""")
                    case d: DateDatatype => ln(s"""${structure.name}.set${f.name.toPascal}(formatter.parse(rs.getString("${f.name}")));""")
                    case _ => ln(s"""${structure.name}.set${f.name.toPascal}(rs.getString("${f.name}"));""")
                  }
                }
                ln(s"${structure.name}List.add(${structure.name});")
              }
              ln(s"return ${structure.name}List;")
            }
          }
        }

      }
    }
     file(s"BaseDao.java") {
      ln("package basicDao;")
      bigBlock(s"""
          |import java.sql.*;
          |import java.util.*;
          |import java.text.SimpleDateFormat;
		      |import java.lang.Throwable;
          |import com.dataSource.dbcp2.DataSource;
          |
          |public class BaseDao {
          |    protected DataSource ds;
          |
          |    protected BaseDao(DataSource ds) {
          |        if ( ds == null )
          |            throw new IllegalArgumentException("DataSource is not specified.");
          |       
          |        this.ds = ds;
          |    }
          |}""")
     }
  }
}
