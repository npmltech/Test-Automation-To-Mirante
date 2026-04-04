package br.com.automation.project.ui.pages;

import br.com.automation.project.utils.ManagerFileUtils;
import br.com.automation.project.utils.WebDriverUtils;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.List;

public class FirstWebTestPG extends LoadableComponent<FirstWebTestPG> {

    public void accessUrl(String urlName) {
        WebDriverUtils.getDriverManager().getDriver().navigate().to(ManagerFileUtils.getUrlFromJson(urlName));
    }

    public List<WebElement> getSearchTextBoxInList() {
        return WebDriverUtils.getWait()
            .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@*='text'][@*='Pesquisar']")));
    }

    public List<WebElement> getResultSearchLinks() {
        return WebDriverUtils.getWait()
            .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@*='center_col']//a//span")));
    }

    public void validateTitle(String titulo) {
        MatcherAssert.assertThat(WebDriverUtils.getDriverManager().getDriver().getTitle(),
            CoreMatchers.containsString(titulo));
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
    }
}
