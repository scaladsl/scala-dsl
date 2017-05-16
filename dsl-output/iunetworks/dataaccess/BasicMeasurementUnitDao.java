package am.iunetworks.ppcm.api.dataaccess;

import java.util.*;
import java.sql.*;
import javax.sql.DataSource; 
import am.iunetworks.ppcm.api.service.*;

import static org.jooq.impl.DSL.*;
import org.jooq.*;
import database.dbcp2.*;

public class BasicMeasurementUnitDao extends BaseDao {
  public static Table MEASUREMENT_UNIT = table(name("measurement_unit"));
  public static final Field<UUID> ID = field(name("measurement_unit", "id"), UUID.class);
  public static final Field<Integer> CODE = field(name("measurement_unit", "code"), Integer.class);
  public static final Field<String> NAME_HY = field(name("measurement_unit", "name_hy"), String.class);
  public static final Field<String> NAME_EN = field(name("measurement_unit", "name_en"), String.class);
  public static final Field<String> NAME_RU = field(name("measurement_unit", "name_ru"), String.class);
  
  protected BasicMeasurementUnitDao (DSLContextFactory dslContextFactory) {
    super(dslContextFactory);
  }
  
  private MeasurementUnit makeMeasurementUnit(Record r) {
    MeasurementUnit e = new MeasurementUnit();
    e.setId(r.getValue(ID));
    e.setCode(r.getValue(CODE));
    e.setNameHy(r.getValue(NAME_HY));
    e.setNameEn(r.getValue(NAME_EN));
    e.setNameRu(r.getValue(NAME_RU));
    return e;
  }
  
  public void insert(MeasurementUnit measurementUnit) throws Throwable {
    try( DSLContext create = dsl() ){
      create.insertInto(MEASUREMENT_UNIT, ID, CODE, NAME_HY, NAME_EN, NAME_RU).values(measurementUnit.getId(), measurementUnit.getCode(), measurementUnit.getNameHy(), measurementUnit.getNameEn(), measurementUnit.getNameRu()).execute();
    }
  }
  
  public void update(MeasurementUnit measurementUnit) throws Throwable {
    try( DSLContext create = dsl() ) {
      create.update(MEASUREMENT_UNIT)
      .set(ID, measurementUnit.getId())
      .set(CODE, measurementUnit.getCode())
      .set(NAME_HY, measurementUnit.getNameHy())
      .set(NAME_EN, measurementUnit.getNameEn())
      .set(NAME_RU, measurementUnit.getNameRu())
      .where(ID.equal(measurementUnit.getId()))
      .execute();
    }
  }
  
  public void remove(UUID id) throws Throwable {
    try( DSLContext create = dsl() ) {
      create.delete(MEASUREMENT_UNIT).where(ID.equal(id)).execute();
    }
  }
  
  public List<MeasurementUnit> selectAll() throws Throwable {
    return selectAll(null, null);
  }
  
  public List<MeasurementUnit> selectAll(PageInfo page, OrderInfo order) throws Throwable {
    try( DSLContext create = dsl() ){
      List<MeasurementUnit> list = new ArrayList<MeasurementUnit>();
      SelectQuery q = create.selectQuery();
      q.addFrom(MEASUREMENT_UNIT);
      if( page != null ) {
        q.addLimit(page.getLimit());
        q.addOffset(page.getOffset());
      }
      if( order != null ) q.addOrderBy(sortedField(order));
      Result<Record> result = q.fetch();
      for(Record r: result) {
        list.add( makeMeasurementUnit(r) );
      }
      return list;
    }
  }
  
  public MeasurementUnit selectByKey(UUID id) throws Throwable {
    try( DSLContext create = dsl() ) {
      return  makeMeasurementUnit( create.select().from(MEASUREMENT_UNIT).where(ID.equal(id)).fetchOne() ); 
    }
  }
}
