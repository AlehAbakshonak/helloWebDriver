package googleCloud.hurtMePlenty.test;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

enum Field {
   QUANTITY("computeServer.quantity", "4"),
   LABEL("computeServer.label", ""),
   OS("computeServer.os", "free"),
   CLASS("computeServer.class", "regular", "VM class: regular"),
   SERIES("computeServer.series", "n1"),
   INSTANCE("computeServer.instance", "CP-COMPUTEENGINE-VMIMAGE-N1-STANDARD-8", "Instance type: n1-standard-8"),
   ADD_GPU("computeServer.addGPUs"),
   GPU_COUNT("computeServer.gpuCount", "1"),
   GPU_TYPE("computeServer.gpuType", "NVIDIA_TESLA_V100"),
   SSD("computeServer.ssd", "2", "Total available local SSD space 2x375 GiB"),
   LOCATION("computeServer.location", "europe-west3", "Region: Frankfurt"),
   SERVER_CUD("computeServer.cud", "1", "Commitment term: 1 Year");

   String name;
   String value;
   String nameInFinalList;

   Field(String name, String value, String nameInFinalList) {
      this.name = name;
      this.value = value;
      this.nameInFinalList = nameInFinalList;
   }

   Field(String name, String value) {
      this.name = name;
      this.value = value;
   }

   Field(String name) {
      this.name = name;
   }
}

public class WebDriverGoogleCloudBringItOnTests {
   WebDriver driver;

   public WebElement waitForElementLocatedBy(By by) {
      return new WebDriverWait(driver, 8).
            until(ExpectedConditions.presenceOfElementLocated(by));
   }

   public void selectInstanceInListBoxByValueTag(WebElement element, String value) {
      element
            .click();
      waitForElementLocatedBy(By
            .xpath("//div[@class='md-select-menu-container md-active md-clickable']" +
                  "/descendant::md-option[@value='" + value + "']"))
            .click();
   }

   public void activateAdditionalGPUFieldsOnPage() {
      waitForElementLocatedBy(By.xpath("//md-checkbox[@ng-model='listingCtrl.soleTenant.addGPUs']"))
            .click();
   }

   public void checkForActiveComputeEngineButtonAndActivateItIfNot() {
      WebElement computeEngineButton =
            waitForElementLocatedBy(By.xpath("//md-tab-item[@aria-controls='tab-content-1']"));

      if (!computeEngineButton.getAttribute("class").contains("md-active")) {
         computeEngineButton
               .click();
      }
   }

   public void fillFieldsOnPageAccordingToEnumFields() {
      Field[] allEnumFields = Field.class.getEnumConstants();

      for (int j = 0; j < Field.values().length; j++) {
         String xPathSelectorForInstanceName = "[contains(@ng-model,'" + allEnumFields[j].name + "')]";
         WebElement currentField = waitForElementLocatedBy(By.xpath(
               "//div[@flex-gt-sm='90']" +
                     "/descendant::input" + xPathSelectorForInstanceName +
                     " | descendant::md-checkbox" + xPathSelectorForInstanceName +
                     " | descendant::md-select" + xPathSelectorForInstanceName));
         switch (currentField.getTagName()) {
            case "input":
               currentField.sendKeys(allEnumFields[j].value);
               break;
            case "md-checkbox":
               currentField.click();
               break;
            case "md-select":
               selectInstanceInListBoxByValueTag(currentField, allEnumFields[j].value);
               break;
         }
      }
   }

   @BeforeTest
   public void initiateBrowserWithGoogleCloudConnection() {
      driver = new ChromeDriver();
      driver.get("https://cloud.google.com/");
      driver.manage().window().maximize();
   }

   @Test
   public void estimatingOfNewEngineInPricingCalculatorTest() {
      WebElement searchArea =
            waitForElementLocatedBy(By.xpath("//input[@class='devsite-search-field devsite-search-query']"));

      searchArea
            .sendKeys("Google Cloud Platform Pricing Calculator" +
                  Keys.ENTER);

      waitForElementLocatedBy(By.xpath("//b[text()='Google Cloud Platform Pricing Calculator']/parent::a"))
            .click();

      driver.switchTo().frame(driver.findElement(By.xpath("//iFrame")));
      driver.switchTo().frame("myFrame");

      checkForActiveComputeEngineButtonAndActivateItIfNot();

      activateAdditionalGPUFieldsOnPage();

      fillFieldsOnPageAccordingToEnumFields();

      driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
      waitForElementLocatedBy(
            By.xpath("//form[@name='ComputeEngineForm']" +
                  "/descendant::button[@aria-label='Add to Estimate']"))
            .click();
      waitForElementLocatedBy(
            By.xpath("//form[@name='SoleTenantForm']" +
                  "/descendant::button[@aria-label='Add to Estimate']"))
            .click();
   }

   @Test(dependsOnMethods = {"estimatingOfNewEngineInPricingCalculatorTest"})
   public void checkFinalListWithSelectedElementsOfNewEngine() {
      Field[] allEnumFields = Field.class.getEnumConstants();
      List<WebElement> listWithElementsOfNewEngine = driver.findElements(By
            .xpath("//md-content[@ng-if='cloudCartCtrl.showComputeItems']" +
                  "/descendant::md-list-item[@role='listitem']" +
                  "/descendant::div"));
      boolean matchFound;

      for (int j = 0; j < allEnumFields.length; j++) {
         matchFound = false;
         if (allEnumFields[j].nameInFinalList != null) {
            for (int i = 0; i < listWithElementsOfNewEngine.size(); i++) {
               if (allEnumFields[j].nameInFinalList.equals(listWithElementsOfNewEngine.get(i).getText())) {
                  matchFound = true;
               }
            }
            Assert.assertTrue(
                  matchFound,
                  "Failed to locate right value of field named " + allEnumFields[j].name + " in final engine list");
         }
      }
   }

   @Test(dependsOnMethods = {"estimatingOfNewEngineInPricingCalculatorTest"})
   public void createdPasteNameSyntaxCheck() {
      WebElement totalCost =
            waitForElementLocatedBy(
                  By.xpath("//div[@ng-controller='CloudCartCtrl as cloudCartCtrl']" +
                        "/descendant::h2[@class='md-title']" +
                        "/b"));
      String totalCostFromManualTesting = "USD 1,082.77";
      Assert.assertTrue(
            totalCost.getText().contains(totalCostFromManualTesting),
            "Failed to locate proper total cost of new engine (proper = manually achieved)\n" +
                  "Target text: " + totalCost.getText() + "\n" +
                  "Expected to find: " + totalCostFromManualTesting);
   }

   @AfterTest(alwaysRun = true)
   public void browserTerminate() throws InterruptedException {
      Thread.sleep(3000);
      driver.quit();
      driver = null;
   }
}
