package br.com.automation.project.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread responsável por consumir e registrar o conteúdo de fluxos de entrada e
 * erro de processos externos.
 */
public class StreamGobbler extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(StreamGobbler.class);
    InputStream is;
    String type;

    /**
     * Cria um consumidor de fluxo para o tipo informado.
     */
    StreamGobbler(InputStream is, String type) {
        this.is = is;
        this.type = type;
    }

    public void run() {
        //
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if ("ERRO".equalsIgnoreCase(type)) {
                    LOGGER.error("{}>{}", type, line);
                } else {
                    LOGGER.info("{}>{}", type, line);
                }
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Falha ao processar stream de entrada.", ex);
        }
    }
}
