package Basic;

import org.junit.*;

/**
 * Created by Administrator on 04/09/2017.
 */
public class understandingJUnit {
    @BeforeClass
    public static void beforeClass(){
        System.out.println("BeforeClass");
    }

    @Before
    public void before(){
        System.out.println("Before");
    }
    @Test
    public void test1(){
        System.out.println("Test");
    }
    @After
    public void after(){
        System.out.println("After");
    }
    @AfterClass
    public static void afterClass(){
        System.out.println("AfterClass");
    }
}
