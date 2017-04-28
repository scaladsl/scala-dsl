package am.iunetworks.ppcm.api.model;

/**  The Measurement unit */
public class MeasurementUnit{
  
  /**  Measurement unit identity */
  private java.util.UUID id;
  
  /**  Measurement unit code */
  private java.lang.Integer code;
  
  /**  Measurement unit name hy */
  private java.lang.String nameHy;
  
  /**  Measurement unit name en */
  private java.lang.String nameEn;
  
  /**  Measurement unit name ru */
  private java.lang.String nameRu;
  
  /** Gets measurement unit identity */
  public java.util.UUID getId(){
    return this.id;
  }
  
  /** Sets measurement unit identity */
  public void setId( java.util.UUID id){
    this.id = id;
  }
  
  /** Gets measurement unit code */
  public java.lang.Integer getCode(){
    return this.code;
  }
  
  /** Sets measurement unit code */
  public void setCode( java.lang.Integer code){
    this.code = code;
  }
  
  /** Gets measurement unit name hy */
  public java.lang.String getNameHy(){
    return this.nameHy;
  }
  
  /** Sets measurement unit name hy */
  public void setNameHy( java.lang.String nameHy){
    this.nameHy = nameHy;
  }
  
  /** Gets measurement unit name en */
  public java.lang.String getNameEn(){
    return this.nameEn;
  }
  
  /** Sets measurement unit name en */
  public void setNameEn( java.lang.String nameEn){
    this.nameEn = nameEn;
  }
  
  /** Gets measurement unit name ru */
  public java.lang.String getNameRu(){
    return this.nameRu;
  }
  
  /** Sets measurement unit name ru */
  public void setNameRu( java.lang.String nameRu){
    this.nameRu = nameRu;
  }
}
