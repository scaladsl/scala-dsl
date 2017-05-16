package am.iunetworks.ppcm.api.dataaccess;

import java.util.*;
import java.sql.*;
import javax.sql.DataSource; 
import am.iunetworks.ppcm.api.service.*;

import static org.jooq.impl.DSL.*;
import org.jooq.*;
import database.dbcp2.*;

public class BasicContractDao extends BaseDao {
  public static Table CONTRACT = table(name("contract"));
  public static final Field<UUID> ID = field(name("contract", "id"), UUID.class);
  public static final Field<UUID> TENDER_ID = field(name("contract", "tender_id"), UUID.class);
  public static final Field<UUID> SUPPLIER_ID = field(name("contract", "supplier_id"), UUID.class);
  public static final Field<String> NUMBER = field(name("contract", "number"), String.class);
  public static final Field<UUID> PARENT_ID = field(name("contract", "parent_id"), UUID.class);
  public static final Field<Timestamp> DATE_SIGNED = field(name("contract", "date_signed"), Timestamp.class);
  public static final Field<Timestamp> DEADLINE = field(name("contract", "deadline"), Timestamp.class);
  public static final Field<Double> ADVANCED_PAYMENT = field(name("contract", "advanced_payment"), Double.class);
  public static final Field<String> FINANCING_SOURCE = field(name("contract", "financing_source"), String.class);
  public static final Field<String> PROCUREMENT_NPNA_BASIS = field(name("contract", "procurement_npna_basis"), String.class);
  public static final Field<String> FINES_OR_PENALTIES = field(name("contract", "fines_or_penalties"), String.class);
  public static final Field<String> TREASURY_CODE = field(name("contract", "treasury_code"), String.class);
  public static final Field<String> PROCUREMENT_PROCEDURE_BASIS = field(name("contract", "procurement_procedure_basis"), String.class);
  public static final Field<String> CONTRACT_SIGNATORY_NAME = field(name("contract", "contract_signatory_name"), String.class);
  public static final Field<String> TAXPAYER_ID = field(name("contract", "taxpayer_id"), String.class);
  public static final Field<String> BANK_ACCOUNT_INFO = field(name("contract", "bank_account_info"), String.class);
  public static final Field<Boolean> CONTRACT_ON_DEMAND = field(name("contract", "contract_on_demand"), Boolean.class);
  public static final Field<Timestamp> DATE_WINNER_DETERMINED = field(name("contract", "date_winner_determined"), Timestamp.class);
  public static final Field<Timestamp> DATE_PARTICIPANT_NOTIFIED = field(name("contract", "date_participant_notified"), Timestamp.class);
  public static final Field<Timestamp> DATE_CONTRACT_SUBMISSION = field(name("contract", "date_contract_submission"), Timestamp.class);
  public static final Field<Timestamp> DATE_SIGNED_BY_AUTHORITY = field(name("contract", "date_signed_by_authority"), Timestamp.class);
  public static final Field<Integer> ARMEPS_ID = field(name("contract", "armeps_id"), Integer.class);
  public static final Field<String> PROCUREMENT_SUBJECT = field(name("contract", "procurement_subject"), String.class);
  public static final Field<Double> CONTRACT_VALUE_TOTAL = field(name("contract", "contract_value_total"), Double.class);
  public static final Field<Double> LATEST_VALUE = field(name("contract", "latest_value"), Double.class);
  public static final Field<String> SERIAL_NO = field(name("contract", "serial_no"), String.class);
  public static final Field<String> LATEST_STATE = field(name("contract", "latest_state"), String.class);
  public static final Field<Timestamp> SUBMITTED_DATE = field(name("contract", "submitted_date"), Timestamp.class);
  
  protected BasicContractDao (DSLContextFactory dslContextFactory) {
    super(dslContextFactory);
  }
  
  private Contract makeContract(Record r) {
    Contract e = new Contract();
    e.setId(r.getValue(ID));
    e.setTenderId(r.getValue(TENDER_ID));
    e.setSupplierId(r.getValue(SUPPLIER_ID));
    e.setNumber(r.getValue(NUMBER));
    e.setParentId(r.getValue(PARENT_ID));
    e.setDateSigned(r.getValue(DATE_SIGNED));
    e.setDeadline(r.getValue(DEADLINE));
    e.setAdvancedPayment(r.getValue(ADVANCED_PAYMENT));
    e.setFinancingSource(r.getValue(FINANCING_SOURCE));
    e.setProcurementNpnaBasis(r.getValue(PROCUREMENT_NPNA_BASIS));
    e.setFinesOrPenalties(r.getValue(FINES_OR_PENALTIES));
    e.setTreasuryCode(r.getValue(TREASURY_CODE));
    e.setProcurementProcedureBasis(r.getValue(PROCUREMENT_PROCEDURE_BASIS));
    e.setContractSignatoryName(r.getValue(CONTRACT_SIGNATORY_NAME));
    e.setTaxpayerId(r.getValue(TAXPAYER_ID));
    e.setBankAccountInfo(r.getValue(BANK_ACCOUNT_INFO));
    e.setContractOnDemand(r.getValue(CONTRACT_ON_DEMAND));
    e.setDateWinnerDetermined(r.getValue(DATE_WINNER_DETERMINED));
    e.setDateParticipantNotified(r.getValue(DATE_PARTICIPANT_NOTIFIED));
    e.setDateContractSubmission(r.getValue(DATE_CONTRACT_SUBMISSION));
    e.setDateSignedByAuthority(r.getValue(DATE_SIGNED_BY_AUTHORITY));
    e.setArmepsId(r.getValue(ARMEPS_ID));
    e.setProcurementSubject(r.getValue(PROCUREMENT_SUBJECT));
    e.setContractValueTotal(r.getValue(CONTRACT_VALUE_TOTAL));
    e.setLatestValue(r.getValue(LATEST_VALUE));
    e.setSerialNo(r.getValue(SERIAL_NO));
    e.setLatestState(r.getValue(LATEST_STATE));
    e.setSubmittedDate(r.getValue(SUBMITTED_DATE));
    return e;
  }
  
  public void insert(Contract contract) throws Throwable {
    try( DSLContext create = dsl() ){
      create.insertInto(CONTRACT, ID, TENDER_ID, SUPPLIER_ID, NUMBER, PARENT_ID, DATE_SIGNED, DEADLINE, ADVANCED_PAYMENT, FINANCING_SOURCE, PROCUREMENT_NPNA_BASIS, FINES_OR_PENALTIES, TREASURY_CODE, PROCUREMENT_PROCEDURE_BASIS, CONTRACT_SIGNATORY_NAME, TAXPAYER_ID, BANK_ACCOUNT_INFO, CONTRACT_ON_DEMAND, DATE_WINNER_DETERMINED, DATE_PARTICIPANT_NOTIFIED, DATE_CONTRACT_SUBMISSION, DATE_SIGNED_BY_AUTHORITY, ARMEPS_ID, PROCUREMENT_SUBJECT, CONTRACT_VALUE_TOTAL, LATEST_VALUE, SERIAL_NO, LATEST_STATE, SUBMITTED_DATE).values(contract.getId(), contract.getTenderId(), contract.getSupplierId(), contract.getNumber(), contract.getParentId(), contract.getDateSigned(), contract.getDeadline(), contract.getAdvancedPayment(), contract.getFinancingSource(), contract.getProcurementNpnaBasis(), contract.getFinesOrPenalties(), contract.getTreasuryCode(), contract.getProcurementProcedureBasis(), contract.getContractSignatoryName(), contract.getTaxpayerId(), contract.getBankAccountInfo(), contract.getContractOnDemand(), contract.getDateWinnerDetermined(), contract.getDateParticipantNotified(), contract.getDateContractSubmission(), contract.getDateSignedByAuthority(), contract.getArmepsId(), contract.getProcurementSubject(), contract.getContractValueTotal(), contract.getLatestValue(), contract.getSerialNo(), contract.getLatestState(), contract.getSubmittedDate()).execute();
    }
  }
  
  public void update(Contract contract) throws Throwable {
    try( DSLContext create = dsl() ) {
      create.update(CONTRACT)
      .set(ID, contract.getId())
      .set(TENDER_ID, contract.getTenderId())
      .set(SUPPLIER_ID, contract.getSupplierId())
      .set(NUMBER, contract.getNumber())
      .set(PARENT_ID, contract.getParentId())
      .set(DATE_SIGNED, contract.getDateSigned())
      .set(DEADLINE, contract.getDeadline())
      .set(ADVANCED_PAYMENT, contract.getAdvancedPayment())
      .set(FINANCING_SOURCE, contract.getFinancingSource())
      .set(PROCUREMENT_NPNA_BASIS, contract.getProcurementNpnaBasis())
      .set(FINES_OR_PENALTIES, contract.getFinesOrPenalties())
      .set(TREASURY_CODE, contract.getTreasuryCode())
      .set(PROCUREMENT_PROCEDURE_BASIS, contract.getProcurementProcedureBasis())
      .set(CONTRACT_SIGNATORY_NAME, contract.getContractSignatoryName())
      .set(TAXPAYER_ID, contract.getTaxpayerId())
      .set(BANK_ACCOUNT_INFO, contract.getBankAccountInfo())
      .set(CONTRACT_ON_DEMAND, contract.getContractOnDemand())
      .set(DATE_WINNER_DETERMINED, contract.getDateWinnerDetermined())
      .set(DATE_PARTICIPANT_NOTIFIED, contract.getDateParticipantNotified())
      .set(DATE_CONTRACT_SUBMISSION, contract.getDateContractSubmission())
      .set(DATE_SIGNED_BY_AUTHORITY, contract.getDateSignedByAuthority())
      .set(ARMEPS_ID, contract.getArmepsId())
      .set(PROCUREMENT_SUBJECT, contract.getProcurementSubject())
      .set(CONTRACT_VALUE_TOTAL, contract.getContractValueTotal())
      .set(LATEST_VALUE, contract.getLatestValue())
      .set(SERIAL_NO, contract.getSerialNo())
      .set(LATEST_STATE, contract.getLatestState())
      .set(SUBMITTED_DATE, contract.getSubmittedDate())
      .where(ID.equal(contract.getId()))
      .execute();
    }
  }
  
  public void remove(UUID id) throws Throwable {
    try( DSLContext create = dsl() ) {
      create.delete(CONTRACT).where(ID.equal(id)).execute();
    }
  }
  
  public List<Contract> selectAll() throws Throwable {
    return selectAll(null, null);
  }
  
  public List<Contract> selectAll(PageInfo page, OrderInfo order) throws Throwable {
    try( DSLContext create = dsl() ){
      List<Contract> list = new ArrayList<Contract>();
      SelectQuery q = create.selectQuery();
      q.addFrom(CONTRACT);
      if( page != null ) {
        q.addLimit(page.getLimit());
        q.addOffset(page.getOffset());
      }
      if( order != null ) q.addOrderBy(sortedField(order));
      Result<Record> result = q.fetch();
      for(Record r: result) {
        list.add( makeContract(r) );
      }
      return list;
    }
  }
  
  public Contract selectByKey(UUID id) throws Throwable {
    try( DSLContext create = dsl() ) {
      return  makeContract( create.select().from(CONTRACT).where(ID.equal(id)).fetchOne() ); 
    }
  }
  
  public List<Contract> selectByTenderId(UUID id, PageInfo page, OrderInfo order) throws Throwable {
    return selectByForignKey(id, TENDER_ID, page, order);
  }
  
  public List<Contract> selectBySupplierId(UUID id, PageInfo page, OrderInfo order) throws Throwable {
    return selectByForignKey(id, SUPPLIER_ID, page, order);
  }
  
  public List<Contract> selectByParentId(UUID id, PageInfo page, OrderInfo order) throws Throwable {
    return selectByForignKey(id, PARENT_ID, page, order);
  }
  
  private List<Contract> selectByForignKey(UUID id, Field<UUID> field_id, PageInfo page, OrderInfo order) throws Throwable {
    try( DSLContext create = dsl() ) {
      List<Contract> list = new ArrayList<Contract>();
      SelectQuery<Record> result = create.selectQuery();
      result.addFrom(CONTRACT);
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
        Contract e = new Contract();
        e.setId(r.getValue(ID));
        e.setTenderId(r.getValue(TENDER_ID));
        e.setSupplierId(r.getValue(SUPPLIER_ID));
        e.setNumber(r.getValue(NUMBER));
        e.setParentId(r.getValue(PARENT_ID));
        e.setDateSigned(r.getValue(DATE_SIGNED));
        e.setDeadline(r.getValue(DEADLINE));
        e.setAdvancedPayment(r.getValue(ADVANCED_PAYMENT));
        e.setFinancingSource(r.getValue(FINANCING_SOURCE));
        e.setProcurementNpnaBasis(r.getValue(PROCUREMENT_NPNA_BASIS));
        e.setFinesOrPenalties(r.getValue(FINES_OR_PENALTIES));
        e.setTreasuryCode(r.getValue(TREASURY_CODE));
        e.setProcurementProcedureBasis(r.getValue(PROCUREMENT_PROCEDURE_BASIS));
        e.setContractSignatoryName(r.getValue(CONTRACT_SIGNATORY_NAME));
        e.setTaxpayerId(r.getValue(TAXPAYER_ID));
        e.setBankAccountInfo(r.getValue(BANK_ACCOUNT_INFO));
        e.setContractOnDemand(r.getValue(CONTRACT_ON_DEMAND));
        e.setDateWinnerDetermined(r.getValue(DATE_WINNER_DETERMINED));
        e.setDateParticipantNotified(r.getValue(DATE_PARTICIPANT_NOTIFIED));
        e.setDateContractSubmission(r.getValue(DATE_CONTRACT_SUBMISSION));
        e.setDateSignedByAuthority(r.getValue(DATE_SIGNED_BY_AUTHORITY));
        e.setArmepsId(r.getValue(ARMEPS_ID));
        e.setProcurementSubject(r.getValue(PROCUREMENT_SUBJECT));
        e.setContractValueTotal(r.getValue(CONTRACT_VALUE_TOTAL));
        e.setLatestValue(r.getValue(LATEST_VALUE));
        e.setSerialNo(r.getValue(SERIAL_NO));
        e.setLatestState(r.getValue(LATEST_STATE));
        e.setSubmittedDate(r.getValue(SUBMITTED_DATE));
        list.add(e);
      }
      return list;
    }
  }
}
