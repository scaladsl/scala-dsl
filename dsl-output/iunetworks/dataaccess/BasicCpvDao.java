package am.iunetworks.ppcm.api.dataaccess;

import java.util.*;
import java.sql.*;
import javax.sql.DataSource; 
import am.iunetworks.ppcm.api.model.*;

import org.apache.log4j.Logger;

public class BasicCpvDao extends BaseDao{
  
  protected BasicCpvDao (DataSource  dataSource){
    super(dataSource, Logger.getLogger(BasicCpvDao.class));
  }
  
  public void insert(Cpv cpv) throws Throwable{
    final String sql = "insert into cpv(id, parent_id, code, name_hy, name_ru, name_en, armeps_id, path) values(?, ?, ?, ?, ?, ?, ?, ?)";
    Object[] fields = {cpv.getId(), cpv.getParentId(), cpv.getCode(), cpv.getNameHy(), cpv.getNameRu(), cpv.getNameEn(), cpv.getArmepsId(), cpv.getPath()};
    logger.info( sqlStatement(sql, fields) );
    try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
      ps.setObject(1, cpv.getId());
      ps.setObject(2, cpv.getParentId());
      ps.setString(3, cpv.getCode());
      ps.setString(4, cpv.getNameHy());
      ps.setString(5, cpv.getNameRu());
      ps.setString(6, cpv.getNameEn());
      ps.setInt(7, cpv.getArmepsId());
      ps.setString(8, cpv.getPath());
      ps.executeUpdate();
    }
  }
  
  public void update(Cpv cpv) throws Throwable{
    final String sql = "update cpv set parent_id = ?, code = ?, name_hy = ?, name_ru = ?, name_en = ?, armeps_id = ?, path = ?, where id = ?;";
    Object[] fields = {cpv.getId(), cpv.getParentId(), cpv.getCode(), cpv.getNameHy(), cpv.getNameRu(), cpv.getNameEn(), cpv.getArmepsId(), cpv.getPath()};
    logger.info( sqlStatement(sql, fields) );
    try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
      ps.setObject(1, cpv.getId());
      ps.setObject(2, cpv.getParentId());
      ps.setString(3, cpv.getCode());
      ps.setString(4, cpv.getNameHy());
      ps.setString(5, cpv.getNameRu());
      ps.setString(6, cpv.getNameEn());
      ps.setInt(7, cpv.getArmepsId());
      ps.setString(8, cpv.getPath());
      ps.executeUpdate();
    }
  }
  
  public void remove(UUID id) throws Throwable{
    final String sql = "delete from cpv where id =?;";
    logger.info("delete from cpv where id = " + id + ")");
    try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
      ps.setObject(1, id);
      ps.executeUpdate();
    }
  }
  
  public List<Cpv> selectAll() throws Throwable{
    return selectAll(null, null);
  }
  
  public List<Cpv> selectAll(PageInfo page) throws Throwable{
    return selectAll(page, null);
  }
  
  public List<Cpv> selectAll(OrderInfo order) throws Throwable{
    return selectAll(null, order);
  }
  
  public List<Cpv> selectAll(PageInfo page, OrderInfo order) throws Throwable{
    String sql = "select * from cpv;";
    if( page != null && order == null )
      sql = "select * from cpv limit ? offset ? ;";
    else if ( page == null && order != null )
    sql = String.format("select * from cpv order by \"%s\" %s;", order.getOrderBy(), order.getOrderDir());
    else if( page != null && order != null )
      sql = String.format("select * from cpv order by \"%s\" %s limit ? offset ?;", order.getOrderBy(), order.getOrderDir());
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
      List<Cpv> list = new ArrayList<Cpv>();
      while (rs.next()){
        Cpv e = new Cpv();
        e.setId(java.util.UUID.fromString(rs.getString("id")));
        e.setParentId(java.util.UUID.fromString(rs.getString("parent_id")));
        e.setCode(rs.getString("code"));
        e.setNameHy(rs.getString("name_hy"));
        e.setNameRu(rs.getString("name_ru"));
        e.setNameEn(rs.getString("name_en"));
        e.setArmepsId(rs.getInt("armeps_id"));
        e.setPath(rs.getString("path"));
        list.add(e);
      }
      return list;
    }
  }
  
  public Cpv selectByKey(java.util.UUID id) throws Throwable{
    final String sql = "select * from cpv where id = ? ;";
    logger.info("select * from cpv where id = " + id + ";");
    try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
      Cpv e = new Cpv();
      ps.setObject(1, id);
      ResultSet rs = ps.executeQuery();
      while (rs.next()){
        e.setId(java.util.UUID.fromString(rs.getString("id")));
        e.setParentId(java.util.UUID.fromString(rs.getString("parent_id")));
        e.setCode(rs.getString("code"));
        e.setNameHy(rs.getString("name_hy"));
        e.setNameRu(rs.getString("name_ru"));
        e.setNameEn(rs.getString("name_en"));
        e.setArmepsId(rs.getInt("armeps_id"));
        e.setPath(rs.getString("path"));
      }
      return e;
    }
  }
}
