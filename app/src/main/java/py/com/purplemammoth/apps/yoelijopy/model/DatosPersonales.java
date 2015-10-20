package py.com.purplemammoth.apps.yoelijopy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by luisrodriguez on 19/10/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DatosPersonales {
    private String nombre;
    private String sexo;
    private String nacionalidad;

    public DatosPersonales() {
    }

    public DatosPersonales(String nombre, String sexo, String nacionalidad) {
        this.nombre = nombre;
        this.sexo = sexo;
        this.nacionalidad = nacionalidad;
    }

    public String getNombre() {
        return nombre;
    }

    @JsonProperty("nombre")
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSexo() {
        return sexo;
    }

    @JsonProperty("sexo")
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    @JsonProperty("nacionalidad")
    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    @Override
    public String toString() {
        return "DatosPersonales{" +
                "nombre='" + nombre + '\'' +
                ", sexo='" + sexo + '\'' +
                ", nacionalidad='" + nacionalidad + '\'' +
                '}';
    }
}
