package model;
class BasicCommentDao{

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

  public void insert(Comment comment) throws Throwable{
    PreparedStatement ps = null;
    try{
      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String stringDateISO = df.format(comment.submittedAt);
      String sql = "insert into comment(id, articleId, author, content, submittedAt) values(?, ?, ?, ?, ?)";
      ps = getPrepareStatement(sql);
      ps.setString(1, comment.id.toString());
      ps.setString(2, comment.articleId.toString());
      ps.setString(3, comment.author);
      ps.setString(4, comment.content);
      ps.setString(5, stringDateISO);
      ps.executeUpdate();
    }
    finally { closePrepareStatement(ps); }
  }
  public void update(Comment comment) throws Throwable{
    PreparedStatement ps = null;
    try{
      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String stringDateISO = df.format(comment.submittedAt);
      String sql = "update comment set where id = ?;";
      ps = getPrepareStatement(sql);
      ps.setString(1, comment.id.toString());
      ps.setString(2, comment.articleId.toString());
      ps.setString(3, comment.author);
      ps.setString(4, comment.content);
      ps.setString(5, stringDateISO);
      ps.executeUpdate();
    }
    finally { closePrepareStatement(ps); }
  }
  public void remove(UUID id) throws Throwable{
    PreparedStatement ps = null;
    try{
      String sql = "delete from comment where id =?;";
      ps = getPrepareStatement(sql);
      ps.setString(1, id.toString());
      ps.executeUpdate();
    }
    finally { closePrepareStatement(ps); }
  }
  public List<Comment> selectAll() throws Throwable{
    List<comment> commentList = null;
    PreparedStatement ps = null;
    try{
      String sql = "Select * from comment";
      commentList = new ArrayList<Comment>();
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      ps = getPrepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()){
        Comment comment = new Comment();
        comment.id = java.util.UUID.fromString(rs.getString("id"));
        comment.articleId = java.util.UUID.fromString(rs.getString("articleId"));
        comment.author = rs.getString("author");
        comment.content = rs.getString("content");
        comment.submittedAt = formatter.parse(rs.getString("submittedAt"));
        commentList.add(comment);
      }
    }
    finally { closePrepareStatement(ps); }
    return commentList;
  }
  public Comment selectByKey() throws Throwable{
    Comment comment = null;
    PreparedStatement ps = null;
    try {
      String sql = "Select * from comment where  ;";
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      comment = new Comment();
      ps = getPrepareStatement(sql);
      ps.setString(1, id.toString());
      ResultSet rs = ps.executeQuery();
      while (rs.next()){
        comment.id = java.util.UUID.fromString(rs.getString("id"));
        comment.articleId = java.util.UUID.fromString(rs.getString("articleId"));
        comment.author = rs.getString("author");
        comment.content = rs.getString("content");
        comment.submittedAt = formatter.parse(rs.getString("submittedAt"));
      }
    }
    finally { closePrepareStatement(ps); }
    return comment;
  }
  public Comment selectBy(java.util.UUID articleId) throws Throwable{
    Comment comment = null;
    PreparedStatement ps = null;
    try {
      String sql = "Select * from comment where article_id = ? ;";
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      comment = new Comment();
      ps = getPrepareStatement(sql);
      ps.setString(1, articleId.toString());
      ResultSet rs = ps.executeQuery();
      while (rs.next()){
        comment.id = java.util.UUID.fromString(rs.getString("id"));
        comment.articleId = java.util.UUID.fromString(rs.getString("articleId"));
        comment.author = rs.getString("author");
        comment.content = rs.getString("content");
        comment.submittedAt = formatter.parse(rs.getString("submittedAt"));
      }
    }
    finally { closePrepareStatement(ps); }
    return comment;
  }
}
