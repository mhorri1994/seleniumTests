package Intermediate;

import ExtentReportManager.*;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.apache.bcel.verifier.exc.AssertionViolatedException;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by Administrator on 04/09/2017.
 */
public class login {
    private WebDriver webDriver;
    private static ExtentReportManager reportManager;

    @BeforeClass
    public static void init(){
        String loc = System.getProperty("user.dir");
        ReportDetails reportDetails = new ReportDetails(loc + "\\TestReport", "Basic Extent Report","Basic Report");
        reportManager = new ExtentReportManager(ExtentReportManager.ReportType.HTML,reportDetails);
    }

    @Before
    public void setUp(){
        webDriver = new ChromeDriver();
    }

    @Test
    public void registerAndLogInUserTest(){
        ExtentTest registerAndLogInUserTest = reportManager.setUpTest();
        registerAndLogInUserTest.log(Status.INFO, "Navigating to website");
        webDriver.navigate().to("http://thedemosite.co.uk/");
        //register a user
        registerAndLogInUserTest.log(Status.INFO, "Navigating to register page.");
        webDriver.findElement(By.linkText("3. Add a User")).click();
        registerAndLogInUserTest.log(Status.INFO, "Completing register form.");
        webDriver.findElement(By.name("username")).sendKeys("myUser");
        webDriver.findElement(By.name("password")).sendKeys("myUser");
        registerAndLogInUserTest.log(Status.INFO, "Submitting register form.");
        webDriver.findElement(By.name("FormsButton2")).click();
        //login that user
        registerAndLogInUserTest.log(Status.INFO, "Navigating to login form.");
        webDriver.findElement(By.linkText("4. Login")).click();
        registerAndLogInUserTest.log(Status.INFO, "Completing login form.");
        webDriver.findElement(By.name("username")).sendKeys("myUser");
        webDriver.findElement(By.name("password")).sendKeys("myUser");
        registerAndLogInUserTest.log(Status.INFO, "Submitting login form.");
        webDriver.findElement(By.name("FormsButton2")).click();

        registerAndLogInUserTest.log(Status.INFO, "Verifying that the user has been logged in.");
        WebDriverWait wait = new WebDriverWait(webDriver, 3);
        WebElement ele = webDriver.findElement(By.cssSelector(".auto-style1 > big:nth-child(6) > blockquote:nth-child(1) > blockquote:nth-child(1) > font:nth-child(1) > center:nth-child(1) > b:nth-child(1)"));
        registerAndLogInUserTest.log(Status.DEBUG, "Checking if the page has \" * * Successful Login **\") on it somewhere");
        wait.until(ExpectedConditions.textToBePresentInElement(ele, "**Successful Login**"));

        try{
            Assert.assertEquals(ele.getText(), "**Successful Login**");
            registerAndLogInUserTest.pass("The user was registered and logged in");
        }catch (AssertionError e){
            registerAndLogInUserTest.fail("Failed for: " + e.getMessage());
            Assert.fail("Failed for: " + e.getMessage());
        }
    }

    @After
    public void cleaning(){
        webDriver.quit();
    }

    @AfterClass
    public static void cleanUp(){
        reportManager.clearTests();
    }
}
