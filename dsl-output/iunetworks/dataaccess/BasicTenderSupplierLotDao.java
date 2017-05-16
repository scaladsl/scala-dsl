package am.iunetworks.ppcm.api.dataaccess;

import java.util.*;
import java.sql.*;
import javax.sql.DataSource; 
import am.iunetworks.ppcm.api.service.*;

import static org.jooq.impl.DSL.*;
import org.jooq.*;
import database.dbcp2.*;

public class BasicTenderSupplierLotDao extends BaseDao {
  public static Table TENDER_SUPPLIER_LOT = table(name("tender_supplier_lot"));
  public static final Field<UUID> ID = field(name("tender_supplier_lot", "id"), UUID.class);
  public static final Field<UUID> TENDER_ID = field(name("tender_supplier_lot", "tender_id"), UUID.class);
  public static final Field<UUID> SUPPLIER_ID = field(name("tender_supplier_lot", "supplier_id"), UUID.class);
  public static final Field<UUID> LOT_ID = field(name("tender_supplier_lot", "lot_id"), UUID.class);
  public static final Field<Double> BID_AMOUNT_BY_A_F_M = field(name("tender_supplier_lot", "bid_amount_by_a_f_m"), Double.class);
  public static final Field<Double> BID_AMOUNT_TOTAL = field(name("tender_supplier_lot", "bid_amount_total"), Double.class);
  public static final Field<Double> V_A_T_BY_A_F_M = field(name("tender_supplier_lot", "v_a_t_by_a_f_m"), Double.class);
  public static final Field<Double> V_A_T_TOTAL = field(name("tender_supplier_lot", "v_a_t_total"), Double.class);
  public static final Field<String> PRICE_REDUCE_INFO = field(name("tender_supplier_lot", "price_reduce_info"), String.class);
  public static final Field<Boolean> BID_REJECTED = field(name("tender_supplier_lot", "bid_rejected"), Boolean.class);
  public static final Field<String> ENVELOPE_MAKEUP = field(name("tender_supplier_lot", "envelope_makeup"), String.class);
  public static final Field<String> DOCUMENT_AVAILABILITY = field(name("tender_supplier_lot", "document_availability"), String.class);
  public static final Field<String> PROCUREMENT_CONFORMITY = field(name("tender_supplier_lot", "procurement_conformity"), String.class);
  public static final Field<String> CURRENT_PROF_ACTIVITY = field(name("tender_supplier_lot", "current_prof_activity"), String.class);
  public static final Field<String> PROF_EXPERIENCE = field(name("tender_supplier_lot", "prof_experience"), String.class);
  public static final Field<String> FINANCIAL_RESOURCES = field(name("tender_supplier_lot", "financial_resources"), String.class);
  public static final Field<String> TECHNICAL_RESOURCES = field(name("tender_supplier_lot", "technical_resources"), String.class);
  public static final Field<String> PROF_RESOURCES = field(name("tender_supplier_lot", "prof_resources"), String.class);
  public static final Field<String> PRICE_OFFER = field(name("tender_supplier_lot", "price_offer"), String.class);
  public static final Field<String> OTHER_REJECTION_BASIS = field(name("tender_supplier_lot", "other_rejection_basis"), String.class);
  public static final Field<String> PRICE_PRIORITY_INFO = field(name("tender_supplier_lot", "price_priority_info"), String.class);
  public static final Field<String> ORIGIN_COUNTRY = field(name("tender_supplier_lot", "origin_country"), String.class);
  
  protected BasicTenderSupplierLotDao (DSLContextFactory dslContextFactory) {
    super(dslContextFactory);
  }
  
  private TenderSupplierLot makeTenderSupplierLot(Record r) {
    TenderSupplierLot e = new TenderSupplierLot();
    e.setId(r.getValue(ID));
    e.setTenderId(r.getValue(TENDER_ID));
    e.setSupplierId(r.getValue(SUPPLIER_ID));
    e.setLotId(r.getValue(LOT_ID));
    e.setBidAmountByAFM(r.getValue(BID_AMOUNT_BY_A_F_M));
    e.setBidAmountTotal(r.getValue(BID_AMOUNT_TOTAL));
    e.setVATByAFM(r.getValue(V_A_T_BY_A_F_M));
    e.setVATTotal(r.getValue(V_A_T_TOTAL));
    e.setPriceReduceInfo(r.getValue(PRICE_REDUCE_INFO));
    e.setBidRejected(r.getValue(BID_REJECTED));
    e.setEnvelopeMakeup(r.getValue(ENVELOPE_MAKEUP));
    e.setDocumentAvailability(r.getValue(DOCUMENT_AVAILABILITY));
    e.setProcurementConformity(r.getValue(PROCUREMENT_CONFORMITY));
    e.setCurrentProfActivity(r.getValue(CURRENT_PROF_ACTIVITY));
    e.setProfExperience(r.getValue(PROF_EXPERIENCE));
    e.setFinancialResources(r.getValue(FINANCIAL_RESOURCES));
    e.setTechnicalResources(r.getValue(TECHNICAL_RESOURCES));
    e.setProfResources(r.getValue(PROF_RESOURCES));
    e.setPriceOffer(r.getValue(PRICE_OFFER));
    e.setOtherRejectionBasis(r.getValue(OTHER_REJECTION_BASIS));
    e.setPricePriorityInfo(r.getValue(PRICE_PRIORITY_INFO));
    e.setOriginCountry(r.getValue(ORIGIN_COUNTRY));
    return e;
  }
  
  public void insert(TenderSupplierLot tenderSupplierLot) throws Throwable {
    try( DSLContext create = dsl() ){
      create.insertInto(TENDER_SUPPLIER_LOT, ID, TENDER_ID, SUPPLIER_ID, LOT_ID, BID_AMOUNT_BY_A_F_M, BID_AMOUNT_TOTAL, V_A_T_BY_A_F_M, V_A_T_TOTAL, PRICE_REDUCE_INFO, BID_REJECTED, ENVELOPE_MAKEUP, DOCUMENT_AVAILABILITY, PROCUREMENT_CONFORMITY, CURRENT_PROF_ACTIVITY, PROF_EXPERIENCE, FINANCIAL_RESOURCES, TECHNICAL_RESOURCES, PROF_RESOURCES, PRICE_OFFER, OTHER_REJECTION_BASIS, PRICE_PRIORITY_INFO, ORIGIN_COUNTRY).values(tenderSupplierLot.getId(), tenderSupplierLot.getTenderId(), tenderSupplierLot.getSupplierId(), tenderSupplierLot.getLotId(), tenderSupplierLot.getBidAmountByAFM(), tenderSupplierLot.getBidAmountTotal(), tenderSupplierLot.getVATByAFM(), tenderSupplierLot.getVATTotal(), tenderSupplierLot.getPriceReduceInfo(), tenderSupplierLot.getBidRejected(), tenderSupplierLot.getEnvelopeMakeup(), tenderSupplierLot.getDocumentAvailability(), tenderSupplierLot.getProcurementConformity(), tenderSupplierLot.getCurrentProfActivity(), tenderSupplierLot.getProfExperience(), tenderSupplierLot.getFinancialResources(), tenderSupplierLot.getTechnicalResources(), tenderSupplierLot.getProfResources(), tenderSupplierLot.getPriceOffer(), tenderSupplierLot.getOtherRejectionBasis(), tenderSupplierLot.getPricePriorityInfo(), tenderSupplierLot.getOriginCountry()).execute();
    }
  }
  
  public void update(TenderSupplierLot tenderSupplierLot) throws Throwable {
    try( DSLContext create = dsl() ) {
      create.update(TENDER_SUPPLIER_LOT)
      .set(ID, tenderSupplierLot.getId())
      .set(TENDER_ID, tenderSupplierLot.getTenderId())
      .set(SUPPLIER_ID, tenderSupplierLot.getSupplierId())
      .set(LOT_ID, tenderSupplierLot.getLotId())
      .set(BID_AMOUNT_BY_A_F_M, tenderSupplierLot.getBidAmountByAFM())
      .set(BID_AMOUNT_TOTAL, tenderSupplierLot.getBidAmountTotal())
      .set(V_A_T_BY_A_F_M, tenderSupplierLot.getVATByAFM())
      .set(V_A_T_TOTAL, tenderSupplierLot.getVATTotal())
      .set(PRICE_REDUCE_INFO, tenderSupplierLot.getPriceReduceInfo())
      .set(BID_REJECTED, tenderSupplierLot.getBidRejected())
      .set(ENVELOPE_MAKEUP, tenderSupplierLot.getEnvelopeMakeup())
      .set(DOCUMENT_AVAILABILITY, tenderSupplierLot.getDocumentAvailability())
      .set(PROCUREMENT_CONFORMITY, tenderSupplierLot.getProcurementConformity())
      .set(CURRENT_PROF_ACTIVITY, tenderSupplierLot.getCurrentProfActivity())
      .set(PROF_EXPERIENCE, tenderSupplierLot.getProfExperience())
      .set(FINANCIAL_RESOURCES, tenderSupplierLot.getFinancialResources())
      .set(TECHNICAL_RESOURCES, tenderSupplierLot.getTechnicalResources())
      .set(PROF_RESOURCES, tenderSupplierLot.getProfResources())
      .set(PRICE_OFFER, tenderSupplierLot.getPriceOffer())
      .set(OTHER_REJECTION_BASIS, tenderSupplierLot.getOtherRejectionBasis())
      .set(PRICE_PRIORITY_INFO, tenderSupplierLot.getPricePriorityInfo())
      .set(ORIGIN_COUNTRY, tenderSupplierLot.getOriginCountry())
      .where(ID.equal(tenderSupplierLot.getId()))
      .execute();
    }
  }
  
  public void remove(UUID id) throws Throwable {
    try( DSLContext create = dsl() ) {
      create.delete(TENDER_SUPPLIER_LOT).where(ID.equal(id)).execute();
    }
  }
  
  public List<TenderSupplierLot> selectAll() throws Throwable {
    return selectAll(null, null);
  }
  
  public List<TenderSupplierLot> selectAll(PageInfo page, OrderInfo order) throws Throwable {
    try( DSLContext create = dsl() ){
      List<TenderSupplierLot> list = new ArrayList<TenderSupplierLot>();
      SelectQuery q = create.selectQuery();
      q.addFrom(TENDER_SUPPLIER_LOT);
      if( page != null ) {
        q.addLimit(page.getLimit());
        q.addOffset(page.getOffset());
      }
      if( order != null ) q.addOrderBy(sortedField(order));
      Result<Record> result = q.fetch();
      for(Record r: result) {
        list.add( makeTenderSupplierLot(r) );
      }
      return list;
    }
  }
  
  public TenderSupplierLot selectByKey(UUID id) throws Throwable {
    try( DSLContext create = dsl() ) {
      return  makeTenderSupplierLot( create.select().from(TENDER_SUPPLIER_LOT).where(ID.equal(id)).fetchOne() ); 
    }
  }
  
  public List<TenderSupplierLot> selectByTenderId(UUID id, PageInfo page, OrderInfo order) throws Throwable {
    return selectByForignKey(id, TENDER_ID, page, order);
  }
  
  public List<TenderSupplierLot> selectBySupplierId(UUID id, PageInfo page, OrderInfo order) throws Throwable {
    return selectByForignKey(id, SUPPLIER_ID, page, order);
  }
  
  public List<TenderSupplierLot> selectByLotId(UUID id, PageInfo page, OrderInfo order) throws Throwable {
    return selectByForignKey(id, LOT_ID, page, order);
  }
  
  private List<TenderSupplierLot> selectByForignKey(UUID id, Field<UUID> field_id, PageInfo page, OrderInfo order) throws Throwable {
    try( DSLContext create = dsl() ) {
      List<TenderSupplierLot> list = new ArrayList<TenderSupplierLot>();
      SelectQuery<Record> result = create.selectQuery();
      result.addFrom(TENDER_SUPPLIER_LOT);
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
        TenderSupplierLot e = new TenderSupplierLot();
        e.setId(r.getValue(ID));
        e.setTenderId(r.getValue(TENDER_ID));
        e.setSupplierId(r.getValue(SUPPLIER_ID));
        e.setLotId(r.getValue(LOT_ID));
        e.setBidAmountByAFM(r.getValue(BID_AMOUNT_BY_A_F_M));
        e.setBidAmountTotal(r.getValue(BID_AMOUNT_TOTAL));
        e.setVATByAFM(r.getValue(V_A_T_BY_A_F_M));
        e.setVATTotal(r.getValue(V_A_T_TOTAL));
        e.setPriceReduceInfo(r.getValue(PRICE_REDUCE_INFO));
        e.setBidRejected(r.getValue(BID_REJECTED));
        e.setEnvelopeMakeup(r.getValue(ENVELOPE_MAKEUP));
        e.setDocumentAvailability(r.getValue(DOCUMENT_AVAILABILITY));
        e.setProcurementConformity(r.getValue(PROCUREMENT_CONFORMITY));
        e.setCurrentProfActivity(r.getValue(CURRENT_PROF_ACTIVITY));
        e.setProfExperience(r.getValue(PROF_EXPERIENCE));
        e.setFinancialResources(r.getValue(FINANCIAL_RESOURCES));
        e.setTechnicalResources(r.getValue(TECHNICAL_RESOURCES));
        e.setProfResources(r.getValue(PROF_RESOURCES));
        e.setPriceOffer(r.getValue(PRICE_OFFER));
        e.setOtherRejectionBasis(r.getValue(OTHER_REJECTION_BASIS));
        e.setPricePriorityInfo(r.getValue(PRICE_PRIORITY_INFO));
        e.setOriginCountry(r.getValue(ORIGIN_COUNTRY));
        list.add(e);
      }
      return list;
    }
  }
}
