package tests;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ProductsPageTest extends BaseTest{

    @Test(groups = {"smoke", "regression", "products"},
            description = "Проверка заголовка страницы 'Products'",
            testName = "Проверка заголовка страницы 'Products'")
    public void testProductsPageTitle() {
        loginAsStandardUser();
        Assert.assertEquals(productsPage.getTitle(), "Products", "Должен быть заголовок Products");
    }

    @Test(groups = {"regression", "products"},
            description = "Проверка отображения списка товаров (должно быть 6)",
            testName = "Проверка отображения списка товаров (должно быть 6)")
    public void testProductsListDisplayed() {
        loginAsStandardUser();
        Assert.assertEquals(productsPage.getItemsCount(), 6, "На странице должно быть 6 товаров");
    }

    @Test(groups = {"regression", "products", "cart"},
            description = "Добавление товара в корзину (кнопка \"Add to cart\")",
            testName = "Добавление товара в корзину (кнопка \"Add to cart\")")
    public void testAddToCart() {
        loginAsStandardUser();
        softAssert.assertTrue(productsPage.isAddButtonVisible(0), "Должна быть кнопка 'Add to cart'");
        productsPage.addToCart(0);
        softAssert.assertTrue(productsPage.isRemoveButtonVisible(0), "После добавления должна быть кнопка 'Remove'");
    }

    @Test(groups = {"regression", "products", "cart"},
            description = "Удаление товара из корзины (кнопка \"Remove\")",
            testName = "Удаление товара из корзины (кнопка \"Remove\")")
    public void testRemoveFromCart() {
        loginAsStandardUser();
        productsPage.addToCart(0);
        softAssert.assertTrue(productsPage.isRemoveButtonVisible(0));
        productsPage.removeFromCart(0);
        softAssert.assertTrue(productsPage.isAddButtonVisible(0), "После удаления должна вернуться кнопка 'Add to cart'");
    }

    @Test(groups = {"regression", "products", "cart"},
            description = "Проверка счётчика товаров в иконке корзины",
            testName = "Проверка счётчика товаров в иконке корзины")
    public void testCartBadgeCount() {
        loginAsStandardUser();
        productsPage.addToCart(0);
        softAssert.assertEquals(productsPage.getCartBadgeCount(), "1", "Счётчик корзины должен быть 1");
        productsPage.addToCart(1);
        softAssert.assertEquals(productsPage.getCartBadgeCount(), "2", "Счётчик корзины должен быть 2");
    }
}