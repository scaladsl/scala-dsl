package am.iunetworks.ppcm.api.dataaccess;

import java.util.*;
import java.sql.*;
import javax.sql.DataSource; 
import am.iunetworks.ppcm.api.model.*;

import org.apache.log4j.Logger;

public class BasicAuthorityDao extends BaseDao{
  
  protected BasicAuthorityDao (DataSource  dataSource){
    super(dataSource, Logger.getLogger(BasicAuthorityDao.class));
  }
  
  public void insert(Authority authority) throws Throwable{
    final String sql = "insert into authority(id, code, name_hy, name_en, name_ru, regional) values(?, ?, ?, ?, ?, ?)";
    Object[] fields = {authority.getId(), authority.getCode(), authority.getNameHy(), authority.getNameEn(), authority.getNameRu(), authority.getRegional()};
    logger.info( sqlStatement(sql, fields) );
    try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
      ps.setObject(1, authority.getId());
      ps.setString(2, authority.getCode());
      ps.setString(3, authority.getNameHy());
      ps.setString(4, authority.getNameEn());
      ps.setString(5, authority.getNameRu());
      ps.setBoolean(6, authority.getRegional());
      ps.executeUpdate();
    }
  }
  
  public void update(Authority authority) throws Throwable{
    final String sql = "update authority set code = ?, name_hy = ?, name_en = ?, name_ru = ?, regional = ?, where id = ?;";
    Object[] fields = {authority.getId(), authority.getCode(), authority.getNameHy(), authority.getNameEn(), authority.getNameRu(), authority.getRegional()};
    logger.info( sqlStatement(sql, fields) );
    try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
      ps.setObject(1, authority.getId());
      ps.setString(2, authority.getCode());
      ps.setString(3, authority.getNameHy());
      ps.setString(4, authority.getNameEn());
      ps.setString(5, authority.getNameRu());
      ps.setBoolean(6, authority.getRegional());
      ps.executeUpdate();
    }
  }
  
  public void remove(UUID id) throws Throwable{
    final String sql = "delete from authority where id =?;";
    logger.info("delete from authority where id = " + id + ")");
    try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
      ps.setObject(1, id);
      ps.executeUpdate();
    }
  }
  
  public List<Authority> selectAll() throws Throwable{
    return selectAll(null, null);
  }
  
  public List<Authority> selectAll(PageInfo page) throws Throwable{
    return selectAll(page, null);
  }
  
  public List<Authority> selectAll(OrderInfo order) throws Throwable{
    return selectAll(null, order);
  }
  
  public List<Authority> selectAll(PageInfo page, OrderInfo order) throws Throwable{
    String sql = "select * from authority;";
    if( page != null && order == null )
      sql = "select * from authority limit ? offset ? ;";
    else if ( page == null && order != null )
    sql = String.format("select * from authority order by \"%s\" %s;", order.getOrderBy(), order.getOrderDir());
    else if( page != null && order != null )
      sql = String.format("select * from authority order by \"%s\" %s limit ? offset ?;", order.getOrderBy(), order.getOrderDir());
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
      List<Authority> list = new ArrayList<Authority>();
      while (rs.next()){
        Authority e = new Authority();
        e.setId(java.util.UUID.fromString(rs.getString("id")));
        e.setCode(rs.getString("code"));
        e.setNameHy(rs.getString("name_hy"));
        e.setNameEn(rs.getString("name_en"));
        e.setNameRu(rs.getString("name_ru"));
        e.setRegional(rs.getBoolean("regional"));
        list.add(e);
      }
      return list;
    }
  }
  
  public Authority selectByKey(java.util.UUID id) throws Throwable{
    final String sql = "select * from authority where id = ? ;";
    logger.info("select * from authority where id = " + id + ";");
    try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
      Authority e = new Authority();
      ps.setObject(1, id);
      ResultSet rs = ps.executeQuery();
      while (rs.next()){
        e.setId(java.util.UUID.fromString(rs.getString("id")));
        e.setCode(rs.getString("code"));
        e.setNameHy(rs.getString("name_hy"));
        e.setNameEn(rs.getString("name_en"));
        e.setNameRu(rs.getString("name_ru"));
        e.setRegional(rs.getBoolean("regional"));
      }
      return e;
    }
  }
}
