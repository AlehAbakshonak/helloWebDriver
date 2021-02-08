package tests.pastebin.page;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import tests.AbstractPage;
import tests.googleCloud.page.HomePageGoogleCloudPF;

public class PastebinMainPagePF extends AbstractPage<PastebinMainPagePF> {
   public PastebinMainPagePF(WebDriver driver) {
      super(driver);
   }

   private final String MAINPAGE_URL = "https://pastebin.com";

   @FindBy(xpath = "//textarea[@id='postform-text']")
   WebElement textArea;

   @FindBy(xpath = "//span[@id='select2-postform-format-container']")
   WebElement syntaxHighlightingSelector;

   @FindBy(xpath = "//input[@class='select2-search__field']")
   WebElement searchFieldOfSyntax;

   @FindBy(xpath = "//span[@id='select2-postform-expiration-container']")
   WebElement pasteExpirationSelector;

   @FindBy(xpath = "//input[@id='postform-name']")
   WebElement pasteNameInput;

   @FindBy(xpath = "//button[@type='submit']")
   WebElement newPasteButton;

   String pasteExpirationTemplateXPath = "//ul[@id='select2-postform-expiration-results']/child::li[text()='%s']";

   public PastebinMainPagePF pageOpen() {
      driver.get(MAINPAGE_URL);
      return this;
   }

   public PastebinMainPagePF maximize() {
      driver.manage().window().maximize();
      return this;
   }

   public PastebinMainPagePF sendKeysToTextArea(String text) {
      textArea.sendKeys(text);
      return this;
   }

   public PastebinMainPagePF openSyntaxHighlightingSelector() {
      syntaxHighlightingSelector.click();
      return this;
   }

   public PastebinMainPagePF selectSyntax(String syntaxName) {
      searchFieldOfSyntax.sendKeys(syntaxName + Keys.ENTER);
      return this;
   }


   public PastebinMainPagePF openPasteExpirationSelector() {
      pasteExpirationSelector.click();
      return this;
   }

   public PastebinMainPagePF selectPasteExpiration(String text) {
      By pasteExpirationActualXPath = By.xpath(String.format(pasteExpirationTemplateXPath, text));
      waitForElementLocatedBy(pasteExpirationActualXPath).click();
      return this;
   }

   public PastebinMainPagePF sendKeysToPasteNameInput(String text) {
      pasteNameInput.sendKeys(text);
      return this;
   }

   public PastebinMainPagePF clickNewPasteButton() {
      newPasteButton.click();
      return this;
   }
}
