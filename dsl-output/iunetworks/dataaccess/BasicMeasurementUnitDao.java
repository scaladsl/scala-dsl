package am.iunetworks.ppcm.api.dataaccess;

import java.util.*;
import java.sql.*;
import javax.sql.DataSource; 
import am.iunetworks.ppcm.api.model.*;

import org.apache.log4j.Logger;

public class BasicMeasurementUnitDao extends BaseDao{
  
  protected BasicMeasurementUnitDao (DataSource  dataSource){
    super(dataSource, Logger.getLogger(BasicMeasurementUnitDao.class));
  }
  
  public void insert(MeasurementUnit measurementUnit) throws Throwable{
    final String sql = "insert into measurementUnit(id, code, name_hy, name_en, name_ru) values(?, ?, ?, ?, ?)";
    Object[] fields = {measurementUnit.getId(), measurementUnit.getCode(), measurementUnit.getNameHy(), measurementUnit.getNameEn(), measurementUnit.getNameRu()};
    logger.info( sqlStatement(sql, fields) );
    try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
      ps.setObject(1, measurementUnit.getId());
      ps.setInt(2, measurementUnit.getCode());
      ps.setString(3, measurementUnit.getNameHy());
      ps.setString(4, measurementUnit.getNameEn());
      ps.setString(5, measurementUnit.getNameRu());
      ps.executeUpdate();
    }
  }
  
  public void update(MeasurementUnit measurementUnit) throws Throwable{
    final String sql = "update measurement_unit set code = ?, name_hy = ?, name_en = ?, name_ru = ?, where id = ?;";
    Object[] fields = {measurementUnit.getId(), measurementUnit.getCode(), measurementUnit.getNameHy(), measurementUnit.getNameEn(), measurementUnit.getNameRu()};
    logger.info( sqlStatement(sql, fields) );
    try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
      ps.setObject(1, measurementUnit.getId());
      ps.setInt(2, measurementUnit.getCode());
      ps.setString(3, measurementUnit.getNameHy());
      ps.setString(4, measurementUnit.getNameEn());
      ps.setString(5, measurementUnit.getNameRu());
      ps.executeUpdate();
    }
  }
  
  public void remove(UUID id) throws Throwable{
    final String sql = "delete from measurement_unit where id =?;";
    logger.info("delete from measurement_unit where id = " + id + ")");
    try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
      ps.setObject(1, id);
      ps.executeUpdate();
    }
  }
  
  public List<MeasurementUnit> selectAll() throws Throwable{
    return selectAll(null, null);
  }
  
  public List<MeasurementUnit> selectAll(PageInfo page) throws Throwable{
    return selectAll(page, null);
  }
  
  public List<MeasurementUnit> selectAll(OrderInfo order) throws Throwable{
    return selectAll(null, order);
  }
  
  public List<MeasurementUnit> selectAll(PageInfo page, OrderInfo order) throws Throwable{
    String sql = "select * from measurement_unit;";
    if( page != null && order == null )
      sql = "select * from measurement_unit limit ? offset ? ;";
    else if ( page == null && order != null )
    sql = String.format("select * from measurement_unit order by \"%s\" %s;", order.getOrderBy(), order.getOrderDir());
    else if( page != null && order != null )
      sql = String.format("select * from measurement_unit order by \"%s\" %s limit ? offset ?;", order.getOrderBy(), order.getOrderDir());
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
      List<MeasurementUnit> list = new ArrayList<MeasurementUnit>();
      while (rs.next()){
        MeasurementUnit e = new MeasurementUnit();
        e.setId(java.util.UUID.fromString(rs.getString("id")));
        e.setCode(rs.getInt("code"));
        e.setNameHy(rs.getString("name_hy"));
        e.setNameEn(rs.getString("name_en"));
        e.setNameRu(rs.getString("name_ru"));
        list.add(e);
      }
      return list;
    }
  }
  
  public MeasurementUnit selectByKey(java.util.UUID id) throws Throwable{
    final String sql = "select * from measurement_unit where id = ? ;";
    logger.info("select * from measurement_unit where id = " + id + ";");
    try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
      MeasurementUnit e = new MeasurementUnit();
      ps.setObject(1, id);
      ResultSet rs = ps.executeQuery();
      while (rs.next()){
        e.setId(java.util.UUID.fromString(rs.getString("id")));
        e.setCode(rs.getInt("code"));
        e.setNameHy(rs.getString("name_hy"));
        e.setNameEn(rs.getString("name_en"));
        e.setNameRu(rs.getString("name_ru"));
      }
      return e;
    }
  }
}
