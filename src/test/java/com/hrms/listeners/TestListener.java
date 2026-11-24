package com.hrms.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.hrms.base.BaseTest;
import com.hrms.utils.ScreenshotUtils;

public class TestListener implements ITestListener {
    @Override public void onTestStart(ITestResult result) { System.out.println("STARTED: " + result.getName()); }
    @Override public void onTestSuccess(ITestResult result) { System.out.println("PASSED: " + result.getName()); }
    @Override public void onTestFailure(ITestResult result) {
        System.out.println("FAILED: " + result.getName());
        String path = ScreenshotUtils.capture(BaseTest.driver, result.getName());
        if (!path.isBlank()) System.out.println("Screenshot: " + path);
        result.getThrowable().printStackTrace();
    }
    @Override public void onTestSkipped(ITestResult result) { System.out.println("SKIPPED: " + result.getName()); }
    @Override public void onStart(ITestContext context) { System.out.println("SUITE STARTED: " + context.getName()); }
    @Override public void onFinish(ITestContext context) { System.out.println("SUITE FINISHED: " + context.getName()); }
}
