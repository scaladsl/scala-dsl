package model;
public interface ArticleService{
  public model.Article retrieveById(java.util.UUID id) throws java.lang.Throwable;
  public java.util.List<model.Article> retrieveAll() throws java.lang.Throwable;
  public void add(model.Article article) throws java.lang.Throwable;
  public void save(java.util.UUID id, model.Article article) throws java.lang.Throwable;
  public void remove(java.util.UUID id) throws java.lang.Throwable;
}
