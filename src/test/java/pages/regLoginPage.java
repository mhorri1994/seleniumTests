package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Created by Administrator on 05/09/2017.
 */
public class regLoginPage {

    @FindBy(name = "username")
    public static WebElement usernameBox;

    @FindBy(name = "password")
    public static WebElement passwordBox;
}
