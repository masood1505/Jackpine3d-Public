package edu.toronto.cs.jackpine.benchmark.scenarios.threed;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
import org.apache.log4j.Logger;
import edu.toronto.cs.jackpine.benchmark.db.SpatialSqlDialect;
import edu.toronto.cs.jackpine.benchmark.scenarios.SpatialScenarioBase;

public class ReverseGeocoding3DScenario extends SpatialScenarioBase {
    private static final Logger logger = Logger.getLogger(ReverseGeocoding3DScenario.class);
    
    protected PreparedStatement[] pstmtArray;
    
    double mLon = -97.74449;
    double mLat = 30.413524;
    double mAlt = 100.0; // Example altitude
    Random rnd = new Random();
    
    SpatialSqlDialect dialect = null;
    
    @Override
    public void prepare() throws Exception {
        dialect = helper.getSpatialSqlDialect();
        
        pstmtArray = new PreparedStatement[2];
        
        String sql = dialect.getCityStateForReverseGeocoding3D();
        pstmtArray[0] = conn.prepareStatement(sql);
        
        sql = dialect.getStreetAddressForReverseGeocoding3D();
        pstmtArray[1] = conn.prepareStatement(sql);
    }
    
    @Override
    public void iterate(long iterationCount) throws Exception {
        double OFFSET = 0.01;
        double lon = mLon + rnd.nextDouble()/100;
        double lat = mLat + rnd.nextDouble()/100;
        double alt = mAlt + rnd.nextDouble()*10; // Random altitude variation
        
        String box3D = dialect.getBox3DString(
            lon - OFFSET, lat - OFFSET, alt - OFFSET,
            lon + OFFSET, lat + OFFSET, alt + OFFSET
        );
        
        PreparedStatement pstmt1 = pstmtArray[0]; // city state
        String point3DParam = dialect.getPoint3DString(lon, lat, alt);
        pstmt1.setString(1, point3DParam);
        
        PreparedStatement pstmt2 = pstmtArray[1]; // Street Address
        pstmt2.setString(1, point3DParam);
        pstmt2.setString(2, box3D);
        pstmt2.setString(3, point3DParam);
        
        logger.warn(pstmt1.toString());
        logger.warn(pstmt2.toString());
        
        // Execute the queries
        ResultSet rs1 = pstmt1.executeQuery();
        if (rs1.next()) {
            String city = rs1.getString("city");
            String state = rs1.getString("state");
            logger.info("Found city: " + city + ", state: " + state);
        }
        rs1.close();
        
        ResultSet rs2 = pstmt2.executeQuery();
        if (rs2.next()) {
            String streetAddress = rs2.getString("street_address");
            logger.info("Found street address: " + streetAddress);
        }
        rs2.close();
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
