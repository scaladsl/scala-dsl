package model;
public interface CommentService{
  public java.collection.List<model.Comment> retrieveByArticle(java.util.UUID articleId) throws java.lang.Throwable;
  public java.lang.Void add(java.util.UUID articleId, model.Comment comment) throws java.lang.Throwable;
  public java.lang.Void save(java.util.UUID articleId, java.util.UUID id, model.Comment comment) throws java.lang.Throwable;
  public java.lang.Void remove(java.util.UUID articleId, java.util.UUID id) throws java.lang.Throwable;
}
