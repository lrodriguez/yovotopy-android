package py.com.purplemammoth.apps.yoelijopy.util;

/**
 * Created by luisrodriguez on 22/10/15.
 */
public class AppConstants {
    // Args
    public static final String ARG_ACTIVITY_TITLE = "arg_activity_title";
    public static final String ARG_CEDULA = "arg_cedula";
    public static final String ARG_FECHA_NAC = "arg_fecha_nac";

    // REST client constants
    public static final String SCHEMA = "http://";
    public static final String HOST = "municipales2015-openshiftpublic.rhcloud.com";
    public static final String PORT = ":80";
    public static final String BASE_PATH = "/municipales2015-%s/rest/";
    public static final String VERSION = "v0.2";

    // Urls & paths
    public static final String PATH_CONSULTA_PADRON = "consultas-padron";
    public static final String URL_MAPS_STATIC_IMAGE = "http://maps.google.com/maps/api/staticmap?center" +
            "=%f,%f&zoom=16&size=480x240&markers=color:blue|%f,%f&sensor=false";

    // Url params
    public static final String PARAM_CEDULA = "ci";
    public static final String PARAM_FECHA_NAC = "fechaNacimiento";
    public static final String PARAM_LATITUD = "latitud";
    public static final String PARAM_LONGITUD = "longitud";

    // Test constants
    public static final Double TEST_LATITUDE = -25.325367;
    public static final Double TEST_LONGITUDE = -57.567217;

    // Misc constants
    public static final int MIN_LENGHT = 4;
    public static final String EMPTY_STRING = "";
    public static final String FECHA_FORMAT = "dd/MM/yy";
}
