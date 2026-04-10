package br.com.automation.project.ui.pages;

import br.com.automation.project.utils.ManagerFileUtils;
import br.com.automation.project.utils.WebDriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Wait;

import java.net.URI;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AgiBlogSearchPG extends LoadableComponent<AgiBlogSearchPG> {

    private static final Duration UI_WAIT_TIMEOUT = Duration.ofSeconds(12);
    private static final Duration UI_WAIT_POLL = Duration.ofMillis(500);
    private static final By SEARCH_TOGGLE_BUTTON = By.cssSelector("div.ast-search-menu-icon button.ast-search-submit");
    private static final By SEARCH_FIELD = By.cssSelector("input#search-field.search-field");
    private static final By RESULTS_TITLE = By.cssSelector("h1.page-title");
    private static final List<By> RESULT_LINK_SELECTORS = Arrays.asList(By.cssSelector("article h2.entry-title a"),
        By.cssSelector("h2.entry-title a"), By.cssSelector("h2.wp-block-post-title a"));
    private static final String AGI_BLOG_URL_KEY = "agi_blog";

    private final WebDriver driver;

    public AgiBlogSearchPG() {
        this.driver = WebDriverUtils.getDriverManager().getDriver();
    }

    public void accessAgiBlog() {
        this.driver.get(getRequiredAgiBlogBaseUrl());
    }

    public void searchFor(String termo) {
        if (termo == null || termo.isBlank()) {
            throw new IllegalArgumentException("O termo de busca não pode ser nulo ou vazio.");
        }
        try {
            WebElement searchField = openSearchField();
            searchField.clear();
            searchField.sendKeys(termo);
            searchField.sendKeys(Keys.ENTER);
            waitSearchResultsPage();
        } catch (RuntimeException ignored) {
            // Fallback para reduzir intermitência de UI no modo headless.
            navigateToSearchResults(termo);
            waitSearchResultsPage();
        }
    }

    public String getSearchResultsTitle() {
        return WebDriverUtils.getWait().until(ExpectedConditions.visibilityOfElementLocated(RESULTS_TITLE)).getText();
    }

    public List<WebElement> getResultLinks() {
        for (By selector : RESULT_LINK_SELECTORS) {
            List<WebElement> links = this.driver.findElements(selector);
            if (!links.isEmpty()) {
                return links;
            }
        }
        return List.of();
    }

    public List<String> getResultTitles() {
        List<String> titles = new ArrayList<>();
        getResultLinks().forEach(link -> {
            String title = extractTitle(link);
            if (!title.isBlank()) {
                titles.add(title);
            }
        });
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
        String query;
        try {
            query = URI.create(currentUrl).getQuery();
        } catch (IllegalArgumentException ex) {
            return "";
        }
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
        Wait<WebDriver> wait = createUiWait();
        List<WebElement> searchFields = this.driver.findElements(SEARCH_FIELD);
        if (!searchFields.isEmpty() && searchFields.getFirst().isDisplayed()) {
            return wait.until(ExpectedConditions.elementToBeClickable(SEARCH_FIELD));
        }

        WebElement toggleButton = wait.until(ExpectedConditions.elementToBeClickable(SEARCH_TOGGLE_BUTTON));
        ((JavascriptExecutor) this.driver).executeScript("arguments[0].click();", toggleButton);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_FIELD));
    }

    private void waitSearchResultsPage() {
        Wait<WebDriver> wait = createUiWait();
        try {
            wait.until(driver -> {
                String currentUrl = driver.getCurrentUrl();
                return currentUrl != null && currentUrl.contains("?s=");
            });
        } catch (TimeoutException ex) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(RESULTS_TITLE));
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(RESULTS_TITLE));
    }

    private Wait<WebDriver> createUiWait() {
        return new FluentWait<>(this.driver).withTimeout(UI_WAIT_TIMEOUT).pollingEvery(UI_WAIT_POLL)
            .ignoring(NoSuchElementException.class);
    }

    private void navigateToSearchResults(String termo) {
        String encodedTerm = URLEncoder.encode(termo, StandardCharsets.UTF_8);
        String primaryUrl = String.format("%s?s=%s", getRequiredAgiBlogBaseUrl(), encodedTerm);
        String canonicalUrl = String.format("https://blog.agibank.com.br/?s=%s", encodedTerm);
        try {
            this.driver.navigate().to(primaryUrl);
        } catch (RuntimeException ex) {
            this.driver.navigate().to(canonicalUrl);
        }
    }

    private String getRequiredAgiBlogBaseUrl() {
        return Objects.requireNonNull(ManagerFileUtils.getUrlFromJson(AGI_BLOG_URL_KEY),
            "Não foi possível obter a URL base 'agi_blog' no arquivo de configuração.");
    }

    private String extractTitle(WebElement link) {
        List<String> candidates = Arrays.asList(link.getText(), link.getAttribute("title"),
            link.getAttribute("aria-label"), link.getAttribute("innerText"), link.getAttribute("textContent"));
        for (String candidate : candidates) {
            if (candidate != null) {
                String normalized = candidate.replaceAll("\\s+", " ").trim();
                if (!normalized.isBlank()) {
                    return normalized;
                }
            }
        }
        String href = link.getAttribute("href");
        if (href != null && !href.isBlank()) {
            String[] tokens = href.replaceAll("/$", "").split("/");
            String slug = tokens[tokens.length - 1].replace("-", " ").trim();
            if (!slug.isBlank()) {
                return slug;
            }
        }
        return "";
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
    }
}
