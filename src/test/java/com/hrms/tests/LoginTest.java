package com.hrms.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.hrms.base.BaseTest;
import com.hrms.pages.DashboardPage;
import com.hrms.pages.LoginPage;
import com.hrms.utils.ConfigReader;

public class LoginTest extends BaseTest {
    @Test(description = "Verify that a valid HRMS user can log in", groups = {"smoke", "login"})
    public void verifyValidLogin() {
        String username = ConfigReader.getUsername();
        String password = ConfigReader.getPassword();
        Assert.assertFalse(username.isBlank(), "Set HRMS_USERNAME or username in config.properties");
        Assert.assertFalse(password.isBlank(), "Set HRMS_PASSWORD or password in config.properties");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);

        DashboardPage dashboardPage = new DashboardPage(driver);
        Assert.assertTrue(dashboardPage.isDashboardDisplayed(),
                "Dashboard URL was not displayed after login. Current URL: " + dashboardPage.getCurrentUrl());
    }
}
