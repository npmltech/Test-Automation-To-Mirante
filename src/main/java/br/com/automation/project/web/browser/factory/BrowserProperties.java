package br.com.automation.project.web.browser.factory;

import br.com.automation.project.web.properties.WebPropertiesClass;

public class BrowserProperties {

    private final String browserName;
    private final String browserVersion;

    public BrowserProperties() {
        this.browserName = getProperty("browser.name");
        this.browserVersion = getProperty("browser.version");
    }

    public String getProperty(String key) {
        return new WebPropertiesClass().getConfigFileProperty(key);
    }

    public String getBrowserName() {
        return this.browserName;
    }

    public String getBrowserVersion() {
        return this.browserVersion;
    }
}
