package py.com.purpleapps.yovotopy.model;

/**
 * Created by luisrodriguez on 30/10/15.
 */
public enum TipoListado {
    DEPARTAMENTO(1, Departamento.class),
    DISTRITO(2, Distrito.class),
    PARTIDO(3, String.class),
    CANDIDATURA(4, String.class),
    CANDIDATO(5, Candidato.class);

    private int id;
    private Class clazz;

    TipoListado(int id, Class clazz) {
        this.id = id;
        this.clazz = clazz;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
