package br.com.automation.project.runner;

import br.com.automation.project.web.driver.factory.DriverManager;
import br.com.automation.project.utils.ManagerFileUtils;
import br.com.automation.project.utils.ScreenshotGenerator;
import br.com.automation.project.utils.WebDriverUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import java.util.Collection;

public class WebHook {

    private static volatile boolean uiScenarioExecuted;

    public static boolean hasUiScenarioExecuted() {
        return uiScenarioExecuted;
    }

    private static boolean isUiScenario(Collection<String> sourceTags) {
        return sourceTags != null && sourceTags.stream().anyMatch(tag -> tag.equals("@ui") || tag.startsWith("@ui_")
            || tag.equals("@web") || tag.startsWith("@web_") || tag.equals("@agi_blog"));
    }

    @BeforeAll
    public static void beforeAll() {
        ManagerFileUtils.checkAndGenerateFilePath("target/log-exec");
    }

    @Before
    public void beforeHook(Scenario scenario) {
        Collection<String> sourceTags = scenario.getSourceTagNames();
        if (sourceTags != null) {
            sourceTags.stream().sorted()
                .forEachOrdered(tagName -> System.out.println("--> Rodando Features por Tag: " + tagName));
        }

        if (isUiScenario(sourceTags)) {
            uiScenarioExecuted = true;
            WebDriverUtils.getDriverManager().getDriver().manage().window().maximize();
        }
    }

    @After
    public void afterHook(Scenario scenario) {
        if (isUiScenario(scenario.getSourceTagNames())) {
            try {
                if (scenario.isFailed()) {
                    scenario.attach(
                        ScreenshotGenerator.getScreenshot(scenario, WebDriverUtils.getDriverManager().getDriver()),
                        "image/png", "<<-- SCREENSHOT DA FALHA -->>");
                    ScreenshotGenerator.generateScreenshot(scenario, WebDriverUtils.getDriverManager().getDriver());
                }
            } finally {
                DriverManager.closeAndQuitDriver();
            }
        }
    }
}
