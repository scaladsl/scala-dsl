package am.iunetworks.ppcm.api.dataaccess;

import java.util.*;
import java.sql.*;
import javax.sql.DataSource; 
import am.iunetworks.ppcm.api.model.*;

import org.apache.log4j.Logger;

public class BasicFormDao extends BaseDao{
  
  protected BasicFormDao (DataSource  dataSource){
    super(dataSource, Logger.getLogger(BasicFormDao.class));
  }
  
  public void insert(Form form) throws Throwable{
    final String sql = "insert into form(id, code, name_hy, abbr_hy, name_en, abbr_en, name_ru, abbr_ru, obsolete) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    Object[] fields = {form.getId(), form.getCode(), form.getNameHy(), form.getAbbrHy(), form.getNameEn(), form.getAbbrEn(), form.getNameRu(), form.getAbbrRu(), form.getObsolete()};
    logger.info( sqlStatement(sql, fields) );
    try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
      ps.setObject(1, form.getId());
      ps.setString(2, form.getCode());
      ps.setString(3, form.getNameHy());
      ps.setString(4, form.getAbbrHy());
      ps.setString(5, form.getNameEn());
      ps.setString(6, form.getAbbrEn());
      ps.setString(7, form.getNameRu());
      ps.setString(8, form.getAbbrRu());
      ps.setBoolean(9, form.getObsolete());
      ps.executeUpdate();
    }
  }
  
  public void update(Form form) throws Throwable{
    final String sql = "update form set code = ?, name_hy = ?, abbr_hy = ?, name_en = ?, abbr_en = ?, name_ru = ?, abbr_ru = ?, obsolete = ?, where id = ?;";
    Object[] fields = {form.getId(), form.getCode(), form.getNameHy(), form.getAbbrHy(), form.getNameEn(), form.getAbbrEn(), form.getNameRu(), form.getAbbrRu(), form.getObsolete()};
    logger.info( sqlStatement(sql, fields) );
    try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
      ps.setObject(1, form.getId());
      ps.setString(2, form.getCode());
      ps.setString(3, form.getNameHy());
      ps.setString(4, form.getAbbrHy());
      ps.setString(5, form.getNameEn());
      ps.setString(6, form.getAbbrEn());
      ps.setString(7, form.getNameRu());
      ps.setString(8, form.getAbbrRu());
      ps.setBoolean(9, form.getObsolete());
      ps.executeUpdate();
    }
  }
  
  public void remove(UUID id) throws Throwable{
    final String sql = "delete from form where id =?;";
    logger.info("delete from form where id = " + id + ")");
    try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
      ps.setObject(1, id);
      ps.executeUpdate();
    }
  }
  
  public List<Form> selectAll() throws Throwable{
    return selectAll(null, null);
  }
  
  public List<Form> selectAll(PageInfo page) throws Throwable{
    return selectAll(page, null);
  }
  
  public List<Form> selectAll(OrderInfo order) throws Throwable{
    return selectAll(null, order);
  }
  
  public List<Form> selectAll(PageInfo page, OrderInfo order) throws Throwable{
    String sql = "select * from form;";
    if( page != null && order == null )
      sql = "select * from form limit ? offset ? ;";
    else if ( page == null && order != null )
    sql = String.format("select * from form order by \"%s\" %s;", order.getOrderBy(), order.getOrderDir());
    else if( page != null && order != null )
      sql = String.format("select * from form order by \"%s\" %s limit ? offset ?;", order.getOrderBy(), order.getOrderDir());
    try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
      if( page != null ){
        ps.setInt(1, page.getSize());
        ps.setInt(2, page.getIndex()*page.getSize());
        Object[] fields = {page.getSize(), page.getIndex()};
        logger.info(sqlStatement(sql,fields));
      }
      else{
        logger.info(sql);
      }
      ResultSet rs = ps.executeQuery();
      List<Form> list = new ArrayList<Form>();
      while (rs.next()){
        Form e = new Form();
        e.setId(java.util.UUID.fromString(rs.getString("id")));
        e.setCode(rs.getString("code"));
        e.setNameHy(rs.getString("name_hy"));
        e.setAbbrHy(rs.getString("abbr_hy"));
        e.setNameEn(rs.getString("name_en"));
        e.setAbbrEn(rs.getString("abbr_en"));
        e.setNameRu(rs.getString("name_ru"));
        e.setAbbrRu(rs.getString("abbr_ru"));
        e.setObsolete(rs.getBoolean("obsolete"));
        list.add(e);
      }
      return list;
    }
  }
  
  public Form selectByKey(java.util.UUID id) throws Throwable{
    final String sql = "select * from form where id = ? ;";
    logger.info("select * from form where id = " + id + ";");
    try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
      Form e = new Form();
      ps.setObject(1, id);
      ResultSet rs = ps.executeQuery();
      while (rs.next()){
        e.setId(java.util.UUID.fromString(rs.getString("id")));
        e.setCode(rs.getString("code"));
        e.setNameHy(rs.getString("name_hy"));
        e.setAbbrHy(rs.getString("abbr_hy"));
        e.setNameEn(rs.getString("name_en"));
        e.setAbbrEn(rs.getString("abbr_en"));
        e.setNameRu(rs.getString("name_ru"));
        e.setAbbrRu(rs.getString("abbr_ru"));
        e.setObsolete(rs.getBoolean("obsolete"));
      }
      return e;
    }
  }
}
