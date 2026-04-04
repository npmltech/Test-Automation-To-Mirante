package br.com.automation.project.ui.pages;

import br.com.automation.project.utils.ManagerFileUtils;
import br.com.automation.project.utils.WebDriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.net.URI;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AgiBlogSearchPG extends LoadableComponent<AgiBlogSearchPG> {

    private static final By SEARCH_TOGGLE_BUTTON = By.cssSelector("div.ast-search-menu-icon button.ast-search-submit");
    private static final By SEARCH_FIELD = By.cssSelector("input#search-field.search-field");
    private static final By RESULTS_TITLE = By.cssSelector("h1.page-title");
    private static final By RESULT_LINKS = By.cssSelector("h2.entry-title a");

    private final WebDriver driver;

    public AgiBlogSearchPG() {
        this.driver = WebDriverUtils.getDriverManager().getDriver();
    }

    public void accessAgiBlog() {
        this.driver.get(ManagerFileUtils.getUrlFromJson("Agi_Blog"));
    }

    public void searchFor(String termo) {
        try {
            WebElement searchField = openSearchField();
            searchField.clear();
            searchField.sendKeys(termo);
            searchField.sendKeys(Keys.ENTER);
        } catch (RuntimeException ex) {
            navigateToSearchResults(termo);
        }
        waitSearchResultsPage();
    }

    public String getSearchResultsTitle() {
        return WebDriverUtils.getWait().until(ExpectedConditions.visibilityOfElementLocated(RESULTS_TITLE)).getText();
    }

    public List<WebElement> getResultLinks() {
        return this.driver.findElements(RESULT_LINKS);
    }

    public List<String> getResultTitles() {
        List<String> titles = new ArrayList<>();
        getResultLinks().forEach(link -> titles.add(link.getText()));
        return titles;
    }

    public int getResultCount() {
        return getResultLinks().size();
    }

    public String getCurrentUrl() {
        return this.driver.getCurrentUrl();
    }

    public String getSearchQueryTerm() {
        String currentUrl = getCurrentUrl();
        if (currentUrl == null || currentUrl.isBlank()) {
            return "";
        }
        String query = URI.create(currentUrl).getQuery();
        if (query == null || query.isBlank()) {
            return "";
        }
        for (String pair : query.split("&")) {
            String[] tokens = pair.split("=", 2);
            if (tokens.length == 2 && "s".equalsIgnoreCase(tokens[0])) {
                return URLDecoder.decode(tokens[1], StandardCharsets.UTF_8);
            }
        }
        return "";
    }

    private WebElement openSearchField() {
        List<WebElement> searchFields = this.driver.findElements(SEARCH_FIELD);
        if (!searchFields.isEmpty() && searchFields.getFirst().isDisplayed()) {
            return WebDriverUtils.getWait().until(ExpectedConditions.elementToBeClickable(SEARCH_FIELD));
        }

        WebElement toggleButton = WebDriverUtils.getWait()
            .until(ExpectedConditions.elementToBeClickable(SEARCH_TOGGLE_BUTTON));
        ((JavascriptExecutor) this.driver).executeScript("arguments[0].click();", toggleButton);
        return WebDriverUtils.getWait().until(ExpectedConditions.visibilityOfElementLocated(SEARCH_FIELD));
    }

    private void waitSearchResultsPage() {
        try {
            WebDriverUtils.getWait().until(driver -> {
                String currentUrl = driver.getCurrentUrl();
                return currentUrl != null && currentUrl.contains("?s=");
            });
        } catch (TimeoutException ex) {
            WebDriverUtils.getWait().until(ExpectedConditions.visibilityOfElementLocated(RESULTS_TITLE));
        }
        WebDriverUtils.getWait().until(ExpectedConditions.visibilityOfElementLocated(RESULTS_TITLE));
    }

    private void navigateToSearchResults(String termo) {
        String encodedTerm = URLEncoder.encode(termo, StandardCharsets.UTF_8);
        String primaryUrl = String.format("%s?s=%s", ManagerFileUtils.getUrlFromJson("Agi_Blog"), encodedTerm);
        String canonicalUrl = String.format("https://blog.agibank.com.br/?s=%s", encodedTerm);
        try {
            this.driver.navigate().to(primaryUrl);
        } catch (RuntimeException ex) {
            this.driver.navigate().to(canonicalUrl);
        }
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
    }
}
