import dslg.gdsl._

def sqlite(dt: Datatype): String = {
  dt match {
    case d: DateDatatype => "TEXT"
    case s: StringDatatype => "TEXT"
    case i: IntDatatype => "INTEGER"
    case _ => ""
  }
}

def error(error: String): String ={
  error
}

def nullable(field: Field): String = {
  if( field.modifier == "required" )
    "not null"
  else if ( field.modifier == "optional" )
    "null"
  else
    error(s"invalid field type: ${field}")
}

def hasPrimaryKey(s: Structure): Boolean = s.fields.find(_.name == "id").isDefined

generate {

  begin ALL { root =>
    options.blockStart = "("
    options.blockEnd = ");"
    options.currentDir = config.output
    openFile("database.sql")
  }

  end ALL { root => closeFile() }

  begin STRUCTURE { structure =>
    block(s"create table ${structure.name}") {
      structure.fields.foreach { f =>
        ln(s"-- ${f.comment}")
        ln(s"${f.name} ${sqlite(f.datatype)} ${nullable(f)},")
      }

      structure.fields.filter(_.has('ref)).foreach { f =>
        ln(s"foreign key ( ${f.name} ) references ${f('ref)}( id ),")
      }

      if ( hasPrimaryKey(structure) ) {
        ln(s"primary key ( id )")
      }
    }
  }

}
