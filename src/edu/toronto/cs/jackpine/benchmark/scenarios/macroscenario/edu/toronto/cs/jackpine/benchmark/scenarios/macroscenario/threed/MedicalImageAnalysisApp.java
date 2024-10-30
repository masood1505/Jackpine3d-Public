package edu.toronto.cs.jackpine.benchmark.scenarios.macroscenario.edu.toronto.cs.jackpine.benchmark.scenarios.macroscenario.threed;


import java.util.*;


import java.sql.SQLException;
import java.util.*;

public class MedicalImageAnalysisApp {
    public static void main(String[] args) {
        Properties props = new Properties();
        // Load properties from your configuration file
        
        try {
            // Create DatabaseManager instances for each database
            DatabaseManager postgresDB = new DatabaseManager("jdbc:postgresql://localhost:5432/yourDB", props);
            DatabaseManager oracleDB = new DatabaseManager("jdbc:oracle:thin:@localhost:1521/XE", props);
            // Add SQL Server if needed

            MedicalImageAnalysis analysis = new MedicalImageAnalysis();

            // Load data from PostgreSQL
            List<Nucleus> postgresNuclei = postgresDB.loadNuclei();
            List<Vessel> postgresVessels = postgresDB.loadVessels();
            analysis.loadData(postgresNuclei, postgresVessels);

            // Load data from Oracle
            List<Nucleus> oracleNuclei = oracleDB.loadNuclei();
            List<Vessel> oracleVessels = oracleDB.loadVessels();
            analysis.loadData(oracleNuclei, oracleVessels);

            // Perform an intersection query
            Vessel targetVessel = postgresVessels.get(0); // Or choose from any other list
            List<Object3D> intersections = analysis.findIntersections(targetVessel);

            System.out.println("Intersections found: " + intersections.size());

            // Perform a 3D buffer operation using PostgreSQL
            List<Object3D> postgresBufferResults = postgresDB.performBuffer3D();
            System.out.println("PostgreSQL 3D Buffer results: " + postgresBufferResults.size());

            // Perform a 3D buffer operation using Oracle
            List<Object3D> oracleBufferResults = oracleDB.performBuffer3D();
            System.out.println("Oracle 3D Buffer results: " + oracleBufferResults.size());

            // Close database connections
            postgresDB.closeConnection();
            oracleDB.closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

