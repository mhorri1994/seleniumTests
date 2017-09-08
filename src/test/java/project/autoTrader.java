package project;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import extentReportManager.*;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;
import pages.regLoginPage;
import utils.SpreadsheetReader.SpreadSheetReader;

import javax.annotation.Nullable;

/**
 * Created by Administrator on 08/09/2017.
 */
public class autoTrader {
    private WebDriver webDriver;
    private Actions builder;
    private static ExtentReportManager reportManager;
    private PageFactory pf = new PageFactory();
    private boolean switchBrowser = false;
    @BeforeClass
    public static void init(){
        String loc = System.getProperty("user.dir");
        ReportDetails reportDetails = new ReportDetails(loc + "\\AutoTraderReport", "AutoTrader Report","AutoTrader Report");
        reportManager = new ExtentReportManager(ExtentReportManager.ReportType.HTML,reportDetails);
    }
    @Before
    public void setUp(){
        if(switchBrowser)
            redoWebDriver("firefox");
        else
            redoWebDriver("chrome");
        builder = new Actions(webDriver);
        webDriver.navigate().to("http://www.autotrader.co.uk/");
    }


    @After
    public void cleaning(){
        webDriver.quit();
    }

    @AfterClass
    public static void cleanUp(){
        reportManager.clearTests();
    }

    public void redoWebDriver(String driverName){
        switch (driverName){
            case "firefox":
                webDriver = new FirefoxDriver();
                break;
            case "chrome":
                webDriver = new ChromeDriver();
                break;
        }
    }

    @Test
    public void search() {
        ExtentTest searches = reportManager.manualSetupTest("Searches");
        SpreadSheetReader sheetReader = new SpreadSheetReader(System.getProperty("user.dir") + "/src/main/resources/AutoTrader.xlsx");
        List<String> row;
        for (int i = 0; i < sheetReader.getNumberOfRows("Search Data"); i++) {
            webDriver.navigate().to("http://www.autotrader.co.uk/");
            row = sheetReader.readRow(i, "Search Data");
//            redoWebDriver(row.get(7));
            ExtentTest child = searches.createNode("Searching for: " + row.get(2) + " " + row.get(3));
            child.log(Status.INFO, "Entering postcode.");
            waitForThisToExist("postcode", 30, 1, byType.ID);
            webDriver.findElement(By.id("postcode")).sendKeys(row.get(0));
            child.log(Status.INFO, "Entering distance..");
            setDropdown(webDriver.findElement(By.id("radius")), row.get(1));
            child.log(Status.INFO, "Entering vehicle make.");
            setDropdown(webDriver.findElement(By.id("searchVehiclesMake")), row.get(2));
            child.log(Status.INFO, "Entering vehicle model.");
            webDriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            setDropdown(webDriver.findElement(By.id("searchVehiclesModel")), row.get(3));
            child.log(Status.INFO, "Entering minimum price.");
            webDriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            setDropdown(webDriver.findElement(By.id("searchVehiclesPriceFrom")), row.get(4));
            child.log(Status.INFO, "Entering maximum price.");
            webDriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            setDropdown(webDriver.findElement(By.id("searchVehiclesPriceTo")), row.get(5));
            child.log(Status.INFO, "Waiting for search results.");
            webDriver.findElement(By.cssSelector("#search > span")).click();
            try {
                waitForThisToExist(".search-form__count", 15, 1, byType.CSS);
                if(row.get(6).equals("pass")){
                    child.pass("Found some vehicles.");
                } else {
                    child.fail("Vehicles found when it should've failed.");
                }
            } catch (ElementNotFoundException e){
                if(row.get(6).equals("pass")){
                    child.fail("Successfully didn't find any vehicles.");
                } else {
                    child.fail("Didn't find any vehicles.");
                }
            }
            webDriver = new ChromeDriver();
        }
    }

    @Test
    public void carValuation(){
        ExtentTest valuations = reportManager.manualSetupTest("valuations");
        SpreadSheetReader sheetReader = new SpreadSheetReader(System.getProperty("user.dir") + "/src/main/resources/AutoTrader.xlsx");
        List<String> row;
        for (int i = 0; i < sheetReader.getNumberOfRows("Valuation Data"); i++) {
            row = sheetReader.readRow(i, "Valuation Data");
            if(row.get(0).equals("REGISTRATION")) {
                ExtentTest child = valuations.createNode("Vehicle Reg:" + row.get(1));
                webDriver.navigate().to("https://www.autotrader.co.uk/car-valuation");
                webDriver.findElement(By.cssSelector(".reg-input-large")).sendKeys(row.get((1)));
                webDriver.findElement(By.cssSelector(".mileage-input-large")).sendKeys(row.get((2)));
                webDriver.findElement(By.cssSelector("body > div.car-valuations > div.parallax-background > section > div > form > button")).click();

                try {
                    waitForThisToExist("body > div.car-valuations-confirm-vehicle > div > section.top-section > div > h3", 15, 1, byType.CSS);
                    if(row.get(4).equals("pass")){
                        child.pass("Found the vehicle.");
                    } else {
                        child.fail("Vehicle found when it should've failed.");
                    }
                } catch (ElementNotFoundException e){
                    if(row.get(4).equals("pass")){
                        child.fail("Successfully didn't find the vehicle.");
                    } else {
                        child.fail("Didn't find the vehicle.");
                    }
                }
            } else continue;
        }
    }

    @Test
    public void customRegistration(){
        ExtentTest customRegTest = reportManager.manualSetupTest("Custom Registration");
        webDriver.navigate().to("http://www.autotrader.co.uk/private-number-plates");
        webDriver.findElement(By.cssSelector("input.input-large:nth-child(1)")).sendKeys("AB16 SSD");
        webDriver.findElement(By.cssSelector("body > div.private-number-plates > div.js-parallax-front > section.plates-section.section-light > div > section:nth-child(2) > form > button > span")).click();
        List<String> tabs = new ArrayList<>(webDriver.getWindowHandles());
        if(tabs.size() > 1){
            webDriver.switchTo().window(tabs.get(1));
            if(webDriver.getCurrentUrl().equals("https://britishcarregistrations.co.uk/search/AB16+SSD")){
                customRegTest.pass("Other tab opened to the correct url.");
                Assert.assertTrue(true);
            } else {
                customRegTest.fail("Other tab was not at the correct url.");
                Assert.assertTrue(false);
            }
        } else {
            customRegTest.fail("Another tab should've been opened.");
            Assert.assertTrue(false);
        }
    }



    @Test
    public void facebookLink(){
        ExtentTest facebookLinkTest = reportManager.manualSetupTest("Facebook Link");
        facebookLinkTest.log(Status.INFO, "Clicking Facebook link.");
        webDriver.findElement(By.cssSelector("#home > div.white-footer > footer > div > nav:nth-child(4) > ul > li.footer__nav-listing--1 > a")).click();
        if(webDriver.getCurrentUrl().equals("https://www.facebook.com/autotraderuk")){
            facebookLinkTest.pass("On the correct url.");
            Assert.assertTrue(true);
        } else {
            facebookLinkTest.fail("Not at the correct url.");
            Assert.assertTrue(false);
        }
    }
    @Test
    public void googleLink(){
        ExtentTest googleLinkTest = reportManager.manualSetupTest("Google Link");
        googleLinkTest.log(Status.INFO, "Clicking Google link.");
        webDriver.findElement(By.cssSelector("#home > div.white-footer > footer > div > nav:nth-child(4) > ul > li.footer__nav-listing--5 > a")).click();
        if(webDriver.getCurrentUrl().equals("https://plus.google.com/108563184034700349395")){
            googleLinkTest.pass("On the correct url.");
            Assert.assertTrue(true);
        } else {
            googleLinkTest.fail("Not at the correct url.");
            Assert.assertTrue(false);
        }
    }


    @Test
    public void youtubeLink(){
        ExtentTest youtubeLinkTest = reportManager.manualSetupTest("Youtube Link");
        youtubeLinkTest.log(Status.INFO, "Clicking youtube link.");
        webDriver.findElement(By.cssSelector("#home > div.white-footer > footer > div > nav:nth-child(4) > ul > li.footer__nav-listing--3 > a")).click();
        if(webDriver.getCurrentUrl().equals("https://www.youtube.com/user/autotraderuk?sub_confirmation=1")){
            youtubeLinkTest.pass("On the correct url.");
            Assert.assertTrue(true);
        } else {
            youtubeLinkTest.fail("Not at the correct url.");
            Assert.assertTrue(false);
        }
    }


    @Test
    public void signIn(){
        ExtentTest loginTest = reportManager.manualSetupTest("Login Test");
        loginTest.log(Status.INFO, "Clicking sign in button.");
        webDriver.findElement(By.cssSelector("#js-header-nav > ul > li.test-header__nav-listing.u-float-right > div.is-not-signed-in > a")).click();
        loginTest.log(Status.INFO, "Entering email.");
        webDriver.findElement(By.id("signin-email")).sendKeys("seleniumtesting3t6@gmail.com");
        loginTest.log(Status.INFO, "Entering password.");
        webDriver.findElement(By.id("signin-password")).sendKeys("sophie13");
        loginTest.log(Status.INFO, "Clicking submit button.");
        webDriver.findElement(By.id("signInSubmit")).click();
        try{
            waitForThisToExist("#js-header-nav > ul > li.test-header__nav-listing.u-float-right > div.is-signed-in > a > i", 15, 1, byType.CSS);
            loginTest.pass("Logged in successfully.");
            Assert.assertTrue(true);
        }catch (Exception e){
            loginTest.fail("Couldn't log in.");
            Assert.assertTrue(false);
        }
    }

    @Test
    public void sellCar(){
        signIn();
        webDriver.navigate().to("https://selling.autotrader.co.uk/find-car?reg=&mileage=&referrer=ATCOUK");
        ExtentTest sellCarTest = reportManager.manualSetupTest("Creating car adverts");

        SpreadSheetReader sheetReader = new SpreadSheetReader(System.getProperty("user.dir") + "/src/main/resources/AutoTrader.xlsx");
        List<String> row;
        for (int i = 0; i < sheetReader.getNumberOfRows("Sale Data"); i++) {
            ExtentTest child = sellCarTest.createNode("Car Test #" + i);
            row = sheetReader.readRow(i, "Sale Data");
            child.log(Status.INFO, "Entering reg details.");
            webDriver.findElement(By.id("reg")).sendKeys(row.get(0));
            child.log(Status.INFO, "Entering mileage details.");
            webDriver.findElement(By.id("mileage")).sendKeys(row.get(1));
            child.log(Status.INFO, "Clicking find details button.");
            webDriver.findElement(By.id("findDetails")).click();
            try {
                child.log(Status.INFO, "Clicking submit button.");
                WebElement ele = waitForThisToExist("submitPage", 15, 1, byType.ID);
                ele.click();
                child.log(Status.INFO, "Entering the asking price.");
                WebElement price = waitForThisToExist("askingPrice", 15, 1, byType.ID);
                price.sendKeys(row.get(2));
                child.log(Status.INFO, "Clicking the set price button.");
                ele = waitForThisToExist("setPriceButton", 15, 1, byType.ID);
                ele.click();
                child.log(Status.INFO, "Entering the title.");
                webDriver.findElement(By.id("attentionGrabberText")).sendKeys(row.get(3));
                child.log(Status.INFO, "Entering the description.");
                webDriver.findElement(By.id("desc")).sendKeys(row.get(4));
                child.log(Status.INFO, "Clicking the next button.");
                webDriver.findElement(By.id("descriptionButton")).click();
                child.log(Status.INFO, "Clicking the add images later button.");
                ele = waitForThisToExist("imagesButton", 15, 1, byType.ID);
                ele.click();
                child.log(Status.INFO, "Entering the postcode.");
                webDriver.findElement(By.id("postcode")).sendKeys(row.get(5));
                child.log(Status.INFO, "Clicking the submit button.");
                ele = waitForThisToExist("submitPage", 15, 1 , byType.ID);
                ele.click();
                child.pass("Got as far as I can without actually using real data.");
            }catch (Exception e){
                if(row.get(7).equals("fail")){
                    child.pass("Vehicle cannot be found, (required).");
                } else {
                    child.fail("Failed to register a new sale");
                }
            }
        }
    }
    public static void setDropdown(WebElement webEle, String optionValue){
        try {
            Select select = new Select(webEle);
            select.selectByValue(optionValue);
        }catch (Error e){
            System.out.println("Select couldn't be located for that option");
        }

    }
    public WebElement waitForThisToExist(final String search, int timeout, int polling, final byType type){
        Wait<WebDriver> wait = new FluentWait<>(webDriver)
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
