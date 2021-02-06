import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class HelloWebDriver {
   public static void main(String[] args) throws InterruptedException {
      WebDriver driver = new ChromeDriver();
      driver.get("https://google.by");
      //*[@id="tsf"]/div[2]/div[1]/div[1]/div/div[2]/input
      Thread.sleep(2000);
      WebElement webElement = driver.findElement(By.xpath("//*[@id='tsf']/div[2]/div[1]/div[1]/div/div[2]/input"));
      webElement.sendKeys("Гугл разбань меня плиз");
      WebElement searchBtn = driver.findElement(By.xpath("//*[@id='tsf']/div[2]/div[1]/div[3]/center/input[1]"));
      searchBtn.click();
      Thread.sleep(1000);
      driver.quit();
   }
}
