package services.authentication;

public class LoginRequest {
  private java.String username;
  private java.String password;
  
  public java.String getUsername(java.String username) {
    return username;
  }
  
  public void setUsername(java.String username) {
    if (username == null) {
      throw new IllegalArgumentException("username");
    }
    this.username = username;
  }
  
  public java.String getPassword(java.String password) {
    return password;
  }
  
  public void setPassword(java.String password) {
    if (password == null) {
      throw new IllegalArgumentException("password");
    }
    this.password = password;
  }
}
