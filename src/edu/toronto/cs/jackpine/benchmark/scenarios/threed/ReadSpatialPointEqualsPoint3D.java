package edu.toronto.cs.jackpine.benchmark.scenarios.threed;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import edu.toronto.cs.jackpine.benchmark.db.SpatialSqlDialect;
import edu.toronto.cs.jackpine.benchmark.scenarios.SpatialScenarioBase;

public class ReadSpatialPointEqualsPoint3D extends SpatialScenarioBase {
    private static final Logger logger = Logger.getLogger(ReadSpatialPointEqualsPoint3D.class);
    private PreparedStatement pstmt;
    private boolean lastResult;
    private boolean isPrepared = false;

    @Override
    public void prepare() throws Exception {
        if (conn == null) {
            logger.error("Database connection is null. Ensure initialize() is called before prepare().");
            throw new IllegalStateException("Database connection is null. Ensure initialize() is called before prepare().");
        }
        
        SpatialSqlDialect dialect = helper.getSpatialSqlDialect();
        if (dialect == null) {
            logger.error("SpatialSqlDialect is null. Check if it's properly initialized.");
            throw new IllegalStateException("SpatialSqlDialect is null. Check if it's properly initialized.");
        }

        // Adjusted SQL query for single parameter scenario
        String sql = "SELECT ST_Equals(geom1, geom2) AS points_equal FROM camb3d_bldg_active_mp WHERE gid = ?";
        
        try {
            pstmt = conn.prepareStatement(sql);
            logger.info("PreparedStatement created successfully");
            isPrepared = true;
        } catch (SQLException e) {
            logger.error("Error creating PreparedStatement: ", e);
            throw e;
        }
    }

    @Override
    public void iterate(long iterationCount) throws Exception {
        if (!isPrepared) {
            logger.info("Preparing benchmark before first iteration");
            prepare();
        }

        if (pstmt == null) {
            logger.error("PreparedStatement is null. Ensure prepare() is called before iterate().");
            throw new IllegalStateException("PreparedStatement is null. Ensure prepare() is called before iterate().");
        }

        int id = getRandomId(); // Use only one ID if that's the case

        ResultSet rs = null;
        try {
            pstmt.setInt(1, id); // Set only one parameter
            rs = pstmt.executeQuery();
            if (rs.next()) {
                lastResult = rs.getBoolean("points_equal");
                logger.info("Iteration " + iterationCount + ": 3D Points Equal for ID " + id + ": " + lastResult);
            } else {
                logger.warn("No result found for ID " + id);
                lastResult = false;
            }
        } catch (SQLException e) {
            logger.error("Error executing query for ID " + id + ": ", e);
            lastResult = false;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.error("Error closing ResultSet: ", e);
                }
            }
        }
    }

    @Override
    public void cleanup() throws Exception {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                logger.error("Error closing PreparedStatement: ", e);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("Error closing Connection: ", e);
            }
        }
        isPrepared = false;
    }

    private int getRandomId() {
        // Ensure IDs are within valid range if necessary
        return (int) (Math.random() * 6) + 1;
    }

    public boolean getLastResult() {
        return lastResult;
    }
}
