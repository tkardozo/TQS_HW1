/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package currencyConversion;

import java.util.concurrent.TimeUnit;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

/**
 *
 * @author tkardozo
 */
public class ConverterBeanIT {
    private WebDriver driver;
    private String baseUrl = "http://localhost:8080/currency/";

    @Before
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver","/home/tkardozo/chromedriver");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get(baseUrl);
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

    @Test
    public void convertToSameCurrency() throws Exception {
        driver.findElement(By.name("j_idt5:j_idt8")).clear();
        driver.findElement(By.name("j_idt5:j_idt8")).sendKeys("12");
        new Select(driver.findElement(By.name("j_idt5:j_idt9"))).selectByVisibleText("EUR");
        new Select(driver.findElement(By.name("j_idt5:j_idt13"))).selectByVisibleText("EUR");
        driver.findElement(By.name("j_idt5:j_idt16")).click();
        String result = driver.findElement(By.name("j_idt5:j_idt12")).getAttribute("value");
       
        assertEquals("12 EUR must convert to 12 EUR", "12.0", result);
        
        driver.quit();
    }
    
}
