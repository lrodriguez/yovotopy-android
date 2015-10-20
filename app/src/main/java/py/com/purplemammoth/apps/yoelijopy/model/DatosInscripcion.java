package py.com.purplemammoth.apps.yoelijopy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by luisrodriguez on 19/10/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DatosInscripcion {
    private String fecha;
    private Integer talonario;
    private Integer boleta;

    public DatosInscripcion() {
    }

    public DatosInscripcion(String fecha, Integer talonario, Integer boleta) {
        this.fecha = fecha;
        this.talonario = talonario;
        this.boleta = boleta;
    }

    public String getFecha() {
        return fecha;
    }

    @JsonProperty("fecha")
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Integer getTalonario() {
        return talonario;
    }

    @JsonProperty("talonario")
    public void setTalonario(Integer talonario) {
        this.talonario = talonario;
    }

    public Integer getBoleta() {
        return boleta;
    }

    @JsonProperty("boleta")
    public void setBoleta(Integer boleta) {
        this.boleta = boleta;
    }

    @Override
    public String toString() {
        return "DatosInscripcion{" +
                "fecha='" + fecha + '\'' +
                ", talonario=" + talonario +
                ", boleta=" + boleta +
                '}';
    }
}
