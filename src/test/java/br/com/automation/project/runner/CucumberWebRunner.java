package br.com.automation.project.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
// spotless:off
@CucumberOptions(
  features = "src/test/resources/features/ui",
  glue = {
    "br.com.automation.project.runner",
    "br.com.automation.project.ui.step.definitions"
  },
  plugin = {
    "html:target/cucumber-reports/cucumber-web.html",
    "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
    "json:target/cucumber-reports/cucumber-web.json",
    "junit:target/cucumber-reports/cucumber-web.xml",
    "pretty",
    "summary"
  },
  snippets = SnippetType.CAMELCASE,
  stepNotifications = true
)
// spotless:on
public class CucumberWebRunner {
}
