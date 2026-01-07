package FoodFrenzyUserPurchase;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;


public class KatalonTest {

    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    JavascriptExecutor js;

    @BeforeEach
    void setUp() {
        // FORCE Selenium to skip Selenium Manager
        System.setProperty("webdriver.chrome.disableSeleniumManager", "true");

        // FORCE WebDriverManager
        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        js = (JavascriptExecutor) driver;
    }

    @Test
    void testKatalon() {
        driver.get("http://localhost:8080/register");
        driver.findElement(By.id("u_id")).clear();
        driver.findElement(By.id("u_id")).sendKeys("99");
        driver.findElement(By.id("uemail")).clear();
        driver.findElement(By.id("uemail")).sendKeys("test99@test.com");
        driver.findElement(By.id("uname")).clear();
        driver.findElement(By.id("uname")).sendKeys("test99");
        driver.findElement(By.id("unumber")).clear();
        driver.findElement(By.id("unumber")).sendKeys("9999999999");
        driver.findElement(By.id("upassword")).clear();
        driver.findElement(By.id("upassword")).sendKeys("Aa@123456");
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        driver.get("http://localhost:8080/login");
//        driver.findElement(By.id("email")).clear();
//        driver.findElement(By.id("email")).sendKeys("test99@test.com");
//        driver.findElement(By.id("password")).clear();
//        driver.findElement(By.id("password")).sendKeys("1234");

        driver.findElement(By.id("userEmail")).clear();
        driver.findElement(By.id("userEmail")).sendKeys("test99@test.com");
        driver.findElement(By.id("userPassword")).clear();
        driver.findElement(By.id("userPassword")).sendKeys("Aa@123456");
        driver.findElement(By.xpath(
                "(.//*[normalize-space(text()) and normalize-space(.)='Password:'])[2]/following::button[1]"
        )).click();

        driver.findElement(By.name("productName")).clear();
        driver.findElement(By.name("productName")).sendKeys("Olive Oil");
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        driver.findElement(By.name("oQuantity")).clear();
        driver.findElement(By.name("oQuantity")).sendKeys("1");
        driver.findElement(By.xpath(
                "(.//*[normalize-space(text()) and normalize-space(.)='SEARCH'])[1]/following::button[1]"
        )).click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
        driver.findElement(By.linkText("Back")).click();

        driver.findElement(By.name("productName")).clear();
        driver.findElement(By.name("productName")).sendKeys("Chicken Breast");
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        driver.findElement(By.name("oQuantity")).clear();
        driver.findElement(By.name("oQuantity")).sendKeys("1");
        driver.findElement(By.xpath(
                "(.//*[normalize-space(text()) and normalize-space(.)='SEARCH'])[1]/following::button[1]"
        )).click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
        driver.findElement(By.linkText("Back")).click();

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
        }
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        String verificationErrorString = verificationErrors.toString();
        if (!verificationErrorString.isEmpty()) {
            Assertions.fail(verificationErrorString);
        }
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}
