package edu.toronto.cs.jackpine.benchmark;

import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatial3DDistance3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatial3DUnion;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatial3DUnionvs3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialArea3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialBuildingsClosestpointArea3Dvs3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialBuildingsInterpolatepointArea3Dvs3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialBuildingsLongestLineArea3Dvs3D;
//import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialClosestPoint3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialLength3D;
//import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialLineContainsVolume3D;
//import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialLineCoveredByVolume3D;
//import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialLineCrossesLine3D;
//import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialLineCrossesRegion3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialLineEqualsLine3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialLineEqualsLine3Dvs3D;
//import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialLineIntersectsVolume3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialLineOverlapsLine3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialBuildingsOverlapsArea3Dvs3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialBuildingsShortestLineArea3Dvs3D;
/*import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialLineWithinRegion3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialLineWithinVolume3D;*/
//import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialLongestLine3D;
/*import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialPointCoveredByRegion3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialPointEqualsPoint3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialPointIntersectsLine3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialPointWithinRegion3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialRegionContainsLine3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialRegionContainsPoint3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialRegionContainsRegion3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialRegionCoveredByRegion3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialRegionCoveredByVolume3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialRegionCoversLine3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialRegionCoversPoint3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialRegionCoversRegion3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialRegionCrossesVolume3D;*/
//import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialRegionEqualsRegion3D;
/*import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialRegionIntersectsVolume3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialRegionWithinVolume3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialShortestLine3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialVolumeContainsLine3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialVolumeContainsRegion3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialVolumeCoversLine3D;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialVolumeCoversRegion3D;*/
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialprimeter3D;

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
            
          /*  scenarios.add(new ReadSpatialRegionContainsLine3D(properties));*/
         //   scenarios.add(new ReadSpatialRegionContainsRegion3D(properties));
         //   scenarios.add(new ReadSpatialLineCrossesLine3D(properties));
            
         //   scenarios.add(new ReadSpatialLineIntersectsVolume3D(properties));
        //    scenarios.add(new ReadSpatialRegionCoversPoint3D(properties));   /* not available*/
        //    scenarios.add(new ReadSpatialRegionCrossesVolume3D(properties));  /* not available*/
        //      scenarios.add(new ReadSpatialRegionWithinVolume3D(properties));
        //      scenarios.add(new ReadSpatialPointWithinRegion3D(properties));   //fix this later 
            
            
            
     //       scenarios.add(new ReadSpatialLineContainsVolume3D(properties));    //wrong logically
      //      scenarios.add(new ReadSpatialLineCoveredByVolume3D(properties));   //not available 
      //      scenarios.add(new ReadSpatialLineCrossesRegion3D(properties));      //not available 
       //     scenarios.add(new ReadSpatialLineEqualsLine3D(properties));
        //    scenarios.add(new ReadSpatialLineOverlapsLine3D(properties)); 
         //   scenarios.add(new ReadSpatialLongestLine3D(properties)); 
          //  scenarios.add(new ReadSpatialClosestPoint3D(properties)); 
  //          scenarios.add(new ReadSpatialShortestLine3D(properties)); 
       //     scenarios.add(new ReadSpatial3DUnion(properties)); 
              
              
              
              scenarios.add(new ReadSpatialBuildingsOverlapsArea3Dvs3D(properties));  //*1
              
    //          scenarios.add(new ReadSpatialLineEqualsLine3Dvs3D(properties));
              
    //          scenarios.add(new ReadSpatial3DUnionvs3D(properties)); 
              
    //         scenarios.add(new ReadSpatialArea3D(properties));  //*2
              
    //          scenarios.add(new ReadSpatialprimeter3D(properties));  //*3
              
     //         scenarios.add(new ReadSpatialLength3D(properties));  //*3
              
     //         scenarios.add(new ReadSpatial3DDistance3D(properties));  //*4
              
     //         scenarios.add(new ReadSpatialBuildingsClosestpointArea3Dvs3D(properties));  //*5
              
      //        scenarios.add(new ReadSpatialBuildingsLongestLineArea3Dvs3D(properties));  //*6
              
     //         scenarios.add(new ReadSpatialBuildingsShortestLineArea3Dvs3D(properties));  //*7

     //         scenarios.add(new ReadSpatialBuildingsInterpolatepointArea3Dvs3D(properties));  //*8









              
              
              


              
              

              

            
     /*       scenarios.add(new ReadSpatialLineWithinRegion3D(properties));
            scenarios.add(new ReadSpatialLineWithinVolume3D(properties));
      //    scenarios.add(new ReadSpatialPointEqualsPoint3D(properties));
            scenarios.add(new ReadSpatialPointIntersectsLine3D(properties));
            
            scenarios.add(new ReadSpatialRegionContainsPoint3D(properties));

         
          //scenarios.add(new ReadSpatialRegionEqualsRegion3D(properties));
          //scenarios.add(new ReadSpatialRegionIntersectsVolume3D(properties));
            scenarios.add(new ReadSpatialVolumeContainsLine3D(properties));
            scenarios.add(new ReadSpatialVolumeContainsRegion3D(properties));
            



*/
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