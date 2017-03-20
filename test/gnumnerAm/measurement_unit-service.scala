'model ::= namespace {

  // The Measurement unit
  'measurement_unit ::= struct {

    // Measurement unit identity
    'id required 'uuid ('pkey -> true)

    // Measurement unit code
    'code required 'int

    // Measurement unit name hy
    'name_hy required 'string

    // Measurement unit name en
    'name_en required 'string

    // Measurement unit name ru
    'name_ru required 'string

  }

  'model ::= namespace {

    //
    // The service designed to operate with measurement units.
    //
    'measurement_unit_api ::= service(s"/api/measurementUnit") {

      //
      // Retrieves the measurement unit associated with the specified identity.
      //
      'retrieve_by_id ::= get(s"/${'id as 'uuid}") {
        returns required 'model::'measurement_unit
      }

      //
      // Retrieves all the measurement units.
      //
      'retrieve_all ::= get(s"/") {
        returns repeated 'model::'measurement_unit
      }

      //
      // Adds a new measurement unit.
      //
      'add ::= post(s"/") {
        'measurement_unit required 'model::'measurement_unit
      }

      //
      // Updates the existing measurement unit.
      //
      'update ::= put(s"/${'id as 'uuid}") {
        'measurement_unit required 'model::'measurement_unit
      }

      //
      // Removes the specified measurement unit.
      //
      'remove ::= delete(s"/${'id as 'uuid}") {
      }
    }
  }
}
