package Intermediate;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by Administrator on 04/09/2017.
 */
public class login {
    WebDriver webDriver;

    @Before
    public void setUp(){
        webDriver = new ChromeDriver();
    }

    @Test
    public void firstTest(){
        webDriver.navigate().to("http://thedemosite.co.uk/");
        //register a user
        webDriver.findElement(By.linkText("3. Add a User")).click();
        webDriver.findElement(By.name("username")).sendKeys("myUser");
        webDriver.findElement(By.name("password")).sendKeys("myUser");
        webDriver.findElement(By.name("FormsButton2")).click();
        //login that user
        webDriver.findElement(By.linkText("4. Login")).click();
        webDriver.findElement(By.name("username")).sendKeys("myUser");
        webDriver.findElement(By.name("password")).sendKeys("myUser");
        webDriver.findElement(By.name("FormsButton2")).click();

        WebDriverWait wait = new WebDriverWait(webDriver, 3);
        wait.until(ExpectedConditions.textToBePresentInElement(By.cssSelector(".auto-style1 > big:nth-child(6) > blockquote:nth-child(1) > blockquote:nth-child(1) > font:nth-child(1) > center:nth-child(1) > b:nth-child(1)"), "**Successful Login**"));
    }

    @After
    public void cleaning(){
        webDriver.quit();
    }
}
