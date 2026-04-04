package br.com.automation.project.runner;

import br.com.automation.project.utils.ScreenshotGenerator;
import br.com.automation.project.utils.WebDriverUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import java.util.Set;

public class WebHook {

    private static volatile boolean uiScenarioExecuted;

    public static boolean hasUiScenarioExecuted() {
        return uiScenarioExecuted;
    }

    private static boolean isUiScenario(Set<String> sourceTags) {
        return sourceTags != null && sourceTags.stream().anyMatch(tag -> tag.equals("@ui") || tag.startsWith("@ui_"));
    }

    @Before
    public void beforeHook(Scenario scenario) {
        Set<String> sourceTags = scenario.getSourceTagNames();
        if (sourceTags != null) {
            sourceTags.stream().sorted().forEachOrdered(tagName -> System.out.println("--> Rodando Features por Tag: " + tagName));
        }

        if (isUiScenario(sourceTags)) {
            uiScenarioExecuted = true;
            WebDriverUtils.getDriverManager().getDriver().manage().window().maximize();
        }
    }

    @After
    public void afterHook(Scenario scenario) {
        if (isUiScenario(scenario.getSourceTagNames())) {
            if (scenario.isFailed()) {
                scenario.attach(
                    ScreenshotGenerator.getScreenshot(scenario, WebDriverUtils.getDriverManager().getDriver()),
                    "image/png", "<<-- SCREENSHOT DA FALHA -->>");
                ScreenshotGenerator.generateScreenshot(scenario, WebDriverUtils.getDriverManager().getDriver());
            }
        }
    }
}
