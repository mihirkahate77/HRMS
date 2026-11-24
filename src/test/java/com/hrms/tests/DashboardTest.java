package com.hrms.tests;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.hrms.base.BaseTest;
import com.hrms.pages.DashboardPage;
import com.hrms.pages.LoginPage;
import com.hrms.utils.ConfigReader;

/**
 * Dashboard tests for the HR Manager role.
 *
 * BaseTest.setUp() (a @BeforeMethod in the parent class) runs first and opens the
 * browser at the login URL. loginAndOpenDashboard() below then logs in, clicks
 * "Access Dashboard" on the organization selection page (/dashboard/organizations),
 * and waits for the real dashboard, so every test starts on a freshly loaded dashboard.
 *
 * config.properties keys used:
 *   username, password   (read through ConfigReader.getUsername()/getPassword(),
 *                         same as LoginTest, so HRMS_USERNAME / HRMS_PASSWORD also work)
 *   dashboard.access.button.xpath   (the "Access Dashboard" button)
 *   dashboard.userName   default "Gauri HR Manager"
 *   dashboard.userRole   default "HR Manager"
 *   dashboard.workspace  default "Orangebits Digital Solutions Private Limited"
 */
public class DashboardTest extends BaseTest {

    private DashboardPage dashboard;
    private String dashboardUrl;

    private String expectedUserName;
    private String expectedUserRole;
    private String expectedWorkspace;


@BeforeMethod
public void loginAndOpenDashboard() {

    expectedUserName = ConfigReader.getProperty(
            "dashboard.userName", "Gauri HR Manager");

    expectedUserRole = ConfigReader.getProperty(
            "dashboard.userRole", "HR Manager");

    expectedWorkspace = ConfigReader.getProperty(
            "dashboard.workspace",
            "Orangebits Digital Solutions Private Limited");

    String username = ConfigReader.getUsername();
    String password = ConfigReader.getPassword();

    Assert.assertFalse(
            username.isBlank(),
            "Set HRMS_USERNAME or username in config.properties");

    Assert.assertFalse(
            password.isBlank(),
            "Set HRMS_PASSWORD or password in config.properties");

    // Login
    new LoginPage(driver).login(username, password);

    // Open dashboard
    dashboard = new DashboardPage(driver);
    dashboard.waitUntilLoaded();

    // Save dashboard URL
    dashboardUrl = driver.getCurrentUrl();

    System.out.println("DASHBOARD URL: " + dashboardUrl);
    System.out.println("DASHBOARD TITLE: " + driver.getTitle());
}


    // ------------------------------------------------------------------
    // 1. Page load
    // ------------------------------------------------------------------
    @Test(priority = 1, description = "Dashboard opens after login with correct URL, title and heading")
    public void verifyDashboardLoads() {
        SoftAssert soft = new SoftAssert();
        soft.assertTrue(driver.getCurrentUrl().contains("/dashboard"), "URL should contain /dashboard");
        soft.assertEquals(dashboard.getBrowserTitle(), "Orangebits HRMS", "Browser tab title");
        soft.assertEquals(dashboard.getPageHeading(), "HR Dashboard", "Page heading");
        soft.assertTrue(dashboard.isSidebarDisplayed(), "Sidebar should be visible");
        soft.assertAll();
    }

    // ------------------------------------------------------------------
    // 2. Header
    // ------------------------------------------------------------------
    @Test(priority = 2, description = "Welcome banner shows the logged-in user and today's date")
    public void verifyWelcomeBannerAndDate() {
        String expectedDate = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.ENGLISH));

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(dashboard.getWelcomeText(), "Welcome, " + expectedUserName, "Welcome text");
        soft.assertEquals(dashboard.getDisplayedDate(), expectedDate, "Date shown in header");
        soft.assertAll();
    }

    @Test(priority = 3, description = "Header shows active workspace and the user's profile chip")
    public void verifyWorkspaceAndProfileChip() {
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(dashboard.getWorkspaceName(), expectedWorkspace, "Active workspace name");
        soft.assertTrue(dashboard.isWorkspaceLogoLoaded(), "Workspace logo should load (not broken)");
        soft.assertEquals(dashboard.getProfileName(), expectedUserName, "Profile chip name");
        soft.assertEquals(dashboard.getProfileRole(), expectedUserRole, "Profile chip role");
        soft.assertEquals(dashboard.getProfileInitial(), expectedUserName.substring(0, 1),
                "Avatar initial should be first letter of the name");
        soft.assertAll();
    }

    @Test(priority = 4, description = "Clicking the profile chip opens the profile menu")
    public void verifyProfileMenuOpens() {
        dashboard.openProfileMenu();
        Assert.assertTrue(dashboard.isProfileMenuExpanded(), "aria-expanded should become true");
    }

    // ------------------------------------------------------------------
    // 3. Sidebar
    // ------------------------------------------------------------------
    @Test(priority = 5, description = "All sidebar entries exist with the right links; Dashboard is active")
    public void verifySidebarMenuItems() {
        Map<String, String> links = new LinkedHashMap<>();
        links.put("Dashboard", "/dashboard");
        links.put("Employee", "/dashboard/employees");
        links.put("Leave Balance", "/dashboard/leave-balances");
        links.put("Onboarding", "/dashboard/onboarding");
        links.put("Attendance", "/dashboard/employee-attendance");
        links.put("Leave", "/dashboard/leave-requests");
        links.put("Vendors", "/dashboard/vendors");

        List<String> titles = dashboard.getNavItemTitles();
        SoftAssert soft = new SoftAssert();

        for (Map.Entry<String, String> entry : links.entrySet()) {
            soft.assertTrue(titles.contains(entry.getKey()), "Missing sidebar link: " + entry.getKey());
            if (titles.contains(entry.getKey())) {
                soft.assertEquals(dashboard.getNavItemHref(entry.getKey()), entry.getValue(),
                        "href of " + entry.getKey());
            }
        }
        // Expandable groups (buttons, no href)
        for (String group : new String[] { "Reports", "Master", "Settings" }) {
            soft.assertTrue(titles.contains(group), "Missing sidebar group: " + group);
        }
        soft.assertTrue(dashboard.isNavItemActive("Dashboard"), "Dashboard should be the active item");
        soft.assertFalse(dashboard.isNavItemActive("Employee"), "Employee should not be active on the dashboard");
        soft.assertAll();
    }

    @Test(priority = 6, description = "Each sidebar link opens its page and highlights as active")
    public void verifySidebarNavigation() {
        String[][] pages = {
                { "Employee", "/dashboard/employees" },
                { "Leave Balance", "/dashboard/leave-balances" },
                { "Onboarding", "/dashboard/onboarding" },
                { "Attendance", "/dashboard/employee-attendance" },
                { "Leave", "/dashboard/leave-requests" },
                { "Vendors", "/dashboard/vendors" } };

        SoftAssert soft = new SoftAssert();
        for (String[] page : pages) {
            dashboard.openNavItem(page[0]);
            soft.assertTrue(dashboard.waitForPath(page[1]), page[0] + " should open " + page[1]);
            soft.assertTrue(dashboard.isNavItemActive(page[0]), page[0] + " should be highlighted as active");

            dashboard.openNavItem("Dashboard");
            soft.assertTrue(dashboard.waitForPath("/dashboard"), "Back to /dashboard from " + page[0]);
        }
        soft.assertAll();
    }

    @Test(priority = 7, description = "Sidebar profile block shows the user and links to /dashboard/profile")
    public void verifySidebarProfileBlock() {
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(dashboard.getSidebarProfileName(), expectedUserName, "Sidebar profile name");
        soft.assertEquals(dashboard.getSidebarProfileRole(), expectedUserRole, "Sidebar profile role");
        soft.assertEquals(dashboard.getSidebarProfileHref(), "/dashboard/profile", "Profile link href");
        soft.assertTrue(dashboard.isSidebarProfileImageLoaded(), "Profile photo should load (not broken)");
        soft.assertAll();

        dashboard.openSidebarProfile();
        Assert.assertTrue(dashboard.waitForPath("/dashboard/profile"), "Should open the profile page");
    }

    @Test(priority = 8, description = "Sidebar expands when the logo button is clicked, and collapses again")
    public void verifySidebarExpandCollapse() {
        // The sidebar loads collapsed (aria-expanded="false")
        Assert.assertFalse(dashboard.isSidebarExpanded(), "Sidebar should start collapsed");

        dashboard.toggleSidebar();
        Assert.assertTrue(dashboard.waitForSidebarState(true), "Sidebar should expand");

        dashboard.toggleSidebar();
        Assert.assertTrue(dashboard.waitForSidebarState(false), "Sidebar should collapse again");
    }

    // ------------------------------------------------------------------
    // 4. Attendance clock card
    // ------------------------------------------------------------------
    @Test(priority = 9, description = "Clock card shows status, time, today's work and a matching button")
    public void verifyClockCard() {
        String status = dashboard.getClockStatus();
        String button = dashboard.getClockButtonText();

        SoftAssert soft = new SoftAssert();
        soft.assertTrue(dashboard.getClockTime().matches("\\d{2}:\\d{2}:\\d{2}"),
                "Clock time should look like HH:mm:ss but was: " + dashboard.getClockTime());
        soft.assertTrue(dashboard.getTodaysWork().matches("\\d+h \\d+m"),
                "Today's work should look like '0h 0m' but was: " + dashboard.getTodaysWork());

        // Status and button must agree with each other
        if (status.contains("Not Clocked In")) {
            soft.assertEquals(button, "Clock In", "Button when not clocked in");
        } else {
            soft.assertEquals(button, "Clock Out", "Button when already clocked in. Status was: " + status);
        }
        soft.assertAll();
    }

    @Test(priority = 10, description = "Live clock keeps ticking")
    public void verifyClockTicks() {
        String before = dashboard.getClockTime();
        Assert.assertTrue(dashboard.waitForClockToChange(before),
                "Clock should change within 5 seconds. Stuck at: " + before);
    }

    /**
     * DISABLED on purpose: this creates a real attendance record for the user.
     * Enable it only on a test/staging account, and note it cannot be repeated on the
     * same day without the record being reset.
     */
    @Test(priority = 11, enabled = false, description = "Clock In flips status and button")
    public void verifyClockIn() {
        Assert.assertEquals(dashboard.getClockButtonText(), "Clock In");
        dashboard.clickClockButton();
        // TODO: confirm with the dev team what the UI shows after clock-in, then assert it here
        // (status text, button text, toast message).
    }

    // ------------------------------------------------------------------
    // 5. Team attendance
    // ------------------------------------------------------------------
    @Test(priority = 12, description = "Team attendance numbers are valid and consistent")
    public void verifyTeamAttendance() {
        int ringPresent = dashboard.getTeamRingPresent();
        int ringTotal = dashboard.getTeamRingTotal();
        int present = dashboard.getTeamMetric("Present");
        int leave = dashboard.getTeamMetric("Leave requests");
        int late = dashboard.getTeamMetric("Late marks");
        int absent = dashboard.getTeamMetric("Absent");

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(dashboard.getTeamAttendanceHeading(), "Team Attendance", "Panel heading");
        soft.assertEquals(ringPresent, present, "Ring numerator should equal the 'Present' count");
        soft.assertTrue(ringPresent <= ringTotal, "Present cannot exceed total employees");
        // ASSUMPTION: every employee is either present or absent. If your business rules count
        // people on approved leave separately, remove or adjust this line.
        soft.assertEquals(present + absent, ringTotal, "Present + Absent should equal total employees");
        soft.assertTrue(leave >= 0 && late >= 0, "Counts cannot be negative");
        soft.assertAll();
    }

    // ------------------------------------------------------------------
    // 6. People moments cards
    // ------------------------------------------------------------------
    @Test(priority = 13, description = "Birthdays, Anniversaries and Announcements cards show data or an empty state")
    public void verifyPeopleMomentCards() {
        SoftAssert soft = new SoftAssert();

        String[] celebrationCards = { "Upcoming Birthdays", "Upcoming Anniversaries" };
        for (String card : celebrationCards) {
            soft.assertTrue(dashboard.isCardDisplayed(card), card + " card should be displayed");
            int count = dashboard.getCardBadgeCount(card);
            String empty = dashboard.getCardEmptyMessage(card);
            if (count == 0) {
                soft.assertEquals(empty, "No celebrations this month", card + " empty-state message");
            } else {
                soft.assertEquals(empty, "", card + " has " + count + " upcoming, so no empty message expected");
            }
        }

        soft.assertTrue(dashboard.isCardDisplayed("Announcements"), "Announcements card should be displayed");
        // Empty state text is only checked if present; real announcements make it disappear.
        String announcementsEmpty = dashboard.getCardEmptyMessage("Announcements");
        if (!announcementsEmpty.isEmpty()) {
            soft.assertEquals(announcementsEmpty, "No announcements", "Announcements empty-state message");
        }
        soft.assertAll();
    }

    @Test(priority = 14, description = "Upcoming Holidays badge matches the list, and every date is today or later")
    public void verifyUpcomingHolidays() {
        List<String> names = dashboard.getHolidayNames();
        List<String> dates = dashboard.getHolidayDates();

        SoftAssert soft = new SoftAssert();
        soft.assertTrue(dashboard.isCardDisplayed("Upcoming Holidays"), "Holidays card should be displayed");
        soft.assertEquals(dashboard.getCardBadgeCount("Upcoming Holidays"), names.size(),
                "Badge count should equal number of holidays listed");
        soft.assertEquals(dates.size(), names.size(), "Every holiday should have a date");

        DateTimeFormatter format = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH);
        for (String date : dates) {
            try {
                LocalDate holiday = LocalDate.parse(date, format);
                soft.assertFalse(holiday.isBefore(LocalDate.now()),
                        "'Upcoming' holiday is in the past: " + date);
            } catch (Exception e) {
                soft.fail("Holiday date not in 'd MMMM yyyy' format: " + date);
            }
        }
        soft.assertAll();
    }

    // ------------------------------------------------------------------
    // 7. Logout / session
    // ------------------------------------------------------------------
    @Test(priority = 15, description = "Logout leaves the dashboard, and the dashboard URL is protected afterwards")
    public void verifyLogoutAndProtectedRoute() {
        dashboard.logout();
        Assert.assertTrue(dashboard.waitUntilLeftDashboard(), "Logout should leave /dashboard");

        // Try to go straight back to the dashboard without a session
        driver.navigate().to(dashboardUrl);
        Assert.assertTrue(dashboard.waitUntilLeftDashboard(),
                "Opening " + dashboardUrl + " after logout should redirect to login, but stayed on: "
                        + driver.getCurrentUrl());
    }
}