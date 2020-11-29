package Trash;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;
public class DeleteTrash {
    private WebDriver driver;
    private WebDriverWait wait;
    @Before
    public void start(){
        driver = new ChromeDriver();

        wait = new WebDriverWait(driver,1000);
    }
    @Test
    public void myFirstTest() throws InterruptedException {
        //Переход на страницу админа
        for (int i = 0; i < 3; i++) {
            driver.get("http://localhost/litecart/en/");
            driver.findElement(By.cssSelector("#box-most-popular [class = 'product column shadow hover-light']")).click();
            String countItems = driver.findElement(By.className("quantity")).getAttribute("textContent");
            if (driver.findElements(By.cssSelector("option")).size() > 0){
                Select size = new Select(driver.findElement(By.cssSelector("[name='options[Size]']")));
                size.selectByValue("Small");
            }
            wait.until(elementToBeClickable(driver.findElement(By.name("add_cart_product"))));
            driver.findElement(By.name("add_cart_product")).click();
            // Ждем пока обновится счетчик
            wait.until(not(ExpectedConditions.attributeToBe(By.cssSelector("#cart .quantity"), "textContent", countItems)));
        }
        driver.findElement(By.cssSelector("#cart .link")).click();
        wait.until(presenceOfElementLocated(By.cssSelector(".dataTable tr")));
        int rows = driver.findElements(By.cssSelector(".dataTable tr")).size() - 5;
        for (int i = 0; i < rows; ++i) {
            WebElement table = driver.findElement(By.cssSelector(".dataTable"));
            wait.until(elementToBeClickable(By.cssSelector("button[name=remove_cart_item]")));
            driver.findElement(By.cssSelector("button[name=remove_cart_item]")).click();
            //обновление таблицы (она исчезает)
            wait.until(stalenessOf(table));
            if (i != rows - 1) {
                wait.until(visibilityOfElementLocated(By.cssSelector(".dataTable")));//после удаления таблицы нет строк
            }
        }
    }
    @After
    public void stop(){
        driver.quit();
        driver = null;
    }
}
