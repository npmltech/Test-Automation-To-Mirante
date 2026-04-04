package br.com.automation.project.utils;

/**
 * Centraliza a leitura de propriedades recebidas via linha de comando da JVM.
 */
public class JavaCMDLineClass {

    private final static String ENV = System.getProperty("env");
    private final static String BROWSER_VERSION = System.getProperty("bwr_version");

    /**
     * Retorna o ambiente informado na execução.
     */
    public static String getEnv() {
        return JavaCMDLineClass.ENV.isEmpty() ? "dev" : JavaCMDLineClass.ENV;
    }

    /**
     * Retorna a versão do navegador informada na execução.
     */
    public static String getBrowserVersion() {
        return JavaCMDLineClass.BROWSER_VERSION.isEmpty() ? "" : JavaCMDLineClass.ENV;
    }
}
