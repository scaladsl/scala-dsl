package blog;
public interface CommentService{
  public java.collection.List<blog.model.Comment> retrieveByArticle(java.util.UUID articleId) throws java.lang.Throwable;
  public java.lang.Void add(java.util.UUID articleId, blog.model.Comment comment) throws java.lang.Throwable;
  public java.lang.Void save(java.util.UUID articleId, java.util.UUID id, blog.model.Comment comment) throws java.lang.Throwable;
  public java.lang.Void remove(java.util.UUID articleId, java.util.UUID id) throws java.lang.Throwable;
}
