package tests.pastebin.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import tests.AbstractPage;

public class PastebinCreatedPastePagePF extends AbstractPage<PastebinCreatedPastePagePF> {
   public PastebinCreatedPastePagePF(WebDriver driver) {
      super(driver);
   }

   @FindBy(xpath = "//div[@class='highlighted-code']/descendant::a")
   WebElement createdPasteSyntax;

   public String getCreatedPasteSyntax() {
      return createdPasteSyntax.getText();
   }

   @FindBy(xpath = "//textarea[@class='textarea']")
   WebElement createdPasteText;

   public String getCreatedPasteText() {
      return createdPasteText.getText();
   }
}
