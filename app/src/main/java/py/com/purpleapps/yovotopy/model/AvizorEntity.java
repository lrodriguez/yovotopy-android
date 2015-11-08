package py.com.purpleapps.yovotopy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by luisrodriguez on 4/11/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AvizorEntity {
    private String dominio;
    private List<AvizorCategoryWrapper> categorias;

    public AvizorEntity() {
    }

    public AvizorEntity(String dominio, List<AvizorCategoryWrapper> categorias) {
        this.dominio = dominio;
        this.categorias = categorias;
    }

    public String getDominio() {
        return dominio;
    }

    @JsonProperty("domain")
    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    public List<AvizorCategoryWrapper> getCategorias() {
        return categorias;
    }

    @JsonProperty("categories")
    public void setCategorias(List<AvizorCategoryWrapper> categorias) {
        this.categorias = categorias;
    }

    @Override
    public String toString() {
        return "AvizorEntity{" +
                "dominio='" + dominio + '\'' +
                ", categorias=" + categorias +
                '}';
    }
}
