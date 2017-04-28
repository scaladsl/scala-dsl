package am.iunetworks.ppcm.api.model;

/**  The Suppliar */
public class Supplier{
  
  /**  Suppliar identity */
  private java.util.UUID id;
  
  /**  Suppliar name */
  private java.lang.String name;
  
  /**  Suppliar email */
  private java.lang.String email;
  
  /**  Suppliar country */
  private java.lang.String country;
  
  /**  Suppliar armeps id */
  private java.lang.Integer armepsId;
  
  /**  Suppliar taxpayer id */
  private java.lang.String taxpayerId;
  
  /** Gets suppliar identity */
  public java.util.UUID getId(){
    return this.id;
  }
  
  /** Sets suppliar identity */
  public void setId( java.util.UUID id){
    this.id = id;
  }
  
  /** Gets suppliar name */
  public java.lang.String getName(){
    return this.name;
  }
  
  /** Sets suppliar name */
  public void setName( java.lang.String name){
    this.name = name;
  }
  
  /** Gets suppliar email */
  public java.lang.String getEmail(){
    return this.email;
  }
  
  /** Sets suppliar email */
  public void setEmail( java.lang.String email){
    this.email = email;
  }
  
  /** Gets suppliar country */
  public java.lang.String getCountry(){
    return this.country;
  }
  
  /** Sets suppliar country */
  public void setCountry( java.lang.String country){
    this.country = country;
  }
  
  /** Gets suppliar armeps id */
  public java.lang.Integer getArmepsId(){
    return this.armepsId;
  }
  
  /** Sets suppliar armeps id */
  public void setArmepsId( java.lang.Integer armepsId){
    this.armepsId = armepsId;
  }
  
  /** Gets suppliar taxpayer id */
  public java.lang.String getTaxpayerId(){
    return this.taxpayerId;
  }
  
  /** Sets suppliar taxpayer id */
  public void setTaxpayerId( java.lang.String taxpayerId){
    this.taxpayerId = taxpayerId;
  }
}
