package br.com.automation.project.runner;

import br.com.automation.project.utils.ManagerFileUtils;
import br.com.automation.project.utils.WebDriverUtils;
import br.com.automation.project.web.driver.factory.DriverManager;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(Cucumber.class)
// spotless:off
@CucumberOptions(
  features = "src/test/resources/features",
  glue = {
    "br.com.automation.project.runner",
    "br.com.automation.project.ui.step.definitions",
    "br.com.automation.project.api.step.definitions",
    "br.com.automation.project.api.domain.starwar.root.step.definitions",
    "br.com.automation.project.api.domain.dog.step.definitions"
  },
  plugin = {
    "html:target/cucumber-reports/cucumber.html",
    // -Dcucumber.options="(substitua por dois hífens)plugin
    // io.qameta.allure.cucumber6jvm.AllureCucumber6Jvm"
    // Comando de execução via Cucumber Maven (equivalente ao definido nesta classe)...
    "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm", "json:target/cucumber-reports/cucumber.json",
    "junit:target/cucumber-reports/cucumber.xml", "pretty", "rerun:target/cucumber-reports/rerun.txt",
    "summary"
  },
  snippets = SnippetType.CAMELCASE,
  stepNotifications = true
)
// spotless:on
public class CucumberRunner {

    @BeforeClass
    public static void setUp() {
        ManagerFileUtils.checkAndGenerateFilePath("target/log-exec");
    }

    @AfterClass
    public static void tearDown() {
        List.of(WebHook.getTagNames()).parallelStream().forEach(tagName -> {
            if (tagName.contains("@ui")) {
                DriverManager.addDriver(WebDriverUtils.getDriverManager().getDriver());
                DriverManager.closeAndQuitDriver();
            }
        });
    }
}
