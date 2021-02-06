package googleCloud.nightmare.test;

import googleCloud.nightmare.page.EngineCalculatorPageGoogleCloudPF;
import googleCloud.nightmare.page.HomePageGoogleCloudPF;
import googleCloud.nightmare.page.TenMinuteMailPagePF;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class WebDriverGoogleCloudNightmareTests {
   WebDriver driver;

   HomePageGoogleCloudPF cloudHomePage;
   EngineCalculatorPageGoogleCloudPF cloudCalculatorPage;
   TenMinuteMailPagePF mailPage;

   Field[] allFieldEnumFields = Field.getAllEnumFieldsAsArray();

   @BeforeTest
   public void initiateBrowserWithGoogleCloudConnection() {
      driver = new ChromeDriver();
      cloudHomePage = new HomePageGoogleCloudPF(driver)
            .maximize()
            .pageOpen();
      cloudCalculatorPage = new EngineCalculatorPageGoogleCloudPF(driver);
   }

   @Test
   public void estimatingOfNewEngineInPricingCalculatorTest() {
      cloudHomePage
            .searchTerm("Google Cloud Platform Pricing Calculator")
            .clickOnSearchResult("Google Cloud Platform Pricing Calculator");
      cloudCalculatorPage
            .switchToCalculatorIFrame()
            .clickComputeEngineButtonIfInactive()
            .fillFieldsOnPageAccordingToEnumFields(allFieldEnumFields)
            .clickOnEstimateButton();
   }

   /*@Test(dependsOnMethods = {"estimatingOfNewEngineInPricingCalculatorTest"})
   public void checkFinalListWithSelectedElementsOfNewEngine() {

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
   }*/

   @Test(dependsOnMethods = {"estimatingOfNewEngineInPricingCalculatorTest"})
   public void compareEstimatedEnginePriceToManualResult() {
      String totalCostFromManualTesting = "USD 1,082.77";
      String actualTotalCost = cloudCalculatorPage.getTotalCost();

      Assert.assertTrue(
            actualTotalCost.contains(totalCostFromManualTesting),
            "Failed to locate proper total cost of new engine (proper = manually achieved)\n" +
                  "Target text: " + actualTotalCost + "\n" +
                  "Expected to find: " + totalCostFromManualTesting);
   }

   //@Test(dependsOnMethods = {"checkFinalListWithSelectedElementsOfNewEngine", "compareEstimatedEnginePriceToManualResult"})
   @Test(dependsOnMethods = {"compareEstimatedEnginePriceToManualResult"})
   public void emailEstimateTest() throws Exception {

      mailPage = new TenMinuteMailPagePF(driver).openInNewPage();
      String temporaryMailAddress = mailPage.getTemporaryMail();

      String totalCostOnCloudPageRaw =
            cloudCalculatorPage
                  .takeFocus()
                  .switchToCalculatorIFrame()
                  .clickMailEstimateButton()
                  .enterMailInMailField(temporaryMailAddress)
                  .clickSendEmailButton()
                  .getTotalCost();
      String totalCostOnMailPageRaw =
            mailPage
                  .takeFocus()
                  .clickOnDeliveredMailByItsTextWithCustomWaitTime(40, "Google Cloud Platform Price Estimate")
                  .getTotalCostFromDeliveredMail();

      String totalCostOnCloudPage = ripCostFromRawTotal(totalCostOnCloudPageRaw);
      String totalCostOnMailPage = ripCostFromRawTotal(totalCostOnMailPageRaw);

      Assert.assertTrue(
            totalCostOnMailPage.contains(totalCostOnCloudPage),
            "Mail total cost does not equal to cloud total cost\n" +
            "Expected: '" + totalCostOnCloudPage + "'\n" +
            "Actual: '" + totalCostOnMailPage + "'");
   }

   @AfterTest(alwaysRun = true)
   public void browserTerminate() throws InterruptedException {
      Thread.sleep(3000);
      driver.quit();
      driver = null;
   }

   private String ripCostFromRawTotal(String rawTotal) {
      return rawTotal.substring(rawTotal.indexOf("Cost")-1, rawTotal.indexOf(".")+2);
   }
}