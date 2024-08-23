package com.cricbuzz.base;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;

public class BaseTest {
    protected AndroidDriver driver;
    AppiumDriverLocalService service;

    public void startServer() {
        service = new AppiumServiceBuilder()
                .withIPAddress("127.0.0.1")
                .usingPort(4723)
                .build();
        service.start();
    }

    public void setUp() throws MalformedURLException {
        startServer();

//        UiAutomator2Options options = new UiAutomator2Options()
//                .setPlatformName("Android")
//                .setDeviceName("RZ8R31BQM5A")
//                .setApp("/home/subhadip-das/Downloads/APK/app-production-release.apk")
//                .setAutomationName("UiAutomator2");
//                .options.autoGrantPermissions();

        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setDeviceName("RZ8R31BQM5A")
                .setApp("/home/subhadip-das/Downloads/APK/app-production-debug.apk")
                .setAutomationName("UiAutomator2")
                .autoGrantPermissions(); // Automatically grant all permissions on install
        driver = new AndroidDriver(service.getUrl(), options);
    }

    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        if (service != null) {
            service.stop();
        }
    }
}
