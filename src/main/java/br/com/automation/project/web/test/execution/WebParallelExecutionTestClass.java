package br.com.automation.project.web.test.execution;

import br.com.automation.project.utils.ManagerFileUtils;
import br.com.automation.project.web.driver.factory.DriverManager;
import br.com.automation.project.web.driver.factory.DriverManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Executa 3 instâncias do teste em paralelo para validar o isolamento via
 * ThreadLocal. Cada thread cria o seu próprio DriverManager (e, portanto, o seu
 * próprio WebDriver), armazenado de forma independente no ThreadLocal do
 * DriverManager.
 */
public class WebParallelExecutionTestClass {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebParallelExecutionTestClass.class);
    private static final int THREAD_COUNT = 3;

    static void main() throws InterruptedException {
        try (ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT)) {
            for (int i = 1; i <= THREAD_COUNT; i++) {
                final int threadId = i;
                executor.submit(() -> runTest(threadId));
            }
            executor.shutdown();
            boolean finished = executor.awaitTermination(5, TimeUnit.MINUTES);
            if (finished) {
                LOGGER.info("[PARALELO] Todas as execuções foram concluídas.");
            } else {
                LOGGER.warn("[PARALELO] Tempo esgotado: nem todas as execuções terminaram no prazo.");
            }
        }
    }

    private static void runTest(int threadId) {
        String tag = "[Thread-" + threadId + "]";
        // Cada thread cria o seu próprio DriverManager → WebDriver isolado no
        // ThreadLocal.
        DriverManager driverManager = new DriverManagerFactory().executeDriverManager();
        WebDriver driver = driverManager.getDriver();
        try {
            LOGGER.info("{} Iniciando execução. Driver: {}", tag, driver);
            driver.manage().window().maximize();
            Wait<WebDriver> wait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(5)).ignoring(NoSuchElementException.class);
            driver.navigate().to(Objects.requireNonNull(ManagerFileUtils.getUrlFromJson("google"),
                "URL 'Google' não encontrada no arquivo de configuração."));
            WebElement textBox = wait.until(dvr -> {
                for (By locator : List.of(By.name("q"), By.cssSelector("textarea[name='q']"),
                    By.cssSelector("input[name='q']"))) {
                    List<WebElement> elements = dvr.findElements(locator);
                    if (!elements.isEmpty() && elements.getFirst().isDisplayed()) {
                        return elements.getFirst();
                    }
                }
                throw new NoSuchElementException("Campo de busca não encontrado na página.");
            });
            textBox.sendKeys("selenium documentation");
            textBox.sendKeys(Keys.ENTER);
            WebElement firstResultLink;
            try {
                firstResultLink = wait.until(dvr -> {
                    List<WebElement> links = dvr.findElements(By.cssSelector("#search a h3"));
                    if (!links.isEmpty())
                        return links.getFirst();
                    throw new NoSuchElementException("Nenhum resultado de busca encontrado.");
                });
            } catch (TimeoutException ex) {
                // Alternativa 1: URL direta de busca.
                driver.navigate().to("https://www.google.com/search?q=selenium+documentation");
                try {
                    firstResultLink = wait.until(dvr -> {
                        List<WebElement> links = dvr.findElements(By.cssSelector("#search a h3"));
                        if (!links.isEmpty())
                            return links.getFirst();
                        throw new NoSuchElementException("Nenhum resultado de busca encontrado.");
                    });
                } catch (TimeoutException ignored) {
                    // Alternativa 2: navega diretamente para a documentação.
                    driver.navigate().to("https://www.selenium.dev/documentation/");
                    firstResultLink = wait.until(dvr -> {
                        List<WebElement> links = dvr.findElements(By.cssSelector("a[href*='selenium.dev']"));
                        if (!links.isEmpty())
                            return links.getFirst();
                        throw new NoSuchElementException("Não foi possível localizar links relevantes para Selenium.");
                    });
                }
            }
            wait.until(ExpectedConditions.elementToBeClickable(firstResultLink)).click();
            if (Objects.requireNonNull(driver.getTitle()).contains("Selenium")) {
                LOGGER.info("{} Teste concluído com sucesso. Título: \"{}\"", tag, driver.getTitle());
            } else {
                LOGGER.warn("{} Teste concluído com falha. Título: \"{}\"", tag, driver.getTitle());
            }
        } catch (Exception ex) {
            LOGGER.error("{} Erro durante a execução do teste paralelo.", tag, ex);
        } finally {
            try {
                driver.manage().deleteAllCookies();
            } catch (Exception ex) {
                LOGGER.warn("{} Falha ao limpar cookies antes do encerramento do driver.", tag, ex);
            }
            DriverManager.closeAndQuitDriver();
            LOGGER.info("{} Driver encerrado.", tag);
        }
    }
}
