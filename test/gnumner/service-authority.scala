'model ::= namespace {
  // The authority
  'authority ::= struct {
    // authority identity
    'id required 'uuid ('pkey -> true)

    // authority code
    'code required 'string

    // authority name_hy
    'name_hy required 'string

    // authority name_en
    'name_en required 'string

    // authority name_ru
    'name_ru required 'string

    // authority obsolate
    'regional required 'bool
  }

  'service ::= namespace {
    //
    // The service designed to operate with armeps tenders
    //
    'authority ::= service("/api/authorities") {

      //
      // Retrieves the authority associated with the specified identity.
      //
      'retrieve_by_id ::= get(s"/${'id as 'uuid}") {
        returns required 'model::'authority
      }

      //
      // Retrieves all the authority
      //
      'retrieve_all ::= get("/") {
        returns repeated 'model::'authority
      }

    }

  }

}
