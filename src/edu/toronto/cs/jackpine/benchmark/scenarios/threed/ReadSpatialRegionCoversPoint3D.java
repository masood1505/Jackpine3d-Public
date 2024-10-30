package edu.toronto.cs.jackpine.benchmark.scenarios.threed;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.util.Properties;
import org.apache.log4j.Logger;
import edu.toronto.cs.jackpine.benchmark.db.threed.SpatialSqlDialect3D;
import edu.toronto.cs.jackpine.benchmark.db.threed.SpatialTableHelper3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.SpatialScenarioBase;

public class ReadSpatialRegionCoversPoint3D extends SpatialScenarioBase {
    private static final Logger logger = Logger.getLogger(ReadSpatialRegionCoversPoint3D.class);

    protected PreparedStatement[] pstmtArray;
    protected Connection conn;
    protected SpatialTableHelper3D helper3D;

    public ReadSpatialRegionCoversPoint3D(Properties props) {
        // Initialize the SpatialTableHelper3D with the provided properties
        String url = props.getProperty("url");
        String login = props.getProperty("user");
        String password = props.getProperty("password");
        this.helper3D = new SpatialTableHelper3D(url, login, password, props);
    }

    /** Create a prepared statement array. */
    public void prepare() throws Exception {
        this.conn = helper3D.getConnection(); // Initialize conn from helper3D
        SpatialSqlDialect3D dialect = helper3D.getSpatialSqlDialect3D(); 
        pstmtArray = new PreparedStatement[1];
        String sql = dialect.getSelectRegionCoversPoint3D();
        pstmtArray[0] = conn.prepareStatement(sql);
    }

    /** Execute an iteration. */
    public void iterate(long iterationCount) throws Exception {
        int index = (int) (Math.random() * pstmtArray.length);
        PreparedStatement pstmt = pstmtArray[index];
        logger.warn(pstmt.toString());
        pstmt.executeQuery();
    }

    /** Clean up resources used by scenario. */
    public void cleanup() throws Exception {
        for (PreparedStatement pstmt : pstmtArray) {
            if (pstmt != null) {
                pstmt.close();
            }
        }
        if (conn != null) {
            conn.close();
        }
    }
}
