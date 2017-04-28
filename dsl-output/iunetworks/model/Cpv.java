package am.iunetworks.ppcm.api.model;

/**  The Cpv */
public class Cpv{
  
  /**  Cpv identity */
  private java.util.UUID id;
  
  /**  Cpv parent id */
  private java.util.UUID parentId;
  
  /**  Cpv code */
  private java.lang.String code;
  
  /**  Cpv name_hy */
  private java.lang.String nameHy;
  
  /**  Cpv name_ru */
  private java.lang.String nameRu;
  
  /**  Cpv name_en */
  private java.lang.String nameEn;
  
  /**  Cpv armeps id */
  private java.lang.Integer armepsId;
  
  /**  Cpv path */
  private java.lang.String path;
  
  /** Gets cpv identity */
  public java.util.UUID getId(){
    return this.id;
  }
  
  /** Sets cpv identity */
  public void setId( java.util.UUID id){
    this.id = id;
  }
  
  /** Gets cpv parent id */
  public java.util.UUID getParentId(){
    return this.parentId;
  }
  
  /** Sets cpv parent id */
  public void setParentId( java.util.UUID parentId){
    this.parentId = parentId;
  }
  
  /** Gets cpv code */
  public java.lang.String getCode(){
    return this.code;
  }
  
  /** Sets cpv code */
  public void setCode( java.lang.String code){
    this.code = code;
  }
  
  /** Gets cpv nameHy */
  public java.lang.String getNameHy(){
    return this.nameHy;
  }
  
  /** Sets cpv nameHy */
  public void setNameHy( java.lang.String nameHy){
    this.nameHy = nameHy;
  }
  
  /** Gets cpv nameRu */
  public java.lang.String getNameRu(){
    return this.nameRu;
  }
  
  /** Sets cpv nameRu */
  public void setNameRu( java.lang.String nameRu){
    this.nameRu = nameRu;
  }
  
  /** Gets cpv nameEn */
  public java.lang.String getNameEn(){
    return this.nameEn;
  }
  
  /** Sets cpv nameEn */
  public void setNameEn( java.lang.String nameEn){
    this.nameEn = nameEn;
  }
  
  /** Gets cpv armeps id */
  public java.lang.Integer getArmepsId(){
    return this.armepsId;
  }
  
  /** Sets cpv armeps id */
  public void setArmepsId( java.lang.Integer armepsId){
    this.armepsId = armepsId;
  }
  
  /** Gets cpv path */
  public java.lang.String getPath(){
    return this.path;
  }
  
  /** Sets cpv path */
  public void setPath( java.lang.String path){
    this.path = path;
  }
}
