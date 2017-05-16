package am.iunetworks.ppcm.api.dataaccess;

import java.util.*;
import java.sql.*;
import javax.sql.DataSource; 
import am.iunetworks.ppcm.api.service.*;

import static org.jooq.impl.DSL.*;
import org.jooq.*;
import database.dbcp2.*;

public class BasicAuthorityDao extends BaseDao {
  public static Table AUTHORITY = table(name("authority"));
  public static final Field<UUID> ID = field(name("authority", "id"), UUID.class);
  public static final Field<String> CODE = field(name("authority", "code"), String.class);
  public static final Field<String> NAME_HY = field(name("authority", "name_hy"), String.class);
  public static final Field<String> NAME_EN = field(name("authority", "name_en"), String.class);
  public static final Field<String> NAME_RU = field(name("authority", "name_ru"), String.class);
  public static final Field<Boolean> REGIONAL = field(name("authority", "regional"), Boolean.class);
  
  protected BasicAuthorityDao (DSLContextFactory dslContextFactory) {
    super(dslContextFactory);
  }
  
  private Authority makeAuthority(Record r) {
    Authority e = new Authority();
    e.setId(r.getValue(ID));
    e.setCode(r.getValue(CODE));
    e.setNameHy(r.getValue(NAME_HY));
    e.setNameEn(r.getValue(NAME_EN));
    e.setNameRu(r.getValue(NAME_RU));
    e.setRegional(r.getValue(REGIONAL));
    return e;
  }
  
  public void insert(Authority authority) throws Throwable {
    try( DSLContext create = dsl() ){
      create.insertInto(AUTHORITY, ID, CODE, NAME_HY, NAME_EN, NAME_RU, REGIONAL).values(authority.getId(), authority.getCode(), authority.getNameHy(), authority.getNameEn(), authority.getNameRu(), authority.getRegional()).execute();
    }
  }
  
  public void update(Authority authority) throws Throwable {
    try( DSLContext create = dsl() ) {
      create.update(AUTHORITY)
      .set(ID, authority.getId())
      .set(CODE, authority.getCode())
      .set(NAME_HY, authority.getNameHy())
      .set(NAME_EN, authority.getNameEn())
      .set(NAME_RU, authority.getNameRu())
      .set(REGIONAL, authority.getRegional())
      .where(ID.equal(authority.getId()))
      .execute();
    }
  }
  
  public void remove(UUID id) throws Throwable {
    try( DSLContext create = dsl() ) {
      create.delete(AUTHORITY).where(ID.equal(id)).execute();
    }
  }
  
  public List<Authority> selectAll() throws Throwable {
    return selectAll(null, null);
  }
  
  public List<Authority> selectAll(PageInfo page, OrderInfo order) throws Throwable {
    try( DSLContext create = dsl() ){
      List<Authority> list = new ArrayList<Authority>();
      SelectQuery q = create.selectQuery();
      q.addFrom(AUTHORITY);
      if( page != null ) {
        q.addLimit(page.getLimit());
        q.addOffset(page.getOffset());
      }
      if( order != null ) q.addOrderBy(sortedField(order));
      Result<Record> result = q.fetch();
      for(Record r: result) {
        list.add( makeAuthority(r) );
      }
      return list;
    }
  }
  
  public Authority selectByKey(UUID id) throws Throwable {
    try( DSLContext create = dsl() ) {
      return  makeAuthority( create.select().from(AUTHORITY).where(ID.equal(id)).fetchOne() ); 
    }
  }
}
