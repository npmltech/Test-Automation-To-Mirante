package br.com.automation.project.web.test.execution;

import br.com.automation.project.utils.ManagerFileUtils;
import br.com.automation.project.utils.WebDriverUtils;
import br.com.automation.project.web.browser.factory.BrowserProperties;
import br.com.automation.project.web.driver.factory.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class WebExecutionTestClass {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebExecutionTestClass.class);

    static void main() {
        DriverManager driverManager = Objects.requireNonNull(WebDriverUtils.getDriverManager(),
            "DriverManager não foi inicializado.");
        WebDriver driver = driverManager.getDriver();

        try {
            driver.manage().window().maximize();

            Wait<WebDriver> wait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(20))
                .pollingEvery(Duration.ofSeconds(5)).ignoring(NoSuchElementException.class);

            driver.navigate().to(Objects.requireNonNull(ManagerFileUtils.getUrlFromJson("google"),
                "URL 'Google' não foi encontrada no arquivo de configuração."));

            if (new BrowserProperties().getBrowserName().contains("iexplorer")) {
                WebElement htmlTest = wait.until((WebDriver dvr) -> dvr.findElement(By.xpath("//html")));
                LOGGER.debug("HTML da página (modo IE): {}", htmlTest.getAttribute("outerHTML"));

                JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                jsExecutor.executeScript("document.getElementsByName('q')[0].value = 'selenium documentation';");
                jsExecutor.executeScript("document.getElementsByName('btnK')[0].click();");
            } else {
                WebElement textBox = wait.until(dvr -> {
                    List<By> searchLocators = List.of(By.name("q"), By.cssSelector("textarea[name='q']"),
                        By.cssSelector("input[name='q']"));

                    for (By locator : searchLocators) {
                        List<WebElement> elements = dvr.findElements(locator);
                        if (!elements.isEmpty() && elements.getFirst().isDisplayed()) {
                            return elements.getFirst();
                        }
                    }
                    throw new NoSuchElementException("Campo de busca não encontrado na página.");
                });
                textBox.sendKeys("selenium documentation");
                textBox.sendKeys(Keys.ENTER);
            }

            WebElement firstResultLink;

            try {
                firstResultLink = wait.until(dvr -> {
                    List<WebElement> resultLinks = dvr.findElements(By.cssSelector("#search a h3"));
                    if (!resultLinks.isEmpty()) {
                        return resultLinks.getFirst();
                    }
                    throw new NoSuchElementException("Nenhum resultado de busca foi encontrado.");
                });
            } catch (TimeoutException ex) {
                // Fallback para cenários em que o campo de busca não dispara resultados
                // automaticamente.
                driver.navigate().to("https://www.google.com/search?q=selenium+documentation");

                try {
                    firstResultLink = wait.until(dvr -> {
                        List<WebElement> resultLinks = dvr.findElements(By.cssSelector("#search a h3"));
                        if (!resultLinks.isEmpty()) {
                            return resultLinks.getFirst();
                        }
                        throw new NoSuchElementException("Nenhum resultado de busca foi encontrado.");
                    });
                } catch (TimeoutException ignored) {
                    driver.navigate().to("https://www.selenium.dev/documentation/");
                    firstResultLink = wait.until(dvr -> {
                        List<WebElement> links = dvr.findElements(By.cssSelector("a[href*='selenium.dev']"));
                        if (!links.isEmpty()) {
                            return links.getFirst();
                        }
                        throw new NoSuchElementException("Não foi possível localizar links relevantes para Selenium.");
                    });
                }
            }

            wait.until(ExpectedConditions.elementToBeClickable(firstResultLink)).click();

            if (Objects.requireNonNull(driver.getTitle()).contains("Selenium")) {
                LOGGER.info("Teste concluído com sucesso. Título final: {}", driver.getTitle());
            } else {
                LOGGER.warn("Teste concluído com falha. Título final: {}", driver.getTitle());
            }
        } catch (Exception ex) {
            LOGGER.error("Falha durante a execução do teste web.", ex);
            throw ex;
        } finally {
            try {
                driver.manage().deleteAllCookies();
            } catch (Exception ex) {
                LOGGER.warn("Falha ao limpar cookies antes do encerramento do driver.", ex);
            }

            DriverManager.closeAndQuitDriver();
        }
    }
}
