package py.com.purpleapps.yovotopy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by luisrodriguez on 9/11/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Partido {
    private String nombre;
    private Integer lista;

    public Partido() {
    }

    public Partido(String nombre, Integer lista) {
        this.nombre = nombre;
        this.lista = lista;
    }

    public String getNombre() {
        return nombre;
    }

    @JsonProperty("nombre")
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getLista() {
        return lista;
    }

    @JsonProperty("lista")
    public void setLista(Integer lista) {
        this.lista = lista;
    }

    @Override
    public String toString() {
        return "Partido{" +
                "nombre='" + nombre + '\'' +
                ", lista=" + lista +
                '}';
    }
}
