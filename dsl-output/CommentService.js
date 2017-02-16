function CommentService(){
  this.retrieveByArticle = function(article_id, done){
    $.ajax({
    type: "get",
    url: `/api/articles/${article_id}/comments`,
    data: article_id,
    success: function(data) {
    done(JSON.parse(data));
    },
     error: function(XMLHttpRequest, textStatus, errorThrown) {
      console.log(XMLHttpRequest);
      console.log(XMLHttpRequest.status);
      console.log(errorThrown);
    }
    });
  }
  this.add = function(article_id, json, done){
    $.ajax({
    type: "post",
    url: `/api/articles/${article_id}/comments`,
    data: json,
    success: function(data) {
    done;
    },
     error: function(XMLHttpRequest, textStatus, errorThrown) {
      console.log(XMLHttpRequest);
      console.log(XMLHttpRequest.status);
      console.log(errorThrown);
    }
    });
  }
  this.save = function(article_id, id, json, done){
    $.ajax({
    type: "put",
    url: `/api/articles/${article_id}/comments/${id}`,
    data: json,
    success: function(data) {
    done;
    },
     error: function(XMLHttpRequest, textStatus, errorThrown) {
      console.log(XMLHttpRequest);
      console.log(XMLHttpRequest.status);
      console.log(errorThrown);
    }
    });
  }
  this.remove = function(article_id, id, done){
    $.ajax({
    type: "delete",
    url: `/api/articles/${article_id}/comments/${id}`,
    data: article_id, id,
    success: function(data) {
    done;
    },
     error: function(XMLHttpRequest, textStatus, errorThrown) {
      console.log(XMLHttpRequest);
      console.log(XMLHttpRequest.status);
      console.log(errorThrown);
    }
    });
  }
}
