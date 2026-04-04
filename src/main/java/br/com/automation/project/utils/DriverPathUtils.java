package br.com.automation.project.utils;

import br.com.automation.project.sysinfo.SystemInfo;
import br.com.automation.project.web.browser.factory.BrowserProperties;

import java.io.File;
import java.util.Arrays;

/**
 * Classe utilitária com métodos para leitura de diretórios e arquivos de
 * driver.
 *
 * @author Niky Lima
 */
public class DriverPathUtils {

    /**
     * Retorna a extensão do driver com base no sistema operacional em que a
     * automação Web-UI está sendo executada.
     */
    public static String getFileExtensionUsingOSName() {
        String osName = new SystemInfo().getOsName().toLowerCase();
        //
        return osName.contains("linux") ? "" : ".exe";
    }

    /**
     * Retorna o caminho de um driver com base no nome do sistema operacional em que
     * a automação Web-UI está sendo executada. No parâmetro, informe o caminho de
     * recurso de onde o arquivo deve ser buscado. Exemplo:
     * src/test/resources/pasta_do_arquivo
     */
    public static String getFilePathUsingOSName(String resourcePath) {
        String osName = new SystemInfo().getOsName().toLowerCase(),
            browserName = new BrowserProperties().getBrowserName().toLowerCase(),
            browserVersion = String.format("version-%s", new BrowserProperties().getBrowserVersion());
        //
        return new File(String.format("%s/%s/%s/%s/", resourcePath,
            osName.contains("linux") && !browserName.contains("iexplorer") ? "linux" : "windows", browserName,
            browserVersion)).getAbsolutePath();
        //
    }

    /**
     * Retorna o caminho absoluto de um driver com base no nome do sistema
     * operacional em que a automação Web-UI está sendo executada. No parâmetro,
     * informe o caminho de recurso de onde o arquivo deve ser buscado. O driver
     * retornado é o primeiro da lista de versões existentes no diretório. Exemplo:
     * se no diretório src/main/resources/drivers/windows/chrome/version... existir
     * mais de uma versão de driver, será selecionada sempre a primeira.
     */
    public static String getDriverPathUsingOSName(String resourcePath) {
        String osName = new SystemInfo().getOsName().toLowerCase(),
            browserName = new BrowserProperties().getBrowserName().toLowerCase(),
            browserVersion = String.format("version-%s", new BrowserProperties().getBrowserVersion());
        //
        File[] driverFiles = new File(String.format("%s/%s/%s/%s/", resourcePath,
            osName.contains("linux") && !browserName.contains("iexplorer") ? "linux" : "windows", browserName,
            browserVersion)).listFiles();
        return Arrays.stream(driverFiles != null ? driverFiles : new File[0]).filter(f -> f.length() > 0).findFirst()
            .orElseThrow(() -> new IllegalStateException("Nenhum driver válido foi encontrado no diretório informado."))
            .getAbsolutePath();
    }

    /**
     * Configura a versão do navegador por meio do Java Command-Line. No parâmetro,
     * informe o caminho de recurso de onde o arquivo deve ser buscado. Exemplo:
     * -Dbwr_version=1.2.3
     */
    public static String getDvrPathUsingOSNameByBwrVersion(String resourcePath) {
        String osName = new SystemInfo().getOsName().toLowerCase(),
            browserName = new BrowserProperties().getBrowserName().toLowerCase(),
            browserVersion = JavaCMDLineClass.getBrowserVersion();
        //
        File[] driverFiles = new File(String.format("%s/%s/%s/%s/", resourcePath,
            osName.contains("linux") && !browserName.contains("iexplorer") ? "linux" : "windows", browserName,
            browserVersion)).listFiles();
        return Arrays.stream(driverFiles != null ? driverFiles : new File[0]).filter(f -> f.length() > 0).findFirst()
            .orElseThrow(() -> new IllegalStateException("Nenhum driver válido foi encontrado no diretório informado."))
            .getAbsolutePath();
    }
}
