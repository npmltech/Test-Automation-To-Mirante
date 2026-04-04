package br.com.automation.project.api.step.definitions;

import br.com.automation.project.api.ApiRegister;
import br.com.automation.project.api.model.ResponseClass;
import br.com.automation.project.utils.GlobalUtils;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonSD extends ApiRegister {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonSD.class);

    @Quando("eu realizar a chamada para a api person inserindo {string}, {string} e {string}")
    public void euRealizarAChamadaParaAApiPersonInserindoE(String nome, String idade, String comentarios) {
        LOGGER.info("[PersonSD] Inserindo pessoa: nome={}, idade={}, comentários={}", nome, idade, comentarios);
        getPersonApi().insertPerson(nome, idade, comentarios);
    }

    @Então("o status code deverá ser {int}")
    public void oStatusCodeDeveráSer(Integer httpStatusCode) {
        LOGGER.info("[PersonSD] Validando código de status HTTP: esperado={}", httpStatusCode);
        GlobalUtils.assertHttpStatusCode(httpStatusCode);
    }

    @Quando("eu realizar a chamada para api person atualizando {string}, {string}, {string} e {string}")
    public void euRealizarAChamadaParaApiPersonAtualizandoE(String id, String nome, String idade, String comentarios) {
        LOGGER.info("[PersonSD] Atualizando pessoa: id={}, nome={}, idade={}, comentários={}", id, nome, idade,
            comentarios);
        getPersonApi().updatePersonUsingId(id, nome, idade, comentarios);
    }

    @Quando("eu realizar uma chamada para a api persons")
    public void euRealizarUmaChamadaParaAApiPersons() {
        LOGGER.info("[PersonSD] Buscando lista de pessoas.");
        getPersonApi().getPersons();
    }

    @Então("a lista de pessoas cadastradas deverá ser retornada")
    public void aListaDePessoasCadastradasDeveráSerRetornada() {
        LOGGER.info("[PersonSD] Retorno da pesquisa: {}", ResponseClass.getResponse());
    }

    @Quando("eu chamo a api person e passo o parâmetro {string} para a pesquisa")
    public void euChamoAApiPersonEPassoOParamêtroParaAPesquisa(String id) {
        LOGGER.info("[PersonSD] Buscando pessoa por id={}", id);
        getPersonApi().getPersonById(id);
    }

    @Então("deverá retornar o {string}, a {string} e o {string}")
    public void deveráRetornarOAEO(String nome, String idade, String comentarios) {
        LOGGER.info("[PersonSD] Validando retorno: nome={}, idade={}, comentários={}", nome, idade, comentarios);
        GlobalUtils.compareValueResponseString(nome);
        GlobalUtils.compareValueResponseString(idade);
        MatcherAssert.assertThat(ResponseClass.getResponse(), CoreMatchers.containsString(comentarios));
    }

    @Quando("eu chamo a api person e passo o paramêtro {string} para exclusão")
    public void euChamoAApiPersonEPassoOParamêtroParaExclusão(String id) {
        LOGGER.info("[PersonSD] Excluindo pessoa por id={}", id);
        getPersonApi().deletePersonById(id);
    }
}
