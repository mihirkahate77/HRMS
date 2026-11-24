package com.hrms.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.hrms.utils.ConfigReader;

public class BaseTest {

    public WebDriver driver;

    @BeforeMethod
    public void setup() {

        ConfigReader.loadConfig();

        driver = new ChromeDriver();

        driver.manage().window().maximize();

        driver.get(ConfigReader.getProperty("url"));
    }

    @AfterMethod
    public void tearDown() {

        if (driver != null) {

            driver.quit();
        }
    }
}