package br.com.automation.project.api.domain.dog.request;

import br.com.automation.project.api.domain.dog.error.DogApiException;
import br.com.automation.project.api.domain.dog.error.DogErrorBO;
import br.com.automation.project.api.domain.dog.model.DogBreedImagesBO;
import br.com.automation.project.api.domain.dog.model.DogBreedsListBO;
import br.com.automation.project.api.domain.dog.model.DogRandomImageBO;
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

public final class DogApiRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DogApiRequest.class);
    private static final String DOG_API_URL_KEY = "dog_api_url";

    public DogBreedsListBO requestAllBreeds() throws DogApiException {
        LOGGER.info("[DogApiRequest] GET /breeds/list/all");
        RequestSpecification request = buildRequestSpecification();
        Response response = request.contentType(ContentType.JSON).get("/breeds/list/all");
        response.then().log().all();

        int statusCode = response.statusCode();
        ResponseClass.setHttpStatusCode(statusCode);
        ResponseClass.setResponse(response.asPrettyString());
        LOGGER.info("[DogApiRequest] GET /breeds/list/all - status={}", statusCode);

        if (statusCode != 200) {
            LOGGER.warn("[DogApiRequest] Resposta inesperada: status={}, body={}", statusCode,
                response.body().asPrettyString());
            throw new DogApiException(statusCode, buildErrorBO(response));
        }

        DogBreedsListBO breedsListBO = response.as(DogBreedsListBO.class);
        LOGGER.debug("[DogApiRequest] Modelo deserializado: {}", breedsListBO);
        return breedsListBO;
    }

    public DogBreedImagesBO requestBreedImages(String breed) throws DogApiException {
        LOGGER.info("[DogApiRequest] GET /breed/{}/images", breed);
        RequestSpecification request = buildRequestSpecification();
        Response response = request.contentType(ContentType.JSON).get(String.format("/breed/%s/images", breed));
        response.then().log().all();

        int statusCode = response.statusCode();
        ResponseClass.setHttpStatusCode(statusCode);
        ResponseClass.setResponse(response.asPrettyString());
        LOGGER.info("[DogApiRequest] GET /breed/{}/images - status={}", breed, statusCode);

        if (statusCode != 200) {
            LOGGER.warn("[DogApiRequest] Resposta inesperada: status={}, body={}", statusCode,
                response.body().asPrettyString());
            throw new DogApiException(statusCode, buildErrorBO(response));
        }

        DogBreedImagesBO imagesBO = response.as(DogBreedImagesBO.class);
        LOGGER.debug("[DogApiRequest] Modelo deserializado: {}", imagesBO);
        return imagesBO;
    }

    public DogRandomImageBO requestRandomImage() throws DogApiException {
        LOGGER.info("[DogApiRequest] GET /breeds/image/random");
        RequestSpecification request = buildRequestSpecification();
        Response response = request.contentType(ContentType.JSON).get("/breeds/image/random");
        response.then().log().all();

        int statusCode = response.statusCode();
        ResponseClass.setHttpStatusCode(statusCode);
        ResponseClass.setResponse(response.asPrettyString());
        LOGGER.info("[DogApiRequest] GET /breeds/image/random - status={}", statusCode);

        if (statusCode != 200) {
            LOGGER.warn("[DogApiRequest] Resposta inesperada: status={}, body={}", statusCode,
                response.body().asPrettyString());
            throw new DogApiException(statusCode, buildErrorBO(response));
        }

        DogRandomImageBO randomImageBO = response.as(DogRandomImageBO.class);
        LOGGER.debug("[DogApiRequest] Modelo deserializado: {}", randomImageBO);
        return randomImageBO;
    }

    private RequestSpecification buildRequestSpecification() {
        String baseUri = ManagerFileUtils.getUrlFromJson(DOG_API_URL_KEY);
        if (baseUri == null || baseUri.isBlank()) {
            throw new IllegalStateException(
                String.format("A URL da Dog API não foi encontrada para a chave '%s'.", DOG_API_URL_KEY));
        }
        LOGGER.debug("[DogApiRequest] Base URI resolvida: {}", baseUri);
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBaseUri(baseUri);
        builder.setRelaxedHTTPSValidation();
        return RestAssured.given().spec(builder.build());
    }

    private DogErrorBO buildErrorBO(Response response) {
        try {
            DogErrorBO errorBO = response.as(DogErrorBO.class);
            return Objects.requireNonNullElseGet(errorBO, DogErrorBO::new);
        } catch (RuntimeException ex) {
            LOGGER.warn("[DogApiRequest] Falha ao desserializar DogErrorBO; usando resposta alternativa.", ex);
            DogErrorBO errorBO = new DogErrorBO();
            errorBO.setCode(response.statusCode());
            errorBO.setMessage(response.asPrettyString());
            return errorBO;
        }
    }
}
