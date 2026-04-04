package br.com.automation.project.api.domain.starwar.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponseBO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @JsonProperty("code")
    private Integer code;
    @JsonProperty("message")
    private String message;

    // noinspection unused
    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorResponseBO{" + "code=" + getCode() + ", message='" + getMessage() + "'" + '}';
    }
}
