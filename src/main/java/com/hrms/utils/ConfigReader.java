package com.hrms.utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Properties prop = new Properties();

    public static void loadConfig() {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) throw new RuntimeException("config.properties was not found in src/test/resources");
            prop.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load config.properties", e);
        }
    }

    public static String getProperty(String key) {
        String value = prop.getProperty(key);
        if (value == null) throw new RuntimeException("Missing property: " + key);
        return value.trim();
    }

    public static String getProperty(String key, String defaultValue) {
        return prop.getProperty(key, defaultValue).trim();
    }

    public static String getUsername() {
        String value = System.getenv("HRMS_USERNAME");
        return value == null || value.isBlank() ? getProperty("username", "") : value;
    }

    public static String getPassword() {
        String value = System.getenv("HRMS_PASSWORD");
        return value == null || value.isBlank() ? getProperty("password", "") : value;
    }
}
