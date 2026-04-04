package br.com.automation.project.web.browser.factory;

import br.com.automation.project.web.properties.WebPropertiesClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ChromeDriverArguments {

    private ChromeDriverArguments() {
        // Classe utilitária — não instanciável.
    }

    // spotless:off
    public static String[] getArguments() {
        List<String> arguments = new ArrayList<>(
            Arrays.asList("--aggressive-cache-discard",
                "--disable-blink-features=AutomationControlled",
                "--disable-dev-shm-usage",
                "--disable-extensions-file-access-check",
                "--disable-features=VizDisplayCompositor",
                "--disable-gl-drawing-for-tests",
                "--disable-gpu",
                "--disable-infobars",
                "--disable-low-res-tiling",
                "--disable-popup-blocking",
                "--disable-software-rasterizer",
                "--disable-web-security",
                "--ignore-gpu-blacklist",
                "--incognito",
                "--lang=pt-BR",
                "--no-sandbox",
                "--ozone-platform=x11",
                "--reduce-security-for-testing",
                "--remote-debugging-pipe",
                "--start-maximized",
                "--test-type",
                "--window-size=1920,1080"
            )
        );

        if (isHeadlessEnabled()) {
            arguments.add("--headless=new");
        }

        return arguments.toArray(new String[0]);
    }
    // spotless:on

    private static boolean isHeadlessEnabled() {
        String headlessFromJvm = System.getProperty("headless");
        if (headlessFromJvm != null && !headlessFromJvm.isBlank()) {
            return Boolean.parseBoolean(headlessFromJvm);
        }
        String headlessFromProperties = new WebPropertiesClass().getConfigFileProperty("headless");
        return Boolean.parseBoolean(headlessFromProperties);
    }
}
