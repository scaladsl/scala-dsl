package am.iunetworks.ppcm.api.model;

/**  The authority */
public class Authority{
  
  /**  authority identity */
  private java.util.UUID id;
  
  /**  authority code */
  private java.lang.String code;
  
  /**  authority name_hy */
  private java.lang.String nameHy;
  
  /**  authority name_en */
  private java.lang.String nameEn;
  
  /**  authority name_ru */
  private java.lang.String nameRu;
  
  /**  authority obsolate */
  private java.lang.Boolean regional;
  
  /** Gets authority identity */
  public java.util.UUID getId(){
    return this.id;
  }
  
  /** Sets authority identity */
  public void setId( java.util.UUID id){
    this.id = id;
  }
  
  /** Gets authority code */
  public java.lang.String getCode(){
    return this.code;
  }
  
  /** Sets authority code */
  public void setCode( java.lang.String code){
    this.code = code;
  }
  
  /** Gets authority nameHy */
  public java.lang.String getNameHy(){
    return this.nameHy;
  }
  
  /** Sets authority nameHy */
  public void setNameHy( java.lang.String nameHy){
    this.nameHy = nameHy;
  }
  
  /** Gets authority nameEn */
  public java.lang.String getNameEn(){
    return this.nameEn;
  }
  
  /** Sets authority nameEn */
  public void setNameEn( java.lang.String nameEn){
    this.nameEn = nameEn;
  }
  
  /** Gets authority nameRu */
  public java.lang.String getNameRu(){
    return this.nameRu;
  }
  
  /** Sets authority nameRu */
  public void setNameRu( java.lang.String nameRu){
    this.nameRu = nameRu;
  }
  
  /** Gets authority obsolate */
  public java.lang.Boolean getRegional(){
    return this.regional;
  }
  
  /** Sets authority obsolate */
  public void setRegional( java.lang.Boolean regional){
    this.regional = regional;
  }
}
