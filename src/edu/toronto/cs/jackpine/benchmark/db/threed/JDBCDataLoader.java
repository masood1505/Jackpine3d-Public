package edu.toronto.cs.jackpine.benchmark.db.threed;

import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;

public class JDBCDataLoader {
    private static final String ORACLE_URL = "jdbc:oracle:thin:@//localhost:1521/xepdb1";
    private static final String ORACLE_USER = "sys";
    private static final String ORACLE_PASS = "15051505";
    private static final int SRID = 4326;

    private static boolean isValidWKT(String wkt) {
        if (!wkt.startsWith("MULTIPOLYGON")) {
            System.out.println("Invalid format: Must start with MULTIPOLYGON");
            return false;
        }
        
        String[] coords = wkt.split("[(),]");
        for (String coord : coords) {
            coord = coord.trim();
            if (coord.isEmpty() || coord.equals("MULTIPOLYGON")) continue;
            
            String[] values = coord.split("\\s+");
            if (values.length == 2) {
                try {
                    double lon = Double.parseDouble(values[0]);
                    double lat = Double.parseDouble(values[1]);
                    
                    if (lon < -180 || lon > 180 || lat < -90 || lat > 90) {
                        System.out.println("Invalid coordinate range: " + lon + " " + lat);
                        return false;
                    }
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        String roadsFilePath = "/home/w3kq9/Desktop/Java-Shapefile-Parser-master/JARs/convertedcsvs/outputroads3d.csv";
        loadRoadsData(roadsFilePath);
    }

    private static List<String[]> fetchRoadsFromCSV(String filePath) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            br.readLine(); // Skip header line
            
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                int geomEnd = line.lastIndexOf(")))");
                if (geomEnd > 0) {
                    String geom = line.substring(0, geomEnd + 4);
                    String fid = line.substring(line.lastIndexOf(",") + 1).trim();
                    
                    geom = geom.replaceAll("^\"|\"$", "");
                    fid = fid.replaceAll("^\"|\"$", "");
                    
                    if (isValidWKT(geom)) {
                        data.add(new String[]{geom, fid});
                        System.out.println("Valid geometry found for FID: " + fid);
                    } else {
                        System.out.println("Invalid geometry skipped for FID: " + fid);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private static void loadRoadsData(String filePath) {
        String createTableSQL = 
            "CREATE TABLE roads_3d_new (" +
            "fid NUMBER PRIMARY KEY, " +
            "geometry SDO_GEOMETRY)";

        String insertMetadataSQL = 
            "INSERT INTO user_sdo_geom_metadata (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID) " +
            "VALUES ('ROADS_3D_NEW', 'GEOMETRY', " +
            "MDSYS.SDO_DIM_ARRAY(" +
            "MDSYS.SDO_DIM_ELEMENT('X', -180, 180, 0.005), " +
            "MDSYS.SDO_DIM_ELEMENT('Y', -90, 90, 0.005), " +
            "MDSYS.SDO_DIM_ELEMENT('Z', -1000, 10000, 0.005)), " +
            "?)";

        String insertSQL = 
            "INSERT INTO roads_3d_new (fid, geometry) " +
            "VALUES (?, SDO_CS.MAKE_3D(SDO_UTIL.FROM_WKTGEOMETRY(?), " + SRID + "))";

        Properties props = new Properties();
        props.put("user", ORACLE_USER);
        props.put("password", ORACLE_PASS);
        props.put("internal_logon", "sysdba");

        try (Connection conn = DriverManager.getConnection(ORACLE_URL, props)) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("GRANT INHERIT PRIVILEGES ON USER SYS TO MDSYS");
                
                try {
                    stmt.execute("DROP TABLE roads_3d_new PURGE");
                } catch (SQLException e) {
                    // Table might not exist
                }
                
                stmt.execute(createTableSQL);
                stmt.execute("DELETE FROM user_sdo_geom_metadata WHERE table_name = 'ROADS_3D_NEW'");
                
                try (PreparedStatement pstmtMeta = conn.prepareStatement(insertMetadataSQL)) {
                    pstmtMeta.setInt(1, SRID);
                    pstmtMeta.executeUpdate();
                }
            }

            List<String[]> roadsData = fetchRoadsFromCSV(filePath);
            
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                conn.setAutoCommit(false);
                
                for (String[] record : roadsData) {
                    try {
                        String fidString = record[1].trim();
                        if (!fidString.matches("\\d+")) {
                            System.out.println("Invalid FID format: " + fidString);
                            continue;
                        }

                        int fidValue = Integer.parseInt(fidString);
                        String coordinateString = record[0].trim();

                        pstmt.setInt(1, fidValue);
                        pstmt.setString(2, coordinateString);
                        pstmt.executeUpdate();
                        System.out.println("Successfully inserted FID: " + fidValue);
                        
                    } catch (NumberFormatException e) {
                        System.out.println("Number formatting error for FID: " + record[1]);
                    } catch (SQLException e) {
                        System.out.println("Error inserting record with FID " + record[1] + ": " + e.getMessage());
                    }
                }

                conn.commit();
                System.out.println("All data loaded successfully.");
            }

            // No need for the SRID update logic anymore, handled during insert

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}