package edu.toronto.cs.jackpine.benchmark.scenarios.threed;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.util.Properties;
import org.apache.log4j.Logger;
import edu.toronto.cs.jackpine.benchmark.db.threed.SpatialSqlDialect3D;
import edu.toronto.cs.jackpine.benchmark.db.threed.SpatialTableHelper3D;

public class ReadSpatialRegionContainsLine3D {
    private static final Logger logger = Logger.getLogger(ReadSpatialRegionContainsLine3D.class);
    protected PreparedStatement[] pstmtArray;
    protected Connection conn;
    protected SpatialTableHelper3D helper3D;
    

    public ReadSpatialRegionContainsLine3D(Properties props) {
        String url = props.getProperty("url");
        String login = props.getProperty("user");
        String password = props.getProperty("password");
        this.helper3D = new SpatialTableHelper3D(url, login, password, props);
    }

    public void prepare() throws Exception {
        this.conn = helper3D.getConnection();
        SpatialSqlDialect3D dialect = helper3D.getSpatialSqlDialect3D();
        pstmtArray = new PreparedStatement[1];
        String sql = dialect.getSelect3DRegionContainsLine();
        pstmtArray[0] = conn.prepareStatement(sql);
    }

    public void iterate(long iterationCount) throws Exception {
        int index = (int) (Math.random() * pstmtArray.length);
        PreparedStatement pstmt = pstmtArray[index];
        logger.warn(pstmt.toString());
        pstmt.executeQuery();
    }

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
