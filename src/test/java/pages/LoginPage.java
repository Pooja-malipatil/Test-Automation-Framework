package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Page Object for the-internet.herokuapp.com/login.
 * Encapsulates the page's locators and actions so tests don't
 * talk to Selenium directly -- they talk to this page's API instead.
 */
public class LoginPage {

    private final WebDriver driver;

    // Locators live here, in one place, instead of scattered across tests.
    private final By usernameField = By.id("username");
    private final By passwordField = By.id("password");
    private final By submitButton = By.cssSelector("button[type='submit']");
    private final By flashMessage = By.id("flash");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get("https://the-internet.herokuapp.com/login");
    }

    public void login(String username, String password) {
        driver.findElement(usernameField).sendKeys(username);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(submitButton).click();
    }

    public String getFlashMessageText() {
        WebElement flash = driver.findElement(flashMessage);
        return flash.getText();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}