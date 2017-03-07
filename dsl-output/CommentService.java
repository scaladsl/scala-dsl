package model;
public interface CommentService{
  public java.util.List<model.Comment> retrieveByArticle(java.util.UUID articleId) throws java.lang.Throwable;
  public void add(java.util.UUID articleId, model.Comment comment) throws java.lang.Throwable;
  public void save(java.util.UUID articleId, java.util.UUID id, model.Comment comment) throws java.lang.Throwable;
  public void remove(java.util.UUID articleId, java.util.UUID id) throws java.lang.Throwable;
}
