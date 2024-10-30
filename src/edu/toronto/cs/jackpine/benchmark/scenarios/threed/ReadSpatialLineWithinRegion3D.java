package edu.toronto.cs.jackpine.benchmark.scenarios.threed;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import edu.toronto.cs.jackpine.benchmark.db.threed.SpatialTableHelper3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.SpatialScenarioBase;

public class ReadSpatialLineWithinRegion3D extends SpatialScenarioBase {
    private static final Logger logger = Logger.getLogger(ReadSpatialLineWithinRegion3D.class);
    private PreparedStatement pstmt;
    private boolean isPrepared = false;
    
    protected SpatialTableHelper3D helper3D;

    public ReadSpatialLineWithinRegion3D(Properties props) {
        String url = props.getProperty("url");
        String login = props.getProperty("user");
        String password = props.getProperty("password");
        this.helper3D = new SpatialTableHelper3D(url, login, password, props);
    }
    
    

    @Override
    public void prepare() throws Exception {
        if (conn == null) {
            logger.error("Database connection is null. Ensure initialize() is called before prepare().");
            throw new IllegalStateException("Database connection is null. Ensure initialize() is called before prepare().");
        }

        // Hard-coded query
     //   String sql = "SELECT COUNT(*) FROM camb3d_bldg_active_mp  WHERE ST_3DIntersects(geom1, ST_3DExtent(ST_MakePoint(?, ?, ?), ST_MakePoint(?, ?, ?)))";


        
     //   String sql = "SELECT COUNT(*) FROM camb3d_bldg_active_mp WHERE geom1 && BOX3D(ST_MakePoint(?, ?, ?), ST_MakePoint(?, ?, ?))";

        
        String sql = "SELECT COUNT(*) FROM camb3d_bldg_active_mp WHERE ST_Intersects(geom1, ST_MakeEnvelope(?, ?, ?, ?, 4326))";



        //String sql = "SELECT COUNT(*) FROM camb3d_bldg_active_mp WHERE ST_3DWithin(geom, ST_MakeBox3D(ST_MakePoint(?, ?, ?), ST_MakePoint(?, ?, ?)), ?)";
        
        try {
            pstmt = conn.prepareStatement(sql);
            logger.info("PreparedStatement created successfully: " + sql);
            isPrepared = true;
        } catch (SQLException e) {
            logger.error("Error creating PreparedStatement: ", e);
            throw e;
        }
    }

    @Override
    public void iterate(long iterationCount) throws Exception {
        if (!isPrepared) {
            logger.info("Preparing benchmark before first iteration");
            prepare();
        }

        if (pstmt == null) {
            logger.error("PreparedStatement is null. Ensure prepare() is called before iterate().");
            throw new IllegalStateException("PreparedStatement is null. Ensure prepare() is called before iterate().");
        }

        try {
            // Set parameters for the query
            double minX = getRandomCoordinate();
            double minY = getRandomCoordinate();
            double maxX = minX + getRandomSize();
            double maxY = minY + getRandomSize();

            pstmt.setDouble(1, minX);
            pstmt.setDouble(2, minY);
            pstmt.setDouble(3, maxX);
            pstmt.setDouble(4, maxY);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                logger.info("Iteration " + iterationCount + ": Found " + count + " geometries intersecting the 2D box");
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Error executing query: ", e);
        }
    }

    private double getRandomCoordinate() {
        return Math.random() * 180 - 90; // Range from -90 to 90 for latitude/longitude
    }

    private double getRandomSize() {
        return Math.random() * 10; // Adjust the range as needed
    }

  

    @Override
    public void cleanup() throws Exception {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                logger.error("Error closing PreparedStatement: ", e);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("Error closing Connection: ", e);
            }
        }
        isPrepared = false;
    }


    private double getRandomDistance() {
        return Math.random() * 10; // Adjust the range as needed
    }
}