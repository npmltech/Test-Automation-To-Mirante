package br.com.automation.project.runner;

import br.com.automation.project.utils.ScreenshotGenerator;
import br.com.automation.project.utils.WebDriverUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.commons.lang3.StringUtils;

public class WebHook {

    private static String[] TAG_NAMES;

    public static void setTagNames(String[] tagNames) {
        TAG_NAMES = tagNames;
    }

    public static String[] getTagNames() {
        return TAG_NAMES;
    }

    @Before
    public void beforeHook(Scenario scenario) {
        if (scenario.getSourceTagNames() != null) {
            scenario.getSourceTagNames().stream().forEachOrdered(tagName -> {
                System.out.println("--> Rodando Features por Tag: " + tagName);
                String[] tagNames = StringUtils.split(tagName, ":");
                setTagNames(tagNames);
            });
        }
        if (getTagNames()[0].contains("@ui")) {
            WebDriverUtils.getDriverManager().getDriver().manage().window().maximize();
        }
    }

    @After
    public void afterHook(Scenario scenario) {
        if (getTagNames()[0].contains("@ui")) {
            if (scenario.isFailed()) {
                scenario.attach(
                    ScreenshotGenerator.getScreenshot(scenario, WebDriverUtils.getDriverManager().getDriver()),
                    "image/png", "<<-- SCREENSHOT DA FALHA -->>");
                ScreenshotGenerator.generateScreenshot(scenario, WebDriverUtils.getDriverManager().getDriver());
            }
        }
    }
}
