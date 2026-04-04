package br.com.automation.project.utils;

import br.com.automation.project.web.driver.factory.DriverManager;
import br.com.automation.project.web.driver.factory.DriverManagerFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;

/**
 * Classe utilitária para acesso ao {@link WebDriver}, ao {@link Wait} padrão e
 * a elementos auxiliares usados nos testes Web.
 */
public class WebDriverUtils {

    private static final DriverManager DRIVER_MANAGER;
    private static final Wait<WebDriver> WAIT;
    static {
        DRIVER_MANAGER = new DriverManagerFactory().executeDriverManager();
        WAIT = new FluentWait<>(DRIVER_MANAGER.getDriver()).withTimeout(Duration.ofSeconds(20))
            .pollingEvery(Duration.ofSeconds(5)).ignoring(NoSuchElementException.class);
    }

    /**
     * Retorna o {@link DriverManager} atual quando a sessão do driver está ativa.
     */
    public static DriverManager getDriverManager() {
        DriverManager dvrManager = null;
        if (((RemoteWebDriver) DRIVER_MANAGER.getDriver()).getSessionId() == null) {
            return dvrManager;
        }
        dvrManager = DRIVER_MANAGER;
        return dvrManager;
    }

    /**
     * Retorna a instância padrão de espera fluente para o {@link WebDriver}.
     */
    public static Wait<WebDriver> getWait() {
        return WAIT;
    }

    /**
     * Retorna o nome do navegador associado ao driver remoto em uso.
     */
    public static String getRemoteDriverBrowserName() {
        return ((RemoteWebDriver) DRIVER_MANAGER.getDriver()).getCapabilities().getBrowserName().toLowerCase();
    }

    /**
     * Retorna um campo de entrada da página com base no índice informado.
     */
    public static WebElement getInputListByIndex(int index) {
        //
        return WebDriverUtils.getWait().until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//input")))
            .get(index - 1);
        //
    }

    /**
     * Retorna o primeiro elemento cujo texto contenha o valor informado.
     */
    public static WebElement getElementListByText(String text) {
        //
        return WebDriverUtils.getWait()
            .until(ExpectedConditions
                .presenceOfAllElementsLocatedBy(By.xpath(String.format("//*[text()[contains(., '%s')]]", text))))
            .getFirst();
        //
    }

    /**
     * Suspende a execução pela quantidade de segundos informada.
     */
    public static void sleepInSeconds(int seconds) {
        try {
            Thread.sleep(Duration.ofSeconds(seconds).toMillis());
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Execução interrompida durante o período de espera.", ex);
        }
    }
}
