package blog;
public interface ArticleService{
  public blog.model.Article retrieveById(java.util.UUID id) throws java.lang.Throwable;
  public java.collection.List<blog.model.Article> retrieveAll() throws java.lang.Throwable;
  public java.lang.Void add(blog.model.Article article) throws java.lang.Throwable;
  public java.lang.Void save(java.util.UUID id, blog.model.Article article) throws java.lang.Throwable;
  public java.lang.Void remove(java.util.UUID id) throws java.lang.Throwable;
}
