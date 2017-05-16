package am.iunetworks.ppcm.api.dataaccess;

import java.util.*;
import java.sql.*;
import javax.sql.DataSource; 
import am.iunetworks.ppcm.api.service.*;

import static org.jooq.impl.DSL.*;
import org.jooq.*;
import database.dbcp2.*;

public class BasicUserDao extends BaseDao {
  public static Table USER = table(name("user"));
  public static final Field<UUID> ID = field(name("user", "id"), UUID.class);
  public static final Field<UUID> ROLE_ID = field(name("user", "role_id"), UUID.class);
  public static final Field<UUID> AUTHORITY_ID = field(name("user", "authority_id"), UUID.class);
  public static final Field<String> USERNAME = field(name("user", "username"), String.class);
  public static final Field<String> STATE = field(name("user", "state"), String.class);
  public static final Field<String> EMAIL = field(name("user", "email"), String.class);
  public static final Field<String> PHONE = field(name("user", "phone"), String.class);
  public static final Field<String> PHONE_MOBILE = field(name("user", "phone_mobile"), String.class);
  public static final Field<String> FORENAME = field(name("user", "forename"), String.class);
  public static final Field<String> SURNAME = field(name("user", "surname"), String.class);
  public static final Field<Timestamp> LAST_LOGGED_IN = field(name("user", "last_logged_in"), Timestamp.class);
  public static final Field<String> ARMEPS_USERNAME = field(name("user", "armeps_username"), String.class);
  
  protected BasicUserDao (DSLContextFactory dslContextFactory) {
    super(dslContextFactory);
  }
  
  private User makeUser(Record r) {
    User e = new User();
    e.setId(r.getValue(ID));
    e.setRoleId(r.getValue(ROLE_ID));
    e.setAuthorityId(r.getValue(AUTHORITY_ID));
    e.setUsername(r.getValue(USERNAME));
    e.setState(r.getValue(STATE));
    e.setEmail(r.getValue(EMAIL));
    e.setPhone(r.getValue(PHONE));
    e.setPhoneMobile(r.getValue(PHONE_MOBILE));
    e.setForename(r.getValue(FORENAME));
    e.setSurname(r.getValue(SURNAME));
    e.setLastLoggedIn(r.getValue(LAST_LOGGED_IN));
    e.setArmepsUsername(r.getValue(ARMEPS_USERNAME));
    return e;
  }
  
  public void insert(User user) throws Throwable {
    try( DSLContext create = dsl() ){
      create.insertInto(USER, ID, ROLE_ID, AUTHORITY_ID, USERNAME, STATE, EMAIL, PHONE, PHONE_MOBILE, FORENAME, SURNAME, LAST_LOGGED_IN, ARMEPS_USERNAME).values(user.getId(), user.getRoleId(), user.getAuthorityId(), user.getUsername(), user.getState(), user.getEmail(), user.getPhone(), user.getPhoneMobile(), user.getForename(), user.getSurname(), user.getLastLoggedIn(), user.getArmepsUsername()).execute();
    }
  }
  
  public void update(User user) throws Throwable {
    try( DSLContext create = dsl() ) {
      create.update(USER)
      .set(ID, user.getId())
      .set(ROLE_ID, user.getRoleId())
      .set(AUTHORITY_ID, user.getAuthorityId())
      .set(USERNAME, user.getUsername())
      .set(STATE, user.getState())
      .set(EMAIL, user.getEmail())
      .set(PHONE, user.getPhone())
      .set(PHONE_MOBILE, user.getPhoneMobile())
      .set(FORENAME, user.getForename())
      .set(SURNAME, user.getSurname())
      .set(LAST_LOGGED_IN, user.getLastLoggedIn())
      .set(ARMEPS_USERNAME, user.getArmepsUsername())
      .where(ID.equal(user.getId()))
      .execute();
    }
  }
  
  public void remove(UUID id) throws Throwable {
    try( DSLContext create = dsl() ) {
      create.delete(USER).where(ID.equal(id)).execute();
    }
  }
  
  public List<User> selectAll() throws Throwable {
    return selectAll(null, null);
  }
  
  public List<User> selectAll(PageInfo page, OrderInfo order) throws Throwable {
    try( DSLContext create = dsl() ){
      List<User> list = new ArrayList<User>();
      SelectQuery q = create.selectQuery();
      q.addFrom(USER);
      if( page != null ) {
        q.addLimit(page.getLimit());
        q.addOffset(page.getOffset());
      }
      if( order != null ) q.addOrderBy(sortedField(order));
      Result<Record> result = q.fetch();
      for(Record r: result) {
        list.add( makeUser(r) );
      }
      return list;
    }
  }
  
  public User selectByKey(UUID id) throws Throwable {
    try( DSLContext create = dsl() ) {
      return  makeUser( create.select().from(USER).where(ID.equal(id)).fetchOne() ); 
    }
  }
  
  public List<User> selectByRoleId(UUID id, PageInfo page, OrderInfo order) throws Throwable {
    return selectByForignKey(id, ROLE_ID, page, order);
  }
  
  public List<User> selectByAuthorityId(UUID id, PageInfo page, OrderInfo order) throws Throwable {
    return selectByForignKey(id, AUTHORITY_ID, page, order);
  }
  
  private List<User> selectByForignKey(UUID id, Field<UUID> field_id, PageInfo page, OrderInfo order) throws Throwable {
    try( DSLContext create = dsl() ) {
      List<User> list = new ArrayList<User>();
      SelectQuery<Record> result = create.selectQuery();
      result.addFrom(USER);
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
        User e = new User();
        e.setId(r.getValue(ID));
        e.setRoleId(r.getValue(ROLE_ID));
        e.setAuthorityId(r.getValue(AUTHORITY_ID));
        e.setUsername(r.getValue(USERNAME));
        e.setState(r.getValue(STATE));
        e.setEmail(r.getValue(EMAIL));
        e.setPhone(r.getValue(PHONE));
        e.setPhoneMobile(r.getValue(PHONE_MOBILE));
        e.setForename(r.getValue(FORENAME));
        e.setSurname(r.getValue(SURNAME));
        e.setLastLoggedIn(r.getValue(LAST_LOGGED_IN));
        e.setArmepsUsername(r.getValue(ARMEPS_USERNAME));
        list.add(e);
      }
      return list;
    }
  }
}
