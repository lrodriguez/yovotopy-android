package py.com.purplemammoth.apps.yoelijopy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by luisrodriguez on 19/10/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DatosVotacion {
    private Integer mesa;
    private Integer orden;
    private String tipoVotoAccesible;
    public DatosVotacion() {
    }

    public DatosVotacion(Integer mesa, Integer orden, String tipoVotoAccesible) {
        this.mesa = mesa;
        this.orden = orden;
        this.tipoVotoAccesible = tipoVotoAccesible;
    }

    public Integer getMesa() {
        return mesa;
    }

    @JsonProperty("mesa")
    public void setMesa(Integer mesa) {
        this.mesa = mesa;
    }

    public Integer getOrden() {
        return orden;
    }

    @JsonProperty("orden")
    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getTipoVotoAccesible() {
        return tipoVotoAccesible;
    }

    @JsonProperty("tipoVotoAccesible")
    public void setTipoVotoAccesible(String tipoVotoAccesible) {
        this.tipoVotoAccesible = tipoVotoAccesible;
    }

    @Override
    public String toString() {
        return "DatosVotacion{" +
                "mesa=" + mesa +
                ", orden=" + orden +
                ", tipoVotoAccesible='" + tipoVotoAccesible + '\'' +
                '}';
    }

    public enum TipoVoto {
        VOTO_CASA("Voto en casa"),
        VOTO_MESA_1("Voto en mesa 1");

        private String descripcion;

        TipoVoto(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }
}
