package edu.toronto.cs.jackpine.benchmark;

import java.util.Iterator;
import org.apache.log4j.Logger;
import com.continuent.bristlecone.benchmark.impl.Config;
import com.continuent.bristlecone.benchmark.impl.ConfigMetadata;
import com.continuent.bristlecone.benchmark.impl.ConfigPropertyMetadata;
import com.continuent.bristlecone.benchmark.HtmlLogger;

public class JackpineHtmlLogger extends HtmlLogger {
    private static Logger logger = Logger.getLogger(JackpineHtmlLogger.class);
    private boolean is3D = false;

    public JackpineHtmlLogger(String outputFileName) {
        super(outputFileName);
    }

    @Override
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

    private void printHeader(Config config, ConfigMetadata metadata) {
        htmlOut.println("<html>");
        htmlOut.println("<head>");
        htmlOut.println("<title>Jackpine 2D/3D Spatial Benchmark Report</title>");
        htmlOut.println("<style type=\"text/css\">");
        htmlOut.println(STYLES);
        htmlOut.println("</style>");
        htmlOut.println("</head>");
        htmlOut.println("<body>");
        htmlOut.println("<h1>Jackpine 2D/3D Spatial Benchmark Report</h1>");

        printFixedConfigValues(config, metadata);
        printBenchmarkRunsHeader(metadata);
    }

    protected void printFixedConfigValues(Config config, ConfigMetadata metadata) {
        htmlOut.println("<h2>Fixed Benchmark Configuration Values</h2>");
        htmlOut.println("<table class=\"details\" border=\"0\" cellpadding=\"5\" cellspacing=\"2\" width=\"95%\">");
        htmlOut.println("<tr><th align=\"left\">Name</th><th>Value</th></tr>");

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
        htmlOut.println("<table class=\"details\" border=\"0\" cellpadding=\"5\" cellspacing=\"2\" width=\"95%\">");
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

    private boolean shouldSkipProperty(String name) {
        return name.equals("datarows") || name.equals("datatype") || name.equals("datawidth") 
               || name.equals("password") || name.equals("url") || name.equals("include");
    }

    private String formatValue(String name, String value) {
        if (name.equals("scenario")) {
            int len = "com.continuent.bristlecone.benchmark.scenarios.".length();
            return value.substring(len);
        }
        if (name.toLowerCase().contains("distance") && is3D) {
            return value + " (3D)";
        }
        return value;
    }
}