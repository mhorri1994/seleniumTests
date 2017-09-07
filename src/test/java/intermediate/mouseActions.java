package intermediate;

import com.aventstack.extentreports.model.ExceptionInfo;
import com.google.common.base.Function;

import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

import extentReportManager.*;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.junit.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.annotation.Nullable;

/**
 * Created by Administrator on 05/09/2017.
 */
public class mouseActions {
    private WebDriver webDriver;
    private Actions builder;
    private static ExtentReportManager reportManager;
    private PageFactory pf = new PageFactory();

    @BeforeClass
    public static void init(){
        String loc = System.getProperty("user.dir");
        ReportDetails reportDetails = new ReportDetails(loc + "\\TestReport", "Basic Extent Report","Basic Report");
        reportManager = new ExtentReportManager(ExtentReportManager.ReportType.HTML,reportDetails);
    }
    @Before
    public void setUp(){
        webDriver = new ChromeDriver();
        builder = new Actions(webDriver);
        webDriver.navigate().to("http://demoqa.com/draggable/");
    }

    @After
    public void cleaning(){
    }

    @AfterClass
    public static void cleanUp(){
        reportManager.clearTests();
    }

    @Test
    public void draggableOne(){
        moveElement("draggable", 100, 100);
    }

    @Test
    public void draggableTwo(){
        webDriver.findElement(By.id("ui-id-2")).click();
        moveElement("draggabl", 0, 100);
        moveElement("draggabl2", 100, 0);
        moveElement("draggabl3", 100, 100);
        moveElement("draggabl5", 100, 100);
    }

    @Test
    public void draggableThree(){
        webDriver.findElement(By.id("ui-id-3")).click();
        moveElement("drag", 0, 100);
        moveElement("drag2", 100, 0);
        moveElement("drag3", 100, 100);
    }

    @Test
    public void draggableFour(){
        webDriver.findElement(By.id("ui-id-4")).click();
        moveElement("dragevent", 0, 100);
        pause(1000);
        moveElement("dragevent", 100, 0);
        pause(1000);
        moveElement("dragevent", 0, -100);
        pause(1000);
        moveElement("dragevent", -100, 0);
    }

    @Test
    public void draggableFive(){
        webDriver.findElement(By.id("ui-id-5")).click();
        moveElement("draggablebox", 0, 25, false);
    }

    @Test
    public void droppableOne(){
        webDriver.navigate().to("http://demoqa.com/droppable/");
        pause(1000);
        moveMeInto("draggableview", "droppableview");
    }
    @Test
    public void droppableTwo(){
        webDriver.navigate().to("http://demoqa.com/droppable/");
        webDriver.findElement(By.id("ui-id-2")).click();

        pause(1000);
        moveMeInto("draggableaccept", "droppableaccept");
        moveMeInto("draggable-nonvalid", "droppableaccept");
    }

    @Test
    public void resizableOne(){
        webDriver.navigate().to("http://demoqa.com/resizable/");
        Wait<WebDriver> wait = new FluentWait<WebDriver>(webDriver)
                .withTimeout(30, TimeUnit.SECONDS)
                .pollingEvery(5, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);
        WebElement dragMe = wait.until(new Function<WebDriver, WebElement>() {
            @Nullable
            public WebElement apply(@Nullable WebDriver webDriver) {
                return webDriver.findElement(By.cssSelector("#resizable > div.ui-resizable-handle.ui-resizable-se.ui-icon.ui-icon-gripsmall-diagonal-se"));
            }
        });
        builder.clickAndHold(dragMe)
            .moveByOffset(100, 100)
            .release(dragMe)
            .perform();
    }

    @Test
    public void selectableOne(){
        webDriver.navigate().to("http://demoqa.com/selectable/");
        List<WebElement> eles = webDriver.findElements(By.cssSelector("#selectable > li"));
        builder.keyDown(Keys.CONTROL)
                .click(eles.get(0))
                .click(eles.get(2))
                .click(eles.get(4))
                .perform();
        List<WebElement> verifyThese = webDriver.findElements(By.className("ui-selected"));
        for(WebElement ele:verifyThese){
            if(ele.getText().equals("Item 1") || ele.getText().equals("Item 3") || ele.getText().equals("Item 5")){
                Assert.assertEquals(ele.getCssValue("background-color"), "rgba(243, 152, 20, 1)");
            }
        }

    }

    public void pause(int ms){
        try {
            Thread.sleep(ms);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void moveElement(String id, int x, int y){
        WebElement src = webDriver.findElement(By.id(id));
        Point oldPos = src.getLocation();
        builder.dragAndDropBy(src, x, y).perform();
        Assert.assertNotEquals(oldPos, src.getLocation());
    }
    public void moveElement(String id, int x, int y, boolean assertOpt){
        WebElement src = webDriver.findElement(By.id(id));
        Point oldPos = src.getLocation();
        builder.dragAndDropBy(src, x, y).perform();
        if(assertOpt)
            Assert.assertNotEquals(oldPos, src.getLocation());
    }

    public void moveMeInto(String id, String target){
        WebElement srcEle = webDriver.findElement(By.id(id));
        WebElement targetEle = webDriver.findElement(By.id(target));
        Point oldPos = srcEle.getLocation();
        builder.clickAndHold(srcEle)
                .moveToElement(targetEle)
                .release(srcEle)
                .perform();
        Assert.assertNotEquals(oldPos, srcEle.getLocation());
    }
}
