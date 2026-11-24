package com.hrms.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
<<<<<<< HEAD
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
=======

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
>>>>>>> 763cb55a9694c947a9ebc8e889459ad01543928b
    }
}
