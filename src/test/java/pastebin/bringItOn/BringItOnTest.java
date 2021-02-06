package pastebin.bringItOn;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BringItOnTest {

   WebDriver driver;
   String textAreaText =
         "git config --global user.name  \"New Sheriff in Town\"" +
               "\ngit reset $(git commit-tree HEAD^{tree} -m \"Legacy code\")" +
               "\ngit push origin master --force";
   String pasteName = "how to gain dominance among developers";
   String syntaxName = "Bash";

   @BeforeTest
   public void initiateBrowserWithPastebinConnection() {
      driver = new ChromeDriver();
      driver.get("https://pastebin.com");
      driver.manage().window().maximize();
   }


   public WebElement waitForElementLocatedBy(By by) {
      return new WebDriverWait(driver, 8).
            until(ExpectedConditions.presenceOfElementLocated(by));
   }

   @Test
   public void newPasteCreationTest() {

      WebElement textArea =
            waitForElementLocatedBy(By.xpath("//textarea[@id='postform-text']"));

      textArea
            .sendKeys(textAreaText);

      WebElement syntaxHighlightingSelector = driver
            .findElement(By.xpath("//span[@id='select2-postform-format-container']"));
      syntaxHighlightingSelector
            .click();

      waitForElementLocatedBy(By.xpath("//input[@class='select2-search__field']"))
            .sendKeys(syntaxName + Keys.ENTER);

      WebElement pasteExpirationSelector = driver
            .findElement(By.xpath("//span[@id='select2-postform-expiration-container']"));
      pasteExpirationSelector
            .click();

      waitForElementLocatedBy(By.xpath("//ul[@id='select2-postform-expiration-results']/child::li[text()='10 Minutes']"))
            .click();

      WebElement pasteNameInput = driver
            .findElement(By.xpath("//input[@id='postform-name']"));
      pasteNameInput
            .sendKeys(pasteName);

      WebElement newPasteButton = driver
            .findElement(By.xpath("//button[@type='submit']"));
      newPasteButton
            .click();
   }

   @Test(dependsOnMethods = {"newPasteCreationTest"})
   public void createdPasteNameHeaderInfoCheck() {
      WebElement pasteHeaderInfo =
            waitForElementLocatedBy(By.xpath("//div[@class='info-top']/child::h1"));
      String pasteHeaderInfoText = pasteHeaderInfo
            .getText();

      Assert.assertEquals(pasteName, pasteHeaderInfoText, "Header name of created paste does not match initial paste name");
   }

   @Test(dependsOnMethods = {"newPasteCreationTest"})
   public void createdPasteNameSyntaxCheck() {
      WebElement createdPasteSyntax =
            waitForElementLocatedBy(By.xpath("//div[@class='highlighted-code']/descendant::a[text()='" + syntaxName + "']"));
      String createdPasteSyntaxText = createdPasteSyntax
            .getText();

      Assert.assertEquals(syntaxName, createdPasteSyntaxText, "Syntax of created paste does not match initial paste syntax");
   }

   @Test(dependsOnMethods = {"newPasteCreationTest"})
   public void createdPasteTextCheck() {
      WebElement createdPasteText =
            waitForElementLocatedBy(By.xpath("//textarea[@class='textarea']"));
      String createdPasteTextAsString = createdPasteText.getText();

      Assert.assertEquals(textAreaText, createdPasteTextAsString, "Text of created paste does not match initial paste text");
   }

   @AfterTest(alwaysRun = true)
   public void browserTerminate() {
      driver.quit();
      driver = null;
   }
}
