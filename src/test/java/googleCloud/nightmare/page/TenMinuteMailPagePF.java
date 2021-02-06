package googleCloud.nightmare.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class TenMinuteMailPagePF extends AbstractPage<TenMinuteMailPagePF> {

   private final String MAIL_URL = "https://10minutemail.com/";

   String deliveredMailTemplateXPath = "//span[text()='%s']/ancestor::div[@class='mail_message']";

   @FindBy (xpath = "//div[@class='mail_address']/child::input")
   private WebElement temporaryMailField;

   @FindBy (xpath = "//h2[contains(text(),'Estimated')]")
   WebElement labelWithTotalCostInMail;

   public TenMinuteMailPagePF(WebDriver driver) {
      super(driver);
   }

   public TenMinuteMailPagePF openInNewPage() {
      openNewPageWithFocus();
      driver.get(MAIL_URL);
      this.windowHandle = driver.getWindowHandle();
      return this;
   }

   public String getTemporaryMail() {
      return waitForWebElementVisible(temporaryMailField).getAttribute("value");
   }

   public TenMinuteMailPagePF clickOnDeliveredMailByItsTextWithCustomWaitTime(int timeInSeconds, String text) {
      By compiledDeliveredMailXPath = By.xpath(String.format(deliveredMailTemplateXPath, text));
      waitForElementLocatedBy(timeInSeconds, compiledDeliveredMailXPath).click();
      return this;
   }

   public String getTotalCostFromDeliveredMail() {
      return labelWithTotalCostInMail.getText();
   }
}
