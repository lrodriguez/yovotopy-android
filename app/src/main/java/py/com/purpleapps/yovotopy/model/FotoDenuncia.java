package py.com.purpleapps.yovotopy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by luisrodriguez on 4/11/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FotoDenuncia {
    private String foto;
    private String mediaType;
    private String nombre;

    public FotoDenuncia() {
    }

    public FotoDenuncia(String foto, String mediaType, String nombre) {
        this.foto = foto;
        this.mediaType = mediaType;
        this.nombre = nombre;
    }

    public String getFoto() {
        return foto;
    }

    @JsonProperty("foto")
    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getMediaType() {
        return mediaType;
    }

    @JsonProperty("mediaType")
    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getNombre() {
        return nombre;
    }

    @JsonProperty("nombre")
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "FotoDenuncia{" +
                "foto='" + foto + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
