package com.hrms.pages;

<<<<<<< HEAD
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.hrms.utils.WaitUtils;

// Add verified HRMS employee-page locators here after inspecting the application.
public class EmployeePage {
    private final WebDriver driver;
    private final WaitUtils wait;
    public EmployeePage(WebDriver driver) { this.driver = driver; this.wait = new WaitUtils(driver); }
    public boolean isElementDisplayed(By locator) {
        try { return wait.visible(locator).isDisplayed(); }
        catch (Exception e) { return false; }
    }
=======
public class EmployeePage {

>>>>>>> 763cb55a9694c947a9ebc8e889459ad01543928b
}
