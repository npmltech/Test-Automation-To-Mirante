package br.com.automation.project.ui.pages;

import br.com.automation.project.utils.ManagerFileUtils;
import br.com.automation.project.utils.WebDriverUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.List;

public class TodoListPG extends LoadableComponent<TodoListPG> {

    private WebDriver driver;

    public TodoListPG() {
        this.driver = WebDriverUtils.getDriverManager().getDriver();
    }

    public void getTodoUrl() {
        this.driver.get(ManagerFileUtils.getUrlFromJson("todo_list"));
    }

    @CacheLookup
    @FindBy(id = "new-todo")
    private WebElement txtNewTodo;

    public WebElement getTxtNewTodo() {
        return txtNewTodo;
    }

    @CacheLookup
    @FindBy(xpath = "//*[@*='toggle-all']")
    private List<WebElement> ckbSelectAllItems;

    public WebElement getCkbSelectAllItems() {
        return ckbSelectAllItems.get(0);
    }

    @CacheLookup
    @FindBy(xpath = "//*[@*='view']/label")
    private List<WebElement> lblTodoList;

    public List<WebElement> getLblTodoList() {
        return lblTodoList;
    }

    @CacheLookup
    @FindBy(id = "todo-list")
    private WebElement ulTodolist;

    public WebElement getUlTodolist() {
        return ulTodolist;
    }

    @CacheLookup
    @FindBy(xpath = "//*[@*='view']/button")
    private List<WebElement> btnDeleteTodoItem;

    public List<WebElement> getBtnDeleteTodoItem() {
        return btnDeleteTodoItem;
    }

    @CacheLookup
    @FindBy(id = "toggle-all")
    private WebElement ckbToggleAll;

    public WebElement getCkbToggleAll() {
        return ckbToggleAll;
    }

    @CacheLookup
    @FindBy(id = "clear-completed")
    private WebElement btnClearCompleted;

    public WebElement getBtnClearCompleted() {
        return btnClearCompleted;
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
    }
}
