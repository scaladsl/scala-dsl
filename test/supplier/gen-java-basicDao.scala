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
    var pkg = "basicDao";
    file(s"${pkg}/Basic${structure.name.toPascal}Dao.java") {
      ln(s"package ${pkg};")
      bigBlock(s"""
          |import java.util.*;
          |import java.sql.*;
          |import database.dbcp2.*;
          |import model.${structure.name.toPascal};\n
          |""")
             
      block(s"public class Basic${structure.name.toPascal}Dao extends BaseDao"){

        block(s"protected Basic${structure.name.toPascal}Dao (Database database)"){
          ln("super(database);")
        }

        block(s"public void insert(${structure.name.toPascal} ${structure.name}) throws Throwable") {
          var sqlParam = structure.fields.map(f=> s"${f.name}").mkString(", ")
          var sqlValues = structure.fields.map(f=> s"?").mkString(", ")
          ln(s"""final String SQL_INSERT = "insert into ${structure.name}($sqlParam) values($sqlValues)";""")

          block(s"try( Connection con = database.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_INSERT))"){
            structure.fields.foreach{f=>
              f.datatype match {
                case d: DateDatatype => ln(s"String stringDateISO = dateFormat().format(${structure.name}.get${f.name.toPascal}());")
                case _ => ""
              }
            }
            var i = 0;
            structure.fields.foreach{f=>
              i += 1
              f.datatype match {
                case s: StringDatatype => ln(s"ps.setString($i, ${structure.name}.get${f.name.toPascal}());")
                case n: IntDatatype    => ln(s"ps.setInt($i, ${structure.name}.get${f.name.toPascal}());")
                case f: FloatDatatype  => ln(s"ps.setDouble($i, ${structure.name}.get${f.name.toPascal}());")
                case b: BoolDatatype   => ln(s"ps.setBoolean($i, ${structure.name}.get${f.name.toPascal}());")
                case u: UuidDatatype   => ln(s"ps.setObject($i, ${structure.name}.get${f.name.toPascal}());")
                case d: DateDatatype   => ln(s"ps.setString($i, stringDateISO);")
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
          block(s"try( Connection con = database.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_UPDATE))"){
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
                case u: UuidDatatype => ln(s"ps.setObject($i, ${structure.name}.get${f.name.toPascal}());")
                case n: IntDatatype =>  ln(s"ps.setInt($i, ${structure.name}.get${f.name.toPascal}());")
                case d: DateDatatype => ln(s"ps.setString($i, stringDateISO);")
                case _ => ln(s"ps.setString($i, ${structure.name}.get${f.name.toPascal}());")
              }
            }
            ln(s"ps.executeUpdate();")
          }
        }

        block(s"public void remove(UUID id) throws Throwable"){
          ln(s"""final String SQL_DELETE = "delete from ${structure.name} where id =?;";""")
          block(s"try( Connection con = database.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_DELETE))"){
            ln(s"ps.setObject(1, id);")
            ln(s"ps.executeUpdate();")
          }
        }

        block(s"public List<${structure.name.toPascal}> selectAll() throws Throwable"){
          ln(s"""final String SQL_SELECT_ALL = "Select * from ${structure.name}";""")
          block(s"try( Connection con = database.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL))"){
            ln(s"List<${structure.name.toPascal}> ${structure.name}List = new ArrayList<${structure.name.toPascal}>();")
            ln(s"ResultSet rs = ps.executeQuery();")
            block(s"while (rs.next())"){
              ln(s"${structure.name.toPascal} ${structure.name} = new ${structure.name.toPascal}();")
              structure.fields.foreach{f=>
                f.datatype match {
                  case u: UuidDatatype => ln(s"""${structure.name}.set${f.name.toPascal}(${jtype(f)}.fromString(rs.getString("${f.name}")));""")
                  case i: IntDatatype => ln(s"""${structure.name}.set${f.name.toPascal}(rs.getInt("${f.name}"));""")
                  case d: DateDatatype => ln(s"""${structure.name}.set${f.name.toPascal}(dateFormat().parse(rs.getString("${f.name}")));""")
                  case _ => ln(s"""${structure.name}.set${f.name.toPascal}(rs.getString("${f.name}"));""")
                }
              }
              ln(s"${structure.name}List.add(${structure.name});")
            }
            ln(s"return ${structure.name}List;")
          }
        }

        //var key = structure.fields.filter(_.has('pkey)).map(f=> jtype(f) + " " + f.name.toCamel).mkString(", ")
        if ( hasPrimaryKey(structure) ) {
          block(s"public ${structure.name.toPascal} selectByKey(java.util.UUID id) throws Throwable"){
            ln(s"""final String SQL_SELECT_BY_KEY = "Select * from ${structure.name} where id = ? ;";""")
            block(s"try( Connection con = database.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_KEY))"){
              ln(s"${structure.name.toPascal} ${structure.name} = new ${structure.name.toPascal}();")
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
                    case u: UuidDatatype => ln(s"""${structure.name}.set${f.name.toPascal}(${jtype(f)}.fromString(rs.getString("${f.name}")));""")
                    case i: IntDatatype => ln(s"""${structure.name}.set${f.name.toPascal}(rs.getInt("${f.name}"));""")
                    case d: DateDatatype => ln(s"""${structure.name}.set${f.name.toPascal}(dateFormat().parse(rs.getString("${f.name}")));""")
                    case _ => ln(s"""${structure.name}.set${f.name.toPascal}(rs.getString("${f.name}"));""")
                  }
                }
              }
              ln(s"return ${structure.name};")
            }   
          }
        }

        if ( !structure.fields.filter(_.has('ref)).map(f=> f).isEmpty ) {
          var fkey = structure.fields.filter(_.has('ref)).map(f=> jtype(f) + " " + f.name.toCamel).mkString(", ")
          var fkeyforsql = structure.fields.filter(_.has('ref)).map(f=> f.name + " = ?").mkString(", ")
          var fkeyName = structure.fields.filter(_.has('ref)).map(f=> f.name.toPascal).mkString(" ")
          block(s"public List<${structure.name.toPascal}> selectBy$fkeyName($fkey) throws Throwable"){
            ln(s"""final String SQL_SELECT_BY_${structure.name.toUpper}_ID = "Select * from ${structure.name} where $fkeyforsql ;";""")
            block(s"try( Connection con = database.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_${structure.name.toUpper}_ID))"){
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
                    case i: IntDatatype => ln(s"""${structure.name}.set${f.name.toPascal}(rs.getInt("${f.name}"));""")
                    case d: DateDatatype => ln(s"""${structure.name}.set${f.name.toPascal}(dateFormat().parse(rs.getString("${f.name}")));""")
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

    file(s"${pkg}/BaseDao.java") {
      ln(s"package ${pkg};")
      bigBlock(s"""
          |import database.dbcp2.*;
          |import java.text.SimpleDateFormat;
          |
          |public class BaseDao {
          |    protected Database database;
          |    
          |    protected BaseDao(Database database) {
          |        if ( database == null )
          |            throw new IllegalArgumentException("Database is not specified.");
          |       
          |        this.database = database ;
          |    }
          |
          |    protected SimpleDateFormat dateFormat() {
          |        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          |    }
          |}""")
    }
  }
}
