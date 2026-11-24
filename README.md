# HRMS Selenium Automation

Framework patterns adapted from the supplied Automation_Learning project: Maven, Selenium, TestNG lifecycle annotations, cross-browser setup, Page Object Model, TestNG listener, screenshots, and Apache POI Excel utility.

## Eclipse setup
1. Import this folder using **File > Import > Existing Maven Projects**.
2. Right-click project > **Maven > Update Project**.
3. Set Java compiler to Java 17 or newer.
4. In `src/test/resources/config.properties`, set the HRMS URL. Do not commit passwords.
5. Set environment variables `HRMS_USERNAME` and `HRMS_PASSWORD`, or enter local credentials in an untracked local config workflow.
6. Verify the three login CSS selectors against the actual HRMS page using Chrome DevTools. They are generic starting selectors, not confirmed selectors for your application.
7. Run `src/test/resources/testng.xml` as **TestNG Suite** or run `mvn test`.

## Cross-browser
Change the `browser` parameter in testng.xml to `chrome`, `firefox`, or `edge`. Selenium Manager normally resolves the driver automatically.

## Important
The uploaded project snapshot's `name=email` locator had already failed with `NoSuchElementException`. The actual login DOM was not included in the ZIP, so the replacement selectors in config.properties must be checked against your live login page before expecting the login test to pass. The dashboard path is carried over from your existing HRMS code and should also be verified.

Excel helper expects `testdata.xlsx` in `src/test/resources`, with a header row and a sheet name passed to `ExcelUtils.getSheetData()`.
