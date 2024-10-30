package edu.toronto.cs.jackpine.benchmark.scenarios.threed;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.log4j.Logger;

import edu.toronto.cs.jackpine.benchmark.db.SpatialSqlDialect;
import edu.toronto.cs.jackpine.benchmark.scenarios.SpatialScenarioBase;

public class FullyWithin3D extends SpatialScenarioBase {
    private static final Logger logger = Logger.getLogger(FullyWithin3D.class);
    private PreparedStatement[] pstmtArray;

    @Override
    public void prepare() throws Exception {
        SpatialSqlDialect dialect = helper.getSpatialSqlDialect();

        pstmtArray = new PreparedStatement[1];
        String sql = "SELECT ST_3DDFullyWithin(geom1, geom2, ?) AS fully_within FROM geometries WHERE id1 = ? AND id2 = ?";
        pstmtArray[0] = conn.prepareStatement(sql);
    }

    @Override
    public void iterate(long iterationCount) throws Exception {
        int index = (int) (Math.random() * pstmtArray.length);
        PreparedStatement pstmt = pstmtArray[index];

        pstmt.setDouble(1, 0.5); // Example distance parameter, adjust as needed
        pstmt.setInt(2, 1); // Example ID 1, adjust as needed
        pstmt.setInt(3, 2); // Example ID 2, adjust as needed

        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            logger.info("Fully Within Distance (3D): " + rs.getBoolean("fully_within"));
        }
        rs.close();
    }

    @Override
    public void cleanup() throws Exception {
        for (int i = 0; i < pstmtArray.length; i++) {
            if (pstmtArray[i] != null) {
                pstmtArray[i].close();
            }
        }
        if (conn != null) {
            conn.close();
        }
    }
}
