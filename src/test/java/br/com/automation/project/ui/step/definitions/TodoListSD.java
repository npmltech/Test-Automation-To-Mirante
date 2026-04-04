package br.com.automation.project.ui.step.definitions;

import br.com.automation.project.ui.pages.PageRegister;
import br.com.automation.project.utils.GlobalUtils;
import br.com.automation.project.utils.WebDriverUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.qameta.allure.Allure.step;

public class TodoListSD extends PageRegister {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoListSD.class);

    private WebDriver driver;
    private Wait<WebDriver> wait;

    public TodoListSD() {
        this.driver = WebDriverUtils.getDriverManager().getDriver();
        this.wait = WebDriverUtils.getWait();
    }

    @Dado("que eu acesso a url de criação de lista de tarefas")
    public void queEuAcessoAUrlDeCriaçãoDeListaDeTarefas() {
        LOGGER.info("[TodoListSD] Acessando URL do Todo-List.");
        step("Acessar a URL do Todo-List");
        getTodoListPG().getTodoUrl();
    }

    @Quando("eu inserir os itens na lista de tarefas")
    public void euInserirOsItensNaListaDeTarefas(DataTable listaItens) {
        LOGGER.info("[TodoListSD] Inserindo itens na lista de tarefas.");
        step("Inserir itens no Todo-List");
        GlobalUtils.getDataTable(listaItens).forEach(item -> {
            LOGGER.debug("[TodoListSD] Inserindo item: {}", item);
            getTodoListPG().getTxtNewTodo().sendKeys(item);
            this.wait.until(ExpectedConditions.visibilityOf(getTodoListPG().getTxtNewTodo()));
            WebDriverUtils.sleepInSeconds(1);
            getTodoListPG().getTxtNewTodo().sendKeys(Keys.ENTER);
        });
    }

    @Então("os dados incluídos deverão estar dispostos na lista")
    public void osDadosIncluídosDeverãoEstarDispostosNaLista() {
        getTodoListPG().getLblTodoList().forEach(item -> LOGGER.info("[TodoListSD] Item na lista: {}", item.getText()));
    }

    @Então("ao clicar no botão de exclusão, os itens deverão ser excluídos")
    public void aoClicarNoBotãoDeExclusãoOsItensDeverãoSerExcluídos() {
        LOGGER.info("[TodoListSD] Excluindo itens da lista de tarefas.");
        this.wait.until(ExpectedConditions.visibilityOf(getTodoListPG().getUlTodolist()));
        var elementsList = getTodoListPG().getBtnDeleteTodoItem();
        for (int i = 0; i < elementsList.size(); i++) {
            WebDriverUtils.sleepInSeconds(1);
            this.wait
                .until(ExpectedConditions.visibilityOf(this.driver.findElements(By.xpath("//div[@*='view']")).get(0)));
            new Actions(this.driver)
                .moveToElement(
                    WebDriverUtils.getDriverManager().getDriver().findElements(By.xpath("//*[@*='view']")).get(0))
                .perform();
            this.driver.findElements(By.xpath("//*[@*='view']/button")).get(0).click();
        }
    }

    @Então("ao clicar no botão Toggle All, todos itens deverão ser selecionados")
    public void aoClicarNoBotãoToggleAllTodosItensDeverãoSerSelecionados() {
        LOGGER.info("[TodoListSD] Clicando em Toggle All.");
        getTodoListPG().getCkbToggleAll().click();
    }

    @Então("ao clicar no botão Clear Completed, todos os itens deverão ser excluídos")
    public void aoClicarNoBotãoClearCompletedTodosOsItensDeverãoSerExcluídos() {
        LOGGER.info("[TodoListSD] Clicando em Clear Completed e limpando cookies.");
        getTodoListPG().getBtnClearCompleted().click();
        this.driver.manage().deleteAllCookies();
    }
}
