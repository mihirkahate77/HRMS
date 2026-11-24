<<<<<<< HEAD

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
=======
package com.hrms.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
>>>>>>> 763cb55a9694c947a9ebc8e889459ad01543928b

import com.hrms.utils.ConfigReader;

public class BaseTest {

<<<<<<< HEAD
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
=======
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
>>>>>>> 763cb55a9694c947a9ebc8e889459ad01543928b
