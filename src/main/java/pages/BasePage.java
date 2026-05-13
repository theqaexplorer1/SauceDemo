package pages;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;
    public final String BASE_URL = "https://www.saucedemo.com";

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Проверка, что страница действительно открыта.
     * Каждая страница должна реализовать этот метод.
     * @return true, если страница загружена и готова к работе
     */
    public abstract boolean isPageLoaded();

    /**
     * Открывает страницу по URL и ждёт её полной загрузки.
     * @param url адрес страницы
     */
    protected void openPage(String url) {
        driver.get(url);
        // Ждём, пока isPageLoaded() вернёт true (но не дольше 10 сек)
        wait.until(driver -> isPageLoaded());
    }
}