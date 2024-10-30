package edu.toronto.cs.jackpine.benchmark.scenarios.threed;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.log4j.Logger;
import edu.toronto.cs.jackpine.benchmark.db.threed.SpatialSqlDialect3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.macroscenario.edu.toronto.cs.jackpine.benchmark.scenarios.macroscenario.threed.Address3DPicker;

public class Geocoding3DMacroScenario extends ScenarioBase3D
{
  private static final Logger logger = Logger.getLogger(Geocoding3DMacroScenario.class);
  protected PreparedStatement[] pstmtArray;
  
  protected Address3DPicker addressPicker = null;
  
  public void prepare() throws Exception
  {
      SpatialSqlDialect3D dialect3D = (SpatialSqlDialect3D) helper.getSqlDialect(); // Assume this method exists in your helper class
      
      pstmtArray = new PreparedStatement[1];
        
      String sql = dialect3D.getGeocodingQuery3D();
      pstmtArray[0] = conn.prepareStatement(sql);
      
      addressPicker = new Address3DPicker();
  }

  public void iterate(long iterationCount) throws Exception
  {
      PreparedStatement pstmt = pstmtArray[0]; 
      Address3DPicker[] addresses = addressPicker.getAllAddresses();
      for (int i = 0; i < addresses.length; i++) {
      
          Address3DPicker address = addresses[i];
          pstmt.setString(1, address.getRoadName());
          
          pstmt.setInt(2, address.getRoadNumber());
          pstmt.setInt(3, address.getRoadNumber());
          pstmt.setInt(4, address.getRoadNumber());
          pstmt.setInt(5, address.getRoadNumber());
          pstmt.setInt(6, address.getRoadNumber());
          pstmt.setInt(7, address.getRoadNumber());
          pstmt.setInt(8, address.getRoadNumber());
          pstmt.setInt(9, address.getRoadNumber());
    
          pstmt.setString(10, address.getZipcode());
          pstmt.setString(11, address.getZipcode());
          
          // 3D-specific parameters
          pstmt.setDouble(12, address.getElevation());
          pstmt.setDouble(13, address.getBuildingHeight());
          pstmt.setInt(14, address.getNumFloors());
          pstmt.setString(15, address.getBuildingType());
            
          if (i == 0) {
              System.out.println(pstmt.toString());
              logger.warn(pstmt.toString());
          }
          // Execute the query
          ResultSet rs = pstmt.executeQuery();
          // Process the result set as needed
          while (rs.next()) {
              // Handle the results
          }
          rs.close();
      }
      logger.warn(addresses.length + " 3D addresses resolved");
  }

  public void cleanup() throws Exception
  {
    for (int i = 0; i < pstmtArray.length; i++)
      pstmtArray[i].close();
    if (conn != null)
      conn.close();
  }
}