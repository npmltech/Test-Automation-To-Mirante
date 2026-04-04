package br.com.automation.project.api;

import br.com.automation.project.api.model.ResponseClass;
import br.com.automation.project.utils.GlobalUtils;
import br.com.automation.project.utils.ManagerFileUtils;
import io.cucumber.datatable.DataTable;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SWPeopleApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(SWPeopleApi.class);

    public void getResponseBodyPeople(String id) {
        LOGGER.info("[SWPeopleApi] GET /people/{}", id);
        RestAssured.baseURI = ManagerFileUtils.getUrlFromJson("star_wars_url");
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.get(String.format("/people/%s", id));
        String bodyAsString = response.getBody().asString();
        ResponseClass.setResponse(bodyAsString);
        ResponseClass.setHttpStatusCode(response.getStatusCode());
        LOGGER.info("[SWPeopleApi] GET /people/{} - status={}", id, response.getStatusCode());
    }

    public void checkHttpStatusCodePeople(int httpStatusCode) {
        LOGGER.info("[SWPeopleApi] Validando código de status HTTP: esperado={}", httpStatusCode);
        GlobalUtils.assertHttpStatusCode(httpStatusCode);
    }

    public void checkResponseBodyPeople(String value) {
        LOGGER.debug("[SWPeopleApi] Validando valor no corpo da resposta: {}", value);
        GlobalUtils.compareValueResponseString(value);
    }

    public void checkResponseBodyPeople(DataTable dataTable) {
        LOGGER.debug("[SWPeopleApi] Validando DataTable com a lista da resposta.");
        GlobalUtils.assertResponseList(GlobalUtils.getDataTable(dataTable), GlobalUtils.getResponse());
    }

    public void checkResponseBodyPeople(DataTable dataTable, String key) {
        LOGGER.debug("[SWPeopleApi] Validando DataTable por chave: {}", key);
        GlobalUtils.assertResponseListReduced(GlobalUtils.getDataTable(dataTable),
            GlobalUtils.getValueByIndexFromResponse(key));
    }
}
