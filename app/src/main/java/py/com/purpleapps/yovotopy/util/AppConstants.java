package py.com.purpleapps.yovotopy.util;

/**
 * Created by luisrodriguez on 22/10/15.
 */
public class AppConstants {
    // Args
    public static final String ARG_ACTIVITY_TITLE = "arg_activity_title";
    public static final String ARG_CEDULA = "arg_cedula";
    public static final String ARG_FECHA_NAC = "arg_fecha_nac";
    public static final String ARG_PROFILE = "arg_profile";
    public static final String ARG_COLUMN_COUNT = "column-count";
    public static final String ARG_TIPO_LISTADO = "arg_listado";
    public static final String ARG_ITEM_ID = "arg_item_id";
    public static final String ARG_ITEM_FILTER = "arg_item_filter";

    // Preferences
    public static final String PREFS_APP = "AppPreferencesFile";
    public static final String PREFS_PROFILE = "hasProfile";
    public static final String PREFS_CEDULA = "cedula";
    public static final String PREFS_FECHA_NAC = "fechaNacimiento";

    // REST client constants
    public static final String SCHEMA = "http://";
    public static final String INSTANCE_01 = "yovoto00";
    public static final String INSTANCE_02 = "yovoto01";
    public static final String INSTANCE_03 = "yovoto02";
    public static final String INSTANCE_04 = "yovoto03";
    public static final String INSTANCE_05 = "yovoto04";
    public static final String INSTANCE_06 = "yovoto05";

    public static final String HOST_DENUNCIAS = "-purpleappspy.rhcloud.com";
    public static final String HOST = "-openshiftpublic.rhcloud.com";
    public static final String HOST_AVIZOR = "www.elavizor.org.py";
    public static final String PORT = ":80";
    public static final String BASE_PATH = "/municipales2015-%s/rest/";
    public static final String BASE_PATH_AVIZOR = "/api";
    public static final String VERSION = "v0.3";

    // Urls & paths
    public static final String PATH_CONSULTA_PADRON = "consultas-padron";
    public static final String PATH_DENUNCIAS = "denuncias";
    public static final String PATH_DEPARTAMENTOS = "departamentos";
    public static final String PATH_DISTRITOS = "distritos";
    public static final String PATH_PARTIDOS = "partidos";
    public static final String PATH_CANDIDATURAS = "candidaturas";
    public static final String PATH_CANDIDATOS = "candidatos";
    public static final String URL_MAPS_STATIC_IMAGE = "http://maps.google.com/maps/api/staticmap?center" +
            "=%f,%f&zoom=16&size=480x240&markers=color:blue|%f,%f&sensor=false";

    // Url params
    public static final String PARAM_CEDULA = "ci";
    public static final String PARAM_FECHA_NAC = "fechaNacimiento";
    public static final String PARAM_LATITUD = "latitud";
    public static final String PARAM_LONGITUD = "longitud";
    public static final String PARAM_LAT = "lat";
    public static final String PARAM_LONG = "lng";
    public static final String PARAM_ORDER_BY = "orderBy";
    public static final String PARAM_EXCLUDE = "exclude";
    public static final String PARAM_LIMIT = "limit";
    public static final String PARAM_OFFSET = "offset";
    public static final String PARAM_DEPARTAMENTO = "departamento";
    public static final String PARAM_DISTRITO = "distrito";
    public static final String PARAM_PARTIDO = "partido";
    public static final String PARAM_PUESTO = "puesto";
    public static final String PARAM_CANDIDATURA = "candidatura";
    public static final String PARAM_ORDEN = "orden";
    public static final String PARAM_LISTA = "lista";
    public static final String PARAM_NOMBRE_CANDIDATO = "nombreApellido";
    public static final String PARAM_TASK_AVIZOR = "task";

    // Test constants
    public static final Double TEST_LATITUDE = -25.325367;
    public static final Double TEST_LONGITUDE = -57.567217;
    public static final String AVIZOR_CATEGORIAS = "categories";

    // Misc constants
    public static final int OPENSHIFT_HOST = 0;
    public static final int AVIZOR_HOST = 1;
    public static final int OPENSHIFT_DENUNCIAS_HOST = 2;
    public static final int MIN_LENGHT = 4;
    public static final String EMPTY_STRING = "";
    public static final String FECHA_FORMAT = "dd/MM/yy";
    public static final int INITIAL_OFFSET = 0;
    public static final int DEFAULT_LIMIT = 10;
}
