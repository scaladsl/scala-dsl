'model ::= namespace {

  // The Suppliar
  'supplier ::= struct {

    // Suppliar identity
    'id required 'uuid ('pkey -> true)

    // Suppliar name
    'name required 'string

    // Suppliar email
    'email required 'string

    // Suppliar country
    'country required 'string

    // Suppliar armeps id
    'armeps_id required 'int

    // Suppliar taxpayer id
    'taxpayer_id required 'string

  }

  'model ::= namespace {

    //
    // The service designed to operate with suppliers.
    //
    'supplier_service ::= service(s"/api/suppliers") {

      //
      // Retrieves the supplier associated with the specified identity.
      //
      'retrieve_by_id ::= get(s"/${'id as 'uuid}") {
        returns required 'model::'supplier
      }

      //
      // Retrieves all the suppliers.
      //
      'retrieve_all ::= get(s"/") {
        returns repeated 'model::'supplier
      }

      //
      // Adds a new supplier.
      //
      'add ::= post(s"/") {
        'supplier required 'model::'supplier
      }

      //
      // Updates the existing supplier.
      //
      'update ::= put(s"/${'id as 'uuid}") {
        'supplier required 'model::'supplier
      }

      //
      // Removes the specified supplier.
      //
      'remove ::= delete(s"/${'id as 'uuid}") {
      }
    }
  }
}
