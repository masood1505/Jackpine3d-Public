package edu.toronto.cs.jackpine.benchmark.scenarios.macroscenario.edu.toronto.cs.jackpine.benchmark.scenarios.macroscenario.threed;


import edu.toronto.cs.jackpine.benchmark.db.threed.SpatialSqlDialect3D;
import edu.toronto.cs.jackpine.benchmark.db.threed.SpatialSqlDialectFactory;

import java.sql.*;
import java.util.*;

public class DatabaseManager {
    private Connection connection;
    private SpatialSqlDialect3D dialect;

    public DatabaseManager(String url, Properties props) throws SQLException {
        connection = DriverManager.getConnection(url, props);
        dialect = SpatialSqlDialectFactory.getInstance().getDialect(url, props);
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public List<Nucleus> loadNuclei() throws SQLException {
        List<Nucleus> nuclei = new ArrayList<>();
        String query = "SELECT * FROM nuclei";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Nucleus nucleus = new Nucleus();
                // Set nucleus properties from ResultSet
                nuclei.add(nucleus);
            }
        }
        return nuclei;
    }

    public List<Vessel> loadVessels() throws SQLException {
        List<Vessel> vessels = new ArrayList<>();
        String query = "SELECT * FROM vessels";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Vessel vessel = new Vessel();
                // Set vessel properties from ResultSet
                vessels.add(vessel);
            }
        }
        return vessels;
    }

    public List<Object3D> performBuffer3D() throws SQLException {
        List<Object3D> results = new ArrayList<>();
        String query = dialect.getSelectBuffer3D();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                // Create Object3D from buffer result
                Object3D obj = new Object3D();
                // Set properties from ResultSet
                results.add(obj);
            }
        }
        return results;
    }

    // Add more methods for other spatial operations as needed
}
