package tests;
import org.testng.Assert;
import org.testng.annotations.Test;


public class CartTest extends BaseTest {

    private static final String EXPECTED_ITEM_NAME = "Sauce Labs Backpack";
    private static final String EXPECTED_ITEM_PRICE = "$29.99";

    // Переход в корзину по иконке + проверка названия товара
    @Test
    public void testCartPageNavigationAndItemName() {
        // Предусловие: товар добавлен в корзину (через ProductsPage)
        loginAsStandardUser();
        productsPage.addToCart(0);  // индекс 0 = "Sauce Labs Backpack"
        // Переход в корзину по иконке
        productsPage.goToCart();
        // Проверка названия товара в корзине
        Assert.assertEquals(cartPage.getFirstItemName(), EXPECTED_ITEM_NAME,
                "Название товара в корзине не совпадает");
    }

    // Проверка цены товара в корзине
    @Test
    public void testCartItemPrice() {
        loginAsStandardUser();
        productsPage.addToCart(0);
        productsPage.goToCart();
        Assert.assertEquals(cartPage.getFirstItemPrice(), EXPECTED_ITEM_PRICE,
                "Цена товара в корзине не совпадает");
    }

    // Удаление товара из корзины
    @Test
    public void testRemoveItemFromCart() {
        loginAsStandardUser();
        productsPage.addToCart(0);
        productsPage.goToCart();
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
    @Test
    public void testGoToCheckout() {
        loginAsStandardUser();
        productsPage.addToCart(0);
        productsPage.goToCart();
        // Переходим к оформлению и проверяем что открылась страница checkout-step-one.html
        cartPage.clickCheckout();
        Assert.assertTrue(driver.getCurrentUrl().contains("/checkout-step-one.html"),
                "Должна открыться страница оформления заказа");
    }

    @Test
    public void testFullCartFlow() {
        loginAsStandardUser();
        // Добавляем товар
        productsPage.addToCart(0);
        // Переходим в корзину
        productsPage.goToCart();
        // Проверяем имя и цену
        softAssert.assertEquals(cartPage.getFirstItemName(), EXPECTED_ITEM_NAME);
        softAssert.assertEquals(cartPage.getFirstItemPrice(), EXPECTED_ITEM_PRICE);
        // Переходим к оформлению
        cartPage.clickCheckout();
        softAssert.assertTrue(driver.getCurrentUrl().contains("/checkout-step-one.html"));
    }
}