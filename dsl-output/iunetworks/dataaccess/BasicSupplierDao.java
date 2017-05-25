package am.iunetworks.ppcm.api.dataaccess;

import java.util.*;
import java.sql.*;
import javax.sql.DataSource; 
import am.iunetworks.ppcm.api.model.*;

import org.apache.log4j.Logger;

public class BasicSupplierDao extends BaseDao{
  
  protected BasicSupplierDao (DataSource  dataSource){
    super(dataSource, Logger.getLogger(BasicSupplierDao.class));
  }
  
  public void insert(Supplier supplier) throws Throwable{
    final String sql = "insert into supplier(id, name, email, country, armeps_id, taxpayer_id) values(?, ?, ?, ?, ?, ?)";
    Object[] fields = {supplier.getId(), supplier.getName(), supplier.getEmail(), supplier.getCountry(), supplier.getArmepsId(), supplier.getTaxpayerId()};
    logger.info( sqlStatement(sql, fields) );
    try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
      ps.setObject(1, supplier.getId());
      ps.setString(2, supplier.getName());
      ps.setString(3, supplier.getEmail());
      ps.setString(4, supplier.getCountry());
      ps.setInt(5, supplier.getArmepsId());
      ps.setString(6, supplier.getTaxpayerId());
      ps.executeUpdate();
    }
  }
  
  public void update(Supplier supplier) throws Throwable{
    final String sql = "update supplier set name = ?, email = ?, country = ?, armeps_id = ?, taxpayer_id = ?, where id = ?;";
    Object[] fields = {supplier.getId(), supplier.getName(), supplier.getEmail(), supplier.getCountry(), supplier.getArmepsId(), supplier.getTaxpayerId()};
    logger.info( sqlStatement(sql, fields) );
    try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
      ps.setObject(1, supplier.getId());
      ps.setString(2, supplier.getName());
      ps.setString(3, supplier.getEmail());
      ps.setString(4, supplier.getCountry());
      ps.setInt(5, supplier.getArmepsId());
      ps.setString(6, supplier.getTaxpayerId());
      ps.executeUpdate();
    }
  }
  
  public void remove(UUID id) throws Throwable{
    final String sql = "delete from supplier where id =?;";
    logger.info("delete from supplier where id = " + id + ")");
    try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
      ps.setObject(1, id);
      ps.executeUpdate();
    }
  }
  
  public List<Supplier> selectAll() throws Throwable{
    return selectAll(null, null);
  }
  
  public List<Supplier> selectAll(PageInfo page) throws Throwable{
    return selectAll(page, null);
  }
  
  public List<Supplier> selectAll(OrderInfo order) throws Throwable{
    return selectAll(null, order);
  }
  
  public List<Supplier> selectAll(PageInfo page, OrderInfo order) throws Throwable{
    String sql = "select * from supplier;";
    if( page != null && order == null )
      sql = "select * from supplier limit ? offset ? ;";
    else if ( page == null && order != null )
    sql = String.format("select * from supplier order by \"%s\" %s;", order.getOrderBy(), order.getOrderDir());
    else if( page != null && order != null )
      sql = String.format("select * from supplier order by \"%s\" %s limit ? offset ?;", order.getOrderBy(), order.getOrderDir());
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
      List<Supplier> list = new ArrayList<Supplier>();
      while (rs.next()){
        Supplier e = new Supplier();
        e.setId(java.util.UUID.fromString(rs.getString("id")));
        e.setName(rs.getString("name"));
        e.setEmail(rs.getString("email"));
        e.setCountry(rs.getString("country"));
        e.setArmepsId(rs.getInt("armeps_id"));
        e.setTaxpayerId(rs.getString("taxpayer_id"));
        list.add(e);
      }
      return list;
    }
  }
  
  public Supplier selectByKey(java.util.UUID id) throws Throwable{
    final String sql = "select * from supplier where id = ? ;";
    logger.info("select * from supplier where id = " + id + ";");
    try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
      Supplier e = new Supplier();
      ps.setObject(1, id);
      ResultSet rs = ps.executeQuery();
      while (rs.next()){
        e.setId(java.util.UUID.fromString(rs.getString("id")));
        e.setName(rs.getString("name"));
        e.setEmail(rs.getString("email"));
        e.setCountry(rs.getString("country"));
        e.setArmepsId(rs.getInt("armeps_id"));
        e.setTaxpayerId(rs.getString("taxpayer_id"));
      }
      return e;
    }
  }
}
