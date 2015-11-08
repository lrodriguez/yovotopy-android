package py.com.purpleapps.yovotopy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by luisrodriguez on 4/11/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DatosDenuncia {
    private String titulo;
    private String descripcion;
    private String fecha;
    private String hora;
    private List<String> categorias;
    private String latitud;
    private String longitud;
    private String lugar;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private List<String> links;
    private List<FotoDenuncia> fotos;

    public DatosDenuncia() {
    }

    public DatosDenuncia(String titulo, String descripcion, String fecha, String hora,
                         List<String> categorias, String latitud, String longitud, String lugar,
                         String nombre, String apellido, String email, String telefono,
                         List<String> links, List<FotoDenuncia> fotos) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.categorias = categorias;
        this.latitud = latitud;
        this.longitud = longitud;
        this.lugar = lugar;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.links = links;
        this.fotos = fotos;
    }

    public String getTitulo() {
        return titulo;
    }

    @JsonProperty("titulo")
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @JsonProperty("descripcion")
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    @JsonProperty("fecha")
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    @JsonProperty("hora")
    public void setHora(String hora) {
        this.hora = hora;
    }

    public List<String> getCategorias() {
        return categorias;
    }

    @JsonProperty("categorias")
    public void setCategorias(List<String> categorias) {
        this.categorias = categorias;
    }

    public String getLatitud() {
        return latitud;
    }

    @JsonProperty("latitud")
    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    @JsonProperty("longitud")
    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getLugar() {
        return lugar;
    }

    @JsonProperty("lugar")
    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getNombre() {
        return nombre;
    }

    @JsonProperty("nombre")
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    @JsonProperty("apellido")
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    @JsonProperty("telefono")
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<String> getLinks() {
        return links;
    }

    @JsonProperty("links")
    public void setLinks(List<String> links) {
        this.links = links;
    }

    public List<FotoDenuncia> getFotos() {
        return fotos;
    }

    @JsonProperty("fotos")
    public void setFotos(List<FotoDenuncia> fotos) {
        this.fotos = fotos;
    }

    @Override
    public String toString() {
        return "DatosDenuncia{" +
                "titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fecha='" + fecha + '\'' +
                ", hora='" + hora + '\'' +
                ", categorias=" + categorias +
                ", latitud='" + latitud + '\'' +
                ", longitud='" + longitud + '\'' +
                ", lugar='" + lugar + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", links=" + links +
                ", fotos=" + fotos +
                '}';
    }
}
