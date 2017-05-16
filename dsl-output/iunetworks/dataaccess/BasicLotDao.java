package am.iunetworks.ppcm.api.dataaccess;

import java.util.*;
import java.sql.*;
import javax.sql.DataSource; 
import am.iunetworks.ppcm.api.service.*;

import static org.jooq.impl.DSL.*;
import org.jooq.*;
import database.dbcp2.*;

public class BasicLotDao extends BaseDao {
  public static Table LOT = table(name("lot"));
  public static final Field<UUID> ID = field(name("lot", "id"), UUID.class);
  public static final Field<UUID> TENDER_ID = field(name("lot", "tender_id"), UUID.class);
  public static final Field<UUID> PLAN_ID = field(name("lot", "plan_id"), UUID.class);
  public static final Field<UUID> MEASUREMENT_UNIT_ID = field(name("lot", "measurement_unit_id"), UUID.class);
  public static final Field<String> NAME = field(name("lot", "name"), String.class);
  public static final Field<String> STATE = field(name("lot", "state"), String.class);
  public static final Field<Double> QUANTITY_AVAILABLE = field(name("lot", "quantity_available"), Double.class);
  public static final Field<Double> QUANTITY_TOTAL = field(name("lot", "quantity_total"), Double.class);
  public static final Field<Double> AMOUNT_AVAILABLE = field(name("lot", "amount_available"), Double.class);
  public static final Field<Double> AMOUNT_TOTAL = field(name("lot", "amount_total"), Double.class);
  public static final Field<Integer> NUMBER = field(name("lot", "number"), Integer.class);
  public static final Field<UUID> WINNER_ID = field(name("lot", "winner_id"), UUID.class);
  public static final Field<String> BRIEF_DESCRIPTION_PER_TENDER = field(name("lot", "brief_description_per_tender"), String.class);
  public static final Field<String> BRIEF_DESCRIPTION_PER_CONTRACT = field(name("lot", "brief_description_per_contract"), String.class);
  public static final Field<String> OTHER_INFO_LOTS_FAILURE = field(name("lot", "other_info_lots_failure"), String.class);
  public static final Field<String> REJECTION_BASIS = field(name("lot", "rejection_basis"), String.class);
  public static final Field<String> TECH_SPEC = field(name("lot", "tech_spec"), String.class);
  public static final Field<String> EXTRA_BUDGET_SOURCE = field(name("lot", "extra_budget_source"), String.class);
  
  protected BasicLotDao (DSLContextFactory dslContextFactory) {
    super(dslContextFactory);
  }
  
  private Lot makeLot(Record r) {
    Lot e = new Lot();
    e.setId(r.getValue(ID));
    e.setTenderId(r.getValue(TENDER_ID));
    e.setPlanId(r.getValue(PLAN_ID));
    e.setMeasurementUnitId(r.getValue(MEASUREMENT_UNIT_ID));
    e.setName(r.getValue(NAME));
    e.setState(r.getValue(STATE));
    e.setQuantityAvailable(r.getValue(QUANTITY_AVAILABLE));
    e.setQuantityTotal(r.getValue(QUANTITY_TOTAL));
    e.setAmountAvailable(r.getValue(AMOUNT_AVAILABLE));
    e.setAmountTotal(r.getValue(AMOUNT_TOTAL));
    e.setNumber(r.getValue(NUMBER));
    e.setWinnerId(r.getValue(WINNER_ID));
    e.setBriefDescriptionPerTender(r.getValue(BRIEF_DESCRIPTION_PER_TENDER));
    e.setBriefDescriptionPerContract(r.getValue(BRIEF_DESCRIPTION_PER_CONTRACT));
    e.setOtherInfoLotsFailure(r.getValue(OTHER_INFO_LOTS_FAILURE));
    e.setRejectionBasis(r.getValue(REJECTION_BASIS));
    e.setTechSpec(r.getValue(TECH_SPEC));
    e.setExtraBudgetSource(r.getValue(EXTRA_BUDGET_SOURCE));
    return e;
  }
  
  public void insert(Lot lot) throws Throwable {
    try( DSLContext create = dsl() ){
      create.insertInto(LOT, ID, TENDER_ID, PLAN_ID, MEASUREMENT_UNIT_ID, NAME, STATE, QUANTITY_AVAILABLE, QUANTITY_TOTAL, AMOUNT_AVAILABLE, AMOUNT_TOTAL, NUMBER, WINNER_ID, BRIEF_DESCRIPTION_PER_TENDER, BRIEF_DESCRIPTION_PER_CONTRACT, OTHER_INFO_LOTS_FAILURE, REJECTION_BASIS, TECH_SPEC, EXTRA_BUDGET_SOURCE).values(lot.getId(), lot.getTenderId(), lot.getPlanId(), lot.getMeasurementUnitId(), lot.getName(), lot.getState(), lot.getQuantityAvailable(), lot.getQuantityTotal(), lot.getAmountAvailable(), lot.getAmountTotal(), lot.getNumber(), lot.getWinnerId(), lot.getBriefDescriptionPerTender(), lot.getBriefDescriptionPerContract(), lot.getOtherInfoLotsFailure(), lot.getRejectionBasis(), lot.getTechSpec(), lot.getExtraBudgetSource()).execute();
    }
  }
  
  public void update(Lot lot) throws Throwable {
    try( DSLContext create = dsl() ) {
      create.update(LOT)
      .set(ID, lot.getId())
      .set(TENDER_ID, lot.getTenderId())
      .set(PLAN_ID, lot.getPlanId())
      .set(MEASUREMENT_UNIT_ID, lot.getMeasurementUnitId())
      .set(NAME, lot.getName())
      .set(STATE, lot.getState())
      .set(QUANTITY_AVAILABLE, lot.getQuantityAvailable())
      .set(QUANTITY_TOTAL, lot.getQuantityTotal())
      .set(AMOUNT_AVAILABLE, lot.getAmountAvailable())
      .set(AMOUNT_TOTAL, lot.getAmountTotal())
      .set(NUMBER, lot.getNumber())
      .set(WINNER_ID, lot.getWinnerId())
      .set(BRIEF_DESCRIPTION_PER_TENDER, lot.getBriefDescriptionPerTender())
      .set(BRIEF_DESCRIPTION_PER_CONTRACT, lot.getBriefDescriptionPerContract())
      .set(OTHER_INFO_LOTS_FAILURE, lot.getOtherInfoLotsFailure())
      .set(REJECTION_BASIS, lot.getRejectionBasis())
      .set(TECH_SPEC, lot.getTechSpec())
      .set(EXTRA_BUDGET_SOURCE, lot.getExtraBudgetSource())
      .where(ID.equal(lot.getId()))
      .execute();
    }
  }
  
  public void remove(UUID id) throws Throwable {
    try( DSLContext create = dsl() ) {
      create.delete(LOT).where(ID.equal(id)).execute();
    }
  }
  
  public List<Lot> selectAll() throws Throwable {
    return selectAll(null, null);
  }
  
  public List<Lot> selectAll(PageInfo page, OrderInfo order) throws Throwable {
    try( DSLContext create = dsl() ){
      List<Lot> list = new ArrayList<Lot>();
      SelectQuery q = create.selectQuery();
      q.addFrom(LOT);
      if( page != null ) {
        q.addLimit(page.getLimit());
        q.addOffset(page.getOffset());
      }
      if( order != null ) q.addOrderBy(sortedField(order));
      Result<Record> result = q.fetch();
      for(Record r: result) {
        list.add( makeLot(r) );
      }
      return list;
    }
  }
  
  public Lot selectByKey(UUID id) throws Throwable {
    try( DSLContext create = dsl() ) {
      return  makeLot( create.select().from(LOT).where(ID.equal(id)).fetchOne() ); 
    }
  }
  
  public List<Lot> selectByTenderId(UUID id, PageInfo page, OrderInfo order) throws Throwable {
    return selectByForignKey(id, TENDER_ID, page, order);
  }
  
  public List<Lot> selectByPlanId(UUID id, PageInfo page, OrderInfo order) throws Throwable {
    return selectByForignKey(id, PLAN_ID, page, order);
  }
  
  public List<Lot> selectByMeasurementUnitId(UUID id, PageInfo page, OrderInfo order) throws Throwable {
    return selectByForignKey(id, MEASUREMENT_UNIT_ID, page, order);
  }
  
  private List<Lot> selectByForignKey(UUID id, Field<UUID> field_id, PageInfo page, OrderInfo order) throws Throwable {
    try( DSLContext create = dsl() ) {
      List<Lot> list = new ArrayList<Lot>();
      SelectQuery<Record> result = create.selectQuery();
      result.addFrom(LOT);
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
        Lot e = new Lot();
        e.setId(r.getValue(ID));
        e.setTenderId(r.getValue(TENDER_ID));
        e.setPlanId(r.getValue(PLAN_ID));
        e.setMeasurementUnitId(r.getValue(MEASUREMENT_UNIT_ID));
        e.setName(r.getValue(NAME));
        e.setState(r.getValue(STATE));
        e.setQuantityAvailable(r.getValue(QUANTITY_AVAILABLE));
        e.setQuantityTotal(r.getValue(QUANTITY_TOTAL));
        e.setAmountAvailable(r.getValue(AMOUNT_AVAILABLE));
        e.setAmountTotal(r.getValue(AMOUNT_TOTAL));
        e.setNumber(r.getValue(NUMBER));
        e.setWinnerId(r.getValue(WINNER_ID));
        e.setBriefDescriptionPerTender(r.getValue(BRIEF_DESCRIPTION_PER_TENDER));
        e.setBriefDescriptionPerContract(r.getValue(BRIEF_DESCRIPTION_PER_CONTRACT));
        e.setOtherInfoLotsFailure(r.getValue(OTHER_INFO_LOTS_FAILURE));
        e.setRejectionBasis(r.getValue(REJECTION_BASIS));
        e.setTechSpec(r.getValue(TECH_SPEC));
        e.setExtraBudgetSource(r.getValue(EXTRA_BUDGET_SOURCE));
        list.add(e);
      }
      return list;
    }
  }
}
