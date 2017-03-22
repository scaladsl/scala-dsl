package am.iunetworks.ppcm.api.dataaccess;

import java.util.*;
import java.sql.*;
import javax.sql.DataSource; 
import am.iunetworks.ppcm.api.model.Supplier;

public class BasicSupplierDao extends BaseDao{
  protected BasicSupplierDao (DataSource  dataSource){
    super(dataSource, org.slf4j.LoggerFactory.getLogger(Basic${structure.name.toPascal}Dao.class));
  }
  public void insert(Supplier supplier) throws Throwable{
    final String SQL_INSERT = "insert into supplier(id, name, email, country, armeps_id, taxpayer_id) values(?, ?, ?, ?, ?, ?)";
    private Object[] a = getId(), getName(), getEmail(), getCountry(), getArmepsId(), getTaxpayerId()
    logger.info( sqlStatement(SQL_INSERT, args) );
    try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_INSERT)){
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
    final String SQL_UPDATE = "update supplier set name = ?, email = ?, country = ?, armeps_id = ?, taxpayer_id = ?, where id = ?;";
    private Object[] a = getId(), getName(), getEmail(), getCountry(), getArmepsId(), getTaxpayerId()
    logger.info( sqlStatement(SQL_UPDATE, args) );
    try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_UPDATE)){
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
    final String SQL_DELETE = "delete from supplier where id =?;";
    logger.info("delete from supplier where id = " + id + ")");
    try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_DELETE)){
      ps.setObject(1, id);
      ps.executeUpdate();
    }
  }
  public List<Supplier> selectAll() throws Throwable{
    final String SQL_SELECT_ALL = "Select * from supplier";
    logger.info(SQL_SELECT_ALL);
    try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL)){
      List<Supplier> supplierList = new ArrayList<Supplier>();
      ResultSet rs = ps.executeQuery();
      while (rs.next()){
        Supplier supplier = new Supplier();
        supplier.setId(java.util.UUID.fromString(rs.getString("id")));
        supplier.setName(rs.getString("name"));
        supplier.setEmail(rs.getString("email"));
        supplier.setCountry(rs.getString("country"));
        supplier.setArmepsId(rs.getInt("armeps_id"));
        supplier.setTaxpayerId(rs.getString("taxpayer_id"));
        supplierList.add(supplier);
      }
      return supplierList;
    }
  }
  public Supplier selectByKey(java.util.UUID id) throws Throwable{
    final String SQL_SELECT_BY_KEY = "Select * from supplier where id = ? ;";
    logger.info("Select * from supplier where id = " + id + ";");
    try( Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_KEY)){
      Supplier supplier = new Supplier();
      ps.setObject(1, id);
      ResultSet rs = ps.executeQuery();
      while (rs.next()){
        supplier.setId(java.util.UUID.fromString(rs.getString("id")));
        supplier.setName(rs.getString("name"));
        supplier.setEmail(rs.getString("email"));
        supplier.setCountry(rs.getString("country"));
        supplier.setArmepsId(rs.getInt("armeps_id"));
        supplier.setTaxpayerId(rs.getString("taxpayer_id"));
      }
      return supplier;
    }
  }
}
