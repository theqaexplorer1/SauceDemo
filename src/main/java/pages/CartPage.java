package pages;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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

    @Step("Открыть страницу корзины: {0}")
    public void open() {
        driver.get(BASE_URL + "/cart.html");
    }

    // Переход в корзину по иконке (можно вызвать из ProductsPage, но оставлено для полноты)
    @Step("Переход в корзину по иконке")
    public void goToCartFromProducts() {
        driver.findElement(By.id("shopping_cart_container")).click();
    }

    @Step("Получить название первого товара в корзине")
    public String getFirstItemName() {
        return driver.findElement(ITEM_NAME).getText();
    }

    @Step("Получить цену первого товара в корзине")
    public String getFirstItemPrice() {
        return driver.findElement(ITEM_PRICE).getText();
    }

    @Step("Удалить первый товар из корзины")
    public void removeFirstItem() {
        driver.findElement(REMOVE_BUTTON).click();
    }

    @Step("Перейти к оформлению заказа")
    public void clickCheckout() {
        driver.findElement(CHECKOUT_BUTTON).click();
    }

    @Step("Проверить наличие товара '{0}' в корзине")
    public boolean isItemInCart(String itemName) {
        return getFirstItemName().equals(itemName);
    }

    // Корзина пуста, если нет элементов .cart_item
    @Step("Проверить, что корзина пуста")
    public boolean isCartEmpty() {
        return driver.findElements(CART_ITEM).isEmpty();
    }

    // Удалённые элементы в корзине в DOM имеют div.removed_cart_item
    @Step("Проверить отображение сообщения о пустой корзине в HTML страницы")
    public boolean isEmptyCartItemInDom() {
        return driver.findElements(EMPTY_CART_ITEM).size() > 0;
    }
}