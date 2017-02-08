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

  'services ::= namespace {

    ///
    /// The service designed to operate with articles.
    ///
    'article_service ::= service(s"/api/articles") {

      ///
      /// Retrieves the article associated with the specified identity.
      ///
      'retrieve_by_id ::= get(s"/${'id as 'uuid}") {
        returns required 'blog::'model::'article
      }

      ///
      /// Retrieves all the articles.
      ///
      'retrieve_all ::= get(s"/") {
        returns repeated 'blog::'model::'article
      }

      ///
      /// Adds a new article.
      ///
      'add ::= post(s"/") {
        'article required 'blog::'model::'article
      }

      ///
      /// Updates the existing article.
      ///
      'save ::= put(s"/${'id as 'uuid}") {
        'article required 'blog::'model::'article
      }

      ///
      /// Removes the specified article.
      ///
      'remove ::= drop(s"/${'id as 'uuid}") {
      }
    }

    ///
    /// The service designed to operate with comment entities.
    ///
    'comment_service ::= service(s"/api/aticles") {

      ///
      /// Retrieves all the comments associated with the specified article.
      ///
      'retrieve_by_article ::= get(s"/${'article_id as 'uuid}/comments") {
        returns repeated 'blog::'model::'comment
      }

      ///
      /// Adds a new comment to the specified article.
      ///
      'add ::= post(s"/${'article_id as 'uuid}/comments") {
        'comment required 'blog::'model::'comment
      }

      ///
      /// Updates the existing comment.
      ///
      'save ::= put(s"/${'article_id as 'uuid}/comments/${'id as 'uuid}") {
        'comment required 'blog::'model::'comment
      }

      ///
      /// Removes the specified comment.
      ///
      'remove ::= drop(s"/${'article_id as 'uuid}/comments/${'id as 'uuid}") {
      }
    }

  }

}
