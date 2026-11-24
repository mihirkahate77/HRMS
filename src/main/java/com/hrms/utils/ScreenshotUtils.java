package com.hrms.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;

public class ScreenshotUtils {
    public static String capture(WebDriver driver, String testName) {
        if (driver == null) return "";
        try {
            Path folder = Path.of("target", "screenshots");
            Files.createDirectories(folder);
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path destination = folder.resolve(testName + "_" + time + ".png");
            FileHandler.copy(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE), destination.toFile());
            return destination.toString();
        } catch (Exception e) {
            System.out.println("Screenshot could not be saved: " + e.getMessage());
            return "";
        }
    }
}
