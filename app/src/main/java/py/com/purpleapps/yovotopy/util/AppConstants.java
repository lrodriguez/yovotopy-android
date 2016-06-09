package py.com.purpleapps.yovotopy.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import butterknife.ButterKnife;
import py.com.purpleapps.yovotopy.R;
import py.com.purpleapps.yovotopy.YoVotoPyApplication;

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
    public static final String ARG_CANDIDATO = "arg_candidato";

    // Preferences
    public static final String PREFS_APP = "AppPreferencesFile";
    public static final String PREFS_PROFILE = "hasProfile";
    public static final String PREFS_CEDULA = "cedula";
    public static final String PREFS_FECHA_NAC = "fechaNacimiento";

    // REST client constants
    public static final String SCHEMA = "http://";
    public static final String HOST_DENUNCIAS = "-purpleappspy.rhcloud.com";
    public static final String HOST = "-yovotopy.rhcloud.com";
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
    public static final String AVIZOR_HOME_PAGE = "https://www.elavizor.org.py";
    public static final String AVIZOR_DENUNCIAS_PAGE = "https://www.elavizor.org.py/reports";
    public static final String PA_FACEBOOK_PROFILE = "http://www.facebook.com/purpleappspy";
    public static final String PA_TWITTER_PROFILE = "http://www.twitter.com/purpleappspy";
    public static final String PA_MAIL = "contact.purpleapps@gmail.com";

    public static void showAboutDialog(final Context context) {
        View aboutView = LayoutInflater.from(context).inflate(R.layout.dialog_about, null);
        Button facebook = ButterKnife.findById(aboutView, R.id.facebook_button);
        Button twitter = ButterKnife.findById(aboutView, R.id.twitter_button);
        Button mail = ButterKnife.findById(aboutView, R.id.email_button);

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(PA_FACEBOOK_PROFILE));
                context.startActivity(i);

                Tracking.track((YoVotoPyApplication) context.getApplicationContext(),
                        Tracking.Pantalla.ACERCA_DE,
                        Tracking.Accion.VER_FACEBOOK);
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(PA_TWITTER_PROFILE));
                context.startActivity(i);

                Tracking.track((YoVotoPyApplication) context.getApplicationContext(),
                        Tracking.Pantalla.ACERCA_DE,
                        Tracking.Accion.VER_TWITTER);
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String[] recipients = {PA_MAIL};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                intent.putExtra(Intent.EXTRA_CC, "");
                intent.setType("text/html");
                context.startActivity(Intent.createChooser(intent, "Enviar correo"));

                Tracking.track((YoVotoPyApplication) context.getApplicationContext(),
                        Tracking.Pantalla.ACERCA_DE,
                        Tracking.Accion.ENVIAR_CORREO);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppTheme_Dialog)
                .setView(aboutView);
        builder.show();
    }

    public enum Instance {
        INSTANCE_01(1, "yovoto00"),
        INSTANCE_02(2, "yovoto01"),
        INSTANCE_03(3, "yovoto02"),
        INSTANCE_04(4, "yovoto03"),
        INSTANCE_05(5, "yovoto04"),
        INSTANCE_06(6, "yovoto05"),
        INSTANCE_07(6, "yovoto06");

        private int id;
        private String nombre;

        Instance(int id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public int getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }
    }
}
