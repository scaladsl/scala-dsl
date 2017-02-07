package blog;
public class Article{
  public java.util.UUID id;
  public java.lang.String title;
  public java.lang.String author;
  public java.lang.String content;
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
