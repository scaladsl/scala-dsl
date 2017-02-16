package model;
/**  The article */
public class Article{
  /**  Article identity */
  public java.util.UUID id;
  /**  Article title */
  public java.lang.String title;
  /**  Article author */
  public java.lang.String author;
  /**  Article content */
  public java.lang.String content;
  /**  Article submission date */
  public java.util.Date submittedAt;
  public Article() {}
  public Article(java.util.UUID id, java.lang.String title, java.lang.String author, java.lang.String content, java.util.Date submittedAt) {
    this.id = id;
    this.title = title;
    this.author = author;
    this.content = content;
    this.submittedAt = submittedAt;
  }
  public String toString(){
    return  "id: '" + this.id"'" + "title: '" + this.title"'" + "author: '" + this.author"'" + "content: '" + this.content"'" + "submitted_at: '" + this.submittedAt;
  }
}
