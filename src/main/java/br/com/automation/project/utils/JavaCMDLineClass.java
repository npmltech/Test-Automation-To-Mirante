package br.com.automation.project.utils;

/**
 * Centraliza a leitura de propriedades recebidas via linha de comando da JVM.
 */
public class JavaCMDLineClass {

    private final static String ENV = System.getProperty("env", "dev");
    private final static String BROWSER_VERSION = System.getProperty("bwr_version", "");

    /**
     * Retorna o ambiente informado na execução.
     */
    public static String getEnv() {
        return JavaCMDLineClass.ENV.isBlank() ? "dev" : JavaCMDLineClass.ENV;
    }

    /**
     * Retorna a versão do navegador informada na execução.
     */
    public static String getBrowserVersion() {
        return JavaCMDLineClass.BROWSER_VERSION.isBlank() ? "" : JavaCMDLineClass.BROWSER_VERSION;
    }
}
