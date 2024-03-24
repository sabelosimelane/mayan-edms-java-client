package za.co.ligabazi.mayanedms.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesLoader {

    public static Properties loadProperties(String propertiesFilePath) {
        Properties properties = new Properties();
        try {
            properties.load(Files.newInputStream(Paths.get(propertiesFilePath)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties from file: " + propertiesFilePath, e);
        }
        return properties;
    }
}