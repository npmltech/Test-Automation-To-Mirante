package br.com.automation.project.ui.step.definitions;

import br.com.automation.project.ui.pages.PageRegister;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import org.openqa.selenium.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirstWebTestSD extends PageRegister {

    private static final Logger LOGGER = LoggerFactory.getLogger(FirstWebTestSD.class);

    @Dado("que eu acesso a {string}")
    public void queEuAcessoA(String url) {
        LOGGER.info("[FirstWebTestSD] Acessando URL: {}", url);
        getFirstWebTestPG().accessUrl(url);
    }

    @Quando("eu preencho o campo de pesquisa com {string}")
    public void euPreenchoOCampoDePesquisaCom(String texto) {
        LOGGER.info("[FirstWebTestSD] Preenchendo campo de pesquisa com: {}", texto);
        getFirstWebTestPG().getSearchTextBoxInList().get(0).sendKeys(texto);
        getFirstWebTestPG().getSearchTextBoxInList().get(0).sendKeys(Keys.ENTER);
    }

    @Quando("clico no primeiro link da pesquisa")
    public void clicoNoPrimeiroLinkDaPesquisa() {
        LOGGER.info("[FirstWebTestSD] Clicando no primeiro link da pesquisa.");
        getFirstWebTestPG().getResultSearchLinks().get(0).click();
    }

    @Então("o {string} deverá ser apresentado")
    public void oDeveráSerApresentado(String titulo) {
        LOGGER.info("[FirstWebTestSD] Validando título da página: {}", titulo);
        getFirstWebTestPG().validateTitle(titulo);
    }
}
