package br.com.automation.project.api.step.definitions;

import br.com.automation.project.api.ApiRegister;
import br.com.automation.project.utils.GlobalUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SWPeopleSD extends ApiRegister {

    private static final Logger LOGGER = LoggerFactory.getLogger(SWPeopleSD.class);

    @Dado("que eu acesso a api people {string}")
    public void queEuAcessoAApiPeople(String id) {
        LOGGER.info("[SWPeopleSD] Acessando API people: id={}", id);
        getSWPeopleApi().getResponseBodyPeople(id);
    }

    @Quando("o http status code da api people for {int}")
    public void oHttpStatusCodeDaApiPeopleFor(Integer httpStatusCode) {
        LOGGER.info("[SWPeopleSD] Validando código de status HTTP: esperado={}", httpStatusCode);
        getSWPeopleApi().checkHttpStatusCodePeople(httpStatusCode);
    }

    @Então("o parâmetro nome da personagem deverá ser {string}")
    public void oParâmetroNomeDaPersonagemDeveráSer(String nome) {
        LOGGER.info("[SWPeopleSD] Validando nome da personagem: {}", nome);
        GlobalUtils.getValueByIndexFromResponse("films");
        getSWPeopleApi().checkResponseBodyPeople(nome);
    }

    @Então("a altura deverá ser {string}")
    public void aAlturaDeveráSer(String altura) {
        LOGGER.info("[SWPeopleSD] Validando altura: {}", altura);
        getSWPeopleApi().checkResponseBodyPeople(altura);
    }

    @Então("o peso deverá ser {string}")
    public void oPesoDeveráSer(String peso) {
        LOGGER.info("[SWPeopleSD] Validando peso: {}", peso);
        getSWPeopleApi().checkResponseBodyPeople(peso);
    }

    @Então("a cor de cabelo deverá ser {string}")
    public void aCorDeCabeloDeveráSer(String corCabelo) {
        LOGGER.info("[SWPeopleSD] Validando cor de cabelo: {}", corCabelo);
        getSWPeopleApi().checkResponseBodyPeople(corCabelo);
    }

    @Então("a cor de pele deverá ser {string}")
    public void aCorDePeleDeveráSer(String corPele) {
        LOGGER.info("[SWPeopleSD] Validando cor de pele: {}", corPele);
        getSWPeopleApi().checkResponseBodyPeople(corPele);
    }

    @Então("a cor do olho deverá ser {string}")
    public void aCorDoOlhoDeveráSer(String corOlho) {
        LOGGER.info("[SWPeopleSD] Validando cor do olho: {}", corOlho);
        getSWPeopleApi().checkResponseBodyPeople(corOlho);
    }

    @Então("a data de nascimento deverá ser {string}")
    public void aDataDeNascimentoDeveráSer(String dataNasc) {
        LOGGER.info("[SWPeopleSD] Validando data de nascimento: {}", dataNasc);
        getSWPeopleApi().checkResponseBodyPeople(dataNasc);
    }

    @Então("o gênero deverá ser {string}")
    public void oGêneroDeveráSer(String genero) {
        LOGGER.info("[SWPeopleSD] Validando gênero: {}", genero);
        getSWPeopleApi().checkResponseBodyPeople(genero);
    }

    @Então("o planeta natal deverá ser apresentado no serviço {string}")
    public void oPlanetaNatalDeveráSerApresentadoNoServiço(String planetaNatal) {
        LOGGER.info("[SWPeopleSD] Validando planeta natal: {}", planetaNatal);
        getSWPeopleApi().checkResponseBodyPeople(planetaNatal);
    }

    @Então("os filmes deverão ser apresentados nos serviços")
    public void osFilmesDeverãoSerApresentadosNosServiços(DataTable urlFilmes) {
        LOGGER.info("[SWPeopleSD] Validando lista de filmes.");
        getSWPeopleApi().checkResponseBodyPeople(urlFilmes, GlobalUtils.getFirstIndexDataTable(urlFilmes));
    }

    @Então("as espécies deverão ser apresentados no serviço")
    public void asEspéciesDeverãoSerApresentadosNoServiço(DataTable urlEspecies) {
        LOGGER.info("[SWPeopleSD] Validando lista de espécies (etapa ainda não implementada).");
        // getSWPeopleApi().checkResponseBodyPeople(urlEspecies);
    }

    @Então("os veículos deverão ser apresentados no serviço")
    public void osVeículosDeverãoSerApresentadosNoServiço(DataTable urlVeiculos) {
        LOGGER.info("[SWPeopleSD] Validando lista de veículos.");
        getSWPeopleApi().checkResponseBodyPeople(urlVeiculos);
    }

    @Então("as naves estelares deverão ser apresentados no serviço")
    public void asNavesEstelaresDeverãoSerApresentadosNoServiço(DataTable urlNavesEstrelares) {
        LOGGER.info("[SWPeopleSD] Validando lista de naves estelares.");
        getSWPeopleApi().checkResponseBodyPeople(urlNavesEstrelares);
    }

    @Então("a data de criação deverá ser {string}")
    public void aDataDeCriaçãoDeveráSer(String data_criacao) {
        LOGGER.info("[SWPeopleSD] Validando data de criação: {}", data_criacao);
        getSWPeopleApi().checkResponseBodyPeople(data_criacao);
    }

    @Então("a data de edição deverá ser {string}")
    public void aDataDeEdiçãoDeveráSer(String data_edicao) {
        LOGGER.info("[SWPeopleSD] Validando data de edição: {}", data_edicao);
        getSWPeopleApi().checkResponseBodyPeople(data_edicao);
    }

    @Então("o endereço de acesso contendo as informação deverá ser {string}")
    public void oEndereçoDeAcessoContendoAsInformaçãoDeveráSer(String url) {
        LOGGER.info("[SWPeopleSD] Validando URL de acesso: {}", url);
        getSWPeopleApi().checkResponseBodyPeople(url);
    }
}
