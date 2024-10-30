package edu.toronto.cs.jackpine.benchmark.scenarios.threed;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import edu.toronto.cs.jackpine.benchmark.db.SpatialSqlDialect;
import edu.toronto.cs.jackpine.benchmark.scenarios.SpatialScenarioBase;

public class ReadSpatialMaxDistance3D extends SpatialScenarioBase {
    private static final Logger logger = Logger.getLogger(ReadSpatialMaxDistance3D.class);
    private PreparedStatement pstmt;
    private double lastDistance;
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

        String sql = "SELECT ST_3DMaxDistance(geom1, geom2) AS max_distance FROM geometries WHERE id1 = ? AND id2 = ?";
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

        int id1 = getRandomId();
        int id2 = getRandomId();

        ResultSet rs = null;
        try {
            pstmt.setInt(1, id1);
            pstmt.setInt(2, id2);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                lastDistance = rs.getDouble("max_distance");
                logger.info("Iteration " + iterationCount + ": 3D Max Distance between " + id1 + " and " + id2 + ": " + lastDistance);
            } else {
                logger.warn("No result found for ids " + id1 + " and " + id2);
                lastDistance = -1;
            }
        } catch (SQLException e) {
            logger.error("Error executing query for ids " + id1 + " and " + id2 + ": ", e);
            lastDistance = -1;
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
        return (int) (Math.random() * 6) + 1;
    }

    public double getDistance() {
        return lastDistance;
    }
}