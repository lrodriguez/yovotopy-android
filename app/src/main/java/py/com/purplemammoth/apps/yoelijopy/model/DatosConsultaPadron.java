package py.com.purplemammoth.apps.yoelijopy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by luisrodriguez on 17/10/15.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class DatosConsultaPadron {
    private DatosPersonales datosPersonales;
    private LocalVotacion localVotacion;
    private DatosVotacion datosVotacion;
    private Boolean puedeVotar;
    private String motivo;

    public DatosConsultaPadron() {
    }

    public DatosConsultaPadron(DatosPersonales datosPersonales, LocalVotacion localVotacion,
                               DatosVotacion datosVotacion, Boolean puedeVotar, String motivo) {
        this.datosPersonales = datosPersonales;
        this.localVotacion = localVotacion;
        this.datosVotacion = datosVotacion;
        this.puedeVotar = puedeVotar;
        this.motivo = motivo;
    }

    public DatosPersonales getDatosPersonales() {
        return datosPersonales;
    }

    @JsonProperty("datosPersonales")
    public void setDatosPersonales(DatosPersonales datosPersonales) {
        this.datosPersonales = datosPersonales;
    }

    public LocalVotacion getLocalVotacion() {
        return localVotacion;
    }

    @JsonProperty("localVotacion")
    public void setLocalVotacion(LocalVotacion localVotacion) {
        this.localVotacion = localVotacion;
    }

    public DatosVotacion getDatosVotacion() {
        return datosVotacion;
    }

    @JsonProperty("datosVotacion")
    public void setDatosVotacion(DatosVotacion datosVotacion) {
        this.datosVotacion = datosVotacion;
    }

    public Boolean getPuedeVotar() {
        return puedeVotar;
    }

    @JsonProperty("puedeVotar")
    public void setPuedeVotar(Boolean puedeVotar) {
        this.puedeVotar = puedeVotar;
    }

    public String getMotivo() {
        return motivo;
    }

    @JsonProperty("motivo")
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    @Override
    public String toString() {
        return "DatosConsultaPadron{" +
                "datosPersonales=" + datosPersonales +
                ", localVotacion=" + localVotacion +
                ", datosVotacion=" + datosVotacion +
                ", puedeVotar=" + puedeVotar +
                ", motivo='" + motivo + '\'' +
                '}';
    }
}
