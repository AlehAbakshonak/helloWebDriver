package tests.googleCloud.page;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import tests.AbstractPage;

public class HomePageGoogleCloudPF extends AbstractPage<HomePageGoogleCloudPF> {

   private final String HOMEPAGE_URL = "https://cloud.google.com/";

   public HomePageGoogleCloudPF(WebDriver driver) {
      super(driver);
   }

   @FindBy(xpath = "//input[@class='devsite-search-field devsite-search-query']")
   private WebElement searchArea;

   public HomePageGoogleCloudPF pageOpen() {
      driver.get(HOMEPAGE_URL);
      this.windowHandle = driver.getWindowHandle();
      return this;
   }

   public HomePageGoogleCloudPF maximize() {
      driver.manage().window().maximize();
      return this;
   }

   public HomePageGoogleCloudPF searchTerm(String term) {
      searchArea.sendKeys(term + Keys.ENTER);
      return this;
   }

   public EngineCalculatorPageGoogleCloudPF clickOnSearchResult(String term) {
      String resultXPathPattern = "//b[text()='%s']/parent::a";
      WebElement currentSearchResult =
            waitForElementLocatedBy(By.xpath(String.format(resultXPathPattern, term)));
      currentSearchResult.click();
      return new EngineCalculatorPageGoogleCloudPF(driver);
   }

}
