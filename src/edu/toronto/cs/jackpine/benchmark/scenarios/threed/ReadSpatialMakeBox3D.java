package edu.toronto.cs.jackpine.benchmark.scenarios.threed;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.log4j.Logger;

import edu.toronto.cs.jackpine.benchmark.db.SpatialSqlDialect;
import edu.toronto.cs.jackpine.benchmark.scenarios.SpatialScenarioBase;

public class ReadSpatialMakeBox3D extends SpatialScenarioBase {
    private static final Logger logger = Logger.getLogger(ReadSpatialMakeBox3D.class);
    private PreparedStatement[] pstmtArray;

    @Override
    public void prepare() throws Exception {
        SpatialSqlDialect dialect = helper.getSpatialSqlDialect();

        // Prepare the SQL statement for creating a BOX3D
        pstmtArray = new PreparedStatement[1];
        String sql = "SELECT ST_MakeBox3D(ST_MakePoint(?, ?, ?, ?), ST_MakePoint(?, ?, ?, ?)) AS box3d";
        pstmtArray[0] = conn.prepareStatement(sql);
    }

    @Override
    public void iterate(long iterationCount) throws Exception {
        int index = (int) (Math.random() * pstmtArray.length);
        PreparedStatement pstmt = pstmtArray[index];

        // Assuming coordinates are dynamically set elsewhere or through some other mechanism
        // If fixed parameters are needed, they can be set here before executing the query.
        // Example:
        // pstmt.setDouble(1, 0.0); // X1
        // pstmt.setDouble(2, 0.0); // Y1
        // pstmt.setDouble(3, 0.0); // Z1
        // pstmt.setDouble(4, 0.0); // M1 (optional measure)
        // pstmt.setDouble(5, 1.0); // X2
        // pstmt.setDouble(6, 1.0); // Y2
        // pstmt.setDouble(7, 1.0); // Z2
        // pstmt.setDouble(8, 1.0); // M2 (optional measure)

        ResultSet rs = null;
        try {
            rs = pstmt.executeQuery();
            if (rs.next()) {
                logger.info("3D Box: " + rs.getString("box3d"));
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
