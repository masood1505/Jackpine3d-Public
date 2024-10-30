package edu.toronto.cs.jackpine.benchmark;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import org.apache.log4j.Logger;
import com.continuent.bristlecone.benchmark.impl.Config;
import com.continuent.bristlecone.benchmark.impl.ConfigMetadata;
import com.continuent.bristlecone.benchmark.impl.ConfigPropertyMetadata;

public class JackpineHtml3DLogger {

    private static Logger logger = Logger.getLogger(JackpineHtml3DLogger.class);
    protected PrintWriter htmlOut;
    private boolean is3D = false;
    private boolean printedHeader = false;

    protected static final String STYLES = 
        "body { font-family: Arial, sans-serif; margin: 20px; }\n" +
        "h1 { color: #333366; }\n" +
        "h2 { color: #666699; }\n" +
        "table { border-collapse: collapse; width: 100%; }\n" +
        "th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n" +
        "th { background-color: #f2f2f2; color: #333366; }\n" +
        "tr:nth-child(even) { background-color: #f9f9f9; }\n" +
        ".error { color: red; font-weight: bold; }";

    public JackpineHtml3DLogger(String outputFileName) {
        try {
            htmlOut = new PrintWriter(new FileWriter(outputFileName));
        } catch (IOException e) {
            logger.error("Failed to create HTML output file: " + outputFileName, e);
        }
    }

    public void resultGenerated(Config config) {
        ConfigMetadata metadata = config.getMetadata();

        // Determine if this is a 3D benchmark
        is3D = config.getProperty("scenario").toLowerCase().contains("3d");

        if (!printedHeader) {
            printHeader(config, metadata);
            printedHeader = true;
        }

        // Write data for non-fixed or output fields.
        htmlOut.println("<tr>");
        Iterator<String> iter = metadata.propertyNames();
        while (iter.hasNext()) {
            String name = iter.next();
            if (shouldSkipProperty(name)) continue;

            ConfigPropertyMetadata cpm = metadata.getPropertyMetadataAsserted(name);
            if (cpm.isVariable() || cpm.isOutput()) {
                String value = config.getProperty(name);
                htmlOut.println("<td>" + formatValue(name, value) + "</td>");
            }
        }
        htmlOut.println("</tr>");
    }

    public void printHeader(Config config, ConfigMetadata metadata) {
        htmlOut.println("<html>");
        htmlOut.println("<head>");
        htmlOut.println("<title>Jackpine 3D Spatial Benchmark Report</title>");
        htmlOut.println("<style type=\"text/css\">");
        htmlOut.println(STYLES);
        htmlOut.println("</style>");
        htmlOut.println("</head>");
        htmlOut.println("<body>");
        htmlOut.println("<h1>Jackpine 3D Spatial Benchmark Report</h1>");
        printFixedConfigValues(config, metadata);
        printBenchmarkRunsHeader(metadata);
    }

    protected void printFixedConfigValues(Config config, ConfigMetadata metadata) {
        htmlOut.println("<h2>Configuration</h2>");
        htmlOut.println("<table>");
        htmlOut.println("<tr><th>Property</th><th>Value</th></tr>");

        Iterator<String> iter = metadata.propertyNames();
        while (iter.hasNext()) {
            String name = iter.next();
            if (shouldSkipProperty(name)) continue;

            String value = config.getProperty(name);
            ConfigPropertyMetadata cpm = metadata.getPropertyMetadataAsserted(name);
            if (!cpm.isVariable() && !cpm.isOutput()) {
                htmlOut.println("<tr>");
                htmlOut.println("<td>" + name + "</td><td>" + formatValue(name, value) + "</td>");
                htmlOut.println("</tr>");
            }
        }
        htmlOut.println("</table>");
    }

    protected void printBenchmarkRunsHeader(ConfigMetadata metadata) {
        htmlOut.println("<h2>Benchmark Runs</h2>");
        htmlOut.println("<table>");
        htmlOut.println("<tr>");
        
        Iterator<String> iter = metadata.propertyNames();
        while (iter.hasNext()) {
            String name = iter.next();
            if (shouldSkipProperty(name)) continue;

            ConfigPropertyMetadata cpm = metadata.getPropertyMetadataAsserted(name);
            if (cpm.isVariable() || cpm.isOutput()) {
                htmlOut.println("<th align=\"left\">" + name + "</th>");
            }
        }
        htmlOut.println("</tr>");
    }

    public void printBenchmarkRun(String[] data) {
        htmlOut.println("<tr>");
        for (String value : data) {
            htmlOut.println("<td>" + value + "</td>");
        }
        htmlOut.println("</tr>");
        htmlOut.flush();
    }

    public void printException(Throwable t) {
        htmlOut.println("<h2 class='error'>Benchmark execution failed</h2>");
        htmlOut.println("<p class='error'>Exception: " + t.getMessage() + "</p>");
        Throwable cause = t;
        while ((cause = cause.getCause()) != null) {
            htmlOut.println("<p class='error'>Caused by: " + cause.getMessage() + "</p>");
        }
        htmlOut.flush();
    }

    public void log(String message) {
        htmlOut.println("<p>" + message + "</p>");
        htmlOut.flush();
    }

    private boolean shouldSkipProperty(String name) {
        return name.equals("datarows") || name.equals("datatype") || name.equals("datawidth") 
               || name.equals("password") || name.equals("url") || name.equals("include");
    }

    protected String formatValue(String name, String value) {
        if (name.equals("scenario")) {
            int len = "com.continuent.bristlecone.benchmark.scenarios.".length();
            return value.substring(len);
        }
        if (name.toLowerCase().contains("distance") && is3D) {
            return value + " (3D)";
        }
        return value;
    }

    public void close() {
        if (htmlOut != null) {
            htmlOut.println("</table>");
            htmlOut.println("</body>");
            htmlOut.println("</html>");
            htmlOut.close();
        }
    }
}