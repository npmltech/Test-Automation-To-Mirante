package br.com.automation.project.api.domain.starwar.root;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class RootApiResponseBO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final RootApiModelBO model;
    private final int httpStatusCode;

    public RootApiResponseBO(RootApiModelBO model, int httpStatusCode) {
        this.model = Objects.requireNonNull(model, "O modelo da API Root não pode ser nulo.");
        this.httpStatusCode = httpStatusCode;
    }

    public RootApiModelBO getModel() {
        return this.model;
    }

    public int getHttpStatusCode() {
        return this.httpStatusCode;
    }

    /** Retorna todas as URLs de serviço expostas pela raiz da SWAPI. */
    // @formatter:off
    public List<String> getServices() {
        return List.of(
            this.model.getFilms(),
            this.model.getPeople(),
            this.model.getPlanets(),
            this.model.getSpecies(),
            this.model.getStarships(),
            this.model.getVehicles());
        //
    }
    // @formatter:on
}
