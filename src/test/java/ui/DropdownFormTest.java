package ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DropdownPage;

import static org.testng.Assert.assertEquals;

/**
 * UI automation against the-internet.herokuapp.com/dropdown, refactored
 * to use the Page Object Model.
 */
public class DropdownFormTest {

    private WebDriver driver;
    private DropdownPage dropdownPage;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1280,800");

        driver = new ChromeDriver(options);
        dropdownPage = new DropdownPage(driver);
        dropdownPage.open();
    }

    @Test(description = "Selecting 'Option 2' from the dropdown updates the selected value")
    public void testSelectDropdownOption_updatesSelection() {
        dropdownPage.selectOption("Option 2");

        assertEquals(dropdownPage.getSelectedOptionText(), "Option 2",
                "Dropdown did not select the expected option");
    }

    @Test(description = "The dropdown's default state is the disabled placeholder option")
    public void testDropdownDefaultState_isPlaceholder() {
        assertEquals(dropdownPage.getSelectedOptionText(), "Please select an option",
                "Expected the placeholder option to be selected by default");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}