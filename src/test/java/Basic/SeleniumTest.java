package Basic;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Created by Administrator on 04/09/2017.
 */
public class SeleniumTest {
    WebDriver webDriver;

    @Before
    public void setUp(){
        webDriver = new ChromeDriver();
    }

    @Test
    public void firstTest(){
        webDriver.navigate().to("https://www.qa.com/");
        Assert.assertEquals("https://www.qa.com/", webDriver.getCurrentUrl());
        Assert.assertEquals("IT Training | Project Management Training | Business Skills Training | QA", webDriver.getTitle());
    }
}
