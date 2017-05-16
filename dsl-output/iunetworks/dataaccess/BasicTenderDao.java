package am.iunetworks.ppcm.api.dataaccess;

import java.util.*;
import java.sql.*;
import javax.sql.DataSource; 
import am.iunetworks.ppcm.api.service.*;

import static org.jooq.impl.DSL.*;
import org.jooq.*;
import database.dbcp2.*;

public class BasicTenderDao extends BaseDao {
  public static Table TENDER = table(name("tender"));
  public static final Field<UUID> ID = field(name("tender", "id"), UUID.class);
  public static final Field<UUID> PROCUREMENT_COORDINATOR_ID = field(name("tender", "procurement_coordinator_id"), UUID.class);
  public static final Field<UUID> SUBDIVISION_STAFF_ID = field(name("tender", "subdivision_staff_id"), UUID.class);
  public static final Field<UUID> AUTHORITY_ID = field(name("tender", "authority_id"), UUID.class);
  public static final Field<String> CODE = field(name("tender", "code"), String.class);
  public static final Field<String> TITLE = field(name("tender", "title"), String.class);
  public static final Field<String> DESCRIPTION = field(name("tender", "description"), String.class);
  public static final Field<String> STATE = field(name("tender", "state"), String.class);
  public static final Field<String> EVALUATION = field(name("tender", "evaluation"), String.class);
  public static final Field<UUID> PROCEDURE = field(name("tender", "procedure"), UUID.class);
  public static final Field<String> LOTTING = field(name("tender", "lotting"), String.class);
  public static final Field<Integer> NUMBER_OF_LOTS = field(name("tender", "number_of_lots"), Integer.class);
  public static final Field<Integer> AWARDIN_LOTS = field(name("tender", "awardin_lots"), Integer.class);
  public static final Field<String> PROCUREMENT_TYPE = field(name("tender", "procurement_type"), String.class);
  public static final Field<Double> ESTIMATED_VALUE = field(name("tender", "estimated_value"), Double.class);
  public static final Field<Timestamp> ANNOUNCEMENT_DATE = field(name("tender", "announcement_date"), Timestamp.class);
  public static final Field<Timestamp> START_DATE = field(name("tender", "start_date"), Timestamp.class);
  public static final Field<Timestamp> SUBMISSION_FINAL_DATE = field(name("tender", "submission_final_date"), Timestamp.class);
  public static final Field<Timestamp> AMENDMENT_DATE = field(name("tender", "amendment_date"), Timestamp.class);
  public static final Field<String> CLARIFICATION_TIMETABLE = field(name("tender", "clarification_timetable"), String.class);
  public static final Field<Timestamp> STANDSTILL_START_DATE = field(name("tender", "standstill_start_date"), Timestamp.class);
  public static final Field<Timestamp> STANDSTILL_END_DATE = field(name("tender", "standstill_end_date"), Timestamp.class);
  public static final Field<String> PROCUREMENT_METHOD_BASIS = field(name("tender", "procurement_method_basis"), String.class);
  public static final Field<String> PUBLIC_ANNOUNCEMENT_INFO = field(name("tender", "public_announcement_info"), String.class);
  public static final Field<String> UNLAWFUL_ACTS_INFO = field(name("tender", "unlawful_acts_info"), String.class);
  public static final Field<String> APPEALS_INFO = field(name("tender", "appeals_info"), String.class);
  public static final Field<String> ADDITIONAL_INFO = field(name("tender", "additional_info"), String.class);
  public static final Field<Integer> ARMEPS_ID = field(name("tender", "armeps_id"), Integer.class);
  public static final Field<String> ARMEPS_USER_NAME = field(name("tender", "armeps_user_name"), String.class);
  public static final Field<Integer> PARENT_CFT_ID = field(name("tender", "parent_cft_id"), Integer.class);
  public static final Field<Integer> PARENT_LOT_NAME = field(name("tender", "parent_lot_name"), Integer.class);
  public static final Field<Boolean> EDITABLE = field(name("tender", "editable"), Boolean.class);
  public static final Field<String> REVIEW_STATE = field(name("tender", "review_state"), String.class);
  public static final Field<Boolean> PAPER_BASED = field(name("tender", "paper_based"), Boolean.class);
  public static final Field<Boolean> CENTRAL = field(name("tender", "central"), Boolean.class);
  
  protected BasicTenderDao (DSLContextFactory dslContextFactory) {
    super(dslContextFactory);
  }
  
  private Tender makeTender(Record r) {
    Tender e = new Tender();
    e.setId(r.getValue(ID));
    e.setProcurementCoordinatorId(r.getValue(PROCUREMENT_COORDINATOR_ID));
    e.setSubdivisionStaffId(r.getValue(SUBDIVISION_STAFF_ID));
    e.setAuthorityId(r.getValue(AUTHORITY_ID));
    e.setCode(r.getValue(CODE));
    e.setTitle(r.getValue(TITLE));
    e.setDescription(r.getValue(DESCRIPTION));
    e.setState(r.getValue(STATE));
    e.setEvaluation(r.getValue(EVALUATION));
    e.setProcedure(r.getValue(PROCEDURE));
    e.setLotting(r.getValue(LOTTING));
    e.setNumberOfLots(r.getValue(NUMBER_OF_LOTS));
    e.setAwardinLots(r.getValue(AWARDIN_LOTS));
    e.setProcurementType(r.getValue(PROCUREMENT_TYPE));
    e.setEstimatedValue(r.getValue(ESTIMATED_VALUE));
    e.setAnnouncementDate(r.getValue(ANNOUNCEMENT_DATE));
    e.setStartDate(r.getValue(START_DATE));
    e.setSubmissionFinalDate(r.getValue(SUBMISSION_FINAL_DATE));
    e.setAmendmentDate(r.getValue(AMENDMENT_DATE));
    e.setClarificationTimetable(r.getValue(CLARIFICATION_TIMETABLE));
    e.setStandstillStartDate(r.getValue(STANDSTILL_START_DATE));
    e.setStandstillEndDate(r.getValue(STANDSTILL_END_DATE));
    e.setProcurementMethodBasis(r.getValue(PROCUREMENT_METHOD_BASIS));
    e.setPublicAnnouncementInfo(r.getValue(PUBLIC_ANNOUNCEMENT_INFO));
    e.setUnlawfulActsInfo(r.getValue(UNLAWFUL_ACTS_INFO));
    e.setAppealsInfo(r.getValue(APPEALS_INFO));
    e.setAdditionalInfo(r.getValue(ADDITIONAL_INFO));
    e.setArmepsId(r.getValue(ARMEPS_ID));
    e.setArmepsUserName(r.getValue(ARMEPS_USER_NAME));
    e.setParentCftId(r.getValue(PARENT_CFT_ID));
    e.setParentLotName(r.getValue(PARENT_LOT_NAME));
    e.setEditable(r.getValue(EDITABLE));
    e.setReviewState(r.getValue(REVIEW_STATE));
    e.setPaperBased(r.getValue(PAPER_BASED));
    e.setCentral(r.getValue(CENTRAL));
    return e;
  }
  
  public void insert(Tender tender) throws Throwable {
    try( DSLContext create = dsl() ){
      create.insertInto(TENDER, ID, PROCUREMENT_COORDINATOR_ID, SUBDIVISION_STAFF_ID, AUTHORITY_ID, CODE, TITLE, DESCRIPTION, STATE, EVALUATION, PROCEDURE, LOTTING, NUMBER_OF_LOTS, AWARDIN_LOTS, PROCUREMENT_TYPE, ESTIMATED_VALUE, ANNOUNCEMENT_DATE, START_DATE, SUBMISSION_FINAL_DATE, AMENDMENT_DATE, CLARIFICATION_TIMETABLE, STANDSTILL_START_DATE, STANDSTILL_END_DATE, PROCUREMENT_METHOD_BASIS, PUBLIC_ANNOUNCEMENT_INFO, UNLAWFUL_ACTS_INFO, APPEALS_INFO, ADDITIONAL_INFO, ARMEPS_ID, ARMEPS_USER_NAME, PARENT_CFT_ID, PARENT_LOT_NAME, EDITABLE, REVIEW_STATE, PAPER_BASED, CENTRAL).values(tender.getId(), tender.getProcurementCoordinatorId(), tender.getSubdivisionStaffId(), tender.getAuthorityId(), tender.getCode(), tender.getTitle(), tender.getDescription(), tender.getState(), tender.getEvaluation(), tender.getProcedure(), tender.getLotting(), tender.getNumberOfLots(), tender.getAwardinLots(), tender.getProcurementType(), tender.getEstimatedValue(), tender.getAnnouncementDate(), tender.getStartDate(), tender.getSubmissionFinalDate(), tender.getAmendmentDate(), tender.getClarificationTimetable(), tender.getStandstillStartDate(), tender.getStandstillEndDate(), tender.getProcurementMethodBasis(), tender.getPublicAnnouncementInfo(), tender.getUnlawfulActsInfo(), tender.getAppealsInfo(), tender.getAdditionalInfo(), tender.getArmepsId(), tender.getArmepsUserName(), tender.getParentCftId(), tender.getParentLotName(), tender.getEditable(), tender.getReviewState(), tender.getPaperBased(), tender.getCentral()).execute();
    }
  }
  
  public void update(Tender tender) throws Throwable {
    try( DSLContext create = dsl() ) {
      create.update(TENDER)
      .set(ID, tender.getId())
      .set(PROCUREMENT_COORDINATOR_ID, tender.getProcurementCoordinatorId())
      .set(SUBDIVISION_STAFF_ID, tender.getSubdivisionStaffId())
      .set(AUTHORITY_ID, tender.getAuthorityId())
      .set(CODE, tender.getCode())
      .set(TITLE, tender.getTitle())
      .set(DESCRIPTION, tender.getDescription())
      .set(STATE, tender.getState())
      .set(EVALUATION, tender.getEvaluation())
      .set(PROCEDURE, tender.getProcedure())
      .set(LOTTING, tender.getLotting())
      .set(NUMBER_OF_LOTS, tender.getNumberOfLots())
      .set(AWARDIN_LOTS, tender.getAwardinLots())
      .set(PROCUREMENT_TYPE, tender.getProcurementType())
      .set(ESTIMATED_VALUE, tender.getEstimatedValue())
      .set(ANNOUNCEMENT_DATE, tender.getAnnouncementDate())
      .set(START_DATE, tender.getStartDate())
      .set(SUBMISSION_FINAL_DATE, tender.getSubmissionFinalDate())
      .set(AMENDMENT_DATE, tender.getAmendmentDate())
      .set(CLARIFICATION_TIMETABLE, tender.getClarificationTimetable())
      .set(STANDSTILL_START_DATE, tender.getStandstillStartDate())
      .set(STANDSTILL_END_DATE, tender.getStandstillEndDate())
      .set(PROCUREMENT_METHOD_BASIS, tender.getProcurementMethodBasis())
      .set(PUBLIC_ANNOUNCEMENT_INFO, tender.getPublicAnnouncementInfo())
      .set(UNLAWFUL_ACTS_INFO, tender.getUnlawfulActsInfo())
      .set(APPEALS_INFO, tender.getAppealsInfo())
      .set(ADDITIONAL_INFO, tender.getAdditionalInfo())
      .set(ARMEPS_ID, tender.getArmepsId())
      .set(ARMEPS_USER_NAME, tender.getArmepsUserName())
      .set(PARENT_CFT_ID, tender.getParentCftId())
      .set(PARENT_LOT_NAME, tender.getParentLotName())
      .set(EDITABLE, tender.getEditable())
      .set(REVIEW_STATE, tender.getReviewState())
      .set(PAPER_BASED, tender.getPaperBased())
      .set(CENTRAL, tender.getCentral())
      .where(ID.equal(tender.getId()))
      .execute();
    }
  }
  
  public void remove(UUID id) throws Throwable {
    try( DSLContext create = dsl() ) {
      create.delete(TENDER).where(ID.equal(id)).execute();
    }
  }
  
  public List<Tender> selectAll() throws Throwable {
    return selectAll(null, null);
  }
  
  public List<Tender> selectAll(PageInfo page, OrderInfo order) throws Throwable {
    try( DSLContext create = dsl() ){
      List<Tender> list = new ArrayList<Tender>();
      SelectQuery q = create.selectQuery();
      q.addFrom(TENDER);
      if( page != null ) {
        q.addLimit(page.getLimit());
        q.addOffset(page.getOffset());
      }
      if( order != null ) q.addOrderBy(sortedField(order));
      Result<Record> result = q.fetch();
      for(Record r: result) {
        list.add( makeTender(r) );
      }
      return list;
    }
  }
  
  public Tender selectByKey(UUID id) throws Throwable {
    try( DSLContext create = dsl() ) {
      return  makeTender( create.select().from(TENDER).where(ID.equal(id)).fetchOne() ); 
    }
  }
}
