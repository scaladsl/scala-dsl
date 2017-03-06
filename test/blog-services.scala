'model ::= namespace {

  // The article
  'article ::= struct {

    // Article identity
    'id required 'uuid

    // Article title
    'title required 'string

    // Article author
    'author required 'string

    // Article content
    'content required 'string

    // Article submission date
    'submitted_at required 'date
  }

  // The comment
  'comment ::= struct {

    // Comment identity
    'id required 'uuid

    // Comment article identity
    'article_id required 'uuid('ref -> "article")

    // Comment author
    'author required 'string

    // Comment content
    'content required 'string

    // Comment submission date
    'submitted_at required 'date
  }
}

'model ::= namespace {

  //
  // The service designed to operate with articles.
  //
  'article_service ::= service(s"/api/articles") {

    //
    // Retrieves the article associated with the specified identity.
    //
    'retrieve_by_id ::= get(s"/${'id as 'uuid}") {
      returns required 'model::'article
    }

    //
    // Retrieves all the articles.
    //
    'retrieve_all ::= get(s"/") {
      returns repeated 'model::'article
    }

    //
    // Adds a new article.
    //
    'add ::= post(s"/") {
      'article required 'model::'article
    }

    //
    // Updates the existing article.
    //
    'save ::= put(s"/${'id as 'uuid}") {
      'article required 'model::'article
    }

    //
    // Removes the specified article.
    //
    'remove ::= delete(s"/${'id as 'uuid}") {
    }
  }

  //
  // The service designed to operate with comment entities.
  //
  'comment_service ::= service(s"/api/articles") {

    //
    // Retrieves all the comments associated with the specified article.
    //
    'retrieve_by_article ::= get(s"/${'article_id as 'uuid}/comments") {
      returns repeated 'model::'comment
    }

    //
    // Adds a new comment to the specified article.
    //
    'add ::= post(s"/${'article_id as 'uuid}/comments") {
      'comment required 'model::'comment
    }

    //
    // Updates the existing comment.
    //
    'save ::= put(s"/${'article_id as 'uuid}/comments/${'id as 'uuid}") {
      'comment required 'model::'comment
    }

    //
    // Removes the specified comment.
    //
    'remove ::= delete(s"/${'article_id as 'uuid}/comments/${'id as 'uuid}") {
    }
  }

}
