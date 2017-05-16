package am.iunetworks.ppcm.api.dataaccess;

import java.util.*;
import java.sql.*;
import javax.sql.DataSource; 
import am.iunetworks.ppcm.api.service.*;

import static org.jooq.impl.DSL.*;
import org.jooq.*;
import database.dbcp2.*;

public class BasicFormDao extends BaseDao {
  public static Table FORM = table(name("form"));
  public static final Field<UUID> ID = field(name("form", "id"), UUID.class);
  public static final Field<String> CODE = field(name("form", "code"), String.class);
  public static final Field<String> NAME_HY = field(name("form", "name_hy"), String.class);
  public static final Field<String> ABBR_HY = field(name("form", "abbr_hy"), String.class);
  public static final Field<String> NAME_EN = field(name("form", "name_en"), String.class);
  public static final Field<String> ABBR_EN = field(name("form", "abbr_en"), String.class);
  public static final Field<String> NAME_RU = field(name("form", "name_ru"), String.class);
  public static final Field<String> ABBR_RU = field(name("form", "abbr_ru"), String.class);
  public static final Field<Boolean> OBSOLETE = field(name("form", "obsolete"), Boolean.class);
  
  protected BasicFormDao (DSLContextFactory dslContextFactory) {
    super(dslContextFactory);
  }
  
  private Form makeForm(Record r) {
    Form e = new Form();
    e.setId(r.getValue(ID));
    e.setCode(r.getValue(CODE));
    e.setNameHy(r.getValue(NAME_HY));
    e.setAbbrHy(r.getValue(ABBR_HY));
    e.setNameEn(r.getValue(NAME_EN));
    e.setAbbrEn(r.getValue(ABBR_EN));
    e.setNameRu(r.getValue(NAME_RU));
    e.setAbbrRu(r.getValue(ABBR_RU));
    e.setObsolete(r.getValue(OBSOLETE));
    return e;
  }
  
  public void insert(Form form) throws Throwable {
    try( DSLContext create = dsl() ){
      create.insertInto(FORM, ID, CODE, NAME_HY, ABBR_HY, NAME_EN, ABBR_EN, NAME_RU, ABBR_RU, OBSOLETE).values(form.getId(), form.getCode(), form.getNameHy(), form.getAbbrHy(), form.getNameEn(), form.getAbbrEn(), form.getNameRu(), form.getAbbrRu(), form.getObsolete()).execute();
    }
  }
  
  public void update(Form form) throws Throwable {
    try( DSLContext create = dsl() ) {
      create.update(FORM)
      .set(ID, form.getId())
      .set(CODE, form.getCode())
      .set(NAME_HY, form.getNameHy())
      .set(ABBR_HY, form.getAbbrHy())
      .set(NAME_EN, form.getNameEn())
      .set(ABBR_EN, form.getAbbrEn())
      .set(NAME_RU, form.getNameRu())
      .set(ABBR_RU, form.getAbbrRu())
      .set(OBSOLETE, form.getObsolete())
      .where(ID.equal(form.getId()))
      .execute();
    }
  }
  
  public void remove(UUID id) throws Throwable {
    try( DSLContext create = dsl() ) {
      create.delete(FORM).where(ID.equal(id)).execute();
    }
  }
  
  public List<Form> selectAll() throws Throwable {
    return selectAll(null, null);
  }
  
  public List<Form> selectAll(PageInfo page, OrderInfo order) throws Throwable {
    try( DSLContext create = dsl() ){
      List<Form> list = new ArrayList<Form>();
      SelectQuery q = create.selectQuery();
      q.addFrom(FORM);
      if( page != null ) {
        q.addLimit(page.getLimit());
        q.addOffset(page.getOffset());
      }
      if( order != null ) q.addOrderBy(sortedField(order));
      Result<Record> result = q.fetch();
      for(Record r: result) {
        list.add( makeForm(r) );
      }
      return list;
    }
  }
  
  public Form selectByKey(UUID id) throws Throwable {
    try( DSLContext create = dsl() ) {
      return  makeForm( create.select().from(FORM).where(ID.equal(id)).fetchOne() ); 
    }
  }
}
