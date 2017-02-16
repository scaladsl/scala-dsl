package model;
public interface ArticleService{
  public model.Article retrieveById(java.util.UUID id) throws java.lang.Throwable;
  public java.collection.List<model.Article> retrieveAll() throws java.lang.Throwable;
  public java.lang.Void add(model.Article article) throws java.lang.Throwable;
  public java.lang.Void save(java.util.UUID id, model.Article article) throws java.lang.Throwable;
  public java.lang.Void remove(java.util.UUID id) throws java.lang.Throwable;
}
