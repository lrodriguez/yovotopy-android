package py.com.purpleapps.yovotopy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by luisrodriguez on 8/11/15.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Candidato {
    private String distrito;
    private String departamento;
    private String partido;
    private String candidatura;
    private String puesto;
    private String nombreApellido;
    private Integer lista;
    private Integer orden;

    public Candidato() {
    }

    public Candidato(String distrito, String departamento, String partido, String candidatura,
                     String puesto, String nombreApellido, Integer lista, Integer orden) {
        this.distrito = distrito;
        this.departamento = departamento;
        this.partido = partido;
        this.candidatura = candidatura;
        this.puesto = puesto;
        this.nombreApellido = nombreApellido;
        this.lista = lista;
        this.orden = orden;
    }

    public String getDistrito() {
        return distrito;
    }

    @JsonProperty("distrito")
    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getDepartamento() {
        return departamento;
    }

    @JsonProperty("departamento")
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getPartido() {
        return partido;
    }

    @JsonProperty("partido")
    public void setPartido(String partido) {
        this.partido = partido;
    }

    public String getCandidatura() {
        return candidatura;
    }

    @JsonProperty("candidatura")
    public void setCandidatura(String candidatura) {
        this.candidatura = candidatura;
    }

    public String getPuesto() {
        return puesto;
    }

    @JsonProperty("puesto")
    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public String getNombreApellido() {
        return nombreApellido;
    }

    @JsonProperty("nombreApellido")
    public void setNombreApellido(String nombreApellido) {
        this.nombreApellido = nombreApellido;
    }

    public Integer getLista() {
        return lista;
    }

    @JsonProperty("lista")
    public void setLista(Integer lista) {
        this.lista = lista;
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
        return "Candidato{" +
                "distrito='" + distrito + '\'' +
                ", departamento='" + departamento + '\'' +
                ", partido='" + partido + '\'' +
                ", candidatura='" + candidatura + '\'' +
                ", puesto='" + puesto + '\'' +
                ", nombreApellido='" + nombreApellido + '\'' +
                ", lista=" + lista +
                ", orden=" + orden +
                '}';
    }
}
