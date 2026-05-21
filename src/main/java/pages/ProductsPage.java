package pages;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

@Log4j2
public class ProductsPage extends BasePage{

    private final By TITLE = By.cssSelector("[data-test='title']");
    private final By INVENTORY_ITEM = By.cssSelector("[data-test='inventory-item']");
    private final By ADD_TO_CART_BUTTON = By.cssSelector("[data-test^='add-to-cart-']");
    private final By REMOVE_BUTTON = By.cssSelector("[data-test^='remove-']");
    private final By CART_BADGE = By.cssSelector("[data-test='shopping-cart-badge']");
    private final By CART_ICON = By.id("shopping_cart_container");
    private final String ADD_TO_CART_PATTERN =
            "//*[text()='%s']/ancestor::div[@class='inventory_item']//button[text()='Add to cart']";
    private final String REMOVE_FROM_CART_PATTERN =
            "//*[text()='%s']/ancestor::div[@class='inventory_item']//button[text()='Remove']";

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

     // Проверка: мы на странице товаров, если видим заголовок "Products"
    @Override
    public boolean isPageLoaded() {
        try {
            return driver.findElement(TITLE).isDisplayed()
                    && driver.getCurrentUrl().contains("/inventory.html");
        } catch (Exception e) {
            log.warn("ProductsPage not loaded: {}", e.getMessage());
            return false;
        }
    }

    @Step("Открыть страницу товаров: {0}")
    public ProductsPage open() {
        log.info("Open products page");
        openPage(BASE_URL + "/inventory.html");
        return this;  // Chain: остаёмся на ProductsPage
    }

    @Step("Получить заголовок страницы")
    public String getTitle() {
        String title = driver.findElement(TITLE).getText();
        log.debug("Page title: '{}'", title);
        return title;
    }

    @Step("Получить количество товаров на странице")
    public int getItemsCount() {
        int count = driver.findElements(INVENTORY_ITEM).size();
        log.debug("Items count: {}", count);
        return count;
    }

    //Добавление товара в корзину (по индексу 0-5)
    @Step("Добавить товар в корзину по индексу: {0}")
    public ProductsPage addToCart(int itemIndex) {
        log.debug("Adding item at index {} to cart", itemIndex);
        List<WebElement> items = driver.findElements(INVENTORY_ITEM);
        items.get(itemIndex).findElement(ADD_TO_CART_BUTTON).click();
        return this;  // Chain: остаёмся на ProductsPage
    }

    @Step("Добавить товар в корзину по имени: {0}")
    public ProductsPage addToCartByName(String product_name){
        log.debug("Adding item '{}' to cart by name", product_name);
        driver.findElement(By.xpath(String.format(ADD_TO_CART_PATTERN, product_name))).click();
        return this;  // Chain: остаёмся на ProductsPage
    }

    @Step("Удалить товар из корзины по индексу: {0}")
    public ProductsPage removeFromCart(int itemIndex) {
        log.debug("Removing item at index {} from cart", itemIndex);
        List<WebElement> items = driver.findElements(INVENTORY_ITEM);
        items.get(itemIndex).findElement(REMOVE_BUTTON).click();
        return this;  // Chain: остаёмся на ProductsPage
    }

    @Step("Удалить товар из корзины по имени: {0}")
    public ProductsPage removeFromCartByName(String product_name){
        log.debug("Removing item '{}' from cart by name", product_name);
        driver.findElement(By.xpath(String.format(REMOVE_FROM_CART_PATTERN, product_name))).click();
        return this;  // Chain: остаёмся на ProductsPage
    }

    // Проверка счётчика товаров в иконке корзины
    @Step("Получить значение бейджа корзины")
    public String getCartBadgeCount() {
        WebElement badge = driver.findElement(CART_BADGE);
        String count = badge.isDisplayed() ? badge.getText() : "0";
        log.debug("Cart badge count: '{}'", count);
        return count;
    }

    @Step("Перейти в корзину по иконке")
    public CartPage goToCart() {
        log.info("Go to cart page");
        driver.findElement(CART_ICON).click();
        return new CartPage(driver); //Chain: переход на другую страницу
    }

    // Есть ли кнопка "Remove" у товара (значит, в корзине)
    @Step("Проверить видимость кнопки 'Remove' у товара: {0}")
    public boolean isRemoveButtonVisible(int itemIndex) {
        List<WebElement> items = driver.findElements(INVENTORY_ITEM);
        boolean visible = !items.get(itemIndex).findElements(REMOVE_BUTTON).isEmpty();
        log.debug("Remove button visible at index {}: {}", itemIndex, visible);
        return visible;
    }

    // Есть ли кнопка "Add to cart" у товара
    @Step("Проверить видимость кнопки 'Add to cart' у товара: {0}")
    public boolean isAddButtonVisible(int itemIndex) {
        List<WebElement> items = driver.findElements(INVENTORY_ITEM);
        boolean visible = !items.get(itemIndex).findElements(ADD_TO_CART_BUTTON).isEmpty();
        log.debug("Add to cart button visible at index {}: {}", itemIndex, visible);
        return visible;
    }
}