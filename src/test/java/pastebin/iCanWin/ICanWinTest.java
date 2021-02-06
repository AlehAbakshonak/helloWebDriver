package pastebin.iCanWin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ICanWinTest {
   public static void main(String[] args) throws InterruptedException {
      WebDriver driver = new ChromeDriver();
      driver.get("https://pastebin.com");

      WebElement textArea = new WebDriverWait(driver,10).
            until(ExpectedConditions.presenceOfElementLocated(By.xpath("//textarea[@id='postform-text']")));
      textArea
            .sendKeys("Hello from WebDriver");

      WebElement pasteExpirationSelector = driver
            .findElement(By.xpath("//span[@id='select2-postform-expiration-container']"));
      pasteExpirationSelector
            .click();

      new WebDriverWait(driver,10)
            .until(ExpectedConditions
                  .presenceOfElementLocated(By.xpath("//ul[@id='select2-postform-expiration-results']/child::li[text()='10 Minutes']")))
            .click();

      WebElement pasteNameInput = driver
            .findElement(By.xpath("//input[@id='postform-name']"));
      pasteNameInput
            .sendKeys("helloweb");

      WebElement newPasteButton = driver
            .findElement(By.xpath("//button[@type='submit']"));
      newPasteButton
            .click();

      Thread.sleep(3000);
      driver.quit();
   }
}
