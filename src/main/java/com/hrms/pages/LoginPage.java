
package com.hrms.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.hrms.utils.ConfigReader;
import com.hrms.utils.WaitUtils;

public class LoginPage {

    private final WebDriver driver;
    private final WaitUtils wait;

    private final By email = By.cssSelector(
            ConfigReader.getProperty("login.email.css"));

    private final By password = By.cssSelector(
            ConfigReader.getProperty("login.password.css"));

    private final By loginButton = By.cssSelector(
            ConfigReader.getProperty("login.submit.css"));

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WaitUtils(driver);
    }

    public void enterEmail(String value) {
        WebElement emailField = wait.visible(email);
        emailField.clear();
        emailField.sendKeys(value);
    }

    public void enterPassword(String value) {
        WebElement passwordField = wait.visible(password);
        passwordField.clear();
        passwordField.sendKeys(value);
    }

    public void clickLogin() {
        wait.clickable(loginButton).click();
    }

    public void login(String emailValue, String passwordValue) {
        enterEmail(emailValue);
        enterPassword(passwordValue);
        clickLogin();
    }

    public boolean isLoginPageDisplayed() {
        return driver.getCurrentUrl().toLowerCase().contains("login");
    }
}
