package br.com.automation.project.web.browser.factory;

import br.com.automation.project.utils.DriverPathUtils;
import br.com.automation.project.utils.GlobalUtils;
import br.com.automation.project.utils.ManagerFileUtils;
import br.com.automation.project.web.driver.factory.DriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ChromeDriverManager_v2 extends DriverManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChromeDriverManager_v2.class);
    private final String browserName;
    private ChromeDriverService chromeService;

    public ChromeDriverManager_v2(String browserName) {
        //
        this.browserName = browserName;
    }

    @Override
    public void createDriver() {
        LOGGER.info("Criando instância do ChromeDriver (modo WebDriverManager).");
        ChromeOptions chromeOptions = new ChromeOptions();
        //
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("application_cache_enabled", false);
        prefs.put("credentials_enable_service", false);
        prefs.put("password_manager_enabled", false);
        prefs.put("profile.default_content_setting_values.notifications", 1);
        prefs.put("settings.language.preferred_languages", "pt-BR");
        chromeOptions.addArguments(ChromeDriverArguments.getArguments());
        chromeOptions.setExperimentalOption("excludeSwitches",
            Arrays.asList("allow-running-insecure-content", "enable-automation", "ignore-certificate-errors"));
        chromeOptions.setExperimentalOption("prefs", prefs);
        chromeOptions.setExperimentalOption("useAutomationExtension", false);
        ChromeDriver chromeDriver = new ChromeDriver(chromeService, chromeOptions);
        // Oculta a propriedade navigator.webdriver via CDP para evitar deteção por
        // reCAPTCHA
        Map<String, Object> cdpParams = new HashMap<>();
        cdpParams.put("source", "Object.defineProperty(navigator, 'webdriver', { get: () => undefined });");
        chromeDriver.executeCdpCommand("Page.addScriptToEvaluateOnNewDocument", cdpParams);
        DriverManager.addDriver(chromeDriver);
    }

    @Override
    public void startService() {
        //
        if (chromeService == null || !chromeService.isRunning()) {
            try {
                LOGGER.info("Iniciando serviço do ChromeDriver (modo WebDriverManager).");
                chromeService = new ChromeDriverService.Builder().usingAnyFreePort()
                    .withLogFile(new File(String.format("%s/%s_driver_%s.log",
                        ManagerFileUtils
                            .checkAndGenerateFilePath(DriverPathUtils.getFilePathUsingOSName("target/log-exec")),
                        this.browserName, GlobalUtils.generateReferenceDate())))
                    .withVerbose(false).build();
                //
                chromeService.start();
            } catch (IOException ex) {
                throw new IllegalStateException("Falha ao iniciar o serviço do ChromeDriver.", ex);
            }
        }
    }

    @Override
    protected void stopService() {
        //
        if (chromeService != null && chromeService.isRunning()) {
            LOGGER.info("Encerrando serviço do ChromeDriver (modo WebDriverManager).");
            chromeService.stop();
            chromeService = null;
        }
    }
}
