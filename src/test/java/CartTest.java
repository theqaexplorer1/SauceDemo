import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.HashMap;

/*
https://www.saucedemo.com/
a. Залогиниться
b. Добавить товар в корзину
c. Перейти в корзину
d. Проверить (assertEquals) стоимость товара и его имя в корзине
 */
public class CartTest {
    //credentials
    private static final String LOGIN_STANDARD_USER = "standard_user";
    private static final String LOGIN_LOCKED_OUT_USER = "locked_out_user";
    private static final String LOGIN_PROBLEM_USER = "problem_user";
    private static final String LOGIN_ERROR_USER = "error_user";
    private static final String PASSWORD = "secret_sauce";
    private static final String BASE_URL = "https://www.saucedemo.com/";

    @Test
    public void testCart() {
        ChromeOptions options = new ChromeOptions();
        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("credentials_enable_service", false);
        chromePrefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", chromePrefs);
        options.addArguments("--incognito");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        SoftAssert softAssert = new SoftAssert();

        try {
            driver.get(BASE_URL);
            driver.findElement(By.id("user-name")).sendKeys(LOGIN_STANDARD_USER);
            driver.findElement(By.id("password")).sendKeys(PASSWORD);
            driver.findElement(By.id("login-button")).click();
            System.out.println("Успешная авторизация");

            WebElement addToCartButton = driver.findElement(By.id("add-to-cart-sauce-labs-backpack"));
            addToCartButton.click();
            System.out.println("Товар добавлен в корзину");

            WebElement shoppingCartButton = driver.findElement(By.cssSelector("#shopping_cart_container a"));
            shoppingCartButton.click();
            System.out.println("Перешли в корзину");

            //получаем название товара и цену
            WebElement itemNameElement = driver.findElement(By.cssSelector(".cart_item .inventory_item_name"));
            String itemName = itemNameElement.getText();
            WebElement itemPriceElement = driver.findElement(By.cssSelector(".cart_item .inventory_item_price"));
            String itemPrice = itemPriceElement.getText();

            // проверяем название и цену
            softAssert.assertEquals(itemName, "Sauce Labs Backpack", "Имя товара не совпадает");
            softAssert.assertEquals(itemPrice, "$29.99", "Цена товара не совпадает");

            System.out.printf("Тест пройден: название товара:  %s, цена: %s %n", itemName, itemPrice);
        } finally {
            System.out.println("Все тесты завершены");
            driver.quit();
        }
    }
}
