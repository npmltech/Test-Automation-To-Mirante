package br.com.automation.project.api.domain.starwar.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serial;

public class ResponseException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final int httpStatusCode;
    private final ErrorResponseBO errorResponse;

    public ResponseException(int httpStatusCode, ErrorResponseBO errorResponse) {
        super(String.format("Falha na requisição. Código HTTP recebido: %s.", httpStatusCode));
        this.httpStatusCode = httpStatusCode;
        this.errorResponse = errorResponse;
    }

    public int getHttpStatusCode() {
        return this.httpStatusCode;
    }

    public ErrorResponseBO getErrorResponse() {
        return this.errorResponse;
    }

    // noinspection unused
    public ErrorMessageBO getErrorMessage() throws JsonProcessingException {
        return MAPPER.readValue(this.getErrorResponse().getMessage(), ErrorMessageBO.class);
    }
}
