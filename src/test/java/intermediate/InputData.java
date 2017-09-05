package intermediate;

import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import pages.regLoginPage;
import utils.SpreadsheetReader.SpreadSheetReader;

import java.util.List;
import extentReportManager.*;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.junit.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


/**
 * Created by Administrator on 04/09/2017.
 */
public class InputData {
    private WebDriver webDriver;
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
    }


    public void registerAndLogInUserTest(String username, String password, String finalState){

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
    public void spreadSheet(){
        SpreadSheetReader sheetReader = new SpreadSheetReader(System.getProperty("user.dir") + "/src/main/resources/users.xlsx");
        List<String> row;
        for(int i = 0; i < sheetReader.getNumberOfRows("Input Data")-1;i++){
            row = sheetReader.readRow(i, "Input Data");
            try {
                ExtentTest registerAndLogInUserTest = reportManager.setUpTest();
                registerAndLogInUserTest.log(Status.INFO, "Navigating to website");
                webDriver.navigate().to("http://thedemosite.co.uk/");
                //register a user
                registerAndLogInUserTest.log(Status.INFO, "Navigating to register page.");
                webDriver.findElement(By.linkText("3. Add a User")).click();
                registerAndLogInUserTest.log(Status.INFO, "Completing register form.");
//                webDriver.findElement(By.name("username")).sendKeys(row.get(0));
//                webDriver.findElement(By.name("password")).sendKeys(row.get(1));

                pf.initElements(webDriver, regLoginPage.class);
                regLoginPage.usernameBox.sendKeys(row.get(0));
                regLoginPage.passwordBox.sendKeys(row.get(1));


                registerAndLogInUserTest.log(Status.INFO, "Submitting register form.");
                webDriver.findElement(By.name("FormsButton2")).click();
                //Login that user
                registerAndLogInUserTest.log(Status.INFO, "Navigating to Login form.");
//                try {
//                    WebDriverWait wait = new WebDriverWait(webDriver, 2);
//                    wait.until(ExpectedConditions.alertIsPresent());
//                    Alert alert = webDriver.switchTo().alert();
//                    alert.accept();
//                } catch (Exception e) {
//                    //exception handling
//                }

                webDriver.findElement(By.linkText("4. Login")).click();
                registerAndLogInUserTest.log(Status.INFO, "Completing Login form.");
                pf.initElements(webDriver, regLoginPage.class);
                regLoginPage.usernameBox.sendKeys(row.get(0));
                regLoginPage.passwordBox.sendKeys(row.get(1));
//                webDriver.findElement(By.name("username")).sendKeys(row.get(0));
//                webDriver.findElement(By.name("password")).sendKeys(row.get(1));
                registerAndLogInUserTest.log(Status.INFO, "Submitting Login form.");
                webDriver.findElement(By.name("FormsButton2")).click();
//                try {
//                    WebDriverWait wait = new WebDriverWait(webDriver, 2);
//                    wait.until(ExpectedConditions.alertIsPresent());
//                    Alert alert = webDriver.switchTo().alert();
//                    alert.accept();
//                } catch (Exception e) {
//                    //exception handling
//                }
                registerAndLogInUserTest.log(Status.INFO, "Verifying that the user has been logged in.");

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

            }catch (NullPointerException e){
//                System.out.println(e.getMessage());
            }
        }
    }
}
