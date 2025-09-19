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

public class ConvexHull extends SpatialScenarioBase {
    private static final Logger logger = Logger.getLogger(ConvexHull.class);

    private PreparedStatement[] pstmtArray;
    private Connection conn;
    private SpatialTableHelper3D helper3D;

    public ConvexHull(Properties props) {
        String url = props.getProperty("url");
        String login = props.getProperty("user");
        String password = props.getProperty("password");
        this.helper3D = new SpatialTableHelper3D(url, login, password, props);
    }

    @Override
    public void prepare() throws Exception {
        this.conn = helper3D.getConnection();
        SpatialSqlDialect3D dialect = helper3D.getSpatialSqlDialect3D();

        // Get the convex hull query and remove EXPLAIN ANALYZE if present
        String sql = dialect.getConvexHullQuery();

        // Remove EXPLAIN ANALYZE prefix to get actual data results
        sql = sql.replaceFirst("(?i)^\\s*EXPLAIN\\s+(ANALYZE\\s+)?", "").trim();

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
        PreparedStatement warmupPstmt = pstmtArray[0];
        try (ResultSet rs = warmupPstmt.executeQuery()) {
            // Process warm-up results but don't log details
            while (rs.next()) {
                // Just consume the results
            }
        } catch (SQLException e) {
            logger.error("Error executing warm-up query", e);
            throw e;
        }

        // Execute the main query
        PreparedStatement pstmt = pstmtArray[0];
        try {
            logger.debug("Executing main convex hull query");
            try (ResultSet rs = pstmt.executeQuery()) {
                int resultCount = 0;
                while (rs.next()) {
                    // Read the correct columns: gid (int) and convex_hull (geometry as string/bytes)
                    int gid = rs.getInt("gid");
                    String convexHull = rs.getString("convex_hull");

                    resultCount++;
                    if (resultCount <= 5) { // Log first 5 results to avoid spam
                        logger.info("Convex Hull result - GID: " + gid +
                                ", Convex Hull geometry length: " + (convexHull != null ? convexHull.length() : 0) + " chars");
                    }
                }
                logger.info("Total convex hull results processed: " + resultCount);
            }
        } catch (SQLException e) {
            logger.error("Error executing main query", e);
            throw e;
        }
    }

    @Override
    public void cleanup() throws Exception {
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