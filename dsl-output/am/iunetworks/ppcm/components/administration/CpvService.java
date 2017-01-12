package am.iunetworks.ppcm.components.administration;
public interface Cpv {
  java.collection.List<am.iunetworks.ppcm.components.administration.Cpv> list(java.util.UUID parent) throws Throwable;
  am.iunetworks.ppcm.components.administration.Cpv get(java.util.UUID id) throws Throwable;
  java.collection.List<am.iunetworks.ppcm.components.administration.Cpv> paht(java.util.UUID id) throws Throwable;
  java.lang.Integer count() throws Throwable;
  /** comment for add method */
  java.lang.Void add(am.iunetworks.ppcm.components.administration.Cpv cpv) throws Throwable;
  java.lang.Void save(am.iunetworks.ppcm.components.administration.Cpv cpvcode) throws Throwable;
}
