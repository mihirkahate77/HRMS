<<<<<<< HEAD

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
=======
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
>>>>>>> 763cb55a9694c947a9ebc8e889459ad01543928b
    }

    public void login(String emailValue, String passwordValue) {
        enterEmail(emailValue);
        enterPassword(passwordValue);
        clickLogin();
    }
<<<<<<< HEAD

    public boolean isLoginPageDisplayed() {
        return driver.getCurrentUrl().toLowerCase().contains("login");
    }
=======
>>>>>>> 763cb55a9694c947a9ebc8e889459ad01543928b
}
