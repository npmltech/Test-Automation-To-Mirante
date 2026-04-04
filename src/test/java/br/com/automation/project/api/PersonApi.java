package br.com.automation.project.api;

import br.com.automation.project.api.model.HeaderClass;
import br.com.automation.project.api.model.ResponseClass;
import br.com.automation.project.utils.ManagerFileUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class PersonApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonApi.class);

    /* INSERIR PESSOA */
    public void insertPerson(String name, String age, String comments) {
        LOGGER.info("[PersonApi] POST /api/usr/person - name={}, age={}", name, age);
        SortedMap<String, String> contentInsertion = new TreeMap<>();
        contentInsertion.put("name", name);
        contentInsertion.put("age", age);
        contentInsertion.put("comments", comments);
        RestAssured.baseURI = ManagerFileUtils.getUrlFromJson("My_Heroku_API_Url");
        RequestSpecification request = RestAssured.given().accept("application/json")
            .header("Authorization", HeaderClass.getToken()).contentType(ContentType.JSON).body(contentInsertion);
        Response response = request.when().post("api/usr/person");
        ResponseClass.setHttpStatusCode(response.getStatusCode());
        LOGGER.info("[PersonApi] POST /api/usr/person - status={}", response.getStatusCode());
        ValidatableResponse validatableResponse = response.then().statusCode(200);
        validatableResponse.log().body();
    }

    /* ATUALIZAR PESSOA */
    public void updatePersonUsingId(String id, String name, String age, String comment) {
        LOGGER.info("[PersonApi] PUT /api/usr/person/{} - name={}, age={}", id, name, age);
        Map<String, Object> contentUpdate = new HashMap<>();
        contentUpdate.put("id", id);
        contentUpdate.put("name", name);
        contentUpdate.put("age", age);
        contentUpdate.put("comment", comment);
        RestAssured.baseURI = ManagerFileUtils.getUrlFromJson("My_Heroku_API_Url");
        RequestSpecification request = RestAssured.given().header("Authorization", HeaderClass.getToken())
            .contentType(ContentType.JSON).body(contentUpdate);
        Response response = request.when().put(String.format("api/usr/person/%s", id));
        ResponseClass.setHttpStatusCode(response.getStatusCode());
        LOGGER.info("[PersonApi] PUT /api/usr/person/{} - status={}", id, response.getStatusCode());
        ValidatableResponse validatableResponse = response.then().statusCode(200);
        validatableResponse.log().body();
    }

    /* CONSULTAR PESSOAS */
    public void getPersons() {
        LOGGER.info("[PersonApi] GET /api/usr/persons");
        RestAssured.baseURI = ManagerFileUtils.getUrlFromJson("My_Heroku_API_Url");
        RequestSpecification httpRequest = RestAssured.given().header("Authorization", HeaderClass.getToken())
            .contentType(ContentType.JSON);
        Response response = httpRequest.get("api/usr/persons");
        response.then().statusCode(200);
        LOGGER.info("[PersonApi] GET /api/usr/persons - status={}, body={}", response.getStatusCode(),
            response.body().asPrettyString());
        ResponseClass.setResponse(response.body().asString());
    }

    /* CONSULTAR PESSOA POR ID */
    public void getPersonById(String id) {
        LOGGER.info("[PersonApi] GET /api/usr/person/{}", id);
        RestAssured.baseURI = ManagerFileUtils.getUrlFromJson("My_Heroku_API_Url");
        RequestSpecification request = RestAssured.given().header("Authorization", HeaderClass.getToken())
            .contentType(ContentType.JSON);
        Response response = request.get(String.format("api/usr/person/%s", id));
        response.then().statusCode(200);
        LOGGER.info("[PersonApi] GET /api/usr/person/{} - status={}, body={}", id, response.getStatusCode(),
            response.body().asPrettyString());
        ResponseClass.setResponse(response.body().asString());
    }

    /* EXCLUIR PESSOA POR ID */
    public void deletePersonById(String id) {
        LOGGER.info("[PersonApi] DELETE /api/usr/person/{}", id);
        RestAssured.baseURI = ManagerFileUtils.getUrlFromJson("My_Heroku_API_Url");
        RequestSpecification request = RestAssured.given().header("Authorization", HeaderClass.getToken())
            .contentType(ContentType.JSON);
        Response response = request.delete(String.format("api/usr/person/%s", id));
        ResponseClass.setHttpStatusCode(response.getStatusCode());
        LOGGER.info("[PersonApi] DELETE /api/usr/person/{} - status={}", id, response.getStatusCode());
        ValidatableResponse validatableResponse = response.then().statusCode(200);
        validatableResponse.log().body();
    }
}
