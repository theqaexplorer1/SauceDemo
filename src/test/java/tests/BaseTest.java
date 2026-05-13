package tests;

import io.qameta.allure.Description;
import io.qameta.allure.testng.AllureTestNg;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestContext;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import pages.BasePage;
import utils.TestListener;

import java.time.Duration;
import java.util.HashMap;

/**
 * Базовый класс для всех тестов.
 * Отвечает за:
 * - Настройку драйвера (Chrome/Firefox)
 * - Инициализацию до/после каждого теста
 * - Хранение общих данных (креды, SoftAssert)
 *
 * Убраны глобальные поля страниц (loginPage, productsPage и т.д.)
 * Теперь страницы создаются локально в тестах через цепочку вызовов.
 */
@Listeners({AllureTestNg.class, TestListener.class})
public class BaseTest {

    protected WebDriver driver;
    protected SoftAssert softAssert = new SoftAssert();
    // Основной пользователь для позитивных тестов
    protected static final String USERNAME = "standard_user";
    protected static final String PASSWORD = "secret_sauce";
    // Пользователи для негативных тестов (по необходимости)
    protected static final String LOCKED_OUT_USER = "locked_out_user";
    protected static final String PROBLEM_USER = "problem_user";
    protected static final String ERROR_USER = "error_user";
    protected static final String VISUAL_USER = "visual_user";

    /**
     * Выполняется ПЕРЕД каждым @Test методом.
     * Настраивает браузер и драйвер.
     *
     * @param browser имя браузера из testng.xml (chrome/firefox)
     * @param iTestContext контекст теста (для передачи драйвера в Listener)
     */
    @Parameters({"browser"})
    @BeforeMethod (alwaysRun = true)
    @Description("Настройка браузера")
    public void setUp(@Optional("chrome") String browser, ITestContext iTestContext) {
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
        //Неявное ожидание : ждать элемент до 10 секун
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        // Сохраняем драйвер для скриншотов в TestListener
        iTestContext.setAttribute("driver", driver);
    }

    /**
     * Выполняется ПОСЛЕ каждого @Test метода.
     * Закрывает браузер и собирает результаты мягких проверок.
     */
    @AfterMethod (alwaysRun = true)
    @Description("Закрытие браузера")
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        softAssert.assertAll();
    }

    /**
     * Вспомогательный метод для быстрого логина.
     * Возвращает ProductsPage, чтобы можно было продолжить цепочку вызовов.
     * Пример использования в тесте:
     * ProductsPage products = loginAsStandardUser().addToCart(0);
     * @return ProductsPage — страница товаров после успешного логина
     */
    protected pages.ProductsPage loginAsStandardUser() {
        return new pages.LoginPage(driver)  // 1. Создаём LoginPage
                .open()                      // 2. Открываем и ждём загрузки → возвращает LoginPage (this)
                .login(USERNAME, PASSWORD);  // 3. Логинимся → возвращает ProductsPage (новая страница)
    }

    /**
     * Вспомогательный метод для логина с произвольными кредами.
     * Возвращает либо ProductsPage (успех), либо LoginPage (ошибка).
     * Использование в тесте:
     *   BasePage result = loginWithCredentials(user, pass);
     *   if (result instanceof ProductsPage) { ... }
     */
    protected BasePage loginWithCredentials(String username, String password) {
        pages.LoginPage loginPage = new pages.LoginPage(driver).open();
        return loginPage.login(username, password);  // Вернёт ProductsPage или LoginPage
    }
}