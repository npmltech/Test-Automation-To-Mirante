package br.com.automation.project.api.domain.dog.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DogRandomImageBO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @JsonProperty("message")
    private String imageUrl;
    @JsonProperty("status")
    private String status;

    public String getImageUrl() {
        return this.imageUrl;
    }

    public String getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        return "DogRandomImageBO{" + "imageUrl='" + this.imageUrl + "', status='" + this.status + "'" + '}';
        //
    }
}
