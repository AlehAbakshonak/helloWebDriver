package tests.googleCloud;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class CustomConditions {

   public static ExpectedCondition<Boolean> jQueryAJAXsCompleted() {
      return new ExpectedCondition<Boolean>() {
         public Boolean apply(WebDriver webDriver) {
            return (Boolean)
                  ((JavascriptExecutor) webDriver)
                        .executeScript("return (window.jQuery != null) & (jQuery.active == 0);");
         }
      };
   }
}
