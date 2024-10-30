package edu.toronto.cs.jackpine.benchmark.scenarios.threed;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.log4j.Logger;

import edu.toronto.cs.jackpine.benchmark.db.SpatialSqlDialect;
import edu.toronto.cs.jackpine.benchmark.scenarios.SpatialScenarioBase;

public class Distance3D extends SpatialScenarioBase {
    private static final Logger logger = Logger.getLogger(Distance3D.class);
    private PreparedStatement pstmt;

    // Method to prepare the SQL statement
    @Override
    public void prepare(Connection conn) throws Exception {
        SpatialSqlDialect dialect = helper.getSpatialSqlDialect();
        String sql = "SELECT ST_3DDistance(geom1, geom2) AS distance FROM geometries WHERE id1 = ? AND id2 = ?";
        pstmt = conn.prepareStatement(sql);
    }

    // Method to execute the prepared statement and log the distance
    public void iterate(long iterationCount) throws Exception {
        pstmt.setInt(1, 1); // Example IDs, adjust as needed
        pstmt.setInt(2, 2);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            logger.info("3D Distance: " + rs.getDouble("distance"));
        }
        rs.close();
    }

    // Method to clean up and close resources
    @Override
    public void cleanup() throws Exception {
        try {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null; // Ensuring the reference is dropped after closing
            }
        } finally {
            // No call to super.cleanup() as it's abstract and has no implementation
        }
    }

	@Override
	public void prepare() throws Exception {
		// TODO Auto-generated method stub
		
	}
}
