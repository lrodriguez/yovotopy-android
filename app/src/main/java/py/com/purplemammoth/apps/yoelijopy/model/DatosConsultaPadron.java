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
    private DatosInscripcion datosInscripcion;
    private DatosConsultaPadron duplicado;
    private DatosConsultaPadron deshabilitado;

    public DatosConsultaPadron() {
    }

    public DatosConsultaPadron(DatosPersonales datosPersonales, LocalVotacion localVotacion,
                               DatosVotacion datosVotacion, DatosInscripcion datosInscripcion,
                               DatosConsultaPadron duplicado, DatosConsultaPadron deshabilitado) {
        this.datosPersonales = datosPersonales;
        this.localVotacion = localVotacion;
        this.datosVotacion = datosVotacion;
        this.datosInscripcion = datosInscripcion;
        this.duplicado = duplicado;
        this.deshabilitado = deshabilitado;
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

    public DatosInscripcion getDatosInscripcion() {
        return datosInscripcion;
    }

    @JsonProperty("datosInscripcion")
    public void setDatosInscripcion(DatosInscripcion datosInscripcion) {
        this.datosInscripcion = datosInscripcion;
    }

    public DatosConsultaPadron getDuplicado() {
        return duplicado;
    }

    @JsonProperty("duplicado")
    public void setDuplicado(DatosConsultaPadron duplicado) {
        this.duplicado = duplicado;
    }

    public DatosConsultaPadron getDeshabilitado() {
        return deshabilitado;
    }

    @JsonProperty("deshabilitado")
    public void setDeshabilitado(DatosConsultaPadron deshabilitado) {
        this.deshabilitado = deshabilitado;
    }

    @Override
    public String toString() {
        return "DatosConsultaPadron{" +
                "datosPersonales=" + datosPersonales +
                ", localVotacion=" + localVotacion +
                ", datosVotacion=" + datosVotacion +
                ", datosInscripcion=" + datosInscripcion +
                ", duplicado=" + duplicado +
                ", deshabilitado=" + deshabilitado +
                '}';
    }
}
