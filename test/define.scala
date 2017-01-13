import sgf.CDSL._
define {
'am :: 'iunetworks :: 'ppcm ::'components ::'administration ::= namespace {

  'article ::= struct {
    'article_id required 'string
    'author required 'string
    'article required 'string
    'date required 'string
  }

  'comment ::= struct {
    'author required 'string
    'comment required 'string
    'date required 'string
    'article_id required 'string
  }

}
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
}
