'model ::= namespace {

  // The form
  'form ::= struct {

    // form identity
    'id required 'uuid

    // form code
    'code required 'string

    // form name_hy
    'name_hy required 'string

    // form abbr_hy 
    'abbr_hy required 'string

    // form name_en
    'name_en required 'string

    // form abbr_en
    'abbr_en required 'string

    // form name_ru
    'name_ru required 'string

    // form abbr_ru
    'abbr_ru required 'string

    // form obsolate
    'obsolete required 'bool
  }

  'model ::= namespace {

    //
    // The service designed to operate with armeps tenders
    //
    'form ::= service("/api/form") {


      //
      // Retrieves the form associated with the specified identity.
      //
      'retrieve_by_id ::= get(s"/${'id as 'uuid}") {
        returns required 'model::'form
      }

      //
      // Retrives all the form
      //
      'retrive_all ::= get("/") {
        returns repeated 'model::'form
      }

    }

  }

}
