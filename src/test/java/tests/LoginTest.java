package tests;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.assertEquals;

public class LoginTest extends BaseTest{

    @Test(groups = {"smoke", "regression", "login"},
            description = "Позитивный тест: вход с валидными данными",
            testName = "Позитивный тест: вход с валидными данными")
    public void checkLoginWithPositiveCreds() {
        loginPage.open();
        loginPage.login(USERNAME, PASSWORD);
        assertEquals(productsPage.getTitle(), "Products");
    }

    @Test(groups = {"regression", "login", "negative"},
            description = "Негативный тест: пустой логин",
            testName = "Негативный тест: пустой логин")
    public void checkLoginWithEmptyUser() {
        loginPage.open();
        loginPage.login("", PASSWORD);
        assertEquals(loginPage.getErrorMessage(), "Epic sadface: Username is required");
    }

    @Test(groups = {"regression", "login", "negative"},
            description = "Негативный тест: пустой пароль",
            testName = "Негативный тест: пустой пароль")
    public void checkLoginWithEmptyPassword() {
        loginPage.open();
        loginPage.login(USERNAME, "");
        assertEquals(loginPage.getErrorMessage(), "Epic sadface: Password is required");
    }

    @Test(groups = {"regression", "login", "negative"},
            description = "Негативный тест: неверный логин",
            testName = "Негативный тест: неверный логин")
    public void checkLoginWithNegativeUser() {
        loginPage.open();
        loginPage.login("ABC", PASSWORD);
        assertEquals(loginPage.getErrorMessage(), "Epic sadface: Username and password do not match " +
                "any user in this service");
    }

    @Test(groups = {"regression", "login", "negative"},
            description = "Негативный тест: неверный пароль",
            testName = "Негативный тест: неверный пароль")
    public void checkLoginWithNegativePassword() {
        loginPage.open();
        loginPage.login(USERNAME, "ABC");
        assertEquals(loginPage.getErrorMessage(), "Epic sadface: Username and password do not match " +
                "any user in this service");
    }

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
    public void negativeLogin(String user, String password, String errorMessage) {
        loginPage.open();
        loginPage.login(user, password);
        assertEquals(loginPage.getErrorMessage(), errorMessage);
    }
}