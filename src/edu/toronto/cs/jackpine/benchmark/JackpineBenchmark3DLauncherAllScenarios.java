package edu.toronto.cs.jackpine.benchmark;

import edu.toronto.cs.jackpine.benchmark.scenarios.threed.BoundingBox3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.BridgeAnalysis;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.Building3DDistanceArea;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.Building3DDistanceLine;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.Building3DDistanceWithinBuilding;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.Building3DIntersectsArea;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.Building3DIntersectsLine;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.CancerousAnalysisIntersection;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ConvexHull;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.Dimensions;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.EmergencyRoutes;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.FutureExpansion;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.GardenAnalysis;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.Length3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.Length3DMedicalAnalysis;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.Perimeter3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.Perimeter3DMedicalAnalysis;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.SubwayStationLocation;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.Properties;
import java.util.ArrayList;
import java.util.List;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class JackpineBenchmark3DLauncherAllScenarios {
    private static final Logger logger = Logger.getLogger(JackpineBenchmark3DLauncherAllScenarios.class);
    private static JackpineHtml3DLogger htmlLogger;

    public static void main(String[] args) {
        try {
            String log4jConfigFile = "/home/w3kq9/jackpine/log4j.properties";
            PropertyConfigurator.configure(log4jConfigFile);

            logger.info("JackpineBenchmark3DLauncherAllScenarios started");

            String propsFile = "connection_general.properties";
            String htmlLogFile = "benchmark_report_all_scenarios.html";

            // Initialize HTML Logger
            htmlLogger = new JackpineHtml3DLogger(htmlLogFile);

            // Load properties
            Properties properties = loadProperties(propsFile);

            // Create scenario instances
            List<Object> scenarios = new ArrayList<>();
              
                /*         scenarios to be added here                 */
                
                  scenarios.add(new Building3DIntersectsLine(properties));
                  scenarios.add(new Building3DIntersectsArea(properties)); 
                  scenarios.add(new Building3DDistanceWithinBuilding(properties));
                  scenarios.add(new Building3DDistanceArea(properties));
                  scenarios.add(new Building3DDistanceLine(properties));
                  
                  scenarios.add(new BoundingBox3D(properties));
                  scenarios.add(new BridgeAnalysis(properties));
                  scenarios.add(new CancerousAnalysisIntersection(properties));
                  scenarios.add(new ConvexHull(properties));
                  scenarios.add(new Dimensions(properties));
                  scenarios.add(new EmergencyRoutes(properties));
                  scenarios.add(new FutureExpansion(properties));
                  scenarios.add(new GardenAnalysis(properties));
                  scenarios.add(new Length3D(properties));
                  scenarios.add(new Length3DMedicalAnalysis(properties));
                  scenarios.add(new Perimeter3D(properties));
                  scenarios.add(new Perimeter3DMedicalAnalysis(properties));
                  scenarios.add(new SubwayStationLocation(properties));                 

                 
            for (Object scenario : scenarios) {
                runScenario(scenario, properties);
            }

            logger.info("All scenarios completed");
            htmlLogger.log("All scenarios completed");
            htmlLogger.close();

        } catch (Exception e) {
            logger.fatal("Unhandled exception in main method: " + e.getMessage(), e);
            htmlLogger.printException(e);
            System.exit(1);
        }
    }

    private static Properties loadProperties(String propsFile) throws Exception {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(propsFile)) {
            properties.load(fis);
            logger.info("Properties loaded from: " + propsFile);
            htmlLogger.log("Properties loaded from: " + propsFile);
        } catch (IOException e) {
            logger.error("Error loading properties: " + e.getMessage(), e);
            htmlLogger.log("Error loading properties: " + e.getMessage());
            throw e;
        }
        return properties;
    }

    private static void runScenario(Object scenario, Properties properties) {
        try {
            logger.info("Running scenario: " + scenario.getClass().getSimpleName());
            htmlLogger.log("Running scenario: " + scenario.getClass().getSimpleName());

            // Prepare the scenario
            scenario.getClass().getMethod("prepare").invoke(scenario);

            // Run iterations
            int totalIterations = Integer.parseInt(properties.getProperty("iterations", "1"));
            long[] iterationTimes = new long[totalIterations];

            for (int i = 0; i < totalIterations; i++) {
                long iterationStartTime = System.nanoTime();
                scenario.getClass().getMethod("iterate", long.class).invoke(scenario, (long)i);
                long iterationEndTime = System.nanoTime();
                iterationTimes[i] = iterationEndTime - iterationStartTime;
            }

            // Calculate and print timing statistics
            printTimingStatistics(scenario.getClass().getSimpleName(), iterationTimes);

            // Cleanup
            scenario.getClass().getMethod("cleanup").invoke(scenario);

            logger.info("Scenario completed: " + scenario.getClass().getSimpleName());
            htmlLogger.log("Scenario completed: " + scenario.getClass().getSimpleName());
        } catch (Exception e) {
            logger.error("Error running scenario " + scenario.getClass().getSimpleName(), e);
            htmlLogger.printException(e);
        }
    }

    private static void printTimingStatistics(String scenarioName, long[] iterationTimes) {
        Arrays.sort(iterationTimes);
        int totalIterations = iterationTimes.length;
        long totalTime = Arrays.stream(iterationTimes).sum();
        double averageTime = totalTime / (double) totalIterations;
        long medianTime = iterationTimes[totalIterations / 2];
        long minTime = iterationTimes[0];
        long maxTime = iterationTimes[totalIterations - 1];
        long percentile95 = iterationTimes[(int) (totalIterations * 0.95)];

        String timingStats = "\nTiming Statistics for " + scenarioName + ":\n" +
            "Total Iterations: " + totalIterations + "\n" +
            "Average Time: " + String.format("%.2f", averageTime) + " ns\n" +
            "Median Time: " + medianTime + " ns\n" +
            "Min Time: " + minTime + " ns\n" +
            "Max Time: " + maxTime + " ns\n" +
            "95th Percentile Time: " + percentile95 + " ns\n";

        logger.info(timingStats);
        htmlLogger.log(timingStats);
    }
}