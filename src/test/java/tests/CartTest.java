package tests;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.LoginPage;

/**
 * Тесты для страницы корзины.
 * Демонстрирует полную цепочку:
 *    LoginPage -> login() -> ProductsPage -> addToCart() -> goToCart() -> CartPage
 */
@Epic("E-commerce")
@Feature("Shopping Cart")
@Owner("ivan.ivanov")
@Link(name = "SauceDemo", url = "https://saucedemo.com")
public class CartTest extends BaseTest {

    private static final String EXPECTED_ITEM_NAME = "Sauce Labs Backpack";
    private static final String EXPECTED_ITEM_PRICE = "$29.99";

    @Test(groups = {"smoke", "regression", "cart"},
            description = "Переход в корзину по иконке и проверка названия товара",
            testName = "Переход в корзину по иконке и проверка названия товара")
    @Description("Проверка: после добавления товара в корзину, его название отображается корректно")
    @TmsLink("QASE-201")
    @Issue("BUG-201")
    @Story("View cart item name")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("ivan.ivanov")
    @Link(name = "SauceDemo", url = "https://saucedemo.com")
    public void testCartPageNavigationAndItemName() {
        // Цепочка вызовов:
        // 1. new LoginPage(driver) -> создаём
        // 2. .open() - открываем и ждём загрузки -> LoginPage (this)
        // 3. .login(...) - логинимся -> ProductsPage
        // 4. .addToCart(0) - добавляем товар -> ProductsPage (this)
        // 5. .goToCart() - переходим в корзину -> CartPage
        CartPage cartPage = new LoginPage(driver)
                .open()
                .login(USERNAME, PASSWORD)
                .addToCart(0)
                .goToCart();
        // Проверка названия товара в корзине
        Assert.assertEquals(cartPage.getFirstItemName(), EXPECTED_ITEM_NAME,
                "Название товара в корзине не совпадает");
    }

    @Test(groups = {"regression", "cart"},
            description = "Проверка цены товара в корзине",
            testName = "Проверка цены товара в корзине")
    @Description("Проверка: цена товара в корзине соответствует цене на странице товаров")
    @TmsLink("QASE-202")
    @Issue("BUG-202")
    @Story("View cart item price")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("ivan.ivanov")
    @Link(name = "SauceDemo", url = "https://saucedemo.com")
    public void testCartItemPrice() {
        CartPage cartPage = new LoginPage(driver)
                .open()
                .login(USERNAME, PASSWORD)
                .addToCart(0)
                .goToCart();
        Assert.assertEquals(cartPage.getFirstItemPrice(), EXPECTED_ITEM_PRICE,
                "Цена товара в корзине не совпадает");
    }

    @Test(groups = {"regression", "cart"},
            description = "Удаление товара из корзины",
            testName = "Удаление товара из корзины")
    @Description("Проверка: после удаления товара корзина становится пустой, отображается сообщение")
    @TmsLink("QASE-203")
    @Issue("BUG-203")
    @Story("Remove item from cart")
    @Severity(SeverityLevel.NORMAL)
    @Owner("ivan.ivanov")
    @Link(name = "SauceDemo", url = "https://saucedemo.com")
    public void testRemoveItemFromCart() {
        CartPage cartPage = new LoginPage(driver)
                .open()
                .login(USERNAME, PASSWORD)
                .addToCart(0)
                .goToCart();
        // Проверяем, что товар есть
        softAssert.assertTrue(cartPage.isItemInCart(EXPECTED_ITEM_NAME),
                "Товар должен быть в корзине перед удалением");
        // Удаляем товар
        cartPage.removeFirstItem();
        // Корзина пуста (нет элементов .cart_item)
        softAssert.assertTrue(cartPage.isCartEmpty(),
                "После удаления корзина должна быть пустой");
        // Проверяем, что появился .removed_cart_item элемент в DOM
        softAssert.assertTrue(cartPage.isEmptyCartItemInDom(),
                "Должно отображаться сообщение о пустой корзине");
    }

    // Проверка перехода к странице Checkout для оформления заказа
    @Test(groups = {"regression", "cart", "checkout"},
            description = "Проверка перехода к оформлению заказа",
            testName = "Проверка перехода к оформлению заказа")
    @Description("Проверка: при нажатии Checkout открывается страница оформления заказа")
    @TmsLink("QASE-204")
    @Issue("BUG-204")
    @Story("Proceed to checkout")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("ivan.ivanov")
    @Link(name = "SauceDemo", url = "https://saucedemo.com")
    public void testGoToCheckout() {
        new LoginPage(driver)
                .open()
                .login(USERNAME, PASSWORD)
                .addToCart(0)
                .goToCart()
                .clickCheckout();  // Возвращает void, цепочка завершена
        Assert.assertTrue(driver.getCurrentUrl().contains("/checkout-step-one.html"),
                "Должна открыться страница оформления заказа");
    }

    @Test(groups = {"regression", "cart", "e2e"},
            description = "Весь пользовательский путь в корзине",
            testName = "Весь пользовательский путь в корзине")
    @Description("E2E-сценарий: добавление товара -> просмотр корзины -> переход к оформлению")
    @TmsLink("QASE-205")
    @Issue("BUG-205")
    @Story("Full cart flow")
    @Severity(SeverityLevel.BLOCKER)
    @Owner("ivan.ivanov")
    @Link(name = "SauceDemo", url = "https://saucedemo.com")
    public void testFullCartFlow() {
        CartPage cartPage = new LoginPage(driver)
                .open()
                .login(USERNAME, PASSWORD)
                .addToCart(0)
                .goToCart();
        // Проверяем имя и цену
        softAssert.assertEquals(cartPage.getFirstItemName(), EXPECTED_ITEM_NAME);
        softAssert.assertEquals(cartPage.getFirstItemPrice(), EXPECTED_ITEM_PRICE);
        // Переходим к оформлению
        cartPage.clickCheckout();
        softAssert.assertTrue(driver.getCurrentUrl().contains("/checkout-step-one.html"));
    }
}