angular.module("app", ['ngRoute']).service("ArticleService", ['$http', ArticleService]);
function ArticleService($http){
  this.retrieveById = function(id){
    return $http.get('/api/articles/'+id+'')
  }
  this.retrieveAll = function(){
    return $http.get('/api/articles')
  }
  this.add = function(article){
    return $http.post('/api/articles', article)
  }
  this.save = function(id, article){
    return $http.put('/api/articles/'+id+'', article)
  }
  this.remove = function(id){
    return $http.delete('/api/articles/'+id+'')
  }
}
