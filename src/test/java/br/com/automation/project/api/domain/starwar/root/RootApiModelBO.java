package br.com.automation.project.api.domain.starwar.root;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RootApiModelBO implements Serializable {

    private static final long serialVersionUID = 1L;
    @JsonProperty("films")
    private String films;
    @JsonProperty("people")
    private String people;
    @JsonProperty("planets")
    private String planets;
    @JsonProperty("species")
    private String species;
    @JsonProperty("starships")
    private String starships;
    @JsonProperty("vehicles")
    private String vehicles;

    public String getFilms() {
        return this.films;
    }

    public String getPeople() {
        return this.people;
    }

    public String getPlanets() {
        return this.planets;
    }

    public String getSpecies() {
        return this.species;
    }

    public String getStarships() {
        return this.starships;
    }

    public String getVehicles() {
        return this.vehicles;
    }

    // @formatter:off
    @Override
    public String toString() {
        return "Root{" + "films='" + this.films + '\'' + ", " +
            "people='" + this.people + '\'' + ", " +
            "planets='" + this.planets + '\'' + ", " +
            "species='" + this.species + '\'' + ", " +
            "starships='" + this.starships + '\'' + ", " +
            "vehicles='" + this.vehicles + '\'' + '}';
        //
    }
    // @formatter:on
}
