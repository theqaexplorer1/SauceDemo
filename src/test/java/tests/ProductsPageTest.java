package tests;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ProductsPageTest extends BaseTest{

    //Проверка заголовка страницы "Products"
    @Test
    public void testProductsPageTitle() {
        loginAsStandardUser();
        Assert.assertEquals(productsPage.getTitle(), "Products", "Должен быть заголовок Products");
    }

    //Проверка отображения списка товаров (должно быть 6)
    @Test
    public void testProductsListDisplayed() {
        loginAsStandardUser();
        Assert.assertEquals(productsPage.getItemsCount(), 6, "На странице должно быть 6 товаров");
    }

    //Добавление товара в корзину (кнопка "Add to cart")
    @Test
    public void testAddToCart() {
        loginAsStandardUser();
        softAssert.assertTrue(productsPage.isAddButtonVisible(0), "Должна быть кнопка 'Add to cart'");
        productsPage.addToCart(0);
        softAssert.assertTrue(productsPage.isRemoveButtonVisible(0), "После добавления должна быть кнопка 'Remove'");
    }

    //Удаление товара из корзины (кнопка "Remove")
    @Test
    public void testRemoveFromCart() {
        loginAsStandardUser();
        productsPage.addToCart(0);
        softAssert.assertTrue(productsPage.isRemoveButtonVisible(0));
        productsPage.removeFromCart(0);
        softAssert.assertTrue(productsPage.isAddButtonVisible(0), "После удаления должна вернуться кнопка 'Add to cart'");
    }

    //Проверка счётчика товаров в иконке корзины
    @Test
    public void testCartBadgeCount() {
        loginAsStandardUser();
        productsPage.addToCart(0);
        softAssert.assertEquals(productsPage.getCartBadgeCount(), "1", "Счётчик корзины должен быть 1");
        productsPage.addToCart(1);
        softAssert.assertEquals(productsPage.getCartBadgeCount(), "2", "Счётчик корзины должен быть 2");
    }
}