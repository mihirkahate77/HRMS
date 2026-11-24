package com.hrms.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DashboardPage {

    WebDriver driver;
    WebDriverWait wait;

    By accessDashboardBtn = By.xpath("//button[.//span[text()='Access Dashboard']]");

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public boolean isDashboardDisplayed() {
        try {
            return wait.until(ExpectedConditions.urlContains("/dashboard/organizations"));
        } catch (Exception e) {
            return false;
        }
    }

    public void clickAccessDashboard() {
        wait.until(ExpectedConditions.elementToBeClickable(accessDashboardBtn)).click();
    }
}
