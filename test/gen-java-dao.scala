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
    case "repeated" => s"java.collection.List<$baseType>"
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
      ln(s"package ${structure.path};")
      block(s"class Basic${structure.name.toPascal}Dao"){
        bigBlock(s"""
          |  private Connection connection;
          |
          |	 public void setConnection(Connection connection){
		      |    this.connection = connection;
          |  };
          |
          |  protected PreparedStatement getPrepareStatement(String sql) throws Throwable {
          |    PreparedStatement ps = null;
          |    ps = connection.prepareStatement(sql);
          |    return ps;
          |  }
	        |
          |  protected void closePrepareStatement(PreparedStatement ps) throws Throwable {
          |    if (ps != null) ps.close();
          |  }
          |
          |""")

        block(s"public void insert(${structure.name.toPascal} ${structure.name}) throws Throwable") {
          ln(s"PreparedStatement ps = null;")
          block(s"try"){
            ln(s"""SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");""")
            structure.fields.foreach{f=>
              f.datatype match {
                case d: DateDatatype => ln(s"String stringDateISO = df.format(${structure.name}.${f.name.toCamel});")
                case _ => ""
              }
            }
            var sqlParam = structure.fields.map(f=> s"${f.name.toCamel}").mkString(", ")
            var sqlValues = structure.fields.map(f=> s"?").mkString(", ")
            ln(s"""String sql = "insert into ${structure.name}($sqlParam) values($sqlValues)";""")
            ln(s"ps = getPrepareStatement(sql);")
            var i = 0;
            structure.fields.foreach{f=>
              i += 1
              f.datatype match {
                case u: UuidDatatype => ln(s"ps.setString($i, ${structure.name}.${f.name.toCamel}.toString());")
                case d: DateDatatype => ln(s"ps.setString($i, stringDateISO);")
                case _ => ln(s"ps.setString($i, ${structure.name}.${f.name.toCamel});")
              }
            }
            ln(s"ps.executeUpdate();")
          }
          ln(s"finally { closePrepareStatement(ps); }")
        }

        block(s"public void update(${structure.name.toPascal} ${structure.name}) throws Throwable") {
          ln(s"PreparedStatement ps = null;")
          block(s"try"){
            ln(s"""SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");""")
            structure.fields.foreach{f=>
              f.datatype match {
                case d: DateDatatype => ln(s"String stringDateISO = df.format(${structure.name}.${f.name.toCamel});")
                case _ => ""
              }
            }
            var sqlParam = ""
            var filtredFildsPkey = structure.fields.filter(_.has('pkey)).map(f=> f)
            filtredFildsPkey.foreach{f=>
              sqlParam += s"${f.name.toCamel} = ?, "
            }
            sqlParam = sqlParam + s"where id = ?"

            ln(s"""String sql = "update ${structure.name} set $sqlParam;";""")
            ln(s"ps = getPrepareStatement(sql);")
            var i = 0
            structure.fields.foreach{f=>
              i += 1
              f.datatype match {
                case u: UuidDatatype => ln(s"ps.setString($i, ${structure.name}.${f.name.toCamel}.toString());")
                case d: DateDatatype => ln(s"ps.setString($i, stringDateISO);")
                case _ => ln(s"ps.setString($i, ${structure.name}.${f.name.toCamel});")
              }
            }
            ln(s"ps.executeUpdate();")
          }
            ln(s"finally { closePrepareStatement(ps); }")
        }

        block(s"public void remove(UUID id) throws Throwable"){
          ln(s"PreparedStatement ps = null;")
          block(s"try"){
            ln(s"""String sql = "delete from ${structure.name} where id =?;";""")
            ln(s"ps = getPrepareStatement(sql);")
            ln(s"ps.setString(1, id.toString());")
            ln(s"ps.executeUpdate();")
          }
          ln(s"finally { closePrepareStatement(ps); }")
        }

        block(s"public List<${structure.name.toPascal}> selectAll() throws Throwable"){
          ln(s"List<${structure.name}> ${structure.name}List = null;")
          ln(s"PreparedStatement ps = null;")
          block(s"try"){
            ln(s"""String sql = "Select * from ${structure.name}";""")
            ln(s"${structure.name}List = new ArrayList<${structure.name.toPascal}>();")
            ln(s"""SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");""")
            ln(s"ps = getPrepareStatement(sql);")
            ln(s"ResultSet rs = ps.executeQuery();")
            block(s"while (rs.next())"){
              ln(s"${structure.name.toPascal} ${structure.name} = new ${structure.name.toPascal}();")
              structure.fields.foreach{f=>
                f.datatype match {
                  case u: UuidDatatype => ln(s"""${structure.name}.${f.name.toCamel} = ${jtype(f)}.fromString(rs.getString("${f.name.toCamel}"));""")
                  case d: DateDatatype => ln(s"""${structure.name}.${f.name.toCamel} = formatter.parse(rs.getString("${f.name.toCamel}"));""")
                  case _ => ln(s"""${structure.name}.${f.name.toCamel} = rs.getString("${f.name.toCamel}");""")
                }
              }
              ln(s"${structure.name}List.add(${structure.name});")
            }
          }
          ln(s"finally { closePrepareStatement(ps); }")
          ln(s"return ${structure.name}List;")
        }

        var key = structure.fields.filter(_.has('pkey)).map(f=> jtype(f) + " " + f.name.toCamel).mkString(", ")
        var keyforsql = structure.fields.filter(_.has('pkey)).map(f=> f.name + " = ?").mkString(", ")
        block(s"public ${structure.name.toPascal} selectByKey($key) throws Throwable"){
          ln(s"${structure.name.toPascal} ${structure.name} = null;")
          ln(s"PreparedStatement ps = null;")
          block(s"try "){
            ln(s"""String sql = "Select * from ${structure.name} where $keyforsql ;";""")
            ln(s"""SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");""")
            ln(s"${structure.name} = new ${structure.name.toPascal}();")
            ln(s"ps = getPrepareStatement(sql);")
            structure.fields.filter(_.has('pkey)).map(f=>f).foreach{f=>
              f.datatype match {
                case u: UuidDatatype => ln(s"ps.setString(1, ${f.name.toCamel}.toString());")
                case _=> ""
              }
            }
            ln(s"ps.setString(1, id.toString());")
            ln(s"ResultSet rs = ps.executeQuery();")
            block(s"while (rs.next())"){
              structure.fields.foreach{f=>
                f.datatype match {
                  case u: UuidDatatype => ln(s"""${structure.name}.${f.name.toCamel} = ${jtype(f)}.fromString(rs.getString("${f.name.toCamel}"));""")
                  case d: DateDatatype => ln(s"""${structure.name}.${f.name.toCamel} = formatter.parse(rs.getString("${f.name.toCamel}"));""")
                  case _ => ln(s"""${structure.name}.${f.name.toCamel} = rs.getString("${f.name.toCamel}");""")
                }
              }
            }
          }
          ln(s"finally { closePrepareStatement(ps); }")
          ln(s"return ${structure.name};")
        }

        var fkey = structure.fields.filter(_.has('ref)).map(f=> jtype(f) + " " + f.name.toCamel).mkString(", ")
        var fkeyforsql = structure.fields.filter(_.has('ref)).map(f=> f.name + " = ?").mkString(", ")
        block(s"public ${structure.name.toPascal} selectBy($fkey) throws Throwable"){
          ln(s"${structure.name.toPascal} ${structure.name} = null;")
          ln(s"PreparedStatement ps = null;")
          block(s"try "){
            ln(s"""String sql = "Select * from ${structure.name} where $fkeyforsql ;";""")
            ln(s"""SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");""")
            ln(s"${structure.name} = new ${structure.name.toPascal}();")
            ln(s"ps = getPrepareStatement(sql);")
            structure.fields.filter(_.has('ref)).map(f=>f).foreach{f=>
              ln(s"ps.setString(1, ${f.name.toCamel}.toString());")
            }
            ln(s"ResultSet rs = ps.executeQuery();")
            block(s"while (rs.next())"){
              structure.fields.foreach{f=>
                f.datatype match {
                  case u: UuidDatatype => ln(s"""${structure.name}.${f.name.toCamel} = ${jtype(f)}.fromString(rs.getString("${f.name.toCamel}"));""")
                  case d: DateDatatype => ln(s"""${structure.name}.${f.name.toCamel} = formatter.parse(rs.getString("${f.name.toCamel}"));""")
                  case _ => ln(s"""${structure.name}.${f.name.toCamel} = rs.getString("${f.name.toCamel}");""")
                }
              }
            }
          }
          ln(s"finally { closePrepareStatement(ps); }")
          ln(s"return ${structure.name};")
        }
       
      }

    }
  }
}
