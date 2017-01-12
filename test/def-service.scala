require("./test/def-entity.scala")

'am::'iunetworks::'ppcm::'components::'administration ::= namespace {
  'cpv ::= service("/private/administration/dictionaries/cpv") {
    'list ::= post(s"/list/${'parent as 'uuid}") {
      returns repeated  'cpv
    }

    'get ::= post(s"/get/${'id as 'uuid}") {
      returns required 'am::'iunetworks::'ppcm::'components::'administration ::'cpv
    }

    'paht ::= post(s"/path/${'id as 'uuid}") {
      returns repeated 'am::'iunetworks::'ppcm::'components::'administration ::'cpv
    }

    'count ::= post(s"/count") {
      returns required 'int
    }

    !"""comment for add method"""
    'add ::= post(s"/add") {
      'cpv arg 'am::'iunetworks::'ppcm::'components::'administration ::'cpv
    }

    'save ::= put(s"/path") {
      'cpvcode arg  'am::'iunetworks::'ppcm::'components::'administration ::'cpv
    }
  }

}
