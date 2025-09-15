package edu.toronto.cs.jackpine.benchmark.db.threed;

import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;

public class JDBCDataLoader6 {
    private static final String DB2_URL = "jdbc:db2://localhost:50000/testdb";
    private static final String DB2_USER = "db2inst1";
    private static final String DB2_PASS = "db2inst1";
    private static final String FILE_BASE_PATH = "/home/w3kq9/Desktop/Java-Shapefile-Parser-master/JARs/convertedcsvs/";

    public static void main(String[] args) {
        try {
            Class.forName("com.ibm.db2.jcc.DB2Driver");
            System.out.println("[INIT] DB2 JDBC Driver registered successfully!");
            
            String testFilePath = FILE_BASE_PATH + "output_riverside0.csv";
            System.out.println("[FILE] Processing file: " + testFilePath);
            
            loadSingleBuildingRecord(testFilePath);
            
        } catch (ClassNotFoundException e) {
            System.out.println("[ERROR] DB2 JDBC Driver not found: " + e.getMessage());
        }
    }

    private static void loadSingleBuildingRecord(String filePath) {
        String createTableSQL = 
            "CREATE TABLE buildings_3d (" +
            "fid INTEGER GENERATED ALWAYS AS IDENTITY, " +
            "geometry DB2GSE.ST_GEOMETRY, " +
            "PRIMARY KEY (fid))";
        String insertSQL = 
            "INSERT INTO buildings_3d (geometry) " +
            "VALUES (DB2GSE.ST_GeomFromText(?, 4326))";

        try (Connection conn = DriverManager.getConnection(DB2_URL, DB2_USER, DB2_PASS)) {
            System.out.println("[DB] Connected to database successfully!");

            // Table setup
            setupTable(conn, createTableSQL);

            // Read CSV data
            List<String[]> buildingsData = fetchBuildingsFromCSV(filePath);
            if (buildingsData.isEmpty()) {
                System.out.println("[DATA] No valid data found in file");
                return;
            }

            // Process first record
            String[] record = buildingsData.get(0);
            System.out.println("[PARSE] Processing first record with raw geometry: " + record[0]);
            String wkt = transformSdoGeometry(record[0].trim());
            System.out.println("[TRANSFORM] Transformed WKT: " + wkt);

            if (wkt != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                    pstmt.setString(1, wkt);
                    
                    // Print the final SQL with actual values
                    String finalSQL = insertSQL.replace("?", "'" + wkt + "'");
                    System.out.println("[SQL] Final SQL statement: " + finalSQL);
                    
                    int result = pstmt.executeUpdate();
                    System.out.println("[INSERT] Successfully inserted record. Rows affected: " + result);
                }
            }

        } catch (SQLException e) {
            System.out.println("[ERROR] Database error: " + e.getMessage());
        }
    }


    private static void setupTable(Connection conn, String createTableSQL) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            try {
                stmt.execute(createTableSQL);
                System.out.println("[TABLE] Created new table successfully!");
            } catch (SQLException e) {
                if (e.getErrorCode() != -601) {
                    throw e;
                }
                System.out.println("[TABLE] Table exists, proceeding with truncate");
            }
            stmt.execute("TRUNCATE TABLE buildings_3d IMMEDIATE");
            System.out.println("[TABLE] Table truncated successfully!");
        }
    }

    private static List<String[]> fetchBuildingsFromCSV(String filePath) {
        List<String[]> data = new ArrayList<>();
        System.out.println("[CSV] Starting CSV parsing from: " + filePath);

        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            String header = br.readLine();
            System.out.println("[CSV] Header found: " + header);

            String line = br.readLine();
            if (line != null) {
                System.out.println("[CSV] First data line found: " + line);
                String[] parts = parseCSVLine(line);
                if (parts != null) {
                    data.add(parts);
                    System.out.println("[CSV] Successfully parsed first record");
                }
            }

        } catch (IOException e) {
            System.out.println("[ERROR] CSV reading error: " + e.getMessage());
        }

        return data;
    }

    private static String[] parseCSVLine(String line) {
        List<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        
        System.out.println("[PARSE] Starting line parsing");
        
        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                parts.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        parts.add(current.toString());

        if (parts.size() >= 2) {
            String geom = parts.get(0).replace("\"", "").trim();
            String fid = parts.get(1).trim();
            System.out.println("[PARSE] Extracted geometry and FID: " + fid);
            return new String[]{geom, fid};
        }
        
        return null;
    }

    private static String transformSdoGeometry(String sdoGeometry) {
        System.out.println("[TRANSFORM] Starting SDO_GEOMETRY transformation");
        
        // Split the coordinates from the SDO_GEOMETRY string
        String[] parts = sdoGeometry.split("SDO_ORDINATE_ARRAY\\(");
        String coordinates = parts[1].substring(0, parts[1].length() - 2);
        String[] coords = coordinates.split(",");
        
        StringBuilder wkt = new StringBuilder("MULTIPOLYGON Z (((");
        
        // Base polygon - ensure it closes by repeating first point
        String firstX = coords[0].trim();
        String firstY = coords[1].trim();
        String firstZ = coords[2].trim();
        
        // Loop through coordinates and build the polygon
        for (int i = 0; i < 21; i += 3) {
            if (i > 0) wkt.append(", ");
            wkt.append(coords[i].trim())
               .append(" ")
               .append(coords[i + 1].trim())
               .append(" ")
               .append(coords[i + 2].trim());
        }
        // Close the base polygon by repeating the first point
        wkt.append(", ").append(firstX).append(" ").append(firstY).append(" ").append(firstZ);
        
        wkt.append("), (");
        
        // Top polygon - ensure it closes by repeating first point
        String topFirstX = coords[24].trim();
        String topFirstY = coords[25].trim();
        String topFirstZ = coords[26].trim();
        
        // Loop through the top coordinates and build the top polygon
        for (int i = 24; i < 45; i += 3) {
            if (i > 24) wkt.append(", ");
            wkt.append(coords[i].trim())
               .append(" ")
               .append(coords[i + 1].trim())
               .append(" ")
               .append(coords[i + 2].trim());
        }
        // Close the top polygon by repeating the first point
        wkt.append(", ").append(topFirstX).append(" ").append(topFirstY).append(" ").append(topFirstZ);
        
        wkt.append(")))");
        
        System.out.println("[TRANSFORM] Generated closed polygons");
        return wkt.toString();
    }



}

