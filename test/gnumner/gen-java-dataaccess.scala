import dslg.gdsl._

def jtype(field: Field): String = {

  val baseType = field.datatype match {
    case _: StringDatatype => "String"
    case _: IntDatatype    => "Integer"
    case _: FloatDatatype  => "Double"
    case _: BoolDatatype   => "Boolean"
    case _: UuidDatatype   => "java.util.UUID"
    case _: DateDatatype   => "java.sql.Timestamp"
    case _ => field.datatype
      if(field.datatype != null && field.datatype.isInstanceOf[Table]){
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

def hasPrimaryKey(s: Table): Boolean = s.fields.find(_.name == "id").isDefined

def dbName(table: Table): scala.collection.mutable.ListBuffer[String] = {
  var fieldList = new scala.collection.mutable.ListBuffer[String]()
  table.fields.foreach { f =>
    if(f.has('db_name))
      fieldList += f.apply('db_name)
    else
      fieldList += f.name
  }
  fieldList
}

def pkeyName(table: Table): String = {
  var pkeyName = ""
  table.fields.filter(_.has('pkey)).foreach{f=>
    if(f.has('db_name))
      pkeyName = f.apply('db_name)
  }
  pkeyName
}

generate {
  begin ALL { root =>
    options.blockStart = " {"
    options.blockEnd = "}"
    options.currentDir = config.output
  }

  begin TABLE { table =>
    println(dbName(table))
    file(s"iunetworks/dataaccess/Basic${table.name.toPascal}Dao.java") {
      ln(s"package am.iunetworks.ppcm.api.dataaccess;")
      bigBlock(s"""
          |import java.util.*;
          |import java.sql.*;
          |import javax.sql.DataSource; 
          |import am.iunetworks.ppcm.api.service.*;\n
          |import static org.jooq.impl.DSL.*;
          |import org.jooq.*;
          |import database.dbcp2.*;
          |
          |""")
      
      block(s"public class Basic${table.name.toPascal}Dao extends BaseDao"){

        ln(s"""public static Table ${table.name.toUpperCase()} = table(name("${table.name}"));""")

        table.fields.foreach{f=>
          f.datatype match {
            case _: StringDatatype => {
              if(f.has('db_name))
                ln(s"""public static final Field<String> ${f.apply('db_name).toUpperCase()} = field(name("${table.name}", "${f.apply('db_name)}"), String.class);""")
              else
                ln(s"""public static final Field<String> ${f.name.toUpperCase()} = field(name("${table.name}", "${f.name}"), String.class);""")
            }
            case _: IntDatatype => {
              if(f.has('db_name))
                ln(s"""public static final Field<Integer> ${f.apply('db_name).toUpperCase()} = field(name("${table.name}", "${f.apply('db_name)}"), Integer.class);""")
              else
                ln(s"""public static final Field<Integer> ${f.name.toUpperCase()} = field(name("${table.name}", "${f.name}"), Integer.class);""")
            }
            case _: FloatDatatype =>{
              if(f.has('db_name))
                ln(s"""public static final Field<Double> ${f.apply('db_name).toUpperCase()} = field(name("${table.name}", "${f.apply('db_name)}"), Double.class);""")
              else
                ln(s"""public static final Field<Double> ${f.name.toUpperCase()} = field(name("${table.name}", "${f.name}"), Double.class);""")
            }
            case _: BoolDatatype =>{
              if(f.has('db_name))
                ln(s"""public static final Field<Bollean> ${f.apply('db_name).toUpperCase()} = field(name("${table.name}", "${f.apply('db_name)}"), Boolean.class);""")
              else
                ln(s"""public static final Field<Boolean> ${f.name.toUpperCase()} = field(name("${table.name}", "${f.name}"), Boolean.class);""")
            }
            case _: UuidDatatype =>{
              if(f.has('db_name))
                ln(s"""public static final Field<UUID> ${f.apply('db_name).toUpperCase()} = field(name("${table.name}", "${f.apply('db_name)}"), UUID.class);""")
              else
                ln(s"""public static final Field<UUID> ${f.name.toUpperCase()} = field(name("${table.name}", "${f.name}"), UUID.class);""")
            }
            case _: DateDatatype =>{
              if(f.has('db_name))
                ln(s"""public static final Field<Timestamp> ${f.apply('db_name).toUpperCase()} = field(name("${table.name}", "${f.apply('db_name)}"), Timestamp.class);""")
              else
                ln(s"""public static final Field<Timestamp> ${f.name.toUpperCase()} = field(name("${table.name}", "${f.name}"), Timestamp.class);""")
            }
          }
        }

        ln("")
        block(s"protected Basic${table.name.toPascal}Dao (DSLContextFactory dslContextFactory)"){
          ln(s"super(dslContextFactory);")
        }

        ln("")
        block(s"private ${table.name.toPascal} make${table.name.toPascal}(Record r)"){
          ln(s"${table.name.toPascal} e = new ${table.name.toPascal}();")
          dbName(table).foreach { f => ln(s"""e.set${f.toPascal}(r.getValue(${f.toUpperCase()}));""") }
          ln(s"return e;")
        }

        ln(s"")
        block(s"public void insert(${table.name.toPascal} ${table.name.toCamel}) throws Throwable") {
          var sqlParam = dbName(table).map(f=> s"${f.toUpperCase()}").mkString(", ")
          var sqlValues = dbName(table).map(f=> s"${table.name.toCamel}.get${f.toPascal}()").mkString(", ")
          //       var args = for(f <- table.fields) yield s"${table.name.toCamel}.get${f.name.toPascal}()"
          block(s"try( DSLContext create = dsl() )"){
            ln(s"create.insertInto(${table.name.toUpperCase()}, $sqlParam).values($sqlValues).execute();")
          }
        }
        ln(s"")

        block(s"public void update(${table.name.toPascal} ${table.name.toCamel}) throws Throwable") {
          // var sqlParam = ""
          // var filtredFildsPkey = table.fields.filter(_.has('pkey) == true).map(_.name)
          // filtredFildsPkey.foreach{ f => sqlParam += s"${f} = ?, " }
          // sqlParam = sqlParam + s"where id = ?"

          // var args = for(f <- table.fields) yield s"${table.name.toCamel}.get${f.name.toPascal}()"
          
          block(s"try( DSLContext create = dsl() )"){
            ln(s"create.update(${table.name.toUpperCase()})")
            dbName(table).foreach { f =>
              ln(s".set(${f.toUpperCase()}, ${table.name.toCamel}.get${f.toPascal}())")
            }
            // var pkeyName = ""
            // table.fields.filter(_.has('pkey)).foreach{f=>
            //   if(f.has('db_name))
            //     pkeyName = f.apply('db_name)
            // }
            ln(s".where(${pkeyName(table).toUpperCase()}.equal(${table.name.toCamel}.get${pkeyName(table).toPascal}()))")
            ln(s".execute();")
          }
        }

        ln(s"")
        block(s"public void remove(UUID id) throws Throwable"){
          block(s"try( DSLContext create = dsl() )"){
            // var pkeyName = ""
            // table.fields.filter(_.has('pkey)).foreach{f=>
            //   if(f.has('db_name))
            //     pkeyName = f.apply('db_name)
            // }
            ln(s"create.delete(${table.name.toUpperCase()}).where(${pkeyName(table).toUpperCase()}.equal(id)).execute();")
          }
        }

        ln(s"")
        block(s"public List<${table.name.toPascal}> selectAll() throws Throwable"){
          ln(s"return selectAll(null, null);")
        }
        
        ln(s"")
        block(s"public List<${table.name.toPascal}> selectAll(PageInfo page, OrderInfo order) throws Throwable") {
          block(s"try( DSLContext create = dsl() )"){
            ln(s"List<${table.name.toPascal}> list = new ArrayList<${table.name.toPascal}>();")
            ln("SelectQuery q = create.selectQuery();")
            ln(s"q.addFrom(${table.name.toUpperCase()});")
            block("if( page != null )") {
              ln("q.addLimit(page.getLimit());")
              ln("q.addOffset(page.getOffset());")
            }
            ln(s"if( order != null ) q.addOrderBy(sortedField(order));")
            ln(s"Result<Record> result = q.fetch();")
            block(s"for(Record r: result)"){
              ln(s"list.add( make${table.name.toPascal}(r) );")
            }
            ln(s"return list;")
          }

        }

        if ( !table.fields.filter(_.has('pkey)).map(f=>f).isEmpty) {
          ln(s"")
          block(s"public ${table.name.toPascal} selectByKey(UUID id) throws Throwable"){
            block(s"try( DSLContext create = dsl() )"){
              ln(s"return make${table.name.toPascal}( create.select().from(${table.name.toUpperCase()}).where(${pkeyName(table).toUpperCase()}.equal(id)).fetchOne() ); ")
            }
          }
        }

        if ( !table.fields.filter(_.has('ref)).map(f=> f).isEmpty ) {
          ln("")
          var fkeyName = table.fields.filter(_.has('ref)).map { f =>
            block(s"public List<${table.name.toPascal}> selectBy${f.name.toPascal}(UUID id, PageInfo page, OrderInfo order) throws Throwable"){
              ln(s"return selectByForignKey(id, ${f.name.toUpperCase}, page, order);")
            }
            ln("")
          }
          block(s"private List<${table.name.toPascal}> selectByForignKey(UUID id, Field<UUID> field_id, PageInfo page, OrderInfo order) throws Throwable"){
            block(s"try( DSLContext create = dsl() )"){
              ln(s"List<${table.name.toPascal}> list = new ArrayList<${table.name.toPascal}>();")
              ln("SelectQuery<Record> result = create.selectQuery();")
              ln(s"result.addFrom(${table.name.toUpperCase()});")
              ln(s"if(id == null)")
              ln(s"  result.addConditions(field_id.isNull());")
              ln(s"else")
              ln(s"  result.addConditions(field_id.equal(id));")
              block("if( page != null )") {
                ln("result.addLimit(page.getLimit());")
                ln("result.addOffset(page.getOffset());")
              }
              ln(s"if( order != null ) result.addOrderBy(sortedField(order));")
              block(s"for( Record r : result)"){
                ln(s"${table.name.toPascal} e = new ${table.name.toPascal}();")
                dbName(table).foreach{f=> ln(s"""e.set${f.toPascal}(r.getValue(${f.toUpperCase}));""") }
                ln(s"list.add(e);")
              }
              ln(s"return list;")
            }
          }
        }

      }
    }
  }
}
