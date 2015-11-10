package py.com.purpleapps.yovotopy.util;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Map;

import py.com.purpleapps.yovotopy.YoVotoPyApplication;

/**
 * Created by mduarte on 7/11/15.
 */
public class Tracking {
    private static String TAG = "Tracking";

    public static void track(Application app, Pantalla pantalla, Accion accion) {
        track(app, pantalla.getNombre(), accion.getNombre(), null, null);
    }

    public static void track(Application app, Pantalla pantalla, String accion) {
        track(app, pantalla.getNombre(), accion, null, null);
    }

    public static void track(Application app, Pantalla pantalla, Accion accion, String label) {
        track(app, pantalla.getNombre(), accion.getNombre(), label, null);
    }

    public static void track(Application app, String category, String action, String label, Long value) {
        Tracker t = ((YoVotoPyApplication) app).getTracker();
        t.enableAdvertisingIdCollection(true);

        HitBuilders.EventBuilder event = new HitBuilders.EventBuilder();

        if (category != null) {
            event.setCategory(category);
        }

        if (action != null) {
            event.setAction(action);
        }

        if (label != null) {
            event.setLabel(label);
        }

        if (value != null) {
            event.setValue(value);
        }

        Map<String, String> params = event.build();

        t.send(params);

        Log.d(TAG, "track: " + params);

        //Toast.makeText(app, params.toString(), Toast.LENGTH_SHORT).show();
    }

    public enum Accion {
        VER_PANTALLA("Ver pantalla"),
        CONSULTAR_PADRON("Consultar padrón"),
        GUARDAR_PREDETERMINADO("Guardar predeterminado"),
        VER_MAPA("Ver mapa"),
        ENVIAR_DENUNCIA("Enviar denuncia"),
        ANHADIR_LINKS("Añadir links"),
        ANHADIR_FOTO("Añadir foto"),
        VER_DEPARTAMENTO("Ver departamento"),
        VER_DISTRITO("Ver distrito"),
        VER_PARTIDO("Ver partido"),
        VER_CANDIDATURA("Ver candidatura"),
        VER_CANDIDATO("Ver candidato"),
        VER_ACERCA_DE("Ver acerca de"),
        VER_TWITTER("Ver twitter"),
        VER_FACEBOOK("Ver facebook"),
        ENVIAR_CORREO("Enviar correo"),
        BORRAR_PREDETERMINADO("Borrar datos predeterminados");

        private String nombre;

        Accion(String nombre) {
            this.nombre = nombre;
        }

        public String getNombre() {
            return nombre;
        }
    }

    public enum Pantalla {
        CONSULTA_PADRON("Consulta del padrón"),
        PERFIL("Perfil"),
        DENUNCIAS("Denuncias"),
        EXPLORAR("Explorar"),
        EXPLORAR_JERARQUIAS("Explorar por jerarquias"),
        INICIO("Inicio"),
        AJUSTES("Ajustes"),
        ACERCA_DE("Acerca de"),;

        private String nombre;

        Pantalla(String nombre) {
            this.nombre = nombre;
        }

        public String getNombre() {
            return nombre;
        }
    }
}
