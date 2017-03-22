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

def hasPrimaryKey(s: Structure): Boolean = s.fields.find(_.name == "id").isDefined

generate {
  begin ALL { root =>
    options.blockStart = "{"
    options.blockEnd = "}"
    options.currentDir = config.output
  }


  begin STRUCTURE { structure =>
    file(s"iunetworks/dataaccess/Basic${structure.name.toPascal}Dao.java") {
      ln(s"package am.iunetworks.ppcm.api.dataaccess;")
      bigBlock(s"""
          |import java.util.*;
          |import java.sql.*;
          |import javax.sql.DataSource; 
          |import am.iunetworks.ppcm.api.model.${structure.name.toPascal};\n
          |""")
             
      block(s"public class Basic${structure.name.toPascal}Dao extends BaseDao"){

        block(s"protected Basic${structure.name.toPascal}Dao (DataSource  dataSource)"){
          ln(s"super(dataSource, org.slf4j.LoggerFactory.getLogger(Basic${structure.name.toPascal}Dao.class));")
        }

        block(s"public void insert(${structure.name.toPascal} ${structure.name.toCamel}) throws Throwable") {
          var sqlParam = structure.fields.map(f=> s"${f.name}").mkString(", ")
          var sqlValues = structure.fields.map(f=> s"?").mkString(", ")
          ln(s"""final String SQL_INSERT = "insert into ${structure.name.toCamel}($sqlParam) values($sqlValues)";""")

          var args = for(f <- structure.fields) yield s"${structure.name.toCamel}.get${f.name.toPascal}()"
          ln(s"""Object[] fields = {${args.mkString(", ")}};""")
          ln(s"""logger.info( sqlStatement(SQL_INSERT, fields) );""")

          block(s"try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_INSERT))"){
            structure.fields.foreach{f=>
              f.datatype match {
                case d: DateDatatype => ln(s"String stringDateISO = dateFormat().format(${structure.name.toCamel}.get${f.name.toPascal}());")
                case _ => ""
              }
            }
            var i = 0;
            structure.fields.foreach{f=>
              i += 1
              f.datatype match {
                case s: StringDatatype => ln(s"ps.setString($i, ${structure.name.toCamel}.get${f.name.toPascal}());")
                case n: IntDatatype    => ln(s"ps.setInt($i, ${structure.name.toCamel}.get${f.name.toPascal}());")
                case f: FloatDatatype  => ln(s"ps.setDouble($i, ${structure.name.toCamel}.get${f.name.toPascal}());")
                case b: BoolDatatype   => ln(s"ps.setBoolean($i, ${structure.name.toCamel}.get${f.name.toPascal}());")
                case u: UuidDatatype   => ln(s"ps.setObject($i, ${structure.name.toCamel}.get${f.name.toPascal}());")
                case d: DateDatatype   => ln(s"ps.setString($i, stringDateISO);")
              }
            }
            ln(s"ps.executeUpdate();")
          }
        }

        block(s"public void update(${structure.name.toPascal} ${structure.name.toCamel}) throws Throwable") {
          var sqlParam = ""
          var filtredFildsPkey = structure.fields.filter(_.has('pkey) == false).map(_.name)
          filtredFildsPkey.foreach{ f =>
              sqlParam += s"${f} = ?, "
            }
          sqlParam = sqlParam + s"where id = ?"
          ln(s"""final String SQL_UPDATE = "update ${structure.name} set $sqlParam;";""")

          var args = for(f <- structure.fields) yield s"${structure.name.toCamel}.get${f.name.toPascal}()"
          ln(s"""Object[] fields = {${args.mkString(", ")}};""")
          ln(s"""logger.info( sqlStatement(SQL_UPDATE, fields) );""")

          block(s"try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_UPDATE))"){
            structure.fields.foreach{f=>
              f.datatype match { 
                case d: DateDatatype => ln(s"String stringDateISO = dateFormat().format(${structure.name}.get${f.name.toPascal}());")
                case _ => ""
              }
            }
            var i = 0
            structure.fields.foreach{f=>
              i += 1
              f.datatype match {
                case u: UuidDatatype => ln(s"ps.setObject($i, ${structure.name.toCamel}.get${f.name.toPascal}());")
                case n: IntDatatype =>  ln(s"ps.setInt($i, ${structure.name.toCamel}.get${f.name.toPascal}());")
                case d: DateDatatype => ln(s"ps.setString($i, stringDateISO);")
                case b: BoolDatatype   => ln(s"ps.setBoolean($i, ${structure.name.toCamel}.get${f.name.toPascal}());")
                case _ => ln(s"ps.setString($i, ${structure.name.toCamel}.get${f.name.toPascal}());")
              }
            }
            ln(s"ps.executeUpdate();")
          }
        }

        block(s"public void remove(UUID id) throws Throwable"){
          ln(s"""final String SQL_DELETE = "delete from ${structure.name} where id =?;";""")
          ln(s"""logger.info("delete from supplier where id = " + id + ")");""")
          block(s"try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_DELETE))"){
            ln(s"ps.setObject(1, id);")
            ln(s"ps.executeUpdate();")
          }
        }

        block(s"public List<${structure.name.toPascal}> selectAll() throws Throwable"){
          ln(s"""final String SQL_SELECT_ALL = "Select * from ${structure.name}";""")
          ln(s"""logger.info(SQL_SELECT_ALL);""")
          block(s"try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL))"){
            ln(s"List<${structure.name.toPascal}> ${structure.name.toCamel}List = new ArrayList<${structure.name.toPascal}>();")
            ln(s"ResultSet rs = ps.executeQuery();")
            block(s"while (rs.next())"){
              ln(s"${structure.name.toPascal} ${structure.name.toCamel} = new ${structure.name.toPascal}();")
              structure.fields.foreach{f=>
                f.datatype match {
                  case u: UuidDatatype => ln(s"""${structure.name.toCamel}.set${f.name.toPascal}(${jtype(f)}.fromString(rs.getString("${f.name}")));""")
                  case i: IntDatatype => ln(s"""${structure.name.toCamel}.set${f.name.toPascal}(rs.getInt("${f.name}"));""")
                  case d: DateDatatype => ln(s"""${structure.name.toCamel}.set${f.name.toPascal}(dateFormat().parse(rs.getString("${f.name}")));""")
                  case b: BoolDatatype   =>  ln(s"""${structure.name.toCamel}.set${f.name.toPascal}(rs.getBoolean("${f.name}"));""")
                  case _ => ln(s"""${structure.name.toCamel}.set${f.name.toPascal}(rs.getString("${f.name}"));""")
                }
              }
              ln(s"${structure.name.toCamel}List.add(${structure.name.toCamel});")
            }
            ln(s"return ${structure.name.toCamel}List;")
          }
        }

        //var key = structure.fields.filter(_.has('pkey)).map(f=> jtype(f) + " " + f.name.toCamel).mkString(", ")
        if ( hasPrimaryKey(structure) ) {
          block(s"public ${structure.name.toPascal} selectByKey(java.util.UUID id) throws Throwable"){
            ln(s"""final String SQL_SELECT_BY_KEY = "Select * from ${structure.name} where id = ? ;";""")
            ln(s"""logger.info("Select * from supplier where id = " + id + ";");""")
            block(s"try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_KEY))"){
              ln(s"${structure.name.toPascal} ${structure.name.toCamel} = new ${structure.name.toPascal}();")
              structure.fields.filter(_.has('pkey)).map(f=>f).foreach{f=>
                f.datatype match {
                  case u: UuidDatatype => ln(s"ps.setObject(1, ${f.name.toCamel});")
                  case n: IntDatatype =>  ln(s"ps.setInt(1, ${structure.name}.get${f.name.toPascal}());")
                  case _=> ""
                }
              }
              ln(s"ResultSet rs = ps.executeQuery();")
              block(s"while (rs.next())"){
                structure.fields.foreach{f=>
                  f.datatype match {
                    case u: UuidDatatype => ln(s"""${structure.name.toCamel}.set${f.name.toPascal}(${jtype(f)}.fromString(rs.getString("${f.name}")));""")
                    case i: IntDatatype => ln(s"""${structure.name.toCamel}.set${f.name.toPascal}(rs.getInt("${f.name}"));""")
                    case d: DateDatatype => ln(s"""${structure.name.toCamel}.set${f.name.toPascal}(dateFormat().parse(rs.getString("${f.name}")));""")
                    case b: BoolDatatype   => ln(s"""${structure.name.toCamel}.set${f.name.toPascal}(rs.getBoolean("${f.name}"));""")
                    case _ => ln(s"""${structure.name.toCamel}.set${f.name.toPascal}(rs.getString("${f.name}"));""")
                  }
                }
              }
              ln(s"return ${structure.name.toCamel};")
            }   
          }
        }
        ////TODO LOGGER
        if ( !structure.fields.filter(_.has('ref)).map(f=> f).isEmpty ) {
          var fkey = structure.fields.filter(_.has('ref)).map(f=> jtype(f) + " " + f.name.toCamel).mkString(", ")
          var fkeyforsql = structure.fields.filter(_.has('ref)).map(f=> f.name + " = ?").mkString(", ")
          var fkeyName = structure.fields.filter(_.has('ref)).map(f=> f.name.toPascal).mkString(" ")
          block(s"public List<${structure.name.toPascal}> selectBy$fkeyName($fkey) throws Throwable"){
            ln(s"""final String SQL_SELECT_BY_${structure.name.toUpper}_ID = "Select * from ${structure.name} where $fkeyforsql ;";""")
            block(s"try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_${structure.name.toUpper}_ID))"){
              ln(s"List<${structure.name.toPascal}> ${structure.name}List = new ArrayList<${structure.name.toPascal}>();")
              structure.fields.filter(_.has('ref)).map(f=>f).foreach{f=>
                ln(s"ps.setString(1, ${f.name.toCamel}.toString());")
              }
              ln(s"ResultSet rs = ps.executeQuery();")
              block(s"while (rs.next())"){
                ln(s"${structure.name.toPascal} ${structure.name} = new ${structure.name.toPascal}();")
                structure.fields.foreach{f=>
                  f.datatype match {
                    case u: UuidDatatype => ln(s"""${structure.name.toCamel}.set${f.name.toPascal}(${jtype(f)}.fromString(rs.getString("${f.name}")));""")
                    case i: IntDatatype => ln(s"""${structure.name.toCamel}.set${f.name.toPascal}(rs.getInt("${f.name}"));""")
                    case d: DateDatatype => ln(s"""${structure.name.toCamel}.set${f.name.toPascal}(dateFormat().parse(rs.getString("${f.name}")));""")
                    case b: BoolDatatype   => ln(s"""${structure.name.toCamel}.set${f.name.toPascal}(rs.getBoolean("${f.name}"));""")
                    case _ => ln(s"""${structure.name.toCamel}.set${f.name.toPascal}(rs.getString("${f.name}"));""")
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

    file(s"iunetworks/dataaccess/BaseDao.java") {
      ln(s"package am.iunetworks.ppcm.api.dataaccess;")
      bigBlock(s"""
          |import javax.sql.DataSource;
          |import org.slf4j.Logger;
          |import org.slf4j.LoggerFactory;
          |
          |public class BaseDao {
          |    protected DataSource dataSource;
          |    protected Logger logger; 
          |
          |    protected BaseDao(DataSource  dataSource, Logger logger) {
          |       if ( dataSource == null )
          |            throw new IllegalArgumentException("Database is not specified.");
          |       this.dataSource = dataSource;
          |       this.logger = logger;
          |    }
          |
          |    protected String sqlStatement(String sql, Object[] args){
          |       for(Object a: args) 
          |         sql = sql.replaceFirst("\\\\?", a.toString());
          |       return sql;
          |    }
          |}""")
    }
  }
}
