'dataaccess ::= namespace {

  // The Cpv
  'cpv ::= table {
    'idik required 'uuid ('pkey -> true)('db_name -> "id")
    'parent_id required 'uuid ('ref -> true)
    'code required 'string
    'name_hy required 'string
    'name_ru required 'string
    'name_en required 'string
    'armeps_id required 'int
    'path required 'string
  }

}
