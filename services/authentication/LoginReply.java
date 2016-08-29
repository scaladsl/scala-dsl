package services.authentication;

public class LoginReply {
  private java.util.Optional<services.authentication.LoginRequest> user1;
  
  public java.util.Optional<services.authentication.LoginRequest> getUser1(java.util.Optional<services.authentication.LoginRequest> user1) {
    return user1;
  }
  
  public void setUser1(java.util.Optional<services.authentication.LoginRequest> user1) {
    if (user1 == null) {
      throw new IllegalArgumentException("user1");
    }
    this.user1 = user1;
  }
}
