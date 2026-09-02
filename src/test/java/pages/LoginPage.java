package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import listeners.RetryAnalyzer;

import java.time.Duration;

/**
 * Page Object for the-internet.herokuapp.com/login.
 */
public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By usernameField = By.id("username");
    private final By passwordField = By.id("password");
    private final By submitButton = By.cssSelector("button[type='submit']");
    private final By flashMessage = By.id("flash");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        // Wait up to 10 seconds for a condition before giving up --
        // far more reliable than assuming elements appear instantly.
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        driver.get("https://the-internet.herokuapp.com/login");
    }

    public void login(String username, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField))
                .sendKeys(username);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(submitButton).click();
    }

    public String getFlashMessageText() {
        // Wait for the flash message to actually be visible before reading it.
        WebElement flash = wait.until(ExpectedConditions.visibilityOfElementLocated(flashMessage));
        return flash.getText();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}