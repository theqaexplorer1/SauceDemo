package tests;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductsPage;

/**
 * Тесты для страницы товаров (инвентаря).
 * Все тесты используют цепочку: LoginPage -> login() -> ProductsPage
 */
@Epic("E-commerce")
@Feature("Product Catalog")
@Owner("ivan.ivanov")
@Link(name = "SauceDemo", url = "https://saucedemo.com")
public class ProductsPageTest extends BaseTest{

    @Test(groups = {"smoke", "regression", "products"},
            description = "Проверка заголовка страницы 'Products'",
            testName = "Проверка заголовка страницы 'Products'")
    @Description("Проверка: после успешного логина отображается заголовок 'Products'")
    @TmsLink("QASE-301")
    @Issue("BUG-301")
    @Story("View products page header")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("ivan.ivanov")
    @Link(name = "SauceDemo", url = "https://saucedemo.com")
    public void testProductsPageTitle() {
        // Получаем ProductsPage через цепочку вызовов
        ProductsPage productsPage = new LoginPage(driver)
                .open()
                .login(USERNAME, PASSWORD);
        Assert.assertEquals(productsPage.getTitle(), "Products", "Должен быть заголовок Products");
    }

    @Test(groups = {"regression", "products"},
            description = "Проверка отображения списка товаров (должно быть 6)",
            testName = "Проверка отображения списка товаров (должно быть 6)")
    @Description("Проверка: на странице товаров отображается ровно 6 карточек товаров")
    @TmsLink("QASE-302")
    @Issue("BUG-302")
    @Story("View product list count")
    @Severity(SeverityLevel.NORMAL)
    @Owner("ivan.ivanov")
    @Link(name = "SauceDemo", url = "https://saucedemo.com")
    public void testProductsListDisplayed() {
        ProductsPage productsPage = new LoginPage(driver)
                .open()
                .login(USERNAME, PASSWORD);
        Assert.assertEquals(productsPage.getItemsCount(), 6, "На странице должно быть 6 товаров");
    }

    @Test(groups = {"regression", "products", "cart"},
            description = "Добавление товара в корзину (кнопка \"Add to cart\")",
            testName = "Добавление товара в корзину (кнопка \"Add to cart\")")
    @Description("Проверка: при нажатии 'Add to cart' кнопка меняется на 'Remove'")
    @TmsLink("QASE-303")
    @Issue("BUG-303")
    @Story("Add item to cart from product list")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("ivan.ivanov")
    @Link(name = "SauceDemo", url = "https://saucedemo.com")
    public void testAddToCart() {
        ProductsPage productsPage = new LoginPage(driver)
                .open()
                .login(USERNAME, PASSWORD);
        softAssert.assertTrue(productsPage.isAddButtonVisible(0),
                "Должна быть кнопка 'Add to cart'");
        productsPage.addToCart(0); // addToCart() возвращает this (ProductsPage) -> можно продолжать цепочку
        softAssert.assertTrue(productsPage.isRemoveButtonVisible(0),
                "После добавления должна быть кнопка 'Remove'");
    }

    @Test(groups = {"regression", "products", "cart"},
            description = "Удаление товара из корзины (кнопка \"Remove\")",
            testName = "Удаление товара из корзины (кнопка \"Remove\")")
    @Description("Проверка: при нажатии 'Remove' кнопка возвращается к 'Add to cart'")
    @TmsLink("QASE-304")
    @Issue("BUG-304")
    @Story("Remove item from product list")
    @Severity(SeverityLevel.NORMAL)
    @Owner("ivan.ivanov")
    @Link(name = "SauceDemo", url = "https://saucedemo.com")
    public void testRemoveFromCart() {
        ProductsPage productsPage = new LoginPage(driver)
                .open()
                .login(USERNAME, PASSWORD);
        productsPage.addToCart(0); //сначала добавляем товар
        softAssert.assertTrue(productsPage.isRemoveButtonVisible(0));
        productsPage.removeFromCart(0); // Удаляем товар (метод возвращает this)
        softAssert.assertTrue(productsPage.isAddButtonVisible(0), "После удаления должна вернуться кнопка 'Add to cart'");
    }

    @Test(groups = {"regression", "products", "cart"},
            description = "Проверка счётчика товаров в иконке корзины",
            testName = "Проверка счётчика товаров в иконке корзины")
    @Description("Проверка: бейдж корзины обновляется при добавлении товаров")
    @TmsLink("QASE-305")
    @Issue("BUG-305")
    @Story("Cart badge counter update")
    @Severity(SeverityLevel.NORMAL)
    @Owner("ivan.ivanov")
    @Link(name = "SauceDemo", url = "https://saucedemo.com")
    public void testCartBadgeCount() {
        ProductsPage productsPage = new LoginPage(driver)
                .open()
                .login(USERNAME, PASSWORD);
        productsPage.addToCart(0);
        softAssert.assertEquals(productsPage.getCartBadgeCount(), "1", "Счётчик корзины должен быть 1");
        productsPage.addToCart(1);
        softAssert.assertEquals(productsPage.getCartBadgeCount(), "2", "Счётчик корзины должен быть 2");
    }
}