package edu.toronto.cs.jackpine.benchmark.scenarios.threed;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import edu.toronto.cs.jackpine.benchmark.db.SpatialSqlDialect;
import edu.toronto.cs.jackpine.benchmark.scenarios.SpatialScenarioBase;

/**
 * Class for benchmarking the 3D length of geometries.
 * Computes the length of a 3D geometry using ST_3DLength.
 * 
 * Developer: S. Ray
 * Contributor(s): 
 */
public class Length3D extends SpatialScenarioBase {
    private static final Logger logger = Logger.getLogger(Length3D.class);
    private PreparedStatement[] pstmtArray;

    /** Create a prepared statement array. */
    @Override
    public void prepare() throws Exception {
        SpatialSqlDialect dialect = helper.getSpatialSqlDialect();
        
        pstmtArray = new PreparedStatement[1];
        String sql = "SELECT ST_3DLength(geom1) AS length_3d FROM geometries";
        pstmtArray[0] = conn.prepareStatement(sql);
    }

    /** Execute an iteration. */
    @Override
    public void iterate(long iterationCount) throws Exception {
        // Pick a prepared statement at random to use.
        int index = (int) (Math.random() * pstmtArray.length);
        PreparedStatement pstmt = pstmtArray[index];

        // Execute the query and log the result.
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            logger.info("3D Length: " + rs.getDouble("length_3d"));
        }
        rs.close();
    }

    /** Clean up resources used by scenario. */
    @Override
    public void cleanup() throws Exception {
        // Clean up prepared statements and connections.
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