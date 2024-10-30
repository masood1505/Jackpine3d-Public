package edu.toronto.cs.jackpine.benchmark.scenarios.threed;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.util.Properties;
import org.apache.log4j.Logger;
import edu.toronto.cs.jackpine.benchmark.db.threed.SpatialSqlDialect3D;
import edu.toronto.cs.jackpine.benchmark.db.threed.SpatialTableHelper3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.SpatialScenarioBase;

/**
 * Scenario for reading spatial region within a volume in 3D.
 * 
 * @author sray
 */
public class ReadSpatialRegionWithinVolume3D extends SpatialScenarioBase
{
    private static final Logger logger = Logger.getLogger(ReadSpatialRegionWithinVolume3D.class);

    protected PreparedStatement[] pstmtArray;
    protected SpatialTableHelper3D helper3D;
    protected Connection conn;  // Added connection field

    public ReadSpatialRegionWithinVolume3D(Properties props) {
        String url = props.getProperty("url");
        String login = props.getProperty("user");
        String password = props.getProperty("password");
        this.helper3D = new SpatialTableHelper3D(url, login, password, props);
    }
  
    /** Create a prepared statement array. */
    @Override
    public void prepare() throws Exception
    {
        // Initialize the connection
        this.conn = helper3D.getConnection();
        if (conn == null) {
            throw new IllegalStateException("Database connection is null. Ensure initialize() is called before prepare().");
        }

        SpatialSqlDialect3D dialect = helper3D.getSpatialSqlDialect3D(); // Corrected helper usage
        if (dialect == null) {
            throw new IllegalStateException("SpatialSqlDialect3D is null. Check if it's properly initialized.");
        }
        
        pstmtArray = new PreparedStatement[1];
        String sql = dialect.getSelectRegionInsideVolume3D();
        pstmtArray[0] = conn.prepareStatement(sql);
    }

    /** Execute an iteration. */
    @Override
    public void iterate(long iterationCount) throws Exception
    {
        if (pstmtArray == null || pstmtArray.length == 0) {
            throw new IllegalStateException("Prepared statements are not initialized. Ensure prepare() is called before iterate().");
        }
        
        int index = (int) (Math.random() * pstmtArray.length);
        PreparedStatement pstmt = pstmtArray[index];
        
        logger.warn(pstmt.toString());
        pstmt.executeQuery();
    }

    /** Clean up resources used by scenario. */
    @Override
    public void cleanup() throws Exception
    {
        if (pstmtArray != null) {
            for (PreparedStatement pstmt : pstmtArray) {
                if (pstmt != null) {
                    pstmt.close();
                }
            }
        }
        if (conn != null) {
            conn.close();
        }
    }
}
