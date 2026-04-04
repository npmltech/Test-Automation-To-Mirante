package br.com.automation.project.api.domain.dog.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DogBreedImagesBO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @JsonProperty("message")
    private List<String> images;
    @JsonProperty("status")
    private String status;

    public List<String> getImages() {
        return this.images;
    }

    public String getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        return "DogBreedImagesBO{" + "totalImages=" + (this.images != null ? this.images.size() : 0) + ", status='"
            + this.status + "'" + '}';
        //
    }
}
