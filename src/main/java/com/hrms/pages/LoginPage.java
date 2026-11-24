package com.hrms.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    WebDriver driver;
    WebDriverWait wait;

    // Locators
    By email = By.name("email");
    By password = By.xpath("//input[@type='password' and @placeholder='Enter your password']");
    By loginButton = By.xpath("//button[@type='submit' and .//span[text()='Sign in']]");

    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Actions
    public void enterEmail(String emailValue) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(email)).sendKeys(emailValue);
    }

    public void enterPassword(String passwordValue) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(password)).sendKeys(passwordValue);
    }

    public void clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    public void login(String emailValue, String passwordValue) {
        enterEmail(emailValue);
        enterPassword(passwordValue);
        clickLogin();
    }
}
