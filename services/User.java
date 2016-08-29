package services;

public class User {
  private java.util.List<java.String> username;
  private java.util.Optional<java.String> forename;
  private java.util.Optional<java.String> surname;
  
  public java.util.List<java.String> getUsername(java.util.List<java.String> username) {
    return username;
  }
  
  public java.util.Optional<java.String> getForename(java.util.Optional<java.String> forename) {
    return forename;
  }
  
  public void setForename(java.util.Optional<java.String> forename) {
    if (forename == null) {
      throw new IllegalArgumentException("forename");
    }
    this.forename = forename;
  }
  
  public java.util.Optional<java.String> getSurname(java.util.Optional<java.String> surname) {
    return surname;
  }
  
  public void setSurname(java.util.Optional<java.String> surname) {
    if (surname == null) {
      throw new IllegalArgumentException("surname");
    }
    this.surname = surname;
  }
}
