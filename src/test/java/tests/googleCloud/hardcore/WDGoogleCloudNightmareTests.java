package tests.googleCloud.hardcore;

import tests.googleCloud.page.EngineCalculatorPageGoogleCloudPF;
import tests.googleCloud.page.HomePageGoogleCloudPF;
import tests.googleCloud.page.TenMinuteMailPagePF;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class WDGoogleCloudNightmareTests {
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
   public void estimatingOfNewEngineInPricingCalculator() {
      cloudHomePage
            .searchTerm("Google Cloud Platform Pricing Calculator")
            .clickOnSearchResult("Google Cloud Platform Pricing Calculator");
      cloudCalculatorPage
            .switchToCalculatorIFrame()
            .clickComputeEngineButtonIfInactive()
            .fillFieldsOnPageAccordingToEnumFields(allFieldEnumFields)
            .clickOnEstimateButton();
   }

   @Test(dependsOnMethods = {"estimatingOfNewEngineInPricingCalculator"})
   public void checkFinalListWithSelectedElementsOfNewEngine() {
      int numberOfAssertionElement = cloudCalculatorPage.checkFinalListWithSelectedElementsOfNewEngine(allFieldEnumFields);
      Assert.assertTrue(numberOfAssertionElement > 0,
            "Failed to locate right value of field named " +
                  allFieldEnumFields[numberOfAssertionElement].getName() + " in final engine list");
   }

   @Test(dependsOnMethods = {"estimatingOfNewEngineInPricingCalculator"})
   public void compareEstimatedEnginePriceToManualResult() {
      String totalCostFromManualTesting = "USD 1,082.77";
      String actualTotalCost = cloudCalculatorPage.getTotalCost();

      Assert.assertTrue(
            actualTotalCost.contains(totalCostFromManualTesting),
            "Failed to locate proper total cost of new engine (proper = manually achieved)\n" +
                  "Target text: " + actualTotalCost + "\n" +
                  "Expected to find: " + totalCostFromManualTesting);
   }

   @Test(dependsOnMethods = {"checkFinalListWithSelectedElementsOfNewEngine", "compareEstimatedEnginePriceToManualResult"})
   public void emailEstimate() {

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
   public void browserTerminate() {
      driver.quit();
      driver = null;
   }

   private String ripCostFromRawTotal(String rawTotal) {
      return rawTotal.substring(rawTotal.indexOf("Cost") - 1, rawTotal.indexOf(".") + 2);
   }
}