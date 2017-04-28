'model ::= namespace {

  // The Cpv
  'cpv ::= struct {

    // Cpv identity
    'id required 'uuid ('pkey -> true)

    // Cpv parent id
    'parent_id required 'uuid

    // Cpv code
    'code required 'string

    // Cpv name_hy
    'name_hy required 'string

    // Cpv name_ru
    'name_ru required 'string

    // Cpv name_en
    'name_en required 'string

    // Cpv armeps id
    'armeps_id required 'int

    // Cpv path
    'path required 'string
  }

  'service ::= namespace {

    //
    // The service designed to operate with cpvs.
    //
    'cpv ::= service(s"/api/cpvs") {

      //
      // Retrieves the cpv associated with the specified identity.
      //
      'retrieve_by_id ::= get(s"/${'id as 'uuid}") {
        returns required 'cpv
      }

      //
      // Retrieves all the cpvs.
      //
      'retrieve_all ::= get(s"/") {
        returns repeated 'cpv
      }
    }
  }
}
