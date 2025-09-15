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

public class Length3D extends SpatialScenarioBase {
    private static final Logger logger = Logger.getLogger(ReadSpatialLineOverlapsLine3D.class);

    private PreparedStatement[] pstmtArray;
    private Connection conn;
    private SpatialTableHelper3D helper3D;

    public Length3D(Properties props) {
        String url = props.getProperty("url");
        String login = props.getProperty("user");
        String password = props.getProperty("password");
        this.helper3D = new SpatialTableHelper3D(url, login, password, props);
    }

    @Override
    public void prepare() throws Exception {
        this.conn = helper3D.getConnection();
        SpatialSqlDialect3D dialect = helper3D.getSpatialSqlDialect3D();

        String sql = dialect.getLength3DQuery();
        pstmtArray = new PreparedStatement[]{conn.prepareStatement(sql)};
        
        logger.info("Prepared statement created: " + sql);
    }

    @Override
    public void iterate(long iterationCount) throws Exception {
        if (pstmtArray == null || pstmtArray.length == 0) {
            throw new IllegalStateException("pstmtArray is null or empty. Ensure prepare() method is called before iterate().");
        }

        // Warm-up run
        logger.info("Executing warm-up run...");
        PreparedStatement warmupPstmt = pstmtArray[0]; // Use the first prepared statement for warm-up
        try (ResultSet rs = warmupPstmt.executeQuery()) {
            // Process results if needed; can be empty
        } catch (SQLException e) {
            logger.error("Error executing warm-up query", e);
            throw e;
        }

        // Execute the main query once
        PreparedStatement pstmt = pstmtArray[0]; // or select a random statement if you have multiple
        try {
            logger.debug("Executing main query: " + pstmt);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int californiaId = rs.getInt(1);
                    int californiaGmlId = rs.getInt(2);
                    String intersectionGeom = rs.getString(3);

                    logger.info("Intersection found: California ID: " + californiaId + 
                                ", California GML ID: " + californiaGmlId + 
                                ", Intersection Geometry: " + intersectionGeom);
                }
            }
        } catch (SQLException e) {
            logger.error("Error executing main query", e);
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


