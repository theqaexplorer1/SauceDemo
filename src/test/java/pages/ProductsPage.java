package pages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

public class ProductsPage extends BasePage{

    private final By TITLE = By.cssSelector("[data-test='title']");
    private final By INVENTORY_ITEM = By.cssSelector("[data-test='inventory-item']");
    private final By ADD_TO_CART_BUTTON = By.cssSelector("[data-test^='add-to-cart-']");
    private final By REMOVE_BUTTON = By.cssSelector("[data-test^='remove-']");
    private final By CART_BADGE = By.cssSelector("[data-test='shopping-cart-badge']");
    private final By CART_ICON = By.id("shopping_cart_container");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get(BASE_URL + "/inventory.html");
    }

    //Проверка заголовка страницы "Products"
    public String getTitle() {
        return driver.findElement(TITLE).getText();
    }

    //Проверка отображения списка товаров
    public int getItemsCount() {
        return driver.findElements(INVENTORY_ITEM).size();
    }

    //Добавление товара в корзину (по индексу 0-5)
    public void addToCart(int itemIndex) {
        List<WebElement> items = driver.findElements(INVENTORY_ITEM);
        items.get(itemIndex).findElement(ADD_TO_CART_BUTTON).click();
    }

    // Удаление товара из корзины (по индексу)
    public void removeFromCart(int itemIndex) {
        List<WebElement> items = driver.findElements(INVENTORY_ITEM);
        items.get(itemIndex).findElement(REMOVE_BUTTON).click();
    }

    // Проверка счётчика товаров в иконке корзины
    public String getCartBadgeCount() {
        WebElement badge = driver.findElement(CART_BADGE);
        return badge.isDisplayed() ? badge.getText() : "0";
    }

    // Перейти в корзину
    public void goToCart() {
        driver.findElement(CART_ICON).click();
    }

    // Есть ли кнопка "Remove" у товара (значит, в корзине)
    public boolean isRemoveButtonVisible(int itemIndex) {
        List<WebElement> items = driver.findElements(INVENTORY_ITEM);
        return !items.get(itemIndex).findElements(REMOVE_BUTTON).isEmpty();
    }

    // Есть ли кнопка "Add to cart" у товара
    public boolean isAddButtonVisible(int itemIndex) {
        List<WebElement> items = driver.findElements(INVENTORY_ITEM);
        return !items.get(itemIndex).findElements(ADD_TO_CART_BUTTON).isEmpty();
    }
}