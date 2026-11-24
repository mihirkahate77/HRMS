package com.hrms.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.hrms.base.BaseTest;
import com.hrms.pages.LoginPage;
import com.hrms.pages.DashboardPage;
import com.hrms.utils.ConfigReader;

public class LoginTest extends BaseTest {

    @Test
    public void verifyValidLogin() {

        LoginPage login = new LoginPage(driver);

        login.login(
            ConfigReader.getProperty("username"),
            ConfigReader.getProperty("password")
        );

        DashboardPage dashboard = new DashboardPage(driver);

        Assert.assertTrue(
            dashboard.isDashboardDisplayed(),
            "Dashboard is not displayed after login"
        );

        // Clicks the first matching Access Dashboard button after URL check passes
        dashboard.clickAccessDashboard();
    }
}
