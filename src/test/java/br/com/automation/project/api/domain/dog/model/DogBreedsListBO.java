package br.com.automation.project.api.domain.dog.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DogBreedsListBO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @JsonProperty("message")
    private Map<String, List<String>> breeds;
    @JsonProperty("status")
    private String status;

    public Map<String, List<String>> getBreeds() {
        return this.breeds;
    }

    public String getStatus() {
        return this.status;
    }

    // @formatter:off
    @Override
    public String toString() {
        return "DogBreedsListBO{" + "totalBreeds=" + (this.breeds != null ? this.breeds.size() : 0) + ", status='" + this.status + "'" + '}';
        //
    }
}
