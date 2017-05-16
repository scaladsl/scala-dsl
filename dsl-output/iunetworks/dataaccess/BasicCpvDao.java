package am.iunetworks.ppcm.api.dataaccess;

import java.util.*;
import java.sql.*;
import javax.sql.DataSource; 
import am.iunetworks.ppcm.api.service.*;

import static org.jooq.impl.DSL.*;
import org.jooq.*;
import database.dbcp2.*;

public class BasicCpvDao extends BaseDao {
  public static Table CPV = table(name("cpv"));
  public static final Field<UUID> ID = field(name("cpv", "id"), UUID.class);
  public static final Field<UUID> PARENT_ID = field(name("cpv", "parent_id"), UUID.class);
  public static final Field<String> CODE = field(name("cpv", "code"), String.class);
  public static final Field<String> NAME_HY = field(name("cpv", "name_hy"), String.class);
  public static final Field<String> NAME_RU = field(name("cpv", "name_ru"), String.class);
  public static final Field<String> NAME_EN = field(name("cpv", "name_en"), String.class);
  public static final Field<Integer> ARMEPS_ID = field(name("cpv", "armeps_id"), Integer.class);
  public static final Field<String> PATH = field(name("cpv", "path"), String.class);
  
  protected BasicCpvDao (DSLContextFactory dslContextFactory) {
    super(dslContextFactory);
  }
  
  private Cpv makeCpv(Record r) {
    Cpv e = new Cpv();
    e.setId(r.getValue(ID));
    e.setParentId(r.getValue(PARENT_ID));
    e.setCode(r.getValue(CODE));
    e.setNameHy(r.getValue(NAME_HY));
    e.setNameRu(r.getValue(NAME_RU));
    e.setNameEn(r.getValue(NAME_EN));
    e.setArmepsId(r.getValue(ARMEPS_ID));
    e.setPath(r.getValue(PATH));
    return e;
  }
  
  public void insert(Cpv cpv) throws Throwable {
    try( DSLContext create = dsl() ){
      create.insertInto(CPV, ID, PARENT_ID, CODE, NAME_HY, NAME_RU, NAME_EN, ARMEPS_ID, PATH).values(cpv.getId(), cpv.getParentId(), cpv.getCode(), cpv.getNameHy(), cpv.getNameRu(), cpv.getNameEn(), cpv.getArmepsId(), cpv.getPath()).execute();
    }
  }
  
  public void update(Cpv cpv) throws Throwable {
    try( DSLContext create = dsl() ) {
      create.update(CPV)
      .set(ID, cpv.getId())
      .set(PARENT_ID, cpv.getParentId())
      .set(CODE, cpv.getCode())
      .set(NAME_HY, cpv.getNameHy())
      .set(NAME_RU, cpv.getNameRu())
      .set(NAME_EN, cpv.getNameEn())
      .set(ARMEPS_ID, cpv.getArmepsId())
      .set(PATH, cpv.getPath())
      .where(ID.equal(cpv.getId()))
      .execute();
    }
  }
  
  public void remove(UUID id) throws Throwable {
    try( DSLContext create = dsl() ) {
      create.delete(CPV).where(ID.equal(id)).execute();
    }
  }
  
  public List<Cpv> selectAll() throws Throwable {
    return selectAll(null, null);
  }
  
  public List<Cpv> selectAll(PageInfo page, OrderInfo order) throws Throwable {
    try( DSLContext create = dsl() ){
      List<Cpv> list = new ArrayList<Cpv>();
      SelectQuery q = create.selectQuery();
      q.addFrom(CPV);
      if( page != null ) {
        q.addLimit(page.getLimit());
        q.addOffset(page.getOffset());
      }
      if( order != null ) q.addOrderBy(sortedField(order));
      Result<Record> result = q.fetch();
      for(Record r: result) {
        list.add( makeCpv(r) );
      }
      return list;
    }
  }
  
  public Cpv selectByKey(UUID id) throws Throwable {
    try( DSLContext create = dsl() ) {
      return  makeCpv( create.select().from(CPV).where(ID.equal(id)).fetchOne() ); 
    }
  }
  
  public List<Cpv> selectByParentId(UUID id, PageInfo page, OrderInfo order) throws Throwable {
    return selectByForignKey(id, PARENT_ID, page, order);
  }
  
  private List<Cpv> selectByForignKey(UUID id, Field<UUID> field_id, PageInfo page, OrderInfo order) throws Throwable {
    try( DSLContext create = dsl() ) {
      List<Cpv> list = new ArrayList<Cpv>();
      SelectQuery<Record> result = create.selectQuery();
      result.addFrom(CPV);
      if(id == null)
        result.addConditions(field_id.isNull());
      else
        result.addConditions(field_id.equal(id));
      if( page != null ) {
        result.addLimit(page.getLimit());
        result.addOffset(page.getOffset());
      }
      if( order != null ) result.addOrderBy(sortedField(order));
      for( Record r : result) {
        Cpv e = new Cpv();
        e.setId(r.getValue(ID));
        e.setParentId(r.getValue(PARENT_ID));
        e.setCode(r.getValue(CODE));
        e.setNameHy(r.getValue(NAME_HY));
        e.setNameRu(r.getValue(NAME_RU));
        e.setNameEn(r.getValue(NAME_EN));
        e.setArmepsId(r.getValue(ARMEPS_ID));
        e.setPath(r.getValue(PATH));
        list.add(e);
      }
      return list;
    }
  }
}
