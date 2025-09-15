package edu.toronto.cs.jackpine.benchmark.db.threed;

import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;

public class JDBCDataLoader3 {
    private static final String ORACLE_URL = "jdbc:oracle:thin:@//localhost:1521/xepdb1";
    private static final String ORACLE_USER = "sys";
    private static final String ORACLE_PASS = "15051505";
    private static final String FILE_BASE_PATH = "/home/w3kq9/Desktop/Java-Shapefile-Parser-master/JARs/convertedcsvs/newfixed/";

    public static void main(String[] args) {
        setupDatabase();

        // Process multiple files: output_riverside0.csv to output_riverside19.csv
        
        for (int i = 0; i < 20; i++) {
            String filePath = FILE_BASE_PATH + "output_riverside" + i + ".csv";
            System.out.println("Processing file: " + filePath);
            loadBuildingData(filePath);
        }
        
        /*for (int i = 0; i < 1; i++) {
            String filePath = FILE_BASE_PATH + "output_riverside0(6)" + ".csv";
            System.out.println("Processing file: " + filePath);
            loadBuildingData(filePath);
        }*/
    }

    private static void setupDatabase() {
        String createTableSQL = 
                "CREATE TABLE buildings3d (" +
                "id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                "geometry SDO_GEOMETRY, " +
                "gml_id VARCHAR2(100), " +
                "hash VARCHAR2(100), " +
                "ubid VARCHAR2(100), " +
                "state VARCHAR2(100), " +
                "county VARCHAR2(20))";

        Properties props = new Properties();
        props.put("user", ORACLE_USER);
        props.put("password", ORACLE_PASS);
        props.put("internal_logon", "sysdba");

        try (Connection conn = DriverManager.getConnection(ORACLE_URL, props);
             Statement stmt = conn.createStatement()) {
            
            if (tableExists(stmt, "buildings3d")) {
                stmt.execute("DROP TABLE buildings3d PURGE");
                System.out.println("Existing table dropped successfully");
            }
            stmt.execute(createTableSQL);
            System.out.println("New table created successfully");

            String insertMetadata = "INSERT INTO USER_SDO_GEOM_METADATA VALUES ('BUILDINGS3D', 'GEOMETRY', " +
                "MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X', -180, 180, 0.005), " +
                "MDSYS.SDO_DIM_ELEMENT('Y', -90, 90, 0.005), " +
                "MDSYS.SDO_DIM_ELEMENT('Z', -1000, 10000, 0.005)), " +
                "4326)";
            stmt.execute(insertMetadata);
            System.out.println("Spatial metadata inserted successfully");

            stmt.execute("CREATE INDEX buildings3d_spatial_idx ON buildings3d(geometry) " +
                       "INDEXTYPE IS MDSYS.SPATIAL_INDEX_V2 PARAMETERS ('sdo_indx_dims=3')");
            System.out.println("Spatial index created successfully");

        } catch (SQLException e) {
            System.err.println("Error setting up database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean tableExists(Statement stmt, String tableName) {
        String query = "SELECT COUNT(*) FROM user_tables WHERE table_name = '" + tableName.toUpperCase() + "'";
        try (ResultSet rs = stmt.executeQuery(query)) {
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error checking if table exists: " + e.getMessage());
        }
        return false;
    }

    private static void loadBuildingData(String filePath) {
        String insertSQL = "INSERT INTO buildings3d (geometry, gml_id, hash, ubid, state, county) " +
                          "VALUES (?, ?, ?, ?, ?, ?)";

        Properties props = new Properties();
        props.put("user", ORACLE_USER);
        props.put("password", ORACLE_PASS);
        props.put("internal_logon", "sysdba");

        try (Connection conn = DriverManager.getConnection(ORACLE_URL, props);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            conn.setAutoCommit(false);

            try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
                br.readLine(); // Skip header
                int count = 0;
                String line;

                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) continue;

                    String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                    
                    if (parts.length >= 6) {
                        try {
                            // Parse SDO geometry directly
                            String sdo = parts[0].replaceAll("^\"|\"$", "");
                            
                            // Create struct for SDO_GEOMETRY
                            try (Statement stmt = conn.createStatement()) {
                                try (ResultSet rs = stmt.executeQuery(
                                    "SELECT " + sdo + " FROM DUAL")) {
                                    if (rs.next()) {
                                        Struct geomStruct = (Struct) rs.getObject(1);
                                        pstmt.setObject(1, geomStruct);
                                    }
                                }
                            }
                            
                            pstmt.setString(2, parts[1].replaceAll("^\"|\"$", ""));
                            pstmt.setString(3, parts[2].replaceAll("^\"|\"$", ""));
                            pstmt.setString(4, parts[3].replaceAll("^\"|\"$", ""));
                            pstmt.setString(5, parts[4].replaceAll("^\"|\"$", ""));
                            pstmt.setString(6, parts[5].replaceAll("^\"|\"$", ""));
                            
                            pstmt.executeUpdate();
                            count++;
                            
                            if (count % 1000 == 0) {
                                conn.commit();
                                System.out.println("Processed " + count + " records");
                            }
                        } catch (SQLException e) {
                            System.err.println("Error inserting record at line " + (count + 2));
                            System.err.println("Error details: " + e.getMessage());
                            continue;
                        }
                    }
                }
                conn.commit();
                System.out.println("Total records loaded: " + count);
            }
        } catch (IOException | SQLException e) {
            System.err.println("Error loading data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
