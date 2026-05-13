package tests;
import io.qameta.allure.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.List;

@Epic("Technical")
@Feature("Locator Validation")
@Owner("ivan.ivanov")
@Link(name = "SauceDemo", url = "https://saucedemo.com")
public class LocatorTest {

    //credentials
    public static final String LOGIN_STANDARD_USER = "standard_user";
    public static final String LOGIN_LOCKED_OUT_USER = "locked_out_user";
    public static final String LOGIN_PROBLEM_USER = "problem_user";
    public static final String LOGIN_ERROR_USER = "error_user";
    public static final String PASSWORD = "secret_sauce";

    @Test(description = "Проверка локаторов", testName = "Проверка локаторов")
    @Description("Технический тест: проверка работы всех типов локаторов Selenium на SauceDemo")
    @TmsLink("QASE-901")
    @Issue("BUG-901")
    @Story("Verify all locator types")
    @Severity(SeverityLevel.MINOR)
    @Owner("ivan.ivanov")
    @Link(name = "SauceDemo", url = "https://saucedemo.com")
    public void checkLocator() throws InterruptedException{
        ChromeOptions options = new ChromeOptions();
        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("credentials_enable_service", false);
        chromePrefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", chromePrefs);
        options.addArguments("--incognito");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
        WebDriver driver = new ChromeDriver(options);

        try {
            //открываем главную страницу
            driver.get("https://www.saucedemo.com");
            System.out.println("Главная Страница открыта");

            //By.id
            WebElement byId = driver.findElement(By.id("user-name"));
            Assert.assertNotNull(byId);
            System.out.println("By.id: user-name");

            //By.name
            WebElement byName = driver.findElement(By.name("password"));
            Assert.assertNotNull(byName);
            System.out.println("By.name: password");

            //By.className
            WebElement byClass = driver.findElement(By.className("login_container"));
            Assert.assertNotNull(byClass);
            System.out.println("By.className: login_container");

            // By.tagName
            WebElement byTag = driver.findElement(By.tagName("form"));
            Assert.assertNotNull(byTag);
            System.out.println("By.tagName: form");

            // Авторизация под standard_user
            driver.findElement(By.id("user-name")).sendKeys(LOGIN_STANDARD_USER);
            driver.findElement(By.id("password")).sendKeys(PASSWORD);
            driver.findElement(By.id("login-button")).click();
            System.out.println("Успешная авторизация");

            //By.linkText
            driver.findElement(By.id("react-burger-menu-btn")).click();
            Thread.sleep(500);  // Ждём появления сайдбара
            WebElement byLinkText = driver.findElement(By.linkText("All Items"));
            Assert.assertNotNull(byLinkText);
            System.out.println("By.linkText: All Items");
            Assert.assertTrue(byLinkText.isDisplayed(), "Ссылка All Items должна отображаться");
            System.out.println("Открыли меню и проверили ссылку All Items");

            // By.partialLinkText
            WebElement byPartialLink = driver.findElement(By.partialLinkText("Reset App"));
            Assert.assertNotNull(byPartialLink);
            System.out.println("By.partialLinkText: Reset App");

            //Закрыть меню
            driver.findElement(By.id("react-burger-cross-btn")).click();
            System.out.println("Закрыли меню");

            //By.XPath: поиск по атрибуту
            WebElement xpathAttr = driver.findElement(By.xpath("//button[@data-test='add-to-cart-sauce-labs-backpack']"));
            Assert.assertNotNull(xpathAttr);
            System.out.println("XPATH tag[@attr='value']: кнопка добавления рюкзака");
            xpathAttr.click();
            System.out.println("Нажали кнопку добавления рюкзака");

            //By.Xpath: поиск по тексту ("//tag[text()='text']")
            WebElement xpathText = driver.findElement(By.xpath("//div[text()='Sauce Labs Backpack']"));
            Assert.assertNotNull(xpathText);
            System.out.println("XPath tag[text()='text']: название товара");

            // By.XPath: частичное совпадение атрибута
            WebElement xpathPartialAttr = driver.findElement(By.xpath("//div[contains(@data-test,'item-price')]"));
            Assert.assertNotNull(xpathPartialAttr);
            System.out.println("XPath tag[contains(@attr,'text')]: div цены с атрибутом data-test содержит 'item-price' присутствует");

            // By.XPath: частичное совпадение текста //tag[contains(text(),'text')]
            WebElement xpathPartialText = driver.findElement(By.xpath("//span[contains(text(),'Products')]"));
            Assert.assertNotNull(xpathPartialText);
            System.out.println("XPath tag[contains(text(),'text')]: текст содержит 'Products'");

            // By.XPath: ancestor //tag//ancestor::tag
            WebElement xpathAncestor = driver.findElement(By.xpath("//div[@class='inventory_item_price']//ancestor::div[@class='inventory_item']"));
            Assert.assertNotNull(xpathAncestor);
            System.out.println("XPath[ancestor]: товар-предок цены");

            //By.XPath: descendant //tag//descendant::tag
            List<WebElement> xpathDescendant = driver.findElements(By.xpath("//div[@data-test='inventory-item']//descendant::button"));
            Assert.assertFalse(xpathDescendant.isEmpty());
            Assert.assertEquals(xpathDescendant.size(), 6, "Кнопок добавления товара должно быть 6");
            System.out.printf("XPath[descendant]: кнопки внутри товаров (%s) найдено %n", xpathDescendant.size());

            // By.XPath: following //tag/following::tag
            WebElement xpathFollowing = driver.findElement(By.xpath("//div[@data-test='inventory-item-name']/following::div[@data-test='inventory-item-price']"));
            Assert.assertNotNull(xpathFollowing);
            System.out.println("XPath[following]: цена после названия");

            // By.XPath: parent //tag/parent::tag
            WebElement xpathParent = driver.findElement(By.xpath("//div[@data-test='inventory-item-name']/parent::a"));
            Assert.assertNotNull(xpathParent);
            System.out.println("XPath[parent]: ссылка родитель названия товара");

            // By.XPath: preceding //tag/preceding::tag
            WebElement xpathPreceding = driver.findElement(By.xpath("//div[@data-test='inventory-item-price']/preceding::div[@data-test='inventory-item-name']"));
            Assert.assertNotNull(xpathPreceding);
            System.out.println("XPath[preceding]: название перед ценой");

            //By.Xpath с условием And
            WebElement xpathAndButton = driver.findElement(
                    By.xpath("//button[@data-test='remove-sauce-labs-backpack' and @name='remove-sauce-labs-backpack']")
            );
            Assert.assertNotNull(xpathAndButton);
            System.out.println("XPath AND: кнопка с двумя атрибутами");

            //CSS: .class
            WebElement cssClass = driver.findElement(By.cssSelector(".inventory_item"));
            Assert.assertNotNull(cssClass);
            System.out.println("Товар по классу .inventory_item");

            //CSS: .class1.class2
            WebElement cssMultiClass = driver.findElement(By.cssSelector(".btn.btn_primary"));
            Assert.assertNotNull(cssMultiClass);
            System.out.println("Кнопка с двумя классами .btn.btn_primary");

            //CSS: .class1 .class2
            WebElement cssNested = driver.findElement(By.cssSelector(".inventory_item .inventory_item_name"));
            Assert.assertNotNull(cssNested);
            System.out.println(".inventory_item .inventory_item_name - название внутри товара");

            //CSS: #id
            WebElement cssId = driver.findElement(By.cssSelector("#inventory_container"));
            Assert.assertNotNull(cssId);
            System.out.println("контейнер по id #inventory_container");

            //CSS: tagname
            WebElement cssTag = driver.findElement(By.cssSelector("select"));
            Assert.assertNotNull(cssTag);
            System.out.println("по тегу select");

            //CSS: tagname.class
            WebElement cssTagClass = driver.findElement(By.cssSelector("button.btn_inventory"));
            Assert.assertNotNull(cssTagClass);
            System.out.println("button.btn_inventory - кнопка товара");

            //CSS: [attribute=value]
            WebElement cssAttrEquals = driver.findElement(By.cssSelector("button[data-test='remove-sauce-labs-backpack']"));
            Assert.assertNotNull(cssAttrEquals);
            System.out.println("CSS: [attribute=value] - кнопка удаления по атрибуту remove-sauce-labs-backpack");

            //CSS: [attribute~=value] содержит слово в списке
            WebElement cssAttrContains = driver.findElement(By.cssSelector("button[class~='btn_primary']"));
            Assert.assertNotNull(cssAttrContains);
            System.out.println("CSS [attr~=value] - кнопка с классом 'btn_primary'");

            //CSS: [attribute|=value] равно или начинается с значение-дефис
            WebElement cssAttrEqualsOrStarts = driver.findElement(By.cssSelector("div[data-test|='inventory']"));
            Assert.assertNotNull(cssAttrEqualsOrStarts);
            System.out.println("CSS [attr|=value] - div с data-test, начинающимся с 'inventory-'");

            //CSS: [attribute^=value] начинается с
            WebElement cssAttrStarts = driver.findElement(By.cssSelector("button[data-test^='add-to-cart']"));
            Assert.assertNotNull(cssAttrStarts);
            System.out.println("CSS [attr^=value] - кнопка, data-test начинается с 'add-to-cart'");

            //CSS: [attribute$=value] (заканчивается на)
            WebElement cssAttrEnds = driver.findElement(By.cssSelector("button[data-test$='sauce-labs-backpack']"));
            Assert.assertNotNull(cssAttrEnds);
            System.out.println(" CSS [attr$=value]: кнопка, data-test заканчивается на 'sauce-labs-backpack'");

            //CSS: [attribute*=value] (содержит)
            WebElement cssAttrContainsSubstring = driver.findElement(By.cssSelector("button[data-test*='add-to-cart']"));
            Assert.assertNotNull(cssAttrContainsSubstring);
            System.out.println("CSS [attr*=value]: кнопка, data-test содержит подстроку 'add-to-cart'");

        } finally {
            System.out.println("Тест завершён");
            driver.quit();
        }
    }
}