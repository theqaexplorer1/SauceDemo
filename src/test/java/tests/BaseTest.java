package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import pages.CartPage;
import pages.LoginPage;
import pages.ProductsPage;
import utils.TestListener;

import java.time.Duration;
import java.util.HashMap;

@Listeners(TestListener.class)
public class BaseTest {

    protected WebDriver driver;
    protected LoginPage loginPage;
    protected ProductsPage productsPage;
    protected CartPage cartPage;
    protected SoftAssert softAssert = new SoftAssert();
    // Основной пользователь для позитивных тестов
    protected static final String USERNAME = "standard_user";
    protected static final String PASSWORD = "secret_sauce";
    // Пользователи для негативных тестов (по необходимости)
    protected static final String LOCKED_OUT_USER = "locked_out_user";
    protected static final String PROBLEM_USER = "problem_user";
    protected static final String ERROR_USER = "error_user";
    protected static final String VISUAL_USER = "visual_user";

    @Parameters({"browser"})
    @BeforeMethod (alwaysRun = true)
    public void setUp(@Optional("chrome") String browser) {
        // Инициализация драйвера в зависимости от браузера
        if (browser.equalsIgnoreCase("firefox")) {
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            //firefoxOptions.addArguments("--headless");  // опционально
            driver = new FirefoxDriver(firefoxOptions);
            driver.manage().window().maximize();
        } else {
            // Chrome по умолчанию
            ChromeOptions chromeOptions = new ChromeOptions();
            HashMap<String, Object> chromePrefs = new HashMap<>();
            chromePrefs.put("credentials_enable_service", false);
            chromePrefs.put("profile.password_manager_enabled", false);
            chromeOptions.setExperimentalOption("prefs", chromePrefs);
            chromeOptions.addArguments("--incognito");
            chromeOptions.addArguments("--disable-notifications");
            chromeOptions.addArguments("--disable-popup-blocking");
            chromeOptions.addArguments("--disable-infobars");
            driver = new ChromeDriver(chromeOptions);
            //DriverManager.setDriver(driver);//1.03 xunit video
        }
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