package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;
import pages.CartPage;
import pages.LoginPage;
import pages.ProductsPage;
import java.time.Duration;
import java.util.HashMap;

public class BaseTest {

    WebDriver driver;
    LoginPage loginPage;
    ProductsPage productsPage;
    CartPage cartPage;
    protected SoftAssert softAssert = new SoftAssert();
    // Основной пользователь для позитивных тестов
    protected static final String USERNAME = "standard_user";
    protected static final String PASSWORD = "secret_sauce";
    // Пользователи для негативных тестов (по необходимости)
    protected static final String LOCKED_OUT_USER = "locked_out_user";
    protected static final String PROBLEM_USER = "problem_user";
    protected static final String ERROR_USER = "error_user";
    protected static final String VISUAL_USER = "visual_user";

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("credentials_enable_service", false);
        chromePrefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", chromePrefs);
        options.addArguments("--incognito");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        loginPage = new LoginPage(driver);
        productsPage = new ProductsPage(driver);
        cartPage = new CartPage(driver);
    }

    @AfterMethod (alwaysRun = true)
    public void tearDown() {
        driver.quit();
        softAssert.assertAll();
    }

    protected void loginAsStandardUser() {
        loginPage.open();
        loginPage.login(USERNAME, PASSWORD);
    }
}