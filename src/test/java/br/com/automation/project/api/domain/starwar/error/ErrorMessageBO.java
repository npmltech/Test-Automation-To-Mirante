package br.com.automation.project.api.domain.starwar.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorMessageBO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @JsonProperty("code")
    private String code;
    @JsonProperty("description")
    private String description;

    // noinspection unused
    public String getCode() {
        return this.code;
    }

    // noinspection unused
    public String getDescription() {
        return this.description;
    }
}
