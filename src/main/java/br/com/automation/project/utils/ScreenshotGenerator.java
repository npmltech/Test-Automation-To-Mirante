package br.com.automation.project.utils;

import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

public class ScreenshotGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenshotGenerator.class);

    /**
     * Gera a evidência em arquivo quando o cenário falha.
     */
    public static void generateScreenshot(Scenario scenario, WebDriver driver) {
        if (scenario.isFailed()) {
            LOGGER.info("Gerando evidência para o cenário com falha: {}", scenario.getName());
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File file = screenshot.getScreenshotAs(OutputType.FILE);
            EvidencePathGenerator.generateEvidencePath(scenario, file);
        }
    }

    /**
     * Retorna a captura de tela do cenário em formato de bytes.
     */
    public static byte[] getScreenshot(Scenario scenario, WebDriver driver) {
        Objects.requireNonNull(scenario, "O cenário não pode ser nulo.").getName();
        return (((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
    }

    /**
     * Anexa a captura de tela ao relatório Allure.
     */
    public static void allureScreenshot(WebDriver driver) {
        LOGGER.info("Anexando screenshot de falha ao relatório Allure.");
        Allure.addAttachment("<<-- SCREENSHOT DE FALHA -->>",
            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }
}

class EvidencePath {

    private static final String evidencePath = "target/screenshots/";

    /**
     * Retorna o diretório-base onde as evidências do cenário serão gravadas.
     */
    public static String getEvidencePath(Scenario scenario) {
        return evidencePath + scenario.getName();
    }
}

class EvidencePathGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(EvidencePathGenerator.class);

    /**
     * Gera o caminho da evidência e copia o arquivo de captura para o diretório de
     * screenshots.
     */
    static void generateEvidencePath(Scenario scenario, File file) {
        try {
            Files.createDirectories(Path.of(EvidencePath.getEvidencePath(scenario)));
            if (Counter.countDirectoryFile(scenario) > 0) {
                Counter.setCounter(Counter.countDirectoryFile(scenario) + 1);
                FileUtils.copyFile(file, new File(EvidencePath.getEvidencePath(scenario) + "/" + "evidencia_0"
                    + Counter.getCounter() + "_" + GlobalUtils.generateReferenceDate() + ".png"));
            } else {
                Counter.setCounter(1L);
                FileUtils.copyFile(file, new File(EvidencePath.getEvidencePath(scenario) + "/" + "evidencia_0"
                    + Counter.getCounter() + "_" + GlobalUtils.generateReferenceDate() + ".png"));
                //
                LOGGER.info("Evidência gerada com sucesso.");
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Falha ao gerar evidência de execução.", ex);
        }
    }
}

class Counter {

    private static long counter;

    static long getCounter() {
        return counter;
    }

    static void setCounter(long counter) {
        Counter.counter = counter;
    }

    /**
     * Conta a quantidade de arquivos existentes no diretório de evidências do
     * cenário.
     */
    public static long countDirectoryFile(Scenario scenario) {
        long $iterator;
        Path evidenceDirectory = Paths.get(EvidencePath.getEvidencePath(scenario));
        if (Files.notExists(evidenceDirectory)) {
            return 0L;
        }
        try (Stream<java.nio.file.Path> files = Files.list(evidenceDirectory)) {
            $iterator = files.filter(arq -> arq.toFile().isFile()).count();
        } catch (IOException ex) {
            throw new IllegalStateException("Falha ao contar arquivos de evidência.", ex);
        }
        return $iterator;
    }
}
