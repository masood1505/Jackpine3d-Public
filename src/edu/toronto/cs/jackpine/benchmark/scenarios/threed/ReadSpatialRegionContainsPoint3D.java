
package edu.toronto.cs.jackpine.benchmark.scenarios.threed;

import java.sql.PreparedStatement;
import java.util.Properties;

import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import org.apache.log4j.Logger;
import edu.toronto.cs.jackpine.benchmark.db.threed.SpatialSqlDialect3D; // Import the 3D interface
import edu.toronto.cs.jackpine.benchmark.db.threed.SpatialTableHelper3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.SpatialScenarioBase;


public class ReadSpatialRegionContainsPoint3D extends SpatialScenarioBase
{
  private static final Logger logger = Logger.getLogger(ReadSpatialRegionContainsPoint3D.class);

  protected PreparedStatement[] pstmtArray;
  
  protected SpatialTableHelper3D helper3D;

  public ReadSpatialRegionContainsPoint3D(Properties props) {
      String url = props.getProperty("url");
      String login = props.getProperty("user");
      String password = props.getProperty("password");
      this.helper3D = new SpatialTableHelper3D(url, login, password, props);
  }

  
  
  
  /** Create a prepared statement array. */
  public void prepare() throws Exception
  {
      SpatialSqlDialect3D dialect = (SpatialSqlDialect3D) helper.getSpatialSqlDialect(); 
      
      pstmtArray = new PreparedStatement[1];
      String sql = dialect.getSelectRegionContainsPoint3D();
      pstmtArray[0] = conn.prepareStatement(sql);
  }

  /** Execute an iteration. */
  public void iterate(long iterationCount) throws Exception
  {
    // Pick a table at random on which to operate.
    int index = (int) (Math.random() * pstmtArray.length);
    PreparedStatement pstmt = pstmtArray[index];
    
    // Do the query.
    logger.warn(pstmt.toString());
    pstmt.executeQuery();
  }

  /** Clean up resources used by scenario. */
  public void cleanup() throws Exception
  {
    // Clean up connections. 
    for (int i = 0; i < pstmtArray.length; i++)
      pstmtArray[i].close();
    if (conn != null)
      conn.close();
  }
}
