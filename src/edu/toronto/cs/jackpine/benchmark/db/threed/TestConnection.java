package edu.toronto.cs.jackpine.benchmark.db.threed;



import java.sql.*;
import java.util.Properties;

public class TestConnection {
    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@localhost:1521:XE";
        Properties props = new Properties();
        props.put("user", "sys");
        props.put("password", "15051505");
        props.put("internal_logon", "sysdba");
        props.put("oracle.jdbc.driver.OracleDriver", "");

        try (Connection conn = DriverManager.getConnection(url, props)) {
            System.out.println("Connection successful!");
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT SYSDATE FROM DUAL");
                while (rs.next()) {
                    System.out.println("Current Oracle date: " + rs.getString(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Connection status: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

