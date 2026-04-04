package br.com.automation.project.web.properties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class WebDriverPropertiesFile {

    public static String getConfigPropFileName() {
        try (Stream<java.nio.file.Path> files = Files.list(Paths.get("src/test/resources/config"))) {
            return files.filter(Files::isRegularFile).map(path -> path.toFile().getName())
                .filter(name -> name.contains("webdriver")).findFirst()
                .orElseThrow(() -> new IllegalStateException("Arquivo de configuração do WebDriver não encontrado."));
        } catch (IOException ex) {
            throw new IllegalStateException("Falha ao listar arquivos de configuração do WebDriver.", ex);
        }
    }
}
