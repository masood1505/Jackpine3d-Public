package edu.toronto.cs.jackpine.benchmark;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ResourceLoader {
    public static void loadProperties() {
        try (InputStream input = ResourceLoader.class.getClassLoader().getResourceAsStream("connection_general.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find connection_general.properties");
                return;
            }

            Properties prop = new Properties();
            prop.load(input);

            System.out.println("Properties loaded successfully from connection_general.properties.");
            for (String key : prop.stringPropertyNames()) {
                System.out.println(key + " = " + prop.getProperty(key));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
