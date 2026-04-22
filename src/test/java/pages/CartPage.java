package pages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

public class CartPage extends BasePage{

    private final By CART_ITEM = By.cssSelector(".cart_item");
    private final By ITEM_NAME = By.cssSelector(".inventory_item_name");
    private final By ITEM_PRICE = By.cssSelector(".inventory_item_price");
    private final By ITEM_QUANTITY = By.cssSelector("[data-test='item-quantity']");
    private final By REMOVE_BUTTON = By.cssSelector(".cart_button");
    private final By CHECKOUT_BUTTON = By.id("checkout");
    private final By CONTINUE_SHOPPING = By.id("continue-shopping");
    private final By CART_BADGE = By.cssSelector("[data-test='shopping-cart-badge']");
    private final By EMPTY_CART_ITEM = By.cssSelector(".removed_cart_item");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get(BASE_URL + "/cart.html");
    }

    // Переход в корзину по иконке (можно вызвать из ProductsPage, но оставлено для полноты)
    public void goToCartFromProducts() {
        driver.findElement(By.id("shopping_cart_container")).click();
    }

    // Проверка названия товара в корзине (первый товар)
    public String getFirstItemName() {
        return driver.findElement(ITEM_NAME).getText();
    }

    // Проверка цены товара в корзине (первый товар)
    public String getFirstItemPrice() {
        return driver.findElement(ITEM_PRICE).getText();
    }

    // Удаление товара из корзины (первый товар)
    public void removeFirstItem() {
        driver.findElement(REMOVE_BUTTON).click();
    }

    // Переход к оформлению заказа
    public void clickCheckout() {
        driver.findElement(CHECKOUT_BUTTON).click();
    }

    // Проверить, что товар есть в корзине
    public boolean isItemInCart(String itemName) {
        return getFirstItemName().equals(itemName);
    }

    // Корзина пуста, если нет элементов .cart_item
    public boolean isCartEmpty() {
        return driver.findElements(CART_ITEM).isEmpty();
    }

    // Удалённые элементы в корзине в DOM имеют div.removed_cart_item
    public boolean isEmptyCartItemInDom() {
        return driver.findElements(EMPTY_CART_ITEM).size() > 0;
    }
}