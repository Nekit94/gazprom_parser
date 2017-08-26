import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class Main {

    static WebDriver driver = new ChromeDriver();
    static Set<String> links = new HashSet();


    static void LinkGathering(String keyWord, String startDate, String endDate) throws Exception {

        driver.get("https://etpgaz.gazprombank.ru/#com/procedure/index");
        TooLongLoadMessage timer = new TooLongLoadMessage();
        timer.startTimer();
        (new WebDriverWait(driver, 20000)).until(presenceOfElementLocated(By.id("ext-gen92")));
        WebElement search = driver.findElement(By.id("ext-gen92"));
        timer.stopTimer();
        search.clear();
        search.sendKeys(keyWord);

        WebElement advSearch = driver.findElement(By.id("ext-gen130"));
        advSearch.click();
        driver.findElement(By.name("status")).click();
        driver.findElement(By.id("ext-gen77_8")).click();
        driver.findElement(By.name("date_end_second_parts_review_from")).clear();
        driver.findElement(By.name("date_end_second_parts_review_from")).sendKeys(startDate);
        driver.findElement(By.name("date_end_second_parts_review_till")).clear();
        driver.findElement(By.name("date_end_second_parts_review_till")).sendKeys(endDate, Keys.ENTER);
        tryToClick(advSearch, 5000, 100);


        (new WebDriverWait(driver, 10000)).until(presenceOfElementLocated(By.xpath("//*[@alt='Протоколы']")));
        boolean q = true;
        while (q) {


            if (Thread.interrupted()) {
                break;
            }

            (new WebDriverWait(driver, 5000))
                    .until(visibilityOf(driver.findElement(By.xpath("//div[1]//tbody/tr[1]/td[23]/div/a[1]"))));
            (new WebDriverWait(driver, 5000))
                    .until(elementToBeClickable(driver.findElement(By.xpath("//div[1]//tbody/tr[1]/td[23]/div/a[1]"))));

            for (int i = 0; i < 20; i++) {
                try {
                    List<WebElement> elements = driver.findElements(By.xpath("//div[1]//tbody/tr[1]/td[23]/div/a[1]"));
                    for (WebElement element : elements) {
                        links.add(element.getAttribute("href"));
                    }
                } catch (StaleElementReferenceException e) {
                    if (i == 19) {
                        throw e;
                    }
                }
                Thread.sleep(100);
            }

            mainFrame.progressBarLabel.setText("0 из " + Main.links.size());

            WebElement currentPageEl = driver.findElement(By.xpath("//*[@class=\"x-form-text x-form-field x-form-num-field x-tbar-page-number\"]"));
            WebElement maxValueEl = driver.findElement(By.xpath("//*[contains(text(), 'из ') and not(contains(text(), 'лот')) and not(contains(text(), 'Процедуры')) and @class='xtb-text']"));
            int currentPage = Integer.parseInt(currentPageEl.getAttribute("value"));
            int maxValue = Integer.parseInt(maxValueEl.getText().substring(3));
            if (currentPage == maxValue) {
                q = false;
            } else {

                for (int i = 0; i < 50; i++) {
                    try {
                        driver.findElement(By.xpath("//*[@class=' x-btn-text x-tbar-page-next']")).click();
                        break;
                    } catch (Exception e) {
                        if (i == 49) {
                            throw e;
                        }
                    }
                    Thread.sleep(100);
                }
            }

        }
    }

    static void StartParsing() throws Exception {

        if (!Thread.interrupted()) {

            (new Parse()).parseSet(links);

            Excel.createXls();

        }



    }

    private static void tryToClick(WebElement element, int wait, int step) throws Exception {
        for (int i = 0; i<wait/step; i++) {
            try {
                element.click();
                break;
            } catch (Exception e) {
                if (i == wait/step-1) {
                    throw e;
                }
            }
            Thread.sleep(step);
        }
    }

}