package ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;

import static org.testng.Assert.assertTrue;

/**
 * UI automation against the-internet.herokuapp.com/login, refactored to
 * use the Page Object Model: this class only orchestrates the test flow
 * and asserts outcomes -- it never touches locators directly.
 */
public class LoginTest {

    private WebDriver driver;
    private LoginPage loginPage;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1280,800");

        driver = new ChromeDriver(options);
        loginPage = new LoginPage(driver);
        loginPage.open();
    }

    @Test(description = "Valid credentials should log the user into the secure area")
    public void testValidLogin_showsSuccessMessage() {
        loginPage.login("tomsmith", "SuperSecretPassword!");

        assertTrue(loginPage.getFlashMessageText().contains("You logged into a secure area"),
                "Expected success message not found");
        assertTrue(loginPage.getCurrentUrl().contains("/secure"),
                "Expected to be redirected to the /secure page");
    }

    @Test(description = "Invalid credentials should show an error and keep the user on the login page")
    public void testInvalidLogin_showsErrorMessage() {
        loginPage.login("wrongUser", "wrongPassword");

        assertTrue(loginPage.getFlashMessageText().contains("Your username is invalid"),
                "Expected error message not found");
        assertTrue(loginPage.getCurrentUrl().contains("/login"),
                "Expected to remain on the login page after a failed login");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}