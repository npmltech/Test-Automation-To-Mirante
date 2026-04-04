package br.com.automation.project.api.domain.starwar.root.step.definitions;

import br.com.automation.project.api.domain.starwar.error.ResponseException;
import br.com.automation.project.api.domain.starwar.request.RootApiRequest;
import br.com.automation.project.api.domain.starwar.root.RootApiResponseBO;
import br.com.automation.project.utils.GlobalUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RootApiSD {

    private static final Logger LOGGER = LoggerFactory.getLogger(RootApiSD.class);

    private ResponseException responseException;
    private RootApiResponseBO rootApiResponseBO;
    private final RootApiRequest request = new RootApiRequest();

    @Dado("que eu acesso a api root")
    public void queEuAcessoAApiRoot() {
        LOGGER.info("[RootApiSD] Acessando a API raiz de Star Wars.");
        this.responseException = null;
        this.rootApiResponseBO = null;

        try {
            this.rootApiResponseBO = this.request.requestRoot();
            LOGGER.info("[RootApiSD] Resposta recebida: status={}, modelo={}",
                this.rootApiResponseBO.getHttpStatusCode(), this.rootApiResponseBO.getModel());
        } catch (ResponseException ex) {
            LOGGER.warn("[RootApiSD] Falha ao acessar a API raiz: status={}", ex.getHttpStatusCode(), ex);
            this.responseException = ex;
        }
    }

    @Quando("o http status code da api root for {int}")
    public void oHttpStatusCodeDaApiRootFor(Integer httpStatusCode) {
        LOGGER.info("[RootApiSD] Validando código de status HTTP: esperado={}", httpStatusCode);
        failIfRequestFailed();
        Assertions.assertNotNull(this.rootApiResponseBO, "A resposta da API Root não foi obtida.");
        Assertions.assertEquals(httpStatusCode.intValue(), this.rootApiResponseBO.getHttpStatusCode(),
            String.format("O HTTP Status Code atual (%s) é diferente do esperado (%s).",
                this.rootApiResponseBO.getHttpStatusCode(), httpStatusCode));
    }

    @Então("os serviços deverão ser apresentados nos serviços")
    public void osServicosDeveraoSerApresentadosNosServicos(DataTable services) {
        LOGGER.info("[RootApiSD] Validando a lista de serviços da API raiz.");
        failIfRequestFailed();
        Assertions.assertNotNull(this.rootApiResponseBO, "A resposta da API Root não foi obtida.");
        GlobalUtils.assertResponseList(GlobalUtils.getDataTable(services), this.rootApiResponseBO.getServices());
    }

    private void failIfRequestFailed() {
        if (this.responseException != null) {
            Integer apiCode = this.responseException.getErrorResponse() != null
                ? this.responseException.getErrorResponse().getCode()
                : null;
            throw new AssertionError(String.format(
                "Falha ao acessar a API Root. Código HTTP recebido: %s. Código de erro retornado pela API: %s.",
                this.responseException.getHttpStatusCode(), apiCode), this.responseException);
        }
    }
}
