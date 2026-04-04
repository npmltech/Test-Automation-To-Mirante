package br.com.automation.project.web.properties;

import br.com.automation.project.utils.GlobalUtils;
import br.com.automation.project.utils.ManagerFileUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class WebPropertiesClass {

    private final Properties properties;
    private final String webDriverFileName;

    public WebPropertiesClass() {
        //
        this.properties = new Properties();
        this.webDriverFileName = WebDriverPropertiesFile.getConfigPropFileName();
    }

    public String getConfigFileProperty(String key) {
        try (InputStream inputStream = new FileInputStream(String.format("%s/config/%s",
            ManagerFileUtils.checkAndGenerateFilePath("src/test/resources"), this.webDriverFileName
        //
        ))) {
            this.properties.load(inputStream);
            //
        } catch (IOException ex) {
            throw new IllegalStateException(
                "Falha ao carregar o arquivo de propriedades: " + this.webDriverFileName + ".", ex);
        }
        //
        return GlobalUtils.removeWhiteSpacesAndTabulation(this.properties.getProperty(key));
        //
    }
}
