package edu.toronto.cs.jackpine.benchmark.scenarios.threed;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;
import edu.toronto.cs.jackpine.benchmark.db.SpatialSqlDialect;
import edu.toronto.cs.jackpine.benchmark.db.threed.SpatialTableHelper3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.SpatialScenarioBase;

public class ReadSpatialLineCrossesRegion3D extends SpatialScenarioBase {
    private static final Logger logger = Logger.getLogger(ReadSpatialLineCrossesRegion3D.class);
    private PreparedStatement pstmt;
    private boolean lastResult;
    private boolean isPrepared = false;
    
    
    protected SpatialTableHelper3D helper3D;

    public ReadSpatialLineCrossesRegion3D(Properties props) {
        String url = props.getProperty("url");
        String login = props.getProperty("user");
        String password = props.getProperty("password");
        this.helper3D = new SpatialTableHelper3D(url, login, password, props);
    }
    
    
    
    

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

        String sql = "SELECT ST_Crosses(a.geom, b.geom) AS line_crosses_region " +
                     "FROM camb3d_bldg_active_mp a, camb3d_bldg_active_mp b " +
                     "WHERE a.gid = ? AND b.gid = ?";
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
                lastResult = rs.getBoolean("line_crosses_region");
                logger.info("Iteration " + iterationCount + ": 3D Line Crosses Region between " + id1 + " and " + id2 + ": " + lastResult);
            } else {
                logger.warn("No result found for ids " + id1 + " and " + id2);
                lastResult = false;
            }
        } catch (SQLException e) {
            logger.error("Error executing query for ids " + id1 + " and " + id2 + ": ", e);
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
        return (int) (Math.random() * 6) + 1;
    }

    public boolean getLastResult() {
        return lastResult;
    }
}