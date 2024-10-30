package edu.toronto.cs.jackpine.benchmark;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import edu.toronto.cs.jackpine.benchmark.scenarios.threed.ReadSpatialRegionEqualsRegion3D;
import java.io.File;

public class JackpineBenchmark3DLauncher {
    private static Logger logger = Logger.getLogger(JackpineBenchmark3DLauncher.class);
    private static JackpineHtml3DLogger htmlLogger;

    public JackpineBenchmark3DLauncher() {
        logger.info("JackpineBenchmark3DLauncher instance created");
    }

    public static void main(String argv[]) {
        try {
            String log4jConfigFile = "/home/w3kq9/jackpine/log4j.properties";
            PropertyConfigurator.configure(log4jConfigFile);

            logger.info("JackpineBenchmark3DLauncher main method started");

            String propsFile = "connection_general.properties"; // Default properties file
            String include = null;
            String htmlLogFile = "benchmark_report1.html"; // Default HTML log file

            // Initialize HTML Logger
            htmlLogger = new JackpineHtml3DLogger(htmlLogFile);

            // Parse arguments.
            logger.info("Parsing command line arguments");
            int argc = 0;
            while (argc < argv.length) {
                String nextArg = argv[argc];
                argc++;
                switch (nextArg) {
                    case "-props":
                        propsFile = argv[argc++];
                        logger.info("Props file set to: " + propsFile);
                        break;
                    case "-include":
                        include = argv[argc++];
                        logger.info("Include property set to: " + include);
                        break;
                    case "-help":
                        logger.info("Help option detected");
                        usage();
                        return;
                    default:
                        String msg = "Unrecognized flag (try -help for usage): " + nextArg;
                        logger.error(msg);
                        htmlLogger.log(msg);
                        exitWithFailure();
                        return;
                }
            }

            // Load properties from the specified properties file.
            Properties properties = new Properties();
            if (propsFile != null) {
                logger.info("Loading properties from file: " + propsFile);
                htmlLogger.log("Loading properties from file: " + propsFile);
                try (FileInputStream fis = new FileInputStream(propsFile)) {
                    properties.load(fis);
                    logger.info("Properties loaded successfully");
                } catch (IOException e) {
                    logger.error("Error loading properties: " + e.getMessage(), e);
                    htmlLogger.log("Error loading properties: " + e.getMessage());
                    throw e;
                }
            } else {
                logger.info("No properties file specified, using default values");
                properties.setProperty("iterations", "1");
                properties.setProperty("url", "jdbc:postgresql://localhost:5432/your_database_name");
                properties.setProperty("user", "your_username");
                properties.setProperty("password", "your_password");
            }

            // Check for required properties
            String[] requiredProps = {"url", "user", "password", "iterations"};
            for (String prop : requiredProps) {
                if (!properties.containsKey(prop) || properties.getProperty(prop) == null) {
                    String errorMsg = "Required property '" + prop + "' is missing or null";
                    logger.error(errorMsg);
                    htmlLogger.log(errorMsg);
                    exitWithFailure();
                }
            }

            // Print all properties
            logger.info("Benchmark Properties:");
            htmlLogger.log("Benchmark Properties:");
            properties.forEach((key, value) -> {
                String keyStr = (String) key;
                String valueStr = "password".equals(keyStr) ? "********" : value.toString();
                logger.info(keyStr + " = " + valueStr);
                htmlLogger.log(keyStr + " = " + valueStr);
            });

            ReadSpatialRegionEqualsRegion3D benchmark = new ReadSpatialRegionEqualsRegion3D();
            try {
                logger.info("Initializing benchmark");
                htmlLogger.log("Initializing benchmark");
                benchmark.initialize(properties);
                logger.info("Benchmark initialized with properties");
                htmlLogger.log("Benchmark initialized with properties");
                logger.info("Starting benchmark execution");
                htmlLogger.log("Starting benchmark execution");
                long globalStartTime = System.nanoTime();
                logger.info("Calling prepare()");
                htmlLogger.log("Calling prepare()");
                benchmark.prepare();
                logger.info("Prepare completed");
                htmlLogger.log("Prepare completed");

                // Warm-up phase
                logger.info("Starting warm-up phase");
                htmlLogger.log("Starting warm-up phase");
                int warmupIterations = 1;
                for (int i = 0; i < warmupIterations; i++) {
                    benchmark.iterate(i);
                }
                logger.info("Warm-up completed");
                htmlLogger.log("Warm-up completed");

                // Timed iterations
                logger.info("Starting timed iterations");
                htmlLogger.log("Starting timed iterations");
                int totalIterations = Integer.parseInt(properties.getProperty("iterations", "1"));
                long[] iterationTimes = new long[totalIterations];
                for (int i = 0; i < totalIterations; i++) {
                    long iterationStartTime = System.nanoTime();
                    benchmark.iterate(i);
                    long iterationEndTime = System.nanoTime();
                    iterationTimes[i] = iterationEndTime - iterationStartTime;
                    if (i % (totalIterations / 10) == 0 || i == totalIterations - 1) {
                        String progressMsg = "Completed " + (i + 1) + " out of " + totalIterations + " iterations";
                        logger.info(progressMsg);
                        htmlLogger.log(progressMsg);
                    }
                }
                logger.info("Benchmark iterations completed");
                htmlLogger.log("Benchmark iterations completed");
                long globalEndTime = System.nanoTime();
                long totalDuration = globalEndTime - globalStartTime;

                // Calculate and print timing statistics
                Arrays.sort(iterationTimes);
                long totalTime = Arrays.stream(iterationTimes).sum();
                double averageTime = totalTime / (double) totalIterations;
                long medianTime = iterationTimes[totalIterations / 2];
                long minTime = iterationTimes[0];
                long maxTime = iterationTimes[totalIterations - 1];
                long percentile95 = iterationTimes[(int) (totalIterations * 0.95)];

                String timingStats = "\nTiming Statistics:\n" +
                    "Total Iterations: " + totalIterations + "\n" +
                    "Average Time: " + String.format("%.2f", averageTime) + " ns\n" +
                    "Median Time: " + medianTime + " ns\n" +
                    "Min Time: " + minTime + " ns\n" +
                    "Max Time: " + maxTime + " ns\n" +
                    "95th Percentile Time: " + percentile95 + " ns\n" +
                    "Total Execution Time: " + (totalDuration / 1_000_000) + " ms";

                logger.info(timingStats);
                htmlLogger.log(timingStats);

            } catch (Throwable t) {
                logger.fatal("Benchmark execution failed due to unexpected exception", t);
                htmlLogger.printException(t);
            } finally {
                try {
                    logger.info("Cleaning up benchmark resources");
                    htmlLogger.log("Cleaning up benchmark resources");
                    benchmark.cleanup();
                    logger.info("Benchmark cleanup completed");
                    htmlLogger.log("Benchmark cleanup completed");
                } catch (Exception e) {
                    logger.error("Error during benchmark cleanup", e);
                    htmlLogger.log("Error during benchmark cleanup: " + e.getMessage());
                }
            }

            logger.info("JackpineBenchmark3DLauncher execution completed");
            htmlLogger.log("JackpineBenchmark3DLauncher execution completed");
            htmlLogger.close();

        } catch (Exception e) {
            logger.fatal("Unhandled exception in main method: " + e.getMessage(), e);
            System.exit(1);
        }
    }

    protected static void usage() {
        String usageMsg = "Usage: java " + JackpineBenchmark3DLauncher.class.getName() + " options \n" +
            "  -props propsfile  Scenario properties file (default=connection_general.properties)\n" +
            "  -include          Additional include property (optional)\n" +
            "  -help             Print usage\n" +
            "Properties file must have at least url, user, password, and iterations values\n" +
            "If no properties file is specified, default values will be used";
        logger.info(usageMsg);
        htmlLogger.log(usageMsg);
    }

    protected static void exitWithFailure() {
        logger.error("Exiting with failure status");
        htmlLogger.log("Exiting with failure status");
        System.exit(1);
    }

    protected static void exitWithSuccess() {
        logger.info("Exiting with success status");
        htmlLogger.log("Exiting with success status");
        System.exit(0);
    }
}