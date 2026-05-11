package pages;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage{

    private final By USERNAME_FIELD = By.xpath("//*[@id='user-name']");
    private final By PASSWORD_FIELD = By.xpath("//*[@id='password']");
    private final By LOGIN_BUTTON = By.xpath("//*[@id='login-button']");
    private final By ERROR_MESSAGE = By.xpath("//*[@data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Step("Открыть страницу логина: {0}")
    public void open() {
        driver.get(BASE_URL);
    }

    @Step("Ввести логин '{0}' и пароль '{1}', нажать Login")
    public void login(String username, String password) {
        driver.findElement(USERNAME_FIELD).sendKeys(username);
        driver.findElement(PASSWORD_FIELD).sendKeys(password);
        driver.findElement(LOGIN_BUTTON).click();
    }

    @Step("Получить текст сообщения об ошибке")
    public String getErrorMessage() {
        return driver.findElement(ERROR_MESSAGE).getText();
    }
}