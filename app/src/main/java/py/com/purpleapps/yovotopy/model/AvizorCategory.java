package py.com.purpleapps.yovotopy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by luisrodriguez on 4/11/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AvizorCategory {
    private String id;
    private String nombre;
    private String descripcion;
    private String posicion;

    public AvizorCategory() {
    }

    public AvizorCategory(String id, String nombre, String descripcion, String posicion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.posicion = posicion;
    }

    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    @JsonProperty("title")
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @JsonProperty("description")
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPosicion() {
        return posicion;
    }

    @JsonProperty("position")
    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    @Override
    public String toString() {
        return "CategoriaDenuncia{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", posicion=" + posicion +
                '}';
    }
}
