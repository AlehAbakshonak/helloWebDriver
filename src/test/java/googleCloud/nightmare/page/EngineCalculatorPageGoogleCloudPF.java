package googleCloud.nightmare.page;

import googleCloud.nightmare.test.Field;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class EngineCalculatorPageGoogleCloudPF extends AbstractPage<EngineCalculatorPageGoogleCloudPF> {

   @FindBy(xpath = "//md-tab-item[@aria-controls='tab-content-1']")
   private WebElement computeEngineButton;

   @FindBy(xpath = "//form[@name='ComputeEngineForm']/descendant::button[@aria-label='Add to Estimate']")
   private WebElement addToEstimateButton;

   @FindBy(xpath = "//div[@ng-controller='CloudCartCtrl as cloudCartCtrl']/descendant::h2[@class='md-title']/b")
   private WebElement totalCost;

   @FindBy(xpath = "//button[@aria-label='Email Estimate']")
   private WebElement mailEstimateButton;

   @FindBy(xpath = "//input[@ng-model='emailQuote.user.email']")
   private WebElement userMailField;

   @FindBy(xpath = "//button[@aria-label='Send Email']")
   WebElement buttonSendEmail;

   String instanceInListBoxTemplateXPath = "//div[@class='md-select-menu-container md-active md-clickable']" +
         "/descendant::md-option[@value='%s']";

   public EngineCalculatorPageGoogleCloudPF(WebDriver driver) {
      super(driver);
   }

   private WebElement findInstanceInListBoxByEnumValueField(String value) {
      By instanceInListBoxActualXPath = By.xpath(String.format(instanceInListBoxTemplateXPath, value));
      //driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);
      return waitForElementLocatedBy(instanceInListBoxActualXPath);
   }

   private WebElement getCalculatorField(String name) {
      String xPathSelectorForInstanceName = "[contains(@ng-model,'" + name + "')]";
      return waitForElementLocatedBy(By.xpath(
            "//div[@flex-gt-sm='90']" +
                  "/descendant::input" + xPathSelectorForInstanceName +
                  " | descendant::md-checkbox" + xPathSelectorForInstanceName +
                  " | descendant::md-select" + xPathSelectorForInstanceName));
   }

   public EngineCalculatorPageGoogleCloudPF clickComputeEngineButtonIfInactive() {
      if (!waitForWebElementVisible(computeEngineButton).getAttribute("class").contains("md-active")) {
         computeEngineButton
               .click();
      }
      return this;
   }

   public EngineCalculatorPageGoogleCloudPF switchToCalculatorIFrame() {
      driver.switchTo().frame(driver.findElement(By.xpath("//iFrame")));
      driver.switchTo().frame("myFrame");
      this.windowHandle = driver.getWindowHandle();
      return this;
   }

   public EngineCalculatorPageGoogleCloudPF fillFieldsOnPageAccordingToEnumFields(Field[] allEnumFields) {
      for (int j = 0; j < Field.values().length; j++) {
         String currentEnumName = allEnumFields[j].getName();
         String currentEnumValue = allEnumFields[j].getValue();
         WebElement currentCalculatorField = getCalculatorField(currentEnumName);
         switch (currentCalculatorField.getTagName()) {
            case "input":
               currentCalculatorField.sendKeys(allEnumFields[j].getValue());
               break;
            case "md-checkbox":
               currentCalculatorField.click();
               break;
            case "md-select":
               currentCalculatorField.click();
               findInstanceInListBoxByEnumValueField(currentEnumValue).click();
               break;
         }
      }
      return this;
   }

   public EngineCalculatorPageGoogleCloudPF clickOnEstimateButton() {
      waitForWebElementVisible(addToEstimateButton).click();
      return this;
   }

   public String getTotalCost() {
      return waitForWebElementVisible(totalCost).getText();
   }

   public EngineCalculatorPageGoogleCloudPF clickMailEstimateButton() {

      waitForWebElementVisible(mailEstimateButton).click();
      return this;
   }

   public EngineCalculatorPageGoogleCloudPF enterMailInMailField(String destination) {
      coldWait(200);
      userMailField.sendKeys(destination);
      return this;
   }

   public EngineCalculatorPageGoogleCloudPF clickSendEmailButton() {
      waitForWebElementVisible(buttonSendEmail).click();
      return this;
   }
}
