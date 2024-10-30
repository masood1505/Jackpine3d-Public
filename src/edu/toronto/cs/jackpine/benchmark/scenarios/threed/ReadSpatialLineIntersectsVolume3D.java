package edu.toronto.cs.jackpine.benchmark.scenarios.threed;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;
import edu.toronto.cs.jackpine.benchmark.db.threed.SpatialSqlDialect3D;
import edu.toronto.cs.jackpine.benchmark.db.threed.SpatialTableHelper3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.SpatialScenarioBase;

public class ReadSpatialLineIntersectsVolume3D extends SpatialScenarioBase {
    private static final Logger logger = Logger.getLogger(ReadSpatialLineIntersectsVolume3D.class);
    private PreparedStatement pstmt;
    private boolean lastResult;
    private boolean isPrepared = false;
    
    protected SpatialTableHelper3D helper3D;
    private String sql;

    public ReadSpatialLineIntersectsVolume3D(Properties props) {
        String url = props.getProperty("url");
        String login = props.getProperty("user");
        String password = props.getProperty("password");
        this.helper3D = new SpatialTableHelper3D(url, login, password, props);
    }

    @Override
    public void prepare() throws Exception {
        this.conn = helper3D.getConnection();

        if (conn == null) {
            throw new IllegalStateException("Database connection is null. Ensure initialize() is called before prepare().");
        }

        SpatialSqlDialect3D dialect = helper3D.getSpatialSqlDialect3D();
        if (dialect == null) {
            throw new IllegalStateException("SpatialSqlDialect3D is null. Check if it's properly initialized.");
        }

        sql = dialect.getSelectLineIntersectsVolume3D();
        logger.info("SQL Query: " + sql);

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
            throw new IllegalStateException("PreparedStatement is null. Ensure prepare() is called before iterate().");
        }

        ResultSet rs = null;
        try {
            logger.info("Executing query");
            rs = pstmt.executeQuery();

            if (rs.next()) {
                lastResult = rs.getBoolean(1);
                logger.info("Iteration " + iterationCount + ": 3D Line Intersects Volume result: " + lastResult);
            } else {
                logger.warn("No result found");
                lastResult = false;
            }
        } catch (SQLException e) {
            logger.error("Error executing query: ", e);
            throw e;
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