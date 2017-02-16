package model;
class BasicArticleDao{

  private Connection connection;

	 public void setConnection(Connection connection){
    this.connection = connection;
  };

  protected PreparedStatement getPrepareStatement(String sql) throws Throwable {
    PreparedStatement ps = null;
    ps = connection.prepareStatement(sql);
    return ps;
  }

  protected void closePrepareStatement(PreparedStatement ps) throws Throwable {
    if (ps != null) ps.close();
  }

  public void insert(Article article) throws Throwable{
    PreparedStatement ps = null;
    try{
      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String stringDateISO = df.format(article.submittedAt);
      String sql = "insert into article(id, title, author, content, submittedAt) values(?, ?, ?, ?, ?)";
      ps = getPrepareStatement(sql);
      ps.setString(1, article.id.toString());
      ps.setString(2, article.title);
      ps.setString(3, article.author);
      ps.setString(4, article.content);
      ps.setString(5, stringDateISO);
      ps.executeUpdate();
    }
    finally { closePrepareStatement(ps); }
  }
  public void update(Article article) throws Throwable{
    PreparedStatement ps = null;
    try{
      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String stringDateISO = df.format(article.submittedAt);
      String sql = "update article set id = ?, where id = ?;";
      ps = getPrepareStatement(sql);
      ps.setString(1, article.id.toString());
      ps.setString(2, article.title);
      ps.setString(3, article.author);
      ps.setString(4, article.content);
      ps.setString(5, stringDateISO);
      ps.executeUpdate();
    }
    finally { closePrepareStatement(ps); }
  }
  public void remove(UUID id) throws Throwable{
    PreparedStatement ps = null;
    try{
      String sql = "delete from article where id =?;";
      ps = getPrepareStatement(sql);
      ps.setString(1, id.toString());
      ps.executeUpdate();
    }
    finally { closePrepareStatement(ps); }
  }
  public List<Article> selectAll() throws Throwable{
    List<article> articleList = null;
    PreparedStatement ps = null;
    try{
      String sql = "Select * from article";
      articleList = new ArrayList<Article>();
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      ps = getPrepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()){
        Article article = new Article();
        article.id = java.util.UUID.fromString(rs.getString("id"));
        article.title = rs.getString("title");
        article.author = rs.getString("author");
        article.content = rs.getString("content");
        article.submittedAt = formatter.parse(rs.getString("submittedAt"));
        articleList.add(article);
      }
    }
    finally { closePrepareStatement(ps); }
    return articleList;
  }
  public Article selectByKey(java.util.UUID id) throws Throwable{
    Article article = null;
    PreparedStatement ps = null;
    try {
      String sql = "Select * from article where id = ? ;";
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      article = new Article();
      ps = getPrepareStatement(sql);
      ps.setString(1, id.toString());
      ps.setString(1, id.toString());
      ResultSet rs = ps.executeQuery();
      while (rs.next()){
        article.id = java.util.UUID.fromString(rs.getString("id"));
        article.title = rs.getString("title");
        article.author = rs.getString("author");
        article.content = rs.getString("content");
        article.submittedAt = formatter.parse(rs.getString("submittedAt"));
      }
    }
    finally { closePrepareStatement(ps); }
    return article;
  }
  public Article selectBy() throws Throwable{
    Article article = null;
    PreparedStatement ps = null;
    try {
      String sql = "Select * from article where  ;";
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      article = new Article();
      ps = getPrepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()){
        article.id = java.util.UUID.fromString(rs.getString("id"));
        article.title = rs.getString("title");
        article.author = rs.getString("author");
        article.content = rs.getString("content");
        article.submittedAt = formatter.parse(rs.getString("submittedAt"));
      }
    }
    finally { closePrepareStatement(ps); }
    return article;
  }
}
