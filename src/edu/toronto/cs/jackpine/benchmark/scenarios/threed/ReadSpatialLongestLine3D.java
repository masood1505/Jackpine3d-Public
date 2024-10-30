package edu.toronto.cs.jackpine.benchmark.scenarios.threed;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import edu.toronto.cs.jackpine.benchmark.db.threed.SpatialSqlDialect3D;
import edu.toronto.cs.jackpine.benchmark.db.threed.SpatialTableHelper3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.SpatialScenarioBase;

public class ReadSpatialLongestLine3D extends SpatialScenarioBase {
    private static final Logger logger = Logger.getLogger(ReadSpatialLongestLine3D.class);
    private PreparedStatement[] pstmtArray;
    private Connection conn;
    private SpatialTableHelper3D helper3D;

    public ReadSpatialLongestLine3D(Properties props) {
        String url = props.getProperty("url");
        String login = props.getProperty("user");
        String password = props.getProperty("password");
        this.helper3D = new SpatialTableHelper3D(url, login, password, props);
    }

    @Override
    public void prepare() throws Exception {
        this.conn = helper3D.getConnection();
        SpatialSqlDialect3D dialect = helper3D.getSpatialSqlDialect3D();
        
        String sql = "SELECT " +
                     "    ca.gid AS california_id, " +
                     "    ca_gml.ogc_fid AS california_gml_id, " +
                     "    ST_3DLongestLine( " +
                     "        ST_Force3D(ST_Transform(ca.geom, 4979)), " +
                     "        ca_gml.geom " +
                     "    ) AS longest_line_geom " +
                     "FROM " +
                     "    california ca " +
                     "JOIN " +
                     "    california_gml ca_gml " +
                     "ON " +
                     "    ST_3DIntersects( " +
                     "        ST_Force3D(ST_Transform(ca.geom, 4979)), " +
                     "        ca_gml.geom " +
                     "    );";
        
        pstmtArray = new PreparedStatement[]{conn.prepareStatement(sql)};
        
        logger.info("Prepared statement created: " + sql);
    }

    @Override
    public void iterate(long iterationCount) throws Exception {
        if (pstmtArray == null || pstmtArray.length == 0) {
            throw new IllegalStateException("pstmtArray is null or empty. Ensure prepare() method is called before iterate().");
        }
        int index = (int) (Math.random() * pstmtArray.length);
        PreparedStatement pstmt = pstmtArray[index];
        
        try {
            logger.debug("Executing query: " + pstmt);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int californiaId = rs.getInt("california_id");
                    int californiaGmlId = rs.getInt("california_gml_id");
                    String longestLineGeom = rs.getString("longest_line_geom");
                    
                    logger.info("Longest line found: California ID: " + californiaId + 
                                ", California GML ID: " + californiaGmlId + 
                                ", Longest Line Geometry: " + longestLineGeom);
                }
            }
        } catch (SQLException e) {
            logger.error("Error executing query", e);
            throw e;
        }
    }

    @Override
    public void cleanup() {
        if (pstmtArray != null) {
            for (PreparedStatement pstmt : pstmtArray) {
                try {
                    if (pstmt != null && !pstmt.isClosed()) {
                        pstmt.close();
                    }
                } catch (SQLException e) {
                    logger.warn("Error closing PreparedStatement", e);
                }
            }
        }
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            logger.warn("Error closing Connection", e);
        }
    }
}
