import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Set;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

class Parse {
    static int ROW = 1;
    static int numberOfLinks = 0;
    private TooLongLoadMessage timer;

    private String getProtocol(WebDriver driver, WebElement protocol) throws Exception {
        timer = new TooLongLoadMessage();
        timer.startTimer();
        protocol.click();
        (new WebDriverWait(driver, 4000))
                .until(presenceOfElementLocated(By.xpath("//*[contains(text(), 'Протоколы подведения итогов')]/../../div//*/a")));
        String protocolLink = driver.findElement(By.xpath("//*[contains(text(), 'Протоколы подведения итогов')]/../../div//*/a")).getAttribute("href");
        driver.navigate().back();
        timer.stopTimer();
        return protocolLink;
    }

void parseSet(Set<String> links) throws Exception {
        Thread.sleep(5000);
        mainFrame.progressBar.setMinimum(0);
        mainFrame.progressBar.setMaximum(links.size());
        for (String link : links) {
            if (Thread.interrupted()) {
                break;
            }
            Main.driver.get(link);
            parseText(Main.driver);
            mainFrame.progressBar.setValue(++numberOfLinks);
            mainFrame.progressBarLabel.setText(numberOfLinks + " из " + links.size());
        }
        Main.driver.close();
    }

    private void parseText(WebDriver driver) throws Exception {

        timer = new TooLongLoadMessage();
        timer.startTimer();
        (new WebDriverWait(driver, 4000))
                .until(presenceOfElementLocated(By.xpath("//tr/td[label [contains(text(), 'Номер извещения')]]/following-sibling::td/label[not(contains(text(), 'не указано'))]")));

        List<WebElement> lots = driver.findElements(By.xpath("//*/ul/li//*[contains(text(), 'Лот')]"));
        timer.stopTimer();
        if (lots.size() == 1){
            int cell = 1;
            Excel.writeToXls(cell, driver.findElement(By.xpath("//tr/td[label [contains(text(), 'Номер извещения')]]/following-sibling::td/label")).getText());
            cell++;
            Excel.writeToXls(cell, driver.findElement(By.xpath("//tr/td[label [contains(text(), 'Уникальный номер закупки')]]/following-sibling::td/label")).getText());
            cell++;
            Excel.writeToXls(cell, driver.findElement(By.xpath("//tr/td[label [contains(text(), 'Номер закупки')]]/following-sibling::td/label")).getText());
            cell++;
            Excel.writeToXls(cell, driver.findElement(By.xpath("//tr/td[label [contains(text(), 'Наименование закупки')]]/following-sibling::td/label")).getText());
            cell++;
            Excel.writeToXls(cell, driver.findElement(By.xpath("//tr/td[label [contains(text(), 'Валюта процедуры')]]/following-sibling::td/label")).getText());
            cell++;
            Excel.writeToXls(cell, driver.findElement(By.xpath("//tr/td[label [contains(text(), 'Начальная цена с НДС')]]/following-sibling::td/label")).getText());
            cell++;
            Excel.writeToXls(cell, driver.findElement(By.xpath("//tr/td[label [contains(text(), 'Предмет договора')]]/following-sibling::td/label")).getText());
            cell++;
            Excel.writeToXls(cell, driver.findElement(By.xpath("//tr/td[label [contains(text(), 'Наименование заказчика')]]/following-sibling::td/label")).getText());
            cell++;
            Excel.writeToXls(cell, driver.findElement(By.xpath("//tr/td[label [contains(text(), 'Дата публикации')]]/following-sibling::td/label")).getText());
            cell++;
            Excel.writeToXls(cell, driver.findElement(By.xpath("//tr/td[label [contains(text(), 'Дата и время окончания срока приема заявок')]]/following-sibling::td/label")).getText());
            cell++;
            Excel.writeToXls(cell, "Лот 1");
            cell++;
            Excel.writeToXls(cell, getProtocol(driver, driver.findElement(By.xpath("//*[@type='button' and contains(text(), 'Протоколы')]"))));
            cell++;
            Excel.writeToXls(cell, driver.getCurrentUrl());

            ROW++;
        } else {

            for (int i = 0; i<(lots.size()>10?10:lots.size()); i++) {
                timer = new TooLongLoadMessage();
                timer.startTimer();
                (new WebDriverWait(driver, 4000)).until(presenceOfElementLocated(By.xpath("//*/ul/li//*[contains(text(), 'Лот')]")));

                String lotXpath = "//*/ul/li//*[contains(text(), 'Лот " + (i+1) + "')]";
                driver.findElement(By.xpath(lotXpath));
                timer.stopTimer();
                List<WebElement> startPrice = driver.findElements(By.xpath("//tr/td[label [contains(text(), 'Начальная цена с НДС')]]/following-sibling::td/label"));
                List<WebElement> dealSubject = driver.findElements(By.xpath("//tr/td[label [contains(text(), 'Предмет договора')]]/following-sibling::td/label"));
                List<WebElement> customerName = driver.findElements(By.xpath("//tr/td[label [contains(text(), 'Наименование заказчика')]]/following-sibling::td/label"));
                List<WebElement> deadline = driver.findElements(By.xpath("//tr/td[label [contains(text(), 'Дата и время окончания срока приема заявок')]]/following-sibling::td/label"));
                List<WebElement> protocols = driver.findElements(By.xpath("//*[@type='button' and contains(text(), 'Протоколы')]"));
                int cell = 1;
                Excel.writeToXls(cell, driver.findElement(By.xpath("//tr/td[label [contains(text(), 'Номер извещения')]]/following-sibling::td/label")).getText());
                cell++;
                Excel.writeToXls(cell, driver.findElement(By.xpath("//tr/td[label [contains(text(), 'Уникальный номер закупки')]]/following-sibling::td/label")).getText());
                cell++;
                Excel.writeToXls(cell, driver.findElement(By.xpath("//tr/td[label [contains(text(), 'Номер закупки')]]/following-sibling::td/label")).getText());
                cell++;
                Excel.writeToXls(cell, driver.findElement(By.xpath("//tr/td[label [contains(text(), 'Наименование закупки')]]/following-sibling::td/label")).getText());
                cell++;
                Excel.writeToXls(cell, driver.findElement(By.xpath("//tr/td[label [contains(text(), 'Валюта процедуры')]]/following-sibling::td/label")).getText());
                cell++;
                Excel.writeToXls(cell, startPrice.get(startPrice.size()-1).getText());
                cell++;
                Excel.writeToXls(cell, dealSubject.get(dealSubject.size()-1).getText());
                cell++;
                Excel.writeToXls(cell, customerName.get(customerName.size()-1).getText());
                cell++;
                Excel.writeToXls(cell, driver.findElement(By.xpath("//tr/td[label [contains(text(), 'Дата публикации')]]/following-sibling::td/label")).getText());
                cell++;
                Excel.writeToXls(cell, deadline.get(deadline.size()-1).getText());
                cell++;
                Excel.writeToXls(cell, "Лот " + (i+1));
                cell++;
                Excel.writeToXls(cell, getProtocol(driver, protocols.get(protocols.size()-1)));
                cell++;
                Excel.writeToXls(cell, driver.getCurrentUrl());

                ROW++;
            }

            if (lots.size()>10) {
                Excel.writeToXls(4, "Больше 10 лотов");
                ROW++;
            }
        }
    }
}
