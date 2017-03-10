angular.module("app", ['ngRoute']).service("CommentService", ['$http', CommentService]);
function CommentService($http){
  this.retrieveByArticle = function(article_id){
    return $http.get('/api/articles/'+article_id+'/comments')
  }
  this.add = function(article_id, comment){
    return $http.post('/api/articles/'+article_id+'/comments', comment)
  }
  this.save = function(article_id, id, comment){
    return $http.put('/api/articles/'+article_id+'/comments/'+id+'', comment)
  }
  this.remove = function(article_id, id){
    return $http.delete('/api/articles/'+article_id+'/comments/'+id+'')
  }
}
