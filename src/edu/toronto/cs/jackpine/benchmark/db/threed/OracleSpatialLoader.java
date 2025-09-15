package edu.toronto.cs.jackpine.benchmark.db.threed;

import java.sql.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import oracle.sql.CLOB;

public class OracleSpatialLoader {
    private static final String ORACLE_URL = "jdbc:oracle:thin:@//localhost:1521/xepdb1";
    private static final String ORACLE_USER = "sys";
    private static final String ORACLE_PASS = "15051505";
    private static final String FILE_PATH = "/home/w3kq9/Desktop/Projects/Jackpine/macro queries/medical/dataset/synthetic cell generated/new3d version/synthetic_cells_3d_with_geom.csv";

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("user", ORACLE_USER);
        props.put("password", ORACLE_PASS);
        props.put("internal_logon", "sysdba");

        try (Connection conn = DriverManager.getConnection(ORACLE_URL, props);
             BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {

            try (Statement stmt = conn.createStatement()) {
                stmt.execute("TRUNCATE TABLE synthetic_cells");
                System.out.println("✅ Table cleared for fresh data load");
            }

            // Create a temporary table to hold the WKT data
            try (Statement stmt = conn.createStatement()) {
                try {
                    stmt.execute("DROP TABLE temp_wkt_data");
                } catch (SQLException e) {
                    // Table might not exist, that's okay
                }
                
                stmt.execute("CREATE TABLE temp_wkt_data (id NUMBER PRIMARY KEY, wkt_data CLOB, " +
                            "cell_type VARCHAR2(50), irregularity NUMBER, rotation NUMBER, height NUMBER)");
            }

            // Insert data into the temporary table
            String insertTempSQL = "INSERT INTO temp_wkt_data (id, wkt_data, cell_type, irregularity, rotation, height) VALUES (?, ?, ?, ?, ?, ?)";
            
            br.readLine(); // Skip header
            String line;
            int recordCount = 0;

            try (PreparedStatement pstmt = conn.prepareStatement(insertTempSQL)) {
                while ((line = br.readLine()) != null) {
                    String[] parts = splitCSVLine(line);
                    String wkt = formatWKT(parts[0]);
                    
                    pstmt.setInt(1, recordCount + 1);
                    
                    // Set the WKT as a CLOB
                    CLOB clob = CLOB.createTemporary(conn, false, CLOB.DURATION_SESSION);
                    Writer writer = clob.setCharacterStream(1);
                    writer.write(wkt);
                    writer.flush();
                    writer.close();
                    pstmt.setClob(2, clob);
                    
                    pstmt.setString(3, parts[1].trim());
                    pstmt.setDouble(4, Double.parseDouble(parts[3].trim()));
                    pstmt.setDouble(5, Double.parseDouble(parts[4].trim()));
                    pstmt.setDouble(6, Double.parseDouble(parts[5].trim()));
                    
                    pstmt.executeUpdate();
                    recordCount++;

                    if (recordCount % 100 == 0) {
                        System.out.println("✅ Processed " + recordCount + " records into temporary table");
                    }
                }
            }
            
            // Insert from temp table into the actual table using Oracle's built-in functions
            try (Statement stmt = conn.createStatement()) {
                String insertFromTempSQL = 
                    "INSERT INTO synthetic_cells (geometry, cell_type, irregularity, rotation, height) " +
                    "SELECT SDO_UTIL.FROM_WKTGEOMETRY(wkt_data), cell_type, irregularity, rotation, height " +
                    "FROM temp_wkt_data";
                
                int inserted = stmt.executeUpdate(insertFromTempSQL);
                System.out.println("\n✅ Total records transferred from temp table: " + inserted);
                
                // Clean up temp table
                stmt.execute("DROP TABLE temp_wkt_data");
                System.out.println("✅ Temporary table dropped");
            }

        } catch (Exception e) {
            System.out.println("❌ Error occurred:");
            e.printStackTrace();
        }
    }

    private static String[] splitCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        Matcher matcher = Pattern.compile("\"([^\"]*)\"|([^,]+)").matcher(line);
        while (matcher.find()) {
            fields.add(matcher.group(1) != null ? matcher.group(1) : matcher.group(2));
        }
        return fields.toArray(new String[0]);
    }

    private static String formatWKT(String wkt) {
        String cleaned = wkt.replaceAll("^\"|\"$", "").trim();
        cleaned = cleaned.replaceAll("\\s+", " ");
        cleaned = cleaned.replaceAll("POLYGON Z", "POLYGON");

        if (!cleaned.contains("((")) {
            cleaned = cleaned.replace("(", "((");
            cleaned = cleaned.replace(")", "))");
        }

        return cleaned;
    }
}