package edu.toronto.cs.jackpine.benchmark.db.threed;

import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;

public class JDBCDataLoader5 {
    private static final String DB2_URL = "jdbc:db2://localhost:50000/testdb";
    private static final String DB2_USER = "db2inst1";
    private static final String DB2_PASS = "db2inst1";
    private static final int SRID = 4326;
    private static final int BATCH_SIZE = 100;
    
    private static final String CREATE_TABLE_SQL = 
        "CREATE TABLE arealm3d (" +
        "id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
        "geometry DB2GSE.ST_GEOMETRY)";
        
    private static final String INSERT_SQL = 
        "INSERT INTO arealm3d (geometry) " +
        "VALUES (DB2GSE.ST_GeomFromText(?, ?))";

    private static class GeometryRecord {
        final int lineNumber;
        final String wkt;
        
        GeometryRecord(int lineNumber, String wkt) {
            this.lineNumber = lineNumber;
            this.wkt = wkt;
        }
    }

    public static void main(String[] args) {
        String filePath = "/home/w3kq9/Desktop/Java-Shapefile-Parser-master/JARs/convertedcsvs/tiger_landmarks_3d.csv";
        loadLandmarkData(filePath);
    }

    private static void setupTable(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            try {
                stmt.execute("DROP TABLE arealm3d");
            } catch (SQLException e) {
                // Table might not exist - continue
            }
            stmt.execute(CREATE_TABLE_SQL);
        }
    }

    private static List<GeometryRecord> fetchGeometriesFromCSV(String filePath) {
        List<GeometryRecord> geometries = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            int lineNumber = 0;
            br.readLine(); // Skip header
            
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;
                
                if (line.contains("MULTIPOLYGON")) {
                    geometries.add(new GeometryRecord(lineNumber, line));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            e.printStackTrace();
        }
        return geometries;
    }

    private static String cleanGeometry(String wkt) {
        try {
            // Pre-process the WKT to handle parenthesized negative coordinates
            wkt = wkt.replaceAll("\\((-\\d+\\.\\d+)\\)", "$1")
                     .replaceAll("\\s+", " ")
                     .trim()
                     .replaceAll("^\"|\"$", "");
            
            int startIdx = wkt.indexOf("(((");
            int endIdx = wkt.lastIndexOf(")))");
            
            String coordsPart = wkt.substring(startIdx + 3, endIdx);
            String[] coords = coordsPart.split(",");
            List<String> cleanedCoords = new ArrayList<>();
            
            for (String coord : coords) {
                coord = coord.trim();
                String[] parts = coord.split("\\s+");
                
                // Direct coordinate parsing after pre-processing
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                
                cleanedCoords.add(String.format(Locale.US, "%.6f %.6f %.6f", x, y, 0.0));
            }
            
            return "MULTIPOLYGON (((" + String.join(", ", cleanedCoords) + ")))";
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to clean geometry: " + e.getMessage());
        }
    }


    private static boolean validateGeometry(String wkt, int lineNumber) {
        try {
            if (!wkt.startsWith("MULTIPOLYGON (((") || !wkt.endsWith(")))")) {
                System.out.println("Line " + lineNumber + ": Invalid MULTIPOLYGON format");
                return false;
            }
            
            String coordsPart = wkt.substring(15, wkt.length() - 3);
            String[] coordinates = coordsPart.split(",");
            
            if (coordinates.length < 4) {
                System.out.println("Line " + lineNumber + ": Not enough coordinates (minimum 4 required)");
                return false;
            }
            
            for (String coord : coordinates) {
                coord = coord.trim();
                String[] parts = coord.split("\\s+");
                
                if (parts.length != 3) {
                    System.out.println("Line " + lineNumber + ": Invalid coordinate format: " + coord);
                    return false;
                }
                
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                double z = Double.parseDouble(parts[2]);
                
                if (Math.abs(x) > 180 || Math.abs(y) > 90) {
                    System.out.println("Line " + lineNumber + ": Coordinates out of bounds: " + coord);
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("Line " + lineNumber + ": Validation error: " + e.getMessage());
            return false;
        }
    }

    private static void loadLandmarkData(String filePath) {
        try (Connection conn = DriverManager.getConnection(DB2_URL, DB2_USER, DB2_PASS)) {
            setupTable(conn);
            conn.setAutoCommit(false);
            
            List<GeometryRecord> geometries = fetchGeometriesFromCSV(filePath);
            System.out.println("Processing " + geometries.size() + " geometries...");
            
            try (PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL)) {
                int successCount = 0;
                int failCount = 0;
                int batchCount = 0;
                
                for (GeometryRecord record : geometries) {
                    try {
                        String cleanedWKT = cleanGeometry(record.wkt);
                        
                        if (validateGeometry(cleanedWKT, record.lineNumber)) {
                            pstmt.setString(1, cleanedWKT);
                            pstmt.setInt(2, SRID);
                            pstmt.addBatch();
                            batchCount++;
                            
                            if (batchCount >= BATCH_SIZE) {
                                executeBatch(pstmt, conn);
                                successCount += batchCount;
                                batchCount = 0;
                                System.out.println("Processed " + successCount + " geometries");
                            }
                        } else {
                            failCount++;
                            if (failCount <= 5) {
                                System.out.println("Original WKT: " + record.wkt);
                                System.out.println("Cleaned WKT: " + cleanedWKT);
                            }
                        }
                    } catch (SQLException e) {
                        conn.rollback();
                        failCount++;
                        System.out.println("Line " + record.lineNumber + " SQL error: " + e.getMessage());
                    } catch (IllegalArgumentException e) {
                        failCount++;
                        System.out.println("Line " + record.lineNumber + " cleaning error: " + e.getMessage());
                    }
                }
                
                if (batchCount > 0) {
                    executeBatch(pstmt, conn);
                    successCount += batchCount;
                }
                
                System.out.println("\nFinal Results:");
                System.out.println("Successfully loaded: " + successCount + " geometries");
                System.out.println("Failed to load: " + failCount + " geometries");
                System.out.println("Total processed: " + (successCount + failCount) + " geometries");
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void executeBatch(PreparedStatement pstmt, Connection conn) throws SQLException {
        try {
            pstmt.executeBatch();
            conn.commit();
            pstmt.clearBatch();
        } catch (BatchUpdateException e) {
            conn.rollback();
            throw e;
        }
    }
}
