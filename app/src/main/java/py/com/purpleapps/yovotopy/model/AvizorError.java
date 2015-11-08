package py.com.purpleapps.yovotopy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by luisrodriguez on 4/11/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AvizorError {
    private String codigo;
    private String mensaje;

    public AvizorError() {
    }

    public AvizorError(String codigo, String mensaje) {
        this.codigo = codigo;
        this.mensaje = mensaje;
    }

    public String getCodigo() {
        return codigo;
    }

    @JsonProperty("code")
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    @JsonProperty("message")
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public String toString() {
        return "AvizorError{" +
                "codigo='" + codigo + '\'' +
                ", mensaje='" + mensaje + '\'' +
                '}';
    }
}
