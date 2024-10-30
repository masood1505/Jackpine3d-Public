package edu.toronto.cs.jackpine.benchmark.scenarios.threed;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.log4j.Logger;

import edu.toronto.cs.jackpine.benchmark.db.SpatialSqlDialect;
import edu.toronto.cs.jackpine.benchmark.scenarios.SpatialScenarioBase;

public class Buffer3D extends SpatialScenarioBase {
    private static final Logger logger = Logger.getLogger(Buffer3D.class);
    private PreparedStatement[] pstmtArray;

    @Override
    public void prepare() throws Exception {
        SpatialSqlDialect dialect = helper.getSpatialSqlDialect();

        pstmtArray = new PreparedStatement[1];
        String sql = "SELECT ST_AsText(ST_3DBuffer(geom1, ?)) AS buffer FROM geometries WHERE id1 = ?";
        pstmtArray[0] = conn.prepareStatement(sql);
    }

    @Override
    public void iterate(long iterationCount) throws Exception {
        int index = (int) (Math.random() * pstmtArray.length);
        PreparedStatement pstmt = pstmtArray[index];

        pstmt.setDouble(1, 10.0); // Example distance parameter, adjust as needed
        pstmt.setInt(2, 1); // Example ID, adjust as needed

        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            logger.info("3D Buffer: " + rs.getString("buffer"));
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
