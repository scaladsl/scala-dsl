'blog ::= namespace {

  'model ::= namespace {

    /// The article
    'article ::= struct {

      /// Article identity
      'id required 'uuid ('pkey -> true)

      /// Article title
      'title required 'string

      /// Article author
      'author required 'string

      /// Article content
      'content required 'string

      /// Article submission date
      'submitted_at required 'date
    }

    // The comment
    'comment ::= struct {

      // Comment identity
      'id required 'uuid ('pkey -> true)

      // Comment article identity
      'article_id required 'uuid('ref -> 'article)

      // Comment author
      'author required 'string

      // Comment content
      'content required 'string

      // Comment submission date
      'submitted_at required 'date
    }
  }
}
