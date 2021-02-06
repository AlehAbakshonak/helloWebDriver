package googleCloud.nightmare.page;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public abstract class AbstractPage<T> {
   protected WebDriver driver;
   protected String windowHandle;
   private Wait<WebDriver> wait;

   protected void coldWait(int time) {
      driver.manage().timeouts().implicitlyWait(time, TimeUnit.MILLISECONDS);
   }

   protected WebElement waitForElementLocatedBy(By by) {
      return new WebDriverWait(driver, 8).
            until(ExpectedConditions.presenceOfElementLocated(by));
   }

   protected WebElement waitForElementLocatedBy(int time, By by) {
      return new WebDriverWait(driver, time).
            until(ExpectedConditions.presenceOfElementLocated(by));
   }

   protected WebElement waitForWebElementVisible(WebElement element) {
      return wait.until(ExpectedConditions.visibilityOf(element));
   }

   protected void openNewPageWithFocus() {
      JavascriptExecutor jse = (JavascriptExecutor) driver;
      jse.executeScript("window.open()");

      ArrayList<String> tabs = getAllWindowHandles();
      String currentWindowHandle = driver.getWindowHandle();
      int indexOfCurrentWindowHandle = tabs.indexOf(currentWindowHandle);
      driver.switchTo().window(tabs.get(indexOfCurrentWindowHandle + 1));
   }

   private ArrayList<String> getAllWindowHandles() {
      return new ArrayList<>(driver.getWindowHandles());
   }

   public T takeFocus() throws Exception {
      ArrayList<String> tabs = getAllWindowHandles();
      boolean focusTaken = false;

      for (String tabsEntity : tabs) {
         if (tabsEntity.contains(this.windowHandle)) {
            driver.switchTo().window(tabsEntity);
            focusTaken = true;
            break;
         }
      }
      if (!focusTaken) {
         throw new Exception("Attempt to gain focus by handle for page that don't exist\n" +
               "List of all handles:\n" + tabs.toString() + "\n" +
               "target handle: " + this.windowHandle);
      }
      return (T) this;
   }

   protected AbstractPage(WebDriver driver) {
      this.driver = driver;
      this.windowHandle = "";
      PageFactory.initElements(driver, this);
      wait = new FluentWait<>(driver)
            .withTimeout(Duration.ofSeconds(20))
            .pollingEvery(Duration.ofSeconds(2));
   }
}
