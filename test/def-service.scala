'am::'iunetworks::'ppcm::'components::'administration ::= namespace {

  'spark ::= service("/articles") {

    'get ::= get(s"/${'id as 'uuid}") {
      returns required 'uuid
    }

    'post ::= post(s"/${'id as 'uuid}/comments") {
      returns required 'uuid
    }

    'get ::= get(s"/${'id as 'uuid}/comments") {
      returns required 'uuid
    }
  }

}
