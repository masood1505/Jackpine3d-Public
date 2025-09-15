package edu.toronto.cs.jackpine.benchmark.db.threed;

import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
import oracle.sql.CLOB;

public class JDBCDataLoader2 {
    private static final String ORACLE_URL = "jdbc:oracle:thin:@//localhost:1521/xepdb1";
    private static final String ORACLE_USER = "sys";
    private static final String ORACLE_PASS = "15051505";
    private static final String CREATE_TABLE_SQL = 
        "CREATE TABLE arealm3d (" +
        "id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
        "geometry SDO_GEOMETRY)";
    private static final String INSERT_SQL = 
        "INSERT INTO arealm3d (geometry) " +
        "VALUES (SDO_UTIL.FROM_WKTGEOMETRY(TO_CLOB(?)))";

    public static void main(String[] args) {
        String filePath = "/home/w3kq9/Desktop/Java-Shapefile-Parser-master/JARs/convertedcsvs/tigerlandmark3d.csv";
        loadLandmarkData(filePath);
    }

    private static String normalizeWKT(String wkt) {
        return wkt.trim()
                 .replaceAll("\\s+", " ")
                 .replaceAll("MULTIPOLYGON Z", "MULTIPOLYGON")
                 .replaceAll("\\(\\s+", "(")
                 .replaceAll("\\s+\\)", ")")
                 .replaceAll(",\\s+", ",");
    }

    private static void setWKTParameter(PreparedStatement pstmt, String wkt, int paramIndex) 
            throws SQLException {
        if (wkt.length() > 4000) {
            CLOB clob = CLOB.createTemporary(
                pstmt.getConnection(), true, CLOB.DURATION_SESSION);
            clob.putString(1, wkt);
            pstmt.setClob(paramIndex, clob);
        } else {
            pstmt.setString(paramIndex, wkt);
        }
    }

    private static void insertSpatialMetadata(Connection conn) throws SQLException {
        String deleteMetadata = 
            "DELETE FROM user_sdo_geom_metadata WHERE table_name = 'AREALM3D' AND column_name = 'GEOMETRY'";
            
        String insertMetadata = 
            "INSERT INTO user_sdo_geom_metadata VALUES (" +
            "'AREALM3D', 'GEOMETRY', " +
            "MDSYS.SDO_DIM_ARRAY(" +
            "MDSYS.SDO_DIM_ELEMENT('X', -180, 180, 0.005), " +
            "MDSYS.SDO_DIM_ELEMENT('Y', -90, 90, 0.005), " +
            "MDSYS.SDO_DIM_ELEMENT('Z', -1000, 10000, 0.005)), " +
            "4326)";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(deleteMetadata);
            stmt.execute(insertMetadata);
        }
    }

    private static List<String> fetchGeometriesFromCSV(String filePath) {
        List<String> data = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            br.readLine(); // Skip header
            
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                if (line.contains("MULTIPOLYGON Z")) {
                    String geometry = line.replaceAll("^\"|\"$", "").trim();
                    data.add(geometry);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
        return data;
    }

    private static void setupTable(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("GRANT INHERIT PRIVILEGES ON USER SYS TO MDSYS");
            
            try {
                stmt.execute("DROP TABLE arealm3d PURGE");
                stmt.execute("DELETE FROM user_sdo_geom_metadata WHERE table_name = 'AREALM3D'");
            } catch (SQLException e) {
                // Table might not exist
            }
            
            stmt.execute(CREATE_TABLE_SQL);
            insertSpatialMetadata(conn);
        }
    }

    private static void executeBatchWithRetry(PreparedStatement pstmt, Connection conn) 
            throws SQLException {
        try {
            pstmt.executeBatch();
            conn.commit();
            pstmt.clearBatch();
        } catch (BatchUpdateException e) {
            conn.rollback();
            System.err.println("Batch execution failed: " + e.getMessage());
            throw e;
        }
    }

    private static void loadLandmarkData(String filePath) {
        Properties props = new Properties();
        props.put("user", ORACLE_USER);
        props.put("password", ORACLE_PASS);
        props.put("internal_logon", "sysdba");

        try (Connection conn = DriverManager.getConnection(ORACLE_URL, props)) {
            setupTable(conn);
            
            List<String> geometries = fetchGeometriesFromCSV(filePath);
            System.out.println("Total geometries found: " + geometries.size());

            try (PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL)) {
                conn.setAutoCommit(false);
                int count = 0;
                int batchSize = 25;

                for (String geometry : geometries) {
                    try {
                        String normalizedWKT = normalizeWKT(geometry);
                        setWKTParameter(pstmt, normalizedWKT, 1);
                        pstmt.addBatch();
                        count++;
                        
                        if (count % batchSize == 0) {
                            executeBatchWithRetry(pstmt, conn);
                            System.out.println("Inserted " + count + " records...");
                        }
                    } catch (SQLException e) {
                        System.err.println("Error processing geometry: " + geometry);
                        e.printStackTrace();
                        conn.rollback();
                    }
                }

                if (count % batchSize != 0) {
                    executeBatchWithRetry(pstmt, conn);
                }
                System.out.println("Total records loaded: " + count);
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
