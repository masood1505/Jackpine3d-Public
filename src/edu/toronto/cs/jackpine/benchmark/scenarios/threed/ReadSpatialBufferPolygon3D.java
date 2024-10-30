package edu.toronto.cs.jackpine.benchmark.scenarios.threed;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.log4j.Logger;
import edu.toronto.cs.jackpine.benchmark.db.SpatialSqlDialect;
import edu.toronto.cs.jackpine.benchmark.scenarios.SpatialScenarioBase;

public class ReadSpatialBufferPolygon3D extends SpatialScenarioBase {
    private static final Logger logger = Logger.getLogger(ReadSpatialBufferPolygon3D.class);
    private PreparedStatement[] pstmtArray;

    @Override
    public void prepare() throws Exception {
        SpatialSqlDialect dialect = helper.getSpatialSqlDialect(); 
        pstmtArray = new PreparedStatement[1];
        // Ensure the SQL command and the table name are correctly set up in your database
        String sql = "SELECT ST_AsText(custom_3d_buffer(geom, 10)) AS buffered_geom FROM your_table";
        pstmtArray[0] = conn.prepareStatement(sql);
    }

    @Override
    public void iterate(long iterationCount) throws Exception {
        int index = (int) (Math.random() * pstmtArray.length);
        PreparedStatement pstmt = pstmtArray[index];

        ResultSet rs = null;
        try {
            rs = pstmt.executeQuery();
            while (rs.next()) {
                // Logging each buffered geometry for demonstration; adjust as needed
                logger.info("Buffered Geometry: " + rs.getString("buffered_geom"));
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
