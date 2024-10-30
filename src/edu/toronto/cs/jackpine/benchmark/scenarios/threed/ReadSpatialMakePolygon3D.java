package edu.toronto.cs.jackpine.benchmark.scenarios.threed;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.log4j.Logger;

import edu.toronto.cs.jackpine.benchmark.db.SpatialSqlDialect;
import edu.toronto.cs.jackpine.benchmark.scenarios.SpatialScenarioBase;

public class ReadSpatialMakePolygon3D extends SpatialScenarioBase {
    private static final Logger logger = Logger.getLogger(ReadSpatialMakePolygon3D.class);
    private PreparedStatement[] pstmtArray;

    @Override
    public void prepare() throws Exception {
        SpatialSqlDialect dialect = helper.getSpatialSqlDialect();

        // Assuming dynamic setting of parameters or prepared statements are managed elsewhere or not needed here
        pstmtArray = new PreparedStatement[1];
        String sql = "SELECT ST_MakePolygon3D(geom) AS polygon FROM geometries WHERE id = ?";
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
                logger.info("3D Polygon: " + rs.getString("polygon"));
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
