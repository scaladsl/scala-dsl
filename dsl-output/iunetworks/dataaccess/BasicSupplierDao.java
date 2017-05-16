package am.iunetworks.ppcm.api.dataaccess;

import java.util.*;
import java.sql.*;
import javax.sql.DataSource; 
import am.iunetworks.ppcm.api.service.*;

import static org.jooq.impl.DSL.*;
import org.jooq.*;
import database.dbcp2.*;

public class BasicSupplierDao extends BaseDao {
  public static Table SUPPLIER = table(name("supplier"));
  public static final Field<UUID> ID = field(name("supplier", "id"), UUID.class);
  public static final Field<String> NAME = field(name("supplier", "name"), String.class);
  public static final Field<String> EMAIL = field(name("supplier", "email"), String.class);
  public static final Field<String> COUNTRY = field(name("supplier", "country"), String.class);
  public static final Field<Integer> ARMEPS_ID = field(name("supplier", "armeps_id"), Integer.class);
  public static final Field<String> TAXPAYER_ID = field(name("supplier", "taxpayer_id"), String.class);
  
  protected BasicSupplierDao (DSLContextFactory dslContextFactory) {
    super(dslContextFactory);
  }
  
  private Supplier makeSupplier(Record r) {
    Supplier e = new Supplier();
    e.setId(r.getValue(ID));
    e.setName(r.getValue(NAME));
    e.setEmail(r.getValue(EMAIL));
    e.setCountry(r.getValue(COUNTRY));
    e.setArmepsId(r.getValue(ARMEPS_ID));
    e.setTaxpayerId(r.getValue(TAXPAYER_ID));
    return e;
  }
  
  public void insert(Supplier supplier) throws Throwable {
    try( DSLContext create = dsl() ){
      create.insertInto(SUPPLIER, ID, NAME, EMAIL, COUNTRY, ARMEPS_ID, TAXPAYER_ID).values(supplier.getId(), supplier.getName(), supplier.getEmail(), supplier.getCountry(), supplier.getArmepsId(), supplier.getTaxpayerId()).execute();
    }
  }
  
  public void update(Supplier supplier) throws Throwable {
    try( DSLContext create = dsl() ) {
      create.update(SUPPLIER)
      .set(ID, supplier.getId())
      .set(NAME, supplier.getName())
      .set(EMAIL, supplier.getEmail())
      .set(COUNTRY, supplier.getCountry())
      .set(ARMEPS_ID, supplier.getArmepsId())
      .set(TAXPAYER_ID, supplier.getTaxpayerId())
      .where(ID.equal(supplier.getId()))
      .execute();
    }
  }
  
  public void remove(UUID id) throws Throwable {
    try( DSLContext create = dsl() ) {
      create.delete(SUPPLIER).where(ID.equal(id)).execute();
    }
  }
  
  public List<Supplier> selectAll() throws Throwable {
    return selectAll(null, null);
  }
  
  public List<Supplier> selectAll(PageInfo page, OrderInfo order) throws Throwable {
    try( DSLContext create = dsl() ){
      List<Supplier> list = new ArrayList<Supplier>();
      SelectQuery q = create.selectQuery();
      q.addFrom(SUPPLIER);
      if( page != null ) {
        q.addLimit(page.getLimit());
        q.addOffset(page.getOffset());
      }
      if( order != null ) q.addOrderBy(sortedField(order));
      Result<Record> result = q.fetch();
      for(Record r: result) {
        list.add( makeSupplier(r) );
      }
      return list;
    }
  }
  
  public Supplier selectByKey(UUID id) throws Throwable {
    try( DSLContext create = dsl() ) {
      return  makeSupplier( create.select().from(SUPPLIER).where(ID.equal(id)).fetchOne() ); 
    }
  }
}
