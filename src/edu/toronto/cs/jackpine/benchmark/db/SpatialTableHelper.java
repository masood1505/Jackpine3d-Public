package  edu.toronto.cs.jackpine.benchmark.db;

import org.apache.log4j.Logger;

import com.continuent.bristlecone.benchmark.db.SqlDialectFactory;
import com.continuent.bristlecone.benchmark.db.TableHelper;


public class SpatialTableHelper extends TableHelper
{
  private static Logger logger = Logger.getLogger(SpatialTableHelper.class);

  protected final SpatialSqlDialect spatialSqlDialect;

  public SpatialTableHelper(String url, String login, String password)
  {
    super(url,login,password);
    this.spatialSqlDialect = SpatialSqlDialectFactory.getInstance().getDialect(url);
  }
  
  
  
  /** 
   * Returns the SQLDialect used by this helper. 
   */
  public SpatialSqlDialect getSpatialSqlDialect()
  {
    return spatialSqlDialect;
  }
}