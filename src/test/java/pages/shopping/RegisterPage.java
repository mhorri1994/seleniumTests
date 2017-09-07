package pages.shopping;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;


/**
 * Created by Administrator on 06/09/2017.
 */
public class RegisterPage {
    @FindBy(id = "id_gender1")
    public static WebElement maleSelector;

    @FindBy(id = "id_gender2")
    public static WebElement femaleSelector;

    @FindBy(name = "customer_firstname")
    public static WebElement firstName;

    @FindBy(name = "customer_lastname")
    public static WebElement lastName;

    @FindBy(name = "email")
    public static WebElement email;

    @FindBy(name = "passwd")
    public static WebElement password;

    @FindBy(name = "days")
    public static WebElement dobDays;

    @FindBy(name = "months")
    public static WebElement dobMonths;

    @FindBy(name = "years")
    public static WebElement dobYears;

    @FindBy(name = "firstname")
    public static WebElement addressFirstName;

    @FindBy(name = "lastname")
    public static WebElement addressLastName;

    @FindBy(name = "address1")
    public static WebElement addressAddress;

    @FindBy(name = "city")
    public static WebElement addressCity;

    @FindBy(name = "id_state")
    public static WebElement addressState;

    @FindBy(name = "postcode")
    public static WebElement addressPostCode;

    @FindBy(name = "id_country")
    public static WebElement addressCountry;

    @FindBy(name = "phone_mobile")
    public static WebElement addressMobilePhone;

    @FindBy(name = "alias")
    public static WebElement addressAlias;

    @FindBy(id = "submitAccount")
    public static WebElement completeForm;

    public static void setDropdown(WebElement webEle, String optionValue){
        try {
            Select select = new Select(webEle);
            select.selectByValue(optionValue);
        }catch (Error e){
            System.out.println("Select couldn't be located for that option");
        }

    }
}
