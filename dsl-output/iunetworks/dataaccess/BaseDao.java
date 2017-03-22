package am.iunetworks.ppcm.api.dataaccess;

import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseDao {
    protected DataSource dataSource;
    protected Logger logger; 

    protected BaseDao(DataSource  dataSource) {
       if ( dataSource == null )
            throw new IllegalArgumentException("Database is not specified.");
       this.dataSource = dataSource;
       this.logger = logger;
    }

    protected String sqlStatement(String sql, Object[] args){
       for(Object a: args) 
         sql = sql.replaceFirst("\?", a.toString());
       return sql;
    }
}