package edu.toronto.cs.jackpine.benchmark.scenarios.threed;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.log4j.Logger;

import edu.toronto.cs.jackpine.benchmark.db.SpatialSqlDialect;
import edu.toronto.cs.jackpine.benchmark.scenarios.SpatialScenarioBase;

public class ReadSpatialIs3D extends SpatialScenarioBase {
    private static final Logger logger = Logger.getLogger(ReadSpatialIs3D.class);
    private PreparedStatement[] pstmtArray;

    @Override
    public void prepare() throws Exception {
        SpatialSqlDialect dialect = helper.getSpatialSqlDialect();
        pstmtArray = new PreparedStatement[1];
        String sql = "SELECT ST_Is3D(geom1) AS is_3d FROM geometries WHERE id = ?";
        pstmtArray[0] = conn.prepareStatement(sql);
    }

    @Override
    public void iterate(long iterationCount) throws Exception {
        int index = (int) (Math.random() * pstmtArray.length);
        PreparedStatement pstmt = pstmtArray[index];

        // This could be set dynamically based on external input or data driven approach
        //int geometryId = getCurrentGeometryId(); // You would need to implement this method
        //pstmt.setInt(1, geometryId);

        ResultSet rs = null;
        try {
            rs = pstmt.executeQuery();
            if (rs.next()) {
                logger.info("Is 3D: " + rs.getBoolean("is_3d"));
            }
        } catch (Exception e) {
            logger.error("Error during database query execution: ", e);
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
