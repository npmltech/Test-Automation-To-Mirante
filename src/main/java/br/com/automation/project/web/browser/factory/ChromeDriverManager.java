package br.com.automation.project.web.browser.factory;

import br.com.automation.project.utils.DriverPathUtils;
import br.com.automation.project.utils.GlobalUtils;
import br.com.automation.project.utils.ManagerFileUtils;
import br.com.automation.project.web.driver.factory.DriverManager;
import br.com.automation.project.web.properties.WebPropertiesClass;
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

public class ChromeDriverManager extends DriverManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChromeDriverManager.class);
    private final String browserName;
    private ChromeDriverService chromeService;
    private final String driverPathByOprtSysName;

    public ChromeDriverManager(String browserName, String driverPathByOprtSysName) {
        //
        this.driverPathByOprtSysName = driverPathByOprtSysName;
        this.browserName = browserName;
    }

    @Override
    public void createDriver() {
        LOGGER.info("Criando instância do ChromeDriver (modo binário local).");
        ChromeOptions chromeOptions = new ChromeOptions();
        //
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("application_cache_enabled", false);
        prefs.put("credentials_enable_service", false);
        prefs.put("password_manager_enabled", false);
        prefs.put("profile.default_content_setting_values.notifications", 1);
        prefs.put("settings.language.preferred_languages", "pt-BR");
        chromeOptions.addArguments(ChromeDriverArguments.getArguments());
        chromeOptions.setBinary(new File(String.format("/%s/%s%s",
            new WebPropertiesClass().getConfigFileProperty(String.format("%s%s", this.browserName, ".dvr.bin.path")),
            this.browserName, DriverPathUtils.getFileExtensionUsingOSName())).getAbsoluteFile());
        chromeOptions.setExperimentalOption("excludeSwitches",
            Arrays.asList("allow-running-insecure-content", "enable-automation", "ignore-certificate-errors"));
        chromeOptions.setExperimentalOption("prefs", prefs);
        chromeOptions.setExperimentalOption("useAutomationExtension", false);
        // System.setProperty("webdriver.chrome.driver", this.driverPathByOprtSysName);
        DriverManager.addDriver(new ChromeDriver(chromeService, chromeOptions));
    }

    @Override
    public void startService() {
        //
        if (chromeService == null || !chromeService.isRunning()) {
            try {
                LOGGER.info("Iniciando serviço do ChromeDriver (modo binário local).");
                chromeService = new ChromeDriverService.Builder().usingAnyFreePort()
                    .usingDriverExecutable(new File(this.driverPathByOprtSysName))
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
            LOGGER.info("Encerrando serviço do ChromeDriver (modo binário local).");
            chromeService.stop();
            chromeService = null;
        }
    }
}
