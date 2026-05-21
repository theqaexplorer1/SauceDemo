package pages;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

@Log4j2
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

    // Проверка: мы в корзине, если URL содержит /cart.html
    @Override
    public boolean isPageLoaded() {
        try {
            return driver.getCurrentUrl().contains("/cart.html");
        } catch (Exception e) {
            log.warn("CartPage not loaded: {}", e.getMessage());
            return false;
        }
    }

    @Step("Открыть страницу корзины: {0}")
    public CartPage open() {
        log.info("Open cart page");
        openPage(BASE_URL + "/cart.html");
        return this;  // 🔗 Chain: остаёмся на CartPage
    }

    // Переход в корзину по иконке (можно вызвать из ProductsPage, но оставлено для полноты)
    @Step("Переход в корзину по иконке")
    public void goToCartFromProducts() {
        log.debug("Go to cart from products page");
        driver.findElement(By.id("shopping_cart_container")).click();
    }

    @Step("Получить название первого товара в корзине")
    public String getFirstItemName() {
        String name = driver.findElement(ITEM_NAME).getText();
        log.debug("First item name in cart: '{}'", name);
        return name;
    }

    @Step("Получить цену первого товара в корзине")
    public String getFirstItemPrice() {
        String price = driver.findElement(ITEM_PRICE).getText();
        log.debug("First item price in cart: '{}'", price);
        return price;
    }

    @Step("Удалить первый товар из корзины")
    public CartPage removeFirstItem() {
        log.info("Removing first item from cart");
        driver.findElement(REMOVE_BUTTON).click();
        return this;  // Chain: остаёмся на CartPage
    }

    @Step("Перейти к оформлению заказа")
    public void clickCheckout() {
        log.info("Clicking checkout button");
        driver.findElement(CHECKOUT_BUTTON).click();
        // Возвращаем void, так как дальше идёт проверка URL в тесте
        // CheckoutPage не реализован на данный момент
    }

    @Step("Проверить наличие товара '{0}' в корзине")
    public boolean isItemInCart(String itemName) {
        boolean inCart = getFirstItemName().equals(itemName);
        log.debug("Item '{}' in cart: {}", itemName, inCart);
        return inCart;
    }

    // Корзина пуста, если нет элементов .cart_item
    @Step("Проверить, что корзина пуста")
    public boolean isCartEmpty() {
        boolean empty = driver.findElements(CART_ITEM).isEmpty();
        log.debug("Cart empty: {}", empty);
        return empty;
    }

    // Удалённые элементы в корзине в DOM имеют div.removed_cart_item
    @Step("Проверить отображение сообщения о пустой корзине в HTML страницы")
    public boolean isEmptyCartItemInDom() {
        boolean hasEmptyMessage = driver.findElements(EMPTY_CART_ITEM).size() > 0;
        log.debug("Empty cart message in DOM: {}", hasEmptyMessage);
        return hasEmptyMessage;
    }
}