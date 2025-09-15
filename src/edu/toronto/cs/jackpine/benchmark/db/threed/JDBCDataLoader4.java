package edu.toronto.cs.jackpine.benchmark.db.threed;

import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;

public class JDBCDataLoader4 {
    private static final String DB2_URL = "jdbc:db2://localhost:50000/testdb";
    private static final String DB2_USER = "db2inst1";
    private static final String DB2_PASS = "db2inst1";
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
        try {
            Class.forName("com.ibm.db2.jcc.DB2Driver");
            System.out.println("DB2 JDBC Driver registered successfully!");
        } catch (ClassNotFoundException e) {
            System.out.println("DB2 JDBC Driver not found. Include it in your library path!");
            return;
        }
        
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
            "fid INTEGER GENERATED ALWAYS AS IDENTITY, " +
            "geometry DB2GSE.ST_GEOMETRY, " +
            "PRIMARY KEY (fid))";

        String insertSQL = 
            "INSERT INTO roads_3d_new (geometry) " +
            "VALUES (DB2GSE.ST_GeomFromText(?, " + SRID + "))";

        try (Connection conn = DriverManager.getConnection(DB2_URL, DB2_USER, DB2_PASS)) {
            System.out.println("Connected to database successfully!");
            
            try (Statement stmt = conn.createStatement()) {
                try {
                    stmt.execute("DROP TABLE roads_3d_new");
                    System.out.println("Existing table dropped successfully!");
                } catch (SQLException e) {
                    System.out.println("Table does not exist, creating new one.");
                }
                
                stmt.execute(createTableSQL);
                System.out.println("Table created successfully!");
            }

            List<String[]> roadsData = fetchRoadsFromCSV(filePath);
            System.out.println("CSV data loaded, starting database insertion...");
            
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                conn.setAutoCommit(false);
                int batchCount = 0;
                
                for (String[] record : roadsData) {
                    try {
                        String coordinateString = record[0].trim();
                        pstmt.setString(1, coordinateString);
                        pstmt.executeUpdate();
                        
                        if (++batchCount % 100 == 0) {
                            conn.commit();
                            System.out.println("Committed batch of " + batchCount + " records");
                        }
                        
                    } catch (SQLException e) {
                        System.out.println("Error inserting record: " + e.getMessage());
                    }
                }

                conn.commit();
                System.out.println("All data loaded successfully! Total records: " + batchCount);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
