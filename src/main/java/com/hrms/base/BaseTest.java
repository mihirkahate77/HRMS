
package com.hrms.base;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.hrms.utils.ConfigReader;

public class BaseTest {

    public static WebDriver driver;

    @BeforeMethod
    @Parameters({"browser", "url"})
    public void setUp(
            @Optional("") String browserParameter,
            @Optional("") String urlParameter) {

        ConfigReader.loadConfig();

        String browser = browserParameter.isBlank()
                ? ConfigReader.getProperty("browser", "chrome")
                : browserParameter;

        if (browser.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
        } else if (browser.equalsIgnoreCase("edge")) {
            driver = new EdgeDriver();
        } else {
            driver = new ChromeDriver();
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);

        String url = urlParameter.isBlank()
                ? ConfigReader.getProperty("url")
                : urlParameter;

        if (url == null || url.isBlank()) {
            throw new IllegalStateException(
                    "Application URL is missing in config.properties");
        }

        driver.get(url);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {

        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
