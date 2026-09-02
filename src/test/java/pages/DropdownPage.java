package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

/**
 * Page Object for the-internet.herokuapp.com/dropdown.
 */
public class DropdownPage {

    private final WebDriver driver;
    private final By dropdown = By.id("dropdown");

    public DropdownPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get("https://the-internet.herokuapp.com/dropdown");
    }

    public void selectOption(String visibleText) {
        Select select = new Select(driver.findElement(dropdown));
        select.selectByVisibleText(visibleText);
    }

    public String getSelectedOptionText() {
        Select select = new Select(driver.findElement(dropdown));
        return select.getFirstSelectedOption().getText();
    }
}