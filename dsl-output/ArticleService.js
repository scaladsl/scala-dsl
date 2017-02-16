function ArticleService(){
  this.retrieveById = function(id, done){
    $.ajax({
    type: "get",
    url: `/api/articles/${id}`,
    data: id,
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
  this.retrieveAll = function(data, done){
    $.ajax({
    type: "get",
    url: `/api/articles`,
    data: data,
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
  this.add = function(json, done){
    $.ajax({
    type: "post",
    url: `/api/articles`,
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
  this.save = function(id, json, done){
    $.ajax({
    type: "put",
    url: `/api/articles/${id}`,
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
  this.remove = function(id, done){
    $.ajax({
    type: "delete",
    url: `/api/articles/${id}`,
    data: id,
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
