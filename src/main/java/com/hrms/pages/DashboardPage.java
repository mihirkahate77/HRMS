
package com.hrms.pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.hrms.utils.ConfigReader;

public class DashboardPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Organization selection page
    private static final By ACCESS_BUTTON = By.xpath(
            "//button[contains(@class,'accessButton') and .//span[normalize-space()='Access Dashboard']]"
    );

    // Dashboard page
    private static final By DASHBOARD_HEADING =
            By.xpath("//h1[normalize-space()='HR Dashboard']");

    private static final By DASHBOARD_SECTION =
            By.cssSelector("section[aria-label='HR manager dashboard']");

    private static final By PAGE_HEADING =
            By.xpath("//section[@aria-label='HR manager dashboard']//h1");

    private static final By WELCOME_TEXT =
            By.xpath("//h1[starts-with(normalize-space(), 'Welcome')]");

    private static final By DATE_TEXT =
            By.xpath("//h1[starts-with(normalize-space(), 'Welcome')]/following-sibling::p");

    private static final By WORKSPACE_NAME =
            By.cssSelector("[class*='activeWorkspaceName']");

    private static final By WORKSPACE_LOGO =
            By.cssSelector("img[class*='activeWorkspaceLogo']");

    private static final By PROFILE_MENU_BUTTON =
            By.cssSelector("button[aria-haspopup='true']");

    private static final By PROFILE_INITIAL =
            By.cssSelector("button[aria-haspopup='true'] span[class$='__avatar']");

    private static final By PROFILE_NAME =
            By.cssSelector("button[aria-haspopup='true'] [class*='avatarName']");

    private static final By PROFILE_ROLE =
            By.cssSelector("button[aria-haspopup='true'] [class*='avatarRole']");

    // Sidebar
    private static final By SIDEBAR = By.cssSelector("aside");

    private static final By SIDEBAR_TOGGLE =
            By.cssSelector("aside button[aria-label*='sidebar']");

    private static final By NAV_ITEMS =
            By.cssSelector("nav[aria-label='Dashboard navigation'] [title]");

    private static final By SIDEBAR_PROFILE_LINK =
            By.cssSelector("aside a[href='/dashboard/profile']");

    private static final By SIDEBAR_PROFILE_IMAGE =
            By.cssSelector("aside a[href='/dashboard/profile'] img");

    private static final By SIDEBAR_PROFILE_NAME =
            By.cssSelector("aside [class*='sidebarProfileName']");

    private static final By SIDEBAR_PROFILE_ROLE =
            By.cssSelector("aside [class*='sidebarProfileRole']");

    private static final By LOGOUT_BUTTON =
            By.cssSelector("button[aria-label='Logout']");

    // Attendance clock card
    private static final String CLOCK_CARD =
            "article[class*='AttendanceClockCard'] ";

    private static final By CLOCK_STATUS =
            By.cssSelector(CLOCK_CARD + "[class*='statusPill']");

    private static final By CLOCK_TIME =
            By.cssSelector(CLOCK_CARD + "[class*='clockRing'] strong");

    private static final By CLOCK_RING_LABEL =
            By.cssSelector(CLOCK_CARD + "[class*='clockRing'] span");

    private static final By TODAYS_WORK =
            By.cssSelector(CLOCK_CARD + "[class*='workSummary'] strong");

    private static final By CLOCK_BUTTON =
            By.cssSelector(CLOCK_CARD + "button[class*='clockButton']");

    // Team attendance panel
    private static final String TEAM_PANEL =
            "article[class*='teamAttendancePanel'] ";

    private static final By TEAM_HEADING =
            By.cssSelector(TEAM_PANEL + "h2");

    private static final By TEAM_RING_VALUE =
            By.cssSelector(TEAM_PANEL + "[class*='attendanceRing'] strong");

    private static final By TEAM_RING_TOTAL =
            By.cssSelector(TEAM_PANEL + "[class*='attendanceRing'] span");


    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Common helpers

    private WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private String textOf(By locator) {
        return visible(locator).getText().trim();
    }

    private static By navItem(String title) {
        return By.cssSelector(
                "nav[aria-label='Dashboard navigation'] [title='" + title + "']");
    }

    private static By cardByTitle(String title) {
        return By.xpath(
                "//h2[normalize-space()='" + title + "']/ancestor::article[1]");
    }

    private boolean imageLoaded(By locator) {
        List<WebElement> found = driver.findElements(locator);

        if (found.isEmpty()) {
            return false;
        }

        Object result = ((JavascriptExecutor) driver).executeScript(
                "return arguments[0].complete && arguments[0].naturalWidth > 0;",
                found.get(0));

        return Boolean.TRUE.equals(result);
    }

    private boolean waitForImage(By locator) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(d -> imageLoaded(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    // Page load

    public boolean isDashboardDisplayed() {
        String expected = ConfigReader.getProperty(
                "dashboard.url.contains", "/dashboard");

        return driver.getCurrentUrl().contains(expected);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageHeading() {
        return textOf(DASHBOARD_HEADING);
    }

    public String getBrowserTitle() {
        return driver.getTitle();
    }

    /**
     * Opens Orangebits organization dashboard.
     */
    public DashboardPage openDashboardFromOrganizationPage() {

        // If already outside organization selection page, do nothing
        if (!driver.getCurrentUrl().contains("/dashboard/organizations")) {
            return this;
        }

        try {
            // Wait for Access Dashboard buttons
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                    ACCESS_BUTTON));

            List<WebElement> buttons = driver.findElements(ACCESS_BUTTON);

            System.out.println(
                    "Access Dashboard buttons found: " + buttons.size());

            // Select Orangebits organization
            if (buttons.size() >= 2) {

                WebElement orangebitsButton = buttons.get(1);

                wait.until(ExpectedConditions.elementToBeClickable(
                        orangebitsButton));

                orangebitsButton.click();

                System.out.println("Orangebits organization selected.");

            } else {
                throw new RuntimeException(
                        "Expected 2 organization buttons but found: "
                                + buttons.size());
            }

            // Wait until organization selection page is closed
            wait.until(d -> !d.getCurrentUrl()
                    .contains("/dashboard/organizations"));

            System.out.println("Dashboard URL: " + driver.getCurrentUrl());

        } catch (TimeoutException e) {

            System.out.println("Current URL: " + driver.getCurrentUrl());
            System.out.println("Page title: " + driver.getTitle());

            System.out.println("Available buttons:");

            for (WebElement button : driver.findElements(By.tagName("button"))) {
                System.out.println("Button text: [" + button.getText() + "]");
            }

            throw new TimeoutException(
                    "Could not open dashboard from organization selection page.",
                    e);
        }

        return this;
    }

    public DashboardPage waitUntilLoaded() {
        openDashboardFromOrganizationPage();
        return this;
    }

    // Header

    public String getWelcomeText() {
        return textOf(WELCOME_TEXT);
    }

    public String getDisplayedDate() {
        return textOf(DATE_TEXT);
    }

    public String getWorkspaceName() {
        return textOf(WORKSPACE_NAME);
    }

    public boolean isWorkspaceLogoLoaded() {
        visible(WORKSPACE_LOGO);
        return waitForImage(WORKSPACE_LOGO);
    }

    public String getProfileInitial() {
        return textOf(PROFILE_INITIAL);
    }

    public String getProfileName() {
        return textOf(PROFILE_NAME);
    }

    public String getProfileRole() {
        return textOf(PROFILE_ROLE);
    }

    public void openProfileMenu() {
        wait.until(ExpectedConditions.elementToBeClickable(
                PROFILE_MENU_BUTTON)).click();
    }

    public boolean isProfileMenuExpanded() {
        try {
            wait.until(d -> "true".equals(
                    d.findElement(PROFILE_MENU_BUTTON)
                            .getDomAttribute("aria-expanded")));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    // Sidebar

    public boolean isSidebarDisplayed() {
        return visible(SIDEBAR).isDisplayed();
    }

    public List<String> getNavItemTitles() {
        visible(NAV_ITEMS);

        List<String> titles = new ArrayList<>();

        for (WebElement item : driver.findElements(NAV_ITEMS)) {
            titles.add(item.getDomAttribute("title"));
        }

        return titles;
    }

    public String getNavItemHref(String title) {
        return visible(navItem(title)).getDomAttribute("href");
    }

    public boolean isNavItemActive(String title) {
        String cls = visible(navItem(title)).getDomAttribute("class");

        return cls != null && cls.contains("activeNavLink");
    }

    public void openNavItem(String title) {
        wait.until(ExpectedConditions.elementToBeClickable(
                navItem(title))).click();
    }

    public boolean waitForPath(String path) {
        try {
            wait.until(d -> d.getCurrentUrl()
                    .split("[?#]")[0]
                    .replaceAll("/+$", "")
                    .endsWith(path));

            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isSidebarExpanded() {
        return "true".equals(
                visible(SIDEBAR_TOGGLE).getDomAttribute("aria-expanded"));
    }

    public void toggleSidebar() {
        wait.until(ExpectedConditions.elementToBeClickable(
                SIDEBAR_TOGGLE)).click();
    }

    public boolean waitForSidebarState(boolean expanded) {
        try {
            wait.until(d -> expanded == "true".equals(
                    d.findElement(SIDEBAR_TOGGLE)
                            .getDomAttribute("aria-expanded")));

            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getSidebarProfileName() {
        visible(SIDEBAR_PROFILE_LINK);

        return driver.findElement(SIDEBAR_PROFILE_NAME)
                .getDomProperty("textContent").trim();
    }

    public String getSidebarProfileRole() {
        visible(SIDEBAR_PROFILE_LINK);

        return driver.findElement(SIDEBAR_PROFILE_ROLE)
                .getDomProperty("textContent").trim();
    }

    public String getSidebarProfileHref() {
        return visible(SIDEBAR_PROFILE_LINK).getDomAttribute("href");
    }

    public boolean isSidebarProfileImageLoaded() {
        visible(SIDEBAR_PROFILE_IMAGE);
        return waitForImage(SIDEBAR_PROFILE_IMAGE);
    }

    public void openSidebarProfile() {
        wait.until(ExpectedConditions.elementToBeClickable(
                SIDEBAR_PROFILE_LINK)).click();
    }

    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(
                LOGOUT_BUTTON)).click();
    }

    public boolean waitUntilLeftDashboard() {
        try {
            wait.until(d -> !d.getCurrentUrl().contains("/dashboard"));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    // Attendance clock card

    public String getClockStatus() {
        return textOf(CLOCK_STATUS);
    }

    public String getClockTime() {
        return textOf(CLOCK_TIME);
    }

    public String getClockRingLabel() {
        return textOf(CLOCK_RING_LABEL);
    }

    public String getTodaysWork() {
        return textOf(TODAYS_WORK);
    }

    public String getClockButtonText() {
        return textOf(CLOCK_BUTTON);
    }

    public void clickClockButton() {
        wait.until(ExpectedConditions.elementToBeClickable(
                CLOCK_BUTTON)).click();
    }

    public boolean waitForClockToChange(String previousTime) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(d -> !d.findElement(CLOCK_TIME)
                            .getText().trim().equals(previousTime));

            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    // Team attendance panel

    public String getTeamAttendanceHeading() {
        return textOf(TEAM_HEADING);
    }

    public int getTeamRingPresent() {
        return Integer.parseInt(textOf(TEAM_RING_VALUE));
    }

    public int getTeamRingTotal() {
        return Integer.parseInt(
                textOf(TEAM_RING_TOTAL).replaceAll("\\D+", ""));
    }

    public int getTeamMetric(String label) {
        By value = By.xpath(
                "//article[contains(@class,'teamAttendancePanel')]"
                + "//span[normalize-space()='" + label
                + "']/preceding-sibling::strong");

        return Integer.parseInt(textOf(value));
    }

    // Dashboard cards

    public boolean isCardDisplayed(String title) {
        try {
            visible(cardByTitle(title));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public int getCardBadgeCount(String title) {
        By badge = By.xpath(
                "//h2[normalize-space()='" + title
                + "']/following-sibling::span");

        return Integer.parseInt(
                textOf(badge).replaceAll("\\D+", ""));
    }

    public String getCardEmptyMessage(String title) {
        By empty = By.xpath(
                "//h2[normalize-space()='" + title
                + "']/ancestor::article[1]"
                + "//p[contains(@class,'celebrationEmpty')]");

        List<WebElement> found = driver.findElements(empty);

        return found.isEmpty() ? "" : found.get(0).getText().trim();
    }

    public List<String> getHolidayNames() {
        return textsOf(By.xpath(
                "//h2[normalize-space()='Upcoming Holidays']"
                + "/ancestor::article[1]//h3"));
    }

    public List<String> getHolidayDates() {
        return textsOf(By.xpath(
                "//h2[normalize-space()='Upcoming Holidays']"
                + "/ancestor::article[1]//h3/following-sibling::p"));
    }

    private List<String> textsOf(By locator) {
        List<String> texts = new ArrayList<>();

        for (WebElement element : driver.findElements(locator)) {
            texts.add(element.getText().trim());
        }

        return texts;
    }
}
