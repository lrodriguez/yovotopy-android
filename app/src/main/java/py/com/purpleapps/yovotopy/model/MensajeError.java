package py.com.purpleapps.yovotopy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by luisrodriguez on 19/10/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MensajeError {
    private String mensaje;
    private String mensajeUsuario;

    public MensajeError() {
    }

    public MensajeError(String mensaje, String mensajeUsuario) {
        this.mensaje = mensaje;
        this.mensajeUsuario = mensajeUsuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    @JsonProperty("mensaje")
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensajeUsuario() {
        return mensajeUsuario;
    }

    @JsonProperty("mensajeUsuario")
    public void setMensajeUsuario(String mensajeUsuario) {
        this.mensajeUsuario = mensajeUsuario;
    }

    @Override
    public String toString() {
        return "MensajeError{" +
                "mensaje='" + mensaje + '\'' +
                ", mensajeUsuario='" + mensajeUsuario + '\'' +
                '}';
    }
}
