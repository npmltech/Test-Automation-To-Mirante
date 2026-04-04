package br.com.automation.project.api.domain.starwar.request;

import br.com.automation.project.api.domain.starwar.error.ErrorResponseBO;
import br.com.automation.project.api.domain.starwar.error.ResponseException;
import br.com.automation.project.api.domain.starwar.root.RootApiModelBO;
import br.com.automation.project.api.domain.starwar.root.RootApiResponseBO;
import br.com.automation.project.api.model.ResponseClass;
import br.com.automation.project.utils.ManagerFileUtils;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public final class RootApiRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RootApiRequest.class);
    private static final String STAR_WARS_URL_KEY = "Star_Wars_Url";

    public RootApiResponseBO requestRoot() throws ResponseException {
        LOGGER.info("[RootApiRequest] GET {} (root)", STAR_WARS_URL_KEY);
        RequestSpecification request = buildRequestSpecification();
        Response response = request.contentType(ContentType.JSON).get();
        response.then().log().all();

        int statusCode = response.statusCode();
        ResponseClass.setHttpStatusCode(statusCode);
        ResponseClass.setResponse(response.asPrettyString());
        LOGGER.info("[RootApiRequest] GET root - status={}", statusCode);

        if (statusCode != 200) {
            LOGGER.warn("[RootApiRequest] Resposta inesperada: status={}, body={}", statusCode,
                response.body().asPrettyString());
            throw new ResponseException(statusCode, buildErrorResponse(response));
        }

        RootApiModelBO rootApiModel = response.as(RootApiModelBO.class);
        LOGGER.debug("[RootApiRequest] Modelo deserializado: {}", rootApiModel);
        return new RootApiResponseBO(rootApiModel, statusCode);
    }

    private RequestSpecification buildRequestSpecification() {
        String baseUri = ManagerFileUtils.getUrlFromJson(STAR_WARS_URL_KEY);
        if (baseUri == null || baseUri.isBlank()) {
            throw new IllegalStateException(
                String.format("A URL da API Star Wars não foi encontrada para a chave '%s'.", STAR_WARS_URL_KEY));
        }
        LOGGER.debug("[RootApiRequest] Base URI resolvida: {}", baseUri);
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBaseUri(baseUri);
        builder.setRelaxedHTTPSValidation();
        return RestAssured.given().spec(builder.build());
    }

    private ErrorResponseBO buildErrorResponse(Response response) {
        try {
            ErrorResponseBO errorResponse = response.as(ErrorResponseBO.class);
            return Objects.requireNonNullElseGet(errorResponse, ErrorResponseBO::new);
        } catch (RuntimeException ex) {
            LOGGER.warn("[RootApiRequest] Falha ao desserializar ErrorResponseBO; usando resposta alternativa.", ex);
            ErrorResponseBO errorResponse = new ErrorResponseBO();
            errorResponse.setCode(response.statusCode());
            errorResponse.setMessage(response.asPrettyString());
            return errorResponse;
        }
    }
}
