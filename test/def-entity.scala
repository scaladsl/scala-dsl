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
