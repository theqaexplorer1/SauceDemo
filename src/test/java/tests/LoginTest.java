package tests;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductsPage;

import static org.testng.AssertJUnit.assertEquals;

/**
 * Тесты для страницы логина.
 * Все тесты используют паттерн Chain of Invocations:
 * new LoginPage(driver).open().login(...)
 * Каждая страница проверяет свою загрузку через isPageLoaded()
 */
public class LoginTest extends BaseTest{

    @Test(groups = {"smoke", "regression", "login"},
            description = "Позитивный тест: вход с валидными данными",
            testName = "Позитивный тест: вход с валидными данными")
    @Description("Позитивный тест: вход с валидными данными")
    @TmsLink("QASE-101")
    @Issue("BUG-001")
    @Story("Successful login")
    @Feature("Login Form")
    @Epic("Authentication")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("ivan.ivanov")
    @Link(name = "SauceDemo", url = "https://saucedemo.com")
    public void checkLoginWithPositiveCreds() {
        // Chain of Invocations:
        // 1. new LoginPage(driver) - создаём объект страницы
        // 2. .open() - открываем URL и ждём isPageLoaded() -> возвращает LoginPage (this)
        // 3. .login(...) - вводим креды, кликаем Login -> возвращает ProductsPage
        ProductsPage productsPage = new LoginPage(driver)
                .open()
                .login(USERNAME, PASSWORD);

        assertEquals(productsPage.getTitle(), "Products");
    }

    @Test(groups = {"regression", "login", "negative"},
            description = "Негативный тест: пустой логин",
            testName = "Негативный тест: пустой логин")
    @Description("Негативный тест: проверка валидации при пустом логине")
    @TmsLink("QASE-102")
    @Issue("BUG-102")
    @Story("Login validation")
    @Feature("Login Form")
    @Epic("Authentication")
    @Severity(SeverityLevel.NORMAL)
    @Owner("ivan.ivanov")
    @Link(name = "SauceDemo", url = "https://saucedemo.com")
    public void checkLoginWithEmptyUser() {
        // Логин не удался → метод login() не вернёт ProductsPage
        // Поэтому работаем с тем же объектом LoginPage
        LoginPage loginPage = new LoginPage(driver).open();
        loginPage.login("", PASSWORD);  // Пустой логин
        assertEquals(loginPage.getErrorMessage(), "Epic sadface: Username is required");
    }

    @Test(groups = {"regression", "login", "negative"},
            description = "Негативный тест: пустой пароль",
            testName = "Негативный тест: пустой пароль")
    @Description("Негативный тест: проверка валидации при пустом пароле")
    @TmsLink("QASE-103")
    @Issue("BUG-103")
    @Story("Login validation")
    @Feature("Login Form")
    @Epic("Authentication")
    @Severity(SeverityLevel.NORMAL)
    @Owner("ivan.ivanov")
    @Link(name = "SauceDemo", url = "https://saucedemo.com")
    public void checkLoginWithEmptyPassword() {
        LoginPage loginPage = new LoginPage(driver).open();
        loginPage.login(USERNAME, "");  // Пустой пароль
        assertEquals(loginPage.getErrorMessage(), "Epic sadface: Password is required");
    }

    @Test(groups = {"regression", "login", "negative"},
            description = "Негативный тест: неверный логин",
            testName = "Негативный тест: неверный логин")
    @Description("Негативный тест: проверка ошибки при неверном логине")
    @TmsLink("QASE-104")
    @Issue("BUG-104")
    @Story("Login validation")
    @Feature("Login Form")
    @Epic("Authentication")
    @Severity(SeverityLevel.NORMAL)
    @Owner("ivan.ivanov")
    @Link(name = "SauceDemo", url = "https://saucedemo.com")
    public void checkLoginWithNegativeUser() {
        LoginPage loginPage = new LoginPage(driver).open();
        loginPage.login("ABC", PASSWORD);  // Неверный логин
        assertEquals(loginPage.getErrorMessage(), "Epic sadface: Username and password do not match " +
                "any user in this service");
    }

    @Test(groups = {"regression", "login", "negative"},
            description = "Негативный тест: неверный пароль",
            testName = "Негативный тест: неверный пароль")
    @Description("Негативный тест: проверка ошибки при неверном пароле")
    @TmsLink("QASE-105")
    @Issue("BUG-105")
    @Story("Login validation")
    @Feature("Login Form")
    @Epic("Authentication")
    @Severity(SeverityLevel.NORMAL)
    @Owner("ivan.ivanov")
    @Link(name = "SauceDemo", url = "https://saucedemo.com")
    public void checkLoginWithNegativePassword() {
        LoginPage loginPage = new LoginPage(driver).open();
        loginPage.login(USERNAME, "ABC");  // Неверный пароль
        assertEquals(loginPage.getErrorMessage(), "Epic sadface: Username and password do not match " +
                "any user in this service");
    }

    /**
     * DataProvider: набор данных для параметризованных тестов
     * Возвращает массив: [логин, пароль, ожидаемое сообщение об ошибке]
     */
    @DataProvider(name = "Тестовые данные для негативного логина")
    public Object[][] loginData() {
        return new Object[][] {
                {"", PASSWORD, "Epic sadface: Username is required"},
                {USERNAME, "", "Epic sadface: Password is required"},
                {"ABC", PASSWORD, "Epic sadface: Username and password do not match any user in this service"},
                {USERNAME, "ABC", "Epic sadface: Username and password do not match any user in this service"}
        };
    }

    @Test(dataProvider = "Тестовые данные для негативного логина")
    @Description("Параметризованный негативный тест логина")
    @TmsLink("QASE-106")
    @Issue("BUG-106")
    @Story("Login validation")
    @Feature("Login Form")
    @Epic("Authentication")
    @Severity(SeverityLevel.NORMAL)
    @Owner("ivan.ivanov")
    @Link(name = "SauceDemo", url = "https://saucedemo.com")
    public void negativeLogin(String user, String password, String errorMessage) {
        LoginPage loginPage = new LoginPage(driver).open();
        loginPage.login(user, password);
        assertEquals(loginPage.getErrorMessage(), errorMessage);
    }
}