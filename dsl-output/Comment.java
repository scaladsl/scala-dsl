package model;
/**  The comment */
public class Comment{
  /**  Comment identity */
  public java.util.UUID id;
  /**  Comment article identity */
  public java.util.UUID articleId;
  /**  Comment author */
  public java.lang.String author;
  /**  Comment content */
  public java.lang.String content;
  /**  Comment submission date */
  public java.util.Date submittedAt;
  public Comment() {}
  public Comment(java.util.UUID id, java.util.UUID articleId, java.lang.String author, java.lang.String content, java.util.Date submittedAt) {
    this.id = id;
    this.articleId = articleId;
    this.author = author;
    this.content = content;
    this.submittedAt = submittedAt;
  }
  public String toString(){
    return  "id: '" + this.id"'" + "article_id: '" + this.articleId"'" + "author: '" + this.author"'" + "content: '" + this.content"'" + "submitted_at: '" + this.submittedAt;
  }
}
