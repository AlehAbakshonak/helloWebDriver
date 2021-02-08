package tests.pastebin.bringItOn;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import tests.pastebin.page.PastebinCreatedPastePagePF;
import tests.pastebin.page.PastebinMainPagePF;

public class WDPastebinBringItOnTests {
   WebDriver driver;

   PastebinMainPagePF pastebinMainPagePF;
   PastebinCreatedPastePagePF pastebinCreatedPastePagePF;

   String textAreaText =
         "git config --global user.name  \"New Sheriff in Town\"" +
               "\ngit reset $(git commit-tree HEAD^{tree} -m \"Legacy code\")" +
               "\ngit push origin master --force";
   String syntaxName = "Bash";
   String pasteExpirationTime = "10 Minutes";
   String pasteName = "how to gain dominance among developers";

   @BeforeTest
   public void initiateBrowserWithGoogleCloudConnection() {
      driver = new ChromeDriver();
      pastebinMainPagePF = new PastebinMainPagePF(driver)
            .pageOpen()
            .maximize();
      pastebinCreatedPastePagePF = new PastebinCreatedPastePagePF(driver);
   }

   public WebElement waitForElementLocatedBy(By by) {
      return new WebDriverWait(driver, 8).
            until(ExpectedConditions.presenceOfElementLocated(by));
   }

   @Test
   public void newPasteCreationTest() {
      pastebinMainPagePF
            .pageOpen()
            .maximize()
            .sendKeysToTextArea(textAreaText)
            .openSyntaxHighlightingSelector()
            .selectSyntax(syntaxName)
            .openPasteExpirationSelector()
            .selectPasteExpiration(pasteExpirationTime)
            .sendKeysToPasteNameInput(pasteName)
            .clickNewPasteButton();
   }

   @Test(dependsOnMethods = {"newPasteCreationTest"})
   public void createdPasteNameHeaderInfoCheck() {
      WebElement pasteHeaderInfo =
            waitForElementLocatedBy(By.xpath("//div[@class='info-top']/child::h1"));
      String pasteHeaderInfoText = pasteHeaderInfo
            .getText();

      Assert.assertEquals(pasteName, pasteHeaderInfoText,
            "Header name of created paste does not match initial paste name");
   }

   @Test(dependsOnMethods = {"newPasteCreationTest"})
   public void createdPasteNameSyntaxCheck() {
      String createdPasteSyntaxText =
            pastebinCreatedPastePagePF
                  .getCreatedPasteSyntax();
      Assert.assertEquals(syntaxName, createdPasteSyntaxText,
            "Syntax of created paste does not match initial paste syntax");
   }

   @Test(dependsOnMethods = {"newPasteCreationTest"})
   public void createdPasteTextCheck() {
      String createdPasteText =
            pastebinCreatedPastePagePF
                  .getCreatedPasteText();
      Assert.assertEquals(textAreaText, createdPasteText,
            "Text of created paste does not match initial paste text");
   }

   @AfterTest(alwaysRun = true)
   public void browserTerminate() {
      driver.quit();
      driver = null;
   }
}
