package edu.toronto.cs.jackpine.benchmark.scenarios.threed;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.log4j.Logger;

import edu.toronto.cs.jackpine.benchmark.db.SpatialSqlDialect;
import edu.toronto.cs.jackpine.benchmark.scenarios.SpatialScenarioBase;

public class ReadSpatialVolume3D extends SpatialScenarioBase {
    private static final Logger logger = Logger.getLogger(ReadSpatialVolume3D.class);
    private PreparedStatement[] pstmtArray;

    @Override
    public void prepare() throws Exception {
        SpatialSqlDialect dialect = helper.getSpatialSqlDialect();

        // Prepare the SQL statement for calculating 3D volume
        pstmtArray = new PreparedStatement[1];
        String sql = "SELECT ST_Volume(geom) AS volume_3d FROM solids WHERE id = ?";
        pstmtArray[0] = conn.prepareStatement(sql);
    }

    @Override
    public void iterate(long iterationCount) throws Exception {
        int index = (int) (Math.random() * pstmtArray.length);
        PreparedStatement pstmt = pstmtArray[index];

        // Assuming ID is dynamically set elsewhere or through some other mechanism
        // If fixed parameters are needed, they can be set here before executing the query.
        // Example:
        // pstmt.setInt(1, 1); // Example ID 1

        ResultSet rs = null;
        try {
            rs = pstmt.executeQuery();
            if (rs.next()) {
                logger.info("3D Volume: " + rs.getDouble("volume_3d"));
            }
        } catch (Exception e) {
            logger.error("Error executing query: ", e);
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    @Override
    public void cleanup() throws Exception {
        try {
            for (PreparedStatement stmt : pstmtArray) {
                if (stmt != null) {
                    stmt.close();
                }
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }
}
