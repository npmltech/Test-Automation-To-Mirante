package br.com.automation.project.web.driver.factory;

import br.com.automation.project.utils.DriverPathUtils;
import br.com.automation.project.web.browser.factory.BrowserProperties;
import br.com.automation.project.web.browser.factory.ChromeDriverManager;
import br.com.automation.project.web.browser.factory.ChromeDriverManager_v2;
import io.github.bonigarcia.wdm.WebDriverManager;

import javax.swing.*;

public class DriverManagerFactory {

    private DriverManager driverManager;
    private String browserName;
    private final String driverResourcePath;

    public DriverManagerFactory() {
        this.driverResourcePath = "src/main/resources/drivers";
        this.browserName = new BrowserProperties().getBrowserName();
    }

    public DriverManager executeDriverManager() {
        //
        switch ((this.browserName == null || this.browserName.isEmpty())
            ? this.browserName = "_NULL_"
            : this.browserName) {
            //
            case "chrome" :
                //
                this.driverManager = new ChromeDriverManager(this.browserName,
                    DriverPathUtils.getDriverPathUsingOSName(driverResourcePath));
                //
                break;
            case "chrome_v2" :
                //
                WebDriverManager.chromedriver().setup();
                this.driverManager = new ChromeDriverManager_v2(this.browserName);
                //
                break;
            default :
                JOptionPane.showMessageDialog(new JFrame(),
                    "O driver '" + this.browserName
                        + "' não foi implementado ou não existe. Verifique o arquivo config.properties.",
                    "# ERRO #", JOptionPane.ERROR_MESSAGE);
                break;
        }
        //
        return this.driverManager;
    }
}
