package edu.toronto.cs.jackpine.benchmark.scenarios.threed;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.log4j.Logger;

import edu.toronto.cs.jackpine.benchmark.db.SpatialSqlDialect;
import edu.toronto.cs.jackpine.benchmark.scenarios.SpatialScenarioBase;

public class VerySimpleBenchmark extends SpatialScenarioBase {
	 private static final Logger logger = Logger.getLogger(VerySimpleBenchmark.class);
	    private PreparedStatement pstmt;

	    public void prepare() throws Exception {
	        String sql = "SELECT 1";
	        pstmt = conn.prepareStatement(sql);
	    }

	    public void iterate(long iterationCount) throws Exception {
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            logger.info("Iteration " + iterationCount + ": Query executed successfully: " + rs.getInt(1));
	        }
	        rs.close();
	    }

	    public void cleanup() throws Exception {
	        if (pstmt != null) pstmt.close();
	    }
}