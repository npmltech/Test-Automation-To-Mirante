package br.com.automation.project.api.domain.dog.error;

import java.io.Serial;

public class DogApiException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    private final int httpStatusCode;
    private final DogErrorBO errorResponse;

    public DogApiException(int httpStatusCode, DogErrorBO errorResponse) {
        super(String.format("Falha na requisição Dog API. HTTP status: %s.", httpStatusCode));
        this.httpStatusCode = httpStatusCode;
        this.errorResponse = errorResponse;
    }

    // noinspection unused
    public int getHttpStatusCode() {
        return this.httpStatusCode;
    }

    // noinspection unused
    public DogErrorBO getErrorResponse() {
        return this.errorResponse;
    }
}
