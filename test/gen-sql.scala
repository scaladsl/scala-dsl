import dslg.gdsl._

def sqlite(dt: Datatype): String = {
  dt match {
    case d: DateDatatype => "TEXT"
    case s: StringDatatype => "TEXT"
    case i: IntDatatype => "INTEGER"
    case _ => ""
  }
}

def nullable(field: Field): String = {
  if( field.modifier == "required" )
    "not null"
  else if ( field.modifier == "optional" )
    "null"
  else
    error(s"invalid field type: ${field}")
}

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
        ln(s"  ${f.name} ${sqlite(f.datatype)} ${nullable(f)},")
      }

      val fkeys = structure.fields.filter(_.has('ref))
      val pkeys = structure.fields.filter(_.has('pkey)).map(_.name)
      ln(s"  primary key ( ${pkeys.mkString(", ")} )")
      fkeys.foreach{f=>
        ln(s"  FOREIGN KEY(${f.name}) REFERENCES ${structure.name}(${f.name})")
      }
    }
  }

}
