package pages;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

@Log4j2
public class LoginPage extends BasePage{

    private final By USERNAME_FIELD = By.xpath("//*[@id='user-name']");
    private final By PASSWORD_FIELD = By.xpath("//*[@id='password']");
    private final By LOGIN_BUTTON = By.xpath("//*[@id='login-button']");
    private final By ERROR_MESSAGE = By.xpath("//*[@data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    //Проверка: мы на странице логина, если видим поле ввода логина
    @Override
    public boolean isPageLoaded() {
        try {
            return driver.findElement(USERNAME_FIELD).isDisplayed();
        } catch (Exception e) {
            log.warn("LoginPage not loaded: {}", e.getMessage());
            return false;
        }
    }

    @Step("Открыть страницу логина: {0}")
    public LoginPage open() {
        log.info("Open login page");
        openPage(BASE_URL);
        return this; //Chain: остаёмся на LoginPage
    }

    @Step("Ввести логин '{0}' и пароль '{1}', нажать Login")
    public ProductsPage login(String username, String password) {
        log.info("Logging in as '{}'", username);
        driver.findElement(USERNAME_FIELD).sendKeys(username);
        driver.findElement(PASSWORD_FIELD).sendKeys(password);
        driver.findElement(LOGIN_BUTTON).click();
        return new ProductsPage(driver); //Chain: после успешного логина возвращаем следующую страницу
    }

    @Step("Получить текст сообщения об ошибке")
    public String getErrorMessage() {
        return driver.findElement(ERROR_MESSAGE).getText();
    }
}