package br.com.automation.project.web.driver.factory;

import br.com.automation.project.web.browser.factory.BrowserProperties;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

public abstract class DriverManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DriverManager.class);
    private static String browserName;
    private static final ThreadLocal<WebDriver> drivers = new ThreadLocal<>();
    private static final ThreadLocal<DriverManager> managers = new ThreadLocal<>();
    private static final List<WebDriver> storedDrivers = new LinkedList<>();

    protected abstract void createDriver();

    protected abstract void startService();

    protected abstract void stopService();

    protected DriverManager() {
        DriverManager.browserName = new BrowserProperties().getBrowserName().toLowerCase();
        DriverManager.managers.set(this);
    }

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Executando shutdown hook para encerrar drivers pendentes.");
            DriverManager.storedDrivers.parallelStream().forEach(dvr -> {
                if (dvr != null) {
                    try {
                        dvr.quit();
                    } catch (Exception ex) {
                        LOGGER.warn("Falha ao encerrar driver no shutdown hook.", ex);
                    }
                }
            });
        }));
    }

    public WebDriver getDriver() {
        //
        if (DriverManager.drivers.get() == null) {
            LOGGER.info("Inicializando driver para o navegador: {}", browserName);
            //
            startService();
            createDriver();
            //
            DriverManager.drivers.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
            //
            DriverManager.drivers.get().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            //
            DriverManager.drivers.get().manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
            //
        }
        //
        return DriverManager.drivers.get();
    }

    public static void addDriver(WebDriver driver) {
        //
        DriverManager.storedDrivers.add(driver);
        DriverManager.drivers.set(driver);
    }

    public static void closeAndQuitDriver() {
        WebDriver currentDriver = DriverManager.drivers.get();
        DriverManager currentManager = DriverManager.managers.get();
        if (currentDriver != null) {
            try {
                currentDriver.quit();
            } finally {
                if (currentManager != null) {
                    LOGGER.info("Encerrando serviço do driver para o navegador: {}", browserName);
                    currentManager.stopService();
                }
                DriverManager.storedDrivers.remove(currentDriver);
                DriverManager.drivers.remove();
                DriverManager.managers.remove();
                LOGGER.info("Driver encerrado com sucesso.");
            }
        }
    }
}
