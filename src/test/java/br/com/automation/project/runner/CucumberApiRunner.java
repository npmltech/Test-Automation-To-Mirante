package br.com.automation.project.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
// spotless:off
@CucumberOptions(
  features = "src/test/resources/features/api",
  glue = {
    "br.com.automation.project.runner",
    "br.com.automation.project.api.step.definitions",
    "br.com.automation.project.api.domain.starwar.root.step.definitions",
    "br.com.automation.project.api.domain.dog.step.definitions"
  },
  plugin = {
    "html:target/cucumber-reports/cucumber-api.html",
    "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
    "json:target/cucumber-reports/cucumber-api.json",
    "junit:target/cucumber-reports/cucumber-api.xml",
    "pretty",
    "summary"
  },
  snippets = SnippetType.CAMELCASE,
  stepNotifications = true
)
// spotless:on
public class CucumberApiRunner {
}
