package advanced;

import org.apache.regexp.RE;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;
import pages.regLoginPage;
import pages.shopping.RegisterPage;
import sun.rmi.registry.RegistryImpl_Stub;
import utils.SpreadsheetReader.SpreadSheetReader;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import extentReportManager.*;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.junit.*;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.annotation.Nullable;

/**
 * Created by Administrator on 06/09/2017.
 */
public class Shopping {

    private WebDriver webDriver;
    private Actions builder;
    private static ExtentReportManager reportManager;
    private PageFactory pf = new PageFactory();

    @BeforeClass
    public static void init(){
        String loc = System.getProperty("user.dir");
        ReportDetails reportDetails = new ReportDetails(loc + "\\ShoppingTestReport", "Basic Extent Report","Basic Report");
        reportManager = new ExtentReportManager(ExtentReportManager.ReportType.HTML,reportDetails);
    }
    @Before
    public void setUp(){
        webDriver = new ChromeDriver();
        builder = new Actions(webDriver);
        webDriver.navigate().to("http://automationpractice.com/index.php");
    }

    @After
    public void cleaning(){
        webDriver.quit();
    }

    @AfterClass
    public static void cleanUp(){
        reportManager.clearTests();
    }

    @Test
    public void register(){
        ExtentTest parent = reportManager.manualSetupTest("Registering Multiple Accounts");
        SpreadSheetReader sheetReader = new SpreadSheetReader(System.getProperty("user.dir") + "/src/main/resources/shoppingRegister.xlsx");
        List<String> row;
        for(int i = 0; i < sheetReader.getNumberOfRows("Input Data");i++) {
            ExtentTest register = parent.createNode("User #" + i);
            register.log(Status.INFO, "Attempting to register user #" + i);
            webDriver.findElement(By.linkText("Sign in")).click();
            row = sheetReader.readRow(i, "Input Data");
            WebElement emailBox = waitForThisToExist("email_create", 30, 1);
            emailBox.sendKeys(row.get(3));
            webDriver.findElement(By.id("SubmitCreate")).click();
            try{
                waitForThisToExist("customer_firstname", 5, 1);
            }catch (TimeoutException e){
                if(row.get(17).equals("fail"))
                    register.pass("User cannot be created, email already taken (PASS)");
                else {
                    register.log(Status.ERROR, "User cannot be created, email already taken (FAIL)");
//                    try{
//                        reportManager.addScreenShot("User #" + i, ScreenShot.take(webDriver, "Register Error"));
//                    }catch (IOException f){
//                        f.printStackTrace();
//                    }
                }
                continue;
            }
            PageFactory.initElements(webDriver, RegisterPage.class);
            RegisterPage.maleSelector.click();
            if(row.get(0).equals("male"))
                RegisterPage.maleSelector.click();
            else
                RegisterPage.femaleSelector.click();
            RegisterPage.firstName.sendKeys(row.get(1));
            RegisterPage.lastName.sendKeys(row.get(2));
            RegisterPage.password.sendKeys(row.get(4));
            RegisterPage.setDropdown(RegisterPage.dobDays, row.get(5));
            RegisterPage.setDropdown(RegisterPage.dobMonths, row.get(6));
            RegisterPage.setDropdown(RegisterPage.dobYears, row.get(7));
            RegisterPage.addressFirstName.sendKeys(row.get(8));
            RegisterPage.addressLastName.sendKeys(row.get(9));
            RegisterPage.addressAddress.sendKeys(row.get(10));
            RegisterPage.addressCity.sendKeys(row.get(11));
            RegisterPage.setDropdown(RegisterPage.addressState, row.get(12));
            RegisterPage.addressPostCode.sendKeys(row.get(13));
            RegisterPage.setDropdown(RegisterPage.addressCountry, row.get(14));
            RegisterPage.addressMobilePhone.sendKeys(row.get(15));
            RegisterPage.completeForm.click();
            try{
                waitForThisToExist("my-account", 30, 1);
                if(row.get(17).equals("pass"))
                    register.log(Status.PASS, "The user #" + i + " was registered and logged in");
                else
                    register.log(Status.ERROR, "User was registered when it shouldn't of been");
            }catch (Exception e){

                if(row.get(17).equals("pass"))
                    register.log(Status.PASS, "The wasn't able to register (PASS)");
                else {
                    register.log(Status.ERROR, "User wouldn't register when it should've");
                    Assert.fail("Couldn't register user # " + i);
                }
            }
            webDriver.findElement(By.className("logout")).click();
        }
        Assert.assertTrue(true);
        parent.pass("Completed registering test");
    }

    @Test
    public void logIn(){
        ExtentTest loginTest = reportManager.setUpTest();
        webDriver.navigate().to("http://automationpractice.com/index.php?controller=authentication&back=my-account");
        SpreadSheetReader sheetReader = new SpreadSheetReader(System.getProperty("user.dir") + "/src/main/resources/shoppingRegister.xlsx");
        List<String> row;
        row = sheetReader.readRow(0, "Input Data");
        webDriver.findElement(By.id("email")).sendKeys(row.get(3));
        webDriver.findElement(By.id("passwd")).sendKeys(row.get(4));
        webDriver.findElement(By.id("SubmitLogin")).click();
        try {
            WebElement ele = waitForThisToExist("my-account", 5, 1, byType.ID);
            loginTest.pass("Logged in fine");
            Assert.assertTrue(true);
        } catch (Exception e){
            loginTest.fail("Failed to log in");
            Assert.assertTrue(false);
        }
    }

    @Test
    public void basketTest(){
        ExtentTest parentTest = reportManager.manualSetupTest("Adding items to the basket.");
        SpreadSheetReader sheetReader = new SpreadSheetReader(System.getProperty("user.dir") + "/src/main/resources/shoppingBasket.xlsx");
        List<String> row;
        for(int i = 0; i < sheetReader.getNumberOfRows("Input Data");i++) {
            ExtentTest basketTest = parentTest.createNode("Item #" + i);
            row = sheetReader.readRow(i, "Input Data");
            basketTest.log(Status.INFO, "Opening " + row.get(0) + " category");
            try{
                webDriver.findElement(By.linkText(row.get(3))).click();
                WebElement ele = waitForThisToExist(row.get(0), 30, 1, byType.LINKTEXT);
                ele.click();
            }catch (Exception e){
                webDriver.findElement(By.linkText(row.get(0))).click();
            }
            basketTest.log(Status.INFO, "Clicking on the product");
//            webDriver.findElements(By.cssSelector(".product_img_link")).get(Integer.parseInt(row.get(1))).click();
            builder.moveToElement(webDriver.findElements(By.cssSelector(".product_img_link")).get(Integer.parseInt(row.get(1))), 0, 1)
                .click()
                .perform();
            basketTest.log(Status.INFO, "Increasing the quantity to 4");
            WebElement qty = waitForThisToExist("#quantity_wanted_p > a.btn.btn-default.button-plus.product_quantity_up", 30, 1, byType.CSS);
            for(int j = 0; j < Integer.parseInt(row.get(2)); j++){
                builder.click(qty).perform();
            }
            basketTest.log(Status.INFO, "Adding item to cart");
            webDriver.findElement(By.cssSelector("#add_to_cart > button")).click();
            basketTest.log(Status.INFO, "Going to checkout");
            WebElement button = waitForThisCSSToExist("#layer_cart > div.clearfix > div.layer_cart_cart.col-xs-12.col-md-6 > div.button-container > a", 30, 1);
            button.click();
            basketTest.pass("Finished adding items to basket");
        }
    }

    public WebElement waitForThisToExist(final String id, int timeout, int polling){
        Wait<WebDriver>  wait = new FluentWait<>(webDriver)
                .withTimeout(timeout, TimeUnit.SECONDS)
                .pollingEvery(polling, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);
        WebElement element = wait.until(new com.google.common.base.Function<WebDriver, WebElement>() {
            @Nullable
            public WebElement apply(@Nullable WebDriver webDriver) {
                return webDriver.findElement(By.id(id));
            }
        });
        return element;
    }

    public WebElement waitForThisCSSToExist(final String css, int timeout, int polling){
        Wait<WebDriver>  wait = new FluentWait<>(webDriver)
                .withTimeout(timeout, TimeUnit.SECONDS)
                .pollingEvery(polling, TimeUnit.SECONDS)
                .ignoring(ElementNotVisibleException.class);
        WebElement element = wait.until(new com.google.common.base.Function<WebDriver, WebElement>() {
            @Nullable
            public WebElement apply(@Nullable WebDriver webDriver) {
                return webDriver.findElement(By.cssSelector(css));
            }
        });
        wait.until(ExpectedConditions.elementToBeClickable(element));
        return element;
    }
    public WebElement waitForThisToExist(final String search, int timeout, int polling, final byType type){
        Wait<WebDriver>  wait = new FluentWait<>(webDriver)
                .withTimeout(timeout, TimeUnit.SECONDS)
                .pollingEvery(polling, TimeUnit.SECONDS)
                .ignoring(ElementNotVisibleException.class)
                .ignoring(NoSuchElementException.class);
        WebElement element = wait.until(new com.google.common.base.Function<WebDriver, WebElement>() {
            @Nullable
            public WebElement apply(@Nullable WebDriver webDriver) {
                switch (type){
                    case CSS:
                        return webDriver.findElement(By.cssSelector(search));
                    case LINKTEXT:
                        return webDriver.findElement(By.linkText(search));
                    case ID:
                        return webDriver.findElement(By.id(search));
                    case NAME:
                        return webDriver.findElement(By.name(search));
                }
                return null;
            }
        });
        wait.until(ExpectedConditions.elementToBeClickable(element));
        return element;
    }
}

enum byType{
    CSS, LINKTEXT, ID, NAME
}