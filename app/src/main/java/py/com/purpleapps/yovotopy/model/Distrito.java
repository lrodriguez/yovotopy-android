package py.com.purpleapps.yovotopy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by luisrodriguez on 8/11/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Distrito {
    private String nombre;
    private String departamento;
    private Double latitud;
    private Double longitud;

    public Distrito() {
    }

    public Distrito(String nombre, String departamento, Double latitud, Double longitud) {
        this.nombre = nombre;
        this.departamento = departamento;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getNombre() {
        return nombre;
    }

    @JsonProperty("nombre")
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDepartamento() {
        return departamento;
    }

    @JsonProperty("departamento")
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public Double getLatitud() {
        return latitud;
    }

    @JsonProperty("latitud")
    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    @JsonProperty("longitud")
    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    @Override
    public String toString() {
        return "Distrito{" +
                "nombre='" + nombre + '\'' +
                ", departamento='" + departamento + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                '}';
    }
}
