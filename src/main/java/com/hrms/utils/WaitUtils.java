package com.hrms.utils;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitUtils {
    private final WebDriverWait wait;
    public WaitUtils(WebDriver driver) {
        int seconds = Integer.parseInt(ConfigReader.getProperty("explicit.wait", "15"));
        wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
    }
    public WebElement visible(By locator) { return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)); }
    public WebElement clickable(By locator) { return wait.until(ExpectedConditions.elementToBeClickable(locator)); }
    public boolean urlContains(String value) { return wait.until(ExpectedConditions.urlContains(value)); }
}
