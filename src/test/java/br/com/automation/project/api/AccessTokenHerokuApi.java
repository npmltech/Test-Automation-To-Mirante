package br.com.automation.project.api;

import java.util.HashMap;
import java.util.Map;

import br.com.automation.project.api.model.HeaderClass;
import br.com.automation.project.utils.ManagerFileUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessTokenHerokuApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessTokenHerokuApi.class);

    public void getAccessToken() {
        LOGGER.info("[AccessTokenHerokuApi] POST /api/auth/signin com credenciais do arquivo JSON.");
        Map<String, Object> credencials = new HashMap<>();
        credencials.putAll(ManagerFileUtils.getParamsFromJson("src/test/resources/json-repo/password.json"));
        RestAssured.baseURI = ManagerFileUtils.getUrlFromJson("my_heroku_api_url");
        RequestSpecification request = RestAssured.given().header("Accept", ContentType.JSON.getAcceptHeader())
            .contentType(ContentType.JSON).body(credencials);
        Response response = request.when().post("api/auth/signin");
        ValidatableResponse validatableResponse = response.then().statusCode(200);
        validatableResponse.log().all();
        LOGGER.info("[AccessTokenHerokuApi] Token gerado com sucesso. status={}", response.getStatusCode());
        HeaderClass.setToken(response.path("accessToken").toString());
    }

    public void getAccessToken(String username, String password) {
        LOGGER.info("[AccessTokenHerokuApi] POST /api/auth/signin - usuário={}", username);
        Map<String, Object> credencials = new HashMap<>();
        credencials.put("username", username);
        credencials.put("password", password);
        RestAssured.baseURI = ManagerFileUtils.getUrlFromJson("my_heroku_api_url");
        RequestSpecification request = RestAssured.given().header("Accept", ContentType.JSON.getAcceptHeader())
            .contentType(ContentType.JSON).body(credencials);
        Response response = request.when().post("api/auth/signin");
        ValidatableResponse validatableResponse = response.then().statusCode(200);
        validatableResponse.log().all();
        LOGGER.info("[AccessTokenHerokuApi] Token gerado com sucesso. status={}", response.getStatusCode());
        HeaderClass.setToken(response.path("accessToken").toString());
    }
}
