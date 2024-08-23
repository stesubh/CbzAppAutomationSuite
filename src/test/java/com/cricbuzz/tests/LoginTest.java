package com.cricbuzz.tests;

import com.cricbuzz.base.BaseTest;
import com.google.common.collect.ImmutableMap;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.time.Duration;
import java.util.List;

public class LoginTest extends BaseTest {

    private static final int EXPECTED_CAROUSEL_COUNT = 6; // Update this with the correct expected count

    @BeforeMethod
    public void setUp() throws MalformedURLException {
        super.setUp();
    }

    @Test(priority = 1)
    public void carouselVerification() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Login Steps
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.cricbuzz.android:id/tvLogin")));
        loginButton.click();

        WebElement socialLogin = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//android.view.ViewGroup[@resource-id=\"com.cricbuzz.android:id/constraintLayout\"])[1]")));
        socialLogin.click();

        WebElement backButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//android.widget.ImageButton[@content-desc=\"Navigate up\"]")));
        backButton.click();

        int totalCarouselCount = 0; // Initialize total count
        int swipeCount = 6; // Adjust the number of swipes

        // Swipe action to navigate through carousels
        for (int i = 0; i < swipeCount; i++) {
            List<WebElement> carousels = driver.findElements(By.xpath("//android.widget.TextView[@text='SCHEDULE']"));
            int carouselsFound = carousels.size();
            totalCarouselCount += carouselsFound; // Accumulate total count

            System.out.println("Found " + carouselsFound + " carousels on swipe " + (i + 1));

            if (i < swipeCount - 1) { // Avoid swiping after the last iteration
                swipeLeft(carousels.get(0)); // Swipe the first carousel element
            }
        }

        System.out.println("Total number of carousels found: " + totalCarouselCount);

        if (totalCarouselCount == EXPECTED_CAROUSEL_COUNT) {
            System.out.println("Carousel count matches expected data.");
        } else {
            System.out.println("Carousel count mismatch!");
            throw new AssertionError("Expected " + EXPECTED_CAROUSEL_COUNT + " carousels but found " + totalCarouselCount);
        }
    }

    private void swipeLeft(WebElement carousel) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("mobile: swipeGesture", ImmutableMap.of(
                "elementId", ((RemoteWebElement) carousel).getId(),
                "direction", "left",
                "percent", 0.75
        ));

        // Improved Wait Strategy - Wait for specific element after swipe
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(org.openqa.selenium.WebDriver driver) {
                List<WebElement> updatedElements = driver.findElements(By.xpath("//android.widget.TextView[@text='SCHEDULE']"));
                return !updatedElements.isEmpty(); // Return true if the element is found
            }
        });
    }

    @AfterMethod
    public void tearDown() {
        super.tearDown();
    }
}
