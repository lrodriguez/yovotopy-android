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

    public DatosVotacion() {
    }

    public DatosVotacion(Integer mesa, Integer orden) {
        this.mesa = mesa;
        this.orden = orden;
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

    @Override
    public String toString() {
        return "DatosVotacion{" +
                "mesa=" + mesa +
                ", orden=" + orden +
                '}';
    }
}
