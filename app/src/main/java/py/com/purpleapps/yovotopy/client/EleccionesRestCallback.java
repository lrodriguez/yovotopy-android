package py.com.purpleapps.yovotopy.client;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import hugo.weaving.DebugLog;
import py.com.purpleapps.yovotopy.R;
import py.com.purpleapps.yovotopy.model.AvizorError;
import py.com.purpleapps.yovotopy.model.AvizorResponse;
import py.com.purpleapps.yovotopy.model.Candidato;
import py.com.purpleapps.yovotopy.model.DatosConsultaPadron;
import py.com.purpleapps.yovotopy.model.DatosDenuncia;
import py.com.purpleapps.yovotopy.model.Departamento;
import py.com.purpleapps.yovotopy.model.Distrito;
import py.com.purpleapps.yovotopy.model.Listado;
import py.com.purpleapps.yovotopy.model.MensajeError;
import py.com.purpleapps.yovotopy.model.Partido;
import py.com.purpleapps.yovotopy.util.AppConstants;

/**
 * Created by luisrodriguez on 17/10/15.
 */
public class EleccionesRestCallback {
    public static final String TAG = EleccionesRestCallback.class.getSimpleName();
    private OnResponseReceived mListener;
    private Context context;
    private View loadingView;
    private View parentView;

    public EleccionesRestCallback(OnResponseReceived mListener, Context context, View loadingView,
                                  View parentView) {
        this.mListener = mListener;
        this.context = context;
        this.loadingView = loadingView;
        this.parentView = parentView;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public View getLoadingView() {
        return loadingView;
    }

    public void setLoadingView(View loadingView) {
        this.loadingView = loadingView;
    }

    public View getParentView() {
        return parentView;
    }

    public void setParentView(View parentView) {
        this.parentView = parentView;
    }

    public void getConsultaPadron(String cedula, Double latitud, Double longitud)
            throws JSONException {

        RequestParams requestParams = new RequestParams();
        requestParams.add(AppConstants.PARAM_CEDULA, cedula);
        // TODO update with method params
        requestParams.add(AppConstants.PARAM_LATITUD, Double.toString(latitud));
        requestParams.add(AppConstants.PARAM_LONGITUD, Double.toString(longitud));

        EleccionesRestClient.get(context, AppConstants.OPENSHIFT_HOST,
                AppConstants.PATH_CONSULTA_PADRON, requestParams,
                new BaseJsonHttpResponseHandler<Object>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        ((SwipeRefreshLayout) getLoadingView()).setRefreshing(true);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ((SwipeRefreshLayout) getLoadingView()).setRefreshing(false);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse,
                                          Object response) {

                        Log.i(TAG, "Status Code: " + statusCode + "\nRaw rawJsonResponse: "
                                + rawJsonResponse);
                        Log.i(TAG, response.toString());
                        try {
                            mListener.onSuccessAction((DatosConsultaPadron) response);
                        } catch (ClassCastException e) {
                            Log.e(TAG, "No se pudo realizar el cast del objeto: "
                                    + e.getLocalizedMessage());
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                          String rawJsonData, Object errorResponse) {
                        Log.e(TAG, "Status Code: " + statusCode + "\nRaw JSONData: "
                                + rawJsonData);
                        try {
                            MensajeError mensajeError = (MensajeError) errorResponse;
                            if (statusCode == 404) {
                                Snackbar.make(getParentView(), mensajeError.getMensajeUsuario(),
                                        Snackbar.LENGTH_SHORT).show();
                            } else if (statusCode == 500 || statusCode == 503) {
                                Snackbar.make(getParentView(), "El servicio no está disponible, " +
                                                "intente de nuevo más tarde",
                                        Snackbar.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(getParentView(), "Ocurrió un error, " +
                                                "intente de nuevo más tarde",
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        } catch (ClassCastException e) {
                            Log.e(TAG, "No se pudo realizar el cast del mensaje de error: "
                                    + e.getLocalizedMessage());
                        }

                    }

                    @Override
                    protected Object parseResponse(String rawJsonData, boolean isFailure)
                            throws Throwable {
                        DatosConsultaPadron datosConsultaPadron = null;
                        MensajeError mensajeError = null;
                        ObjectMapper om = new ObjectMapper();
                        JsonParser parser = new JsonFactory().createParser(rawJsonData);

                        if (!isFailure) {
                            datosConsultaPadron = om.readValues(parser, DatosConsultaPadron.class)
                                    .next();
                            return datosConsultaPadron;
                        } else {
                            mensajeError = om.readValues(parser, MensajeError.class).next();
                            return mensajeError;
                        }
                    }
                });
    }

    public void getCategoriasAvizor() throws JSONException {
        RequestParams requestParams = new RequestParams();
        requestParams.add(AppConstants.PARAM_TASK_AVIZOR, AppConstants.AVIZOR_CATEGORIAS);

        EleccionesRestClient.get(getContext(), AppConstants.AVIZOR_HOST, requestParams,
                new BaseJsonHttpResponseHandler<AvizorResponse>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        ((SwipeRefreshLayout) getLoadingView()).setRefreshing(true);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ((SwipeRefreshLayout) getLoadingView()).setRefreshing(false);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse,
                                          AvizorResponse response) {
                        Log.i(TAG, "Status Code: " + statusCode + "\nRaw rawJsonResponse: "
                                + rawJsonResponse);
                        Log.i(TAG, response.toString());
                        try {
                            mListener.onSuccessAction(response.getAvizorEntity().getCategorias());
                        } catch (ClassCastException e) {
                            Log.e(TAG, "No se pudo realizar el cast del objeto: "
                                    + e.getLocalizedMessage());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                          String rawJsonData, AvizorResponse errorResponse) {
                        Log.e(TAG, "Status Code: " + statusCode + "\nRaw JSONData: "
                                + rawJsonData);
                        try {
                            AvizorError mensajeError = errorResponse.getAvizorError();
                            if (statusCode == 404) {
                                Snackbar.make(getParentView(), mensajeError.getMensaje(),
                                        Snackbar.LENGTH_SHORT).show();
                            } else if (statusCode == 500 || statusCode == 503) {
                                Snackbar.make(getParentView(), "El servicio no está disponible, " +
                                                "intente de nuevo más tarde",
                                        Snackbar.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(getParentView(), "Ocurrió un error, " +
                                                "intente de nuevo más tarde",
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        } catch (ClassCastException e) {
                            Log.e(TAG, "No se pudo realizar el cast del mensaje de error: "
                                    + e.getLocalizedMessage());
                        }
                    }

                    @Override
                    protected AvizorResponse parseResponse(String rawJsonData, boolean isFailure)
                            throws Throwable {
                        AvizorResponse avizorResponse = null;

                        ObjectMapper om = new ObjectMapper();
                        JsonParser parser = new JsonFactory().createParser(rawJsonData);
                        avizorResponse = om.readValues(parser, AvizorResponse.class)
                                .next();

                        return avizorResponse;

                    }
                });
    }

    public void postDenuncia(DatosDenuncia datosDenuncia) throws JSONException {
        ObjectMapper om = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = om.writeValueAsString(datosDenuncia);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HttpEntity httpEntity = new StringEntity(jsonString, ContentType.APPLICATION_JSON);

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);

        final TextView textView = (TextView) dialogView.findViewById(R.id.enviando_text);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppTheme_Dialog)
                .setView(dialogView)
                .setCancelable(false);
        final AlertDialog[] dialog = new AlertDialog[1];

        EleccionesRestClient.post(getContext(), AppConstants.OPENSHIFT_DENUNCIAS_HOST,
                AppConstants.PATH_DENUNCIAS, httpEntity, null,
                new BaseJsonHttpResponseHandler<Object>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        dialog[0] = builder.show();
                    }

                    @Override
                    public void onProgress(long bytesWritten, long totalSize) {
                        super.onProgress(bytesWritten, totalSize);
                        textView.setText(String.format("Enviados %d de %d bytes",
                                bytesWritten, totalSize));
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dialog[0].dismiss();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse,
                                          Object response) {
                        Log.e(TAG, "Status Code: " + statusCode + "\nRaw rawJsonResponse: "
                                + rawJsonResponse);
                        if (response != null) {
                            Log.i(TAG, response.toString());
                        }
                        mListener.onSuccessAction();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                          String rawJsonData, Object errorResponse) {
                        Log.e(TAG, "Status Code: " + statusCode + "\nRaw JSONData: "
                                + rawJsonData);
                        try {
                            MensajeError mensajeError = (MensajeError) errorResponse;
                            if (statusCode == 404) {
                                Snackbar.make(getParentView(), mensajeError.getMensajeUsuario(),
                                        Snackbar.LENGTH_SHORT).show();
                            } else if (statusCode == 500 || statusCode == 503) {
                                Snackbar.make(getParentView(), "El servicio no está disponible, " +
                                                "intente de nuevo más tarde",
                                        Snackbar.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(getParentView(), "Ocurrió un error, " +
                                                "intente de nuevo más tarde",
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        } catch (ClassCastException e) {
                            Log.e(TAG, "No se pudo realizar el cast del mensaje de error: "
                                    + e.getLocalizedMessage());
                        }
                    }

                    @Override
                    protected Object parseResponse(String rawJsonData, boolean isFailure)
                            throws Throwable {
                        MensajeError mensajeError = null;
                        ObjectMapper om = new ObjectMapper();
                        JsonParser parser = new JsonFactory().createParser(rawJsonData);

                        if (isFailure) {
                            mensajeError = om.readValues(parser, MensajeError.class).next();
                            return mensajeError;
                        }

                        return null;
                    }
                });
    }

    public void getDepartamentos(Double latitud, Double longitud, String orderBy, String exclude) throws JSONException {
        RequestParams requestParams = new RequestParams();
        requestParams.add(AppConstants.PARAM_LAT, Double.toString(latitud));
        requestParams.add(AppConstants.PARAM_LONG, Double.toString(longitud));
        requestParams.add(AppConstants.PARAM_ORDER_BY, orderBy);
        requestParams.add(AppConstants.PARAM_EXCLUDE, exclude);

        EleccionesRestClient.get(context, AppConstants.OPENSHIFT_HOST,
                AppConstants.PATH_DEPARTAMENTOS, requestParams,
                new BaseJsonHttpResponseHandler<Object>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        ((SwipeRefreshLayout) getLoadingView()).setRefreshing(true);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ((SwipeRefreshLayout) getLoadingView()).setRefreshing(false);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse,
                                          Object response) {

                        Log.i(TAG, "Status Code: " + statusCode + "\nRaw rawJsonResponse: "
                                + rawJsonResponse);
                        Log.i(TAG, response.toString());
                        try {
                            mListener.onSuccessAction((List<Departamento>) response);
                        } catch (ClassCastException e) {
                            Log.e(TAG, "No se pudo realizar el cast del objeto: "
                                    + e.getLocalizedMessage());
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                          String rawJsonData, Object errorResponse) {
                        Log.e(TAG, "Status Code: " + statusCode + "\nRaw JSONData: "
                                + rawJsonData);
                        try {
                            MensajeError mensajeError = (MensajeError) errorResponse;
                            if (statusCode == 404) {
                                Snackbar.make(getParentView(), mensajeError.getMensajeUsuario(),
                                        Snackbar.LENGTH_SHORT).show();
                            } else if (statusCode == 500 || statusCode == 503) {
                                Snackbar.make(getParentView(), "El servicio no está disponible, " +
                                                "intente de nuevo más tarde",
                                        Snackbar.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(getParentView(), "Ocurrió un error, " +
                                                "intente de nuevo más tarde",
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        } catch (ClassCastException e) {
                            Log.e(TAG, "No se pudo realizar el cast del mensaje de error: "
                                    + e.getLocalizedMessage());
                        }

                    }

                    @Override
                    protected Object parseResponse(String rawJsonData, boolean isFailure)
                            throws Throwable {
                        List<Departamento> departamentos = null;
                        MensajeError mensajeError = null;

                        ObjectMapper om = new ObjectMapper();
                        JsonParser parser = new JsonFactory().createParser(rawJsonData);

                        if (!isFailure) {
                            JavaType jt = om.getTypeFactory().constructCollectionType(List.class,
                                    Departamento.class);
                            departamentos = om.readValue(parser, jt);
                            return departamentos;
                        } else {
                            mensajeError = om.readValues(parser, MensajeError.class).next();
                            return mensajeError;
                        }
                    }
                });
    }

    @DebugLog
    public void getDistritos(int offset, int limit, Double latitud, Double longitud, String orderBy,
                             String exclude, String departamento) throws JSONException {

        RequestParams requestParams = new RequestParams();
        requestParams.add(AppConstants.PARAM_OFFSET, String.format("%d", offset));
        requestParams.add(AppConstants.PARAM_LIMIT, String.format("%d", limit));
        requestParams.add(AppConstants.PARAM_LAT, Double.toString(latitud));
        requestParams.add(AppConstants.PARAM_LONG, Double.toString(longitud));
        requestParams.add(AppConstants.PARAM_ORDER_BY, orderBy);
        if (exclude != null && !exclude.isEmpty()) {
            requestParams.add(AppConstants.PARAM_EXCLUDE, exclude);
        }
        if (departamento != null && !departamento.isEmpty()) {
            requestParams.add(AppConstants.PARAM_DEPARTAMENTO, departamento);
        }

        EleccionesRestClient.get(context, AppConstants.OPENSHIFT_HOST,
                AppConstants.PATH_DISTRITOS, requestParams,
                new BaseJsonHttpResponseHandler<Object>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        ((SwipeRefreshLayout) getLoadingView()).setRefreshing(true);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ((SwipeRefreshLayout) getLoadingView()).setRefreshing(false);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse,
                                          Object response) {

                        Log.i(TAG, "Status Code: " + statusCode + "\nRaw rawJsonResponse: "
                                + rawJsonResponse);
                        Log.i(TAG, response.toString());
                        try {
                            mListener.onSuccessAction((Listado<Distrito>) response);
                        } catch (ClassCastException e) {
                            Log.e(TAG, "No se pudo realizar el cast del objeto: "
                                    + e.getLocalizedMessage());
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                          String rawJsonData, Object errorResponse) {
                        Log.e(TAG, "Status Code: " + statusCode + "\nRaw JSONData: "
                                + rawJsonData);
                        try {
                            MensajeError mensajeError = (MensajeError) errorResponse;
                            if (statusCode == 404) {
                                Snackbar.make(getParentView(), mensajeError.getMensajeUsuario(),
                                        Snackbar.LENGTH_SHORT).show();
                            } else if (statusCode == 500 || statusCode == 503) {
                                Snackbar.make(getParentView(), "El servicio no está disponible, " +
                                                "intente de nuevo más tarde",
                                        Snackbar.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(getParentView(), "Ocurrió un error, " +
                                                "intente de nuevo más tarde",
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        } catch (ClassCastException e) {
                            Log.e(TAG, "No se pudo realizar el cast del mensaje de error: "
                                    + e.getLocalizedMessage());
                        }

                    }

                    @Override
                    protected Object parseResponse(String rawJsonData, boolean isFailure)
                            throws Throwable {
                        Listado<Distrito> distritos = null;
                        MensajeError mensajeError = null;

                        ObjectMapper om = new ObjectMapper();
                        JsonParser parser = new JsonFactory().createParser(rawJsonData);

                        if (!isFailure) {
                            JavaType jt = om.getTypeFactory().constructParametrizedType(Listado.class,
                                    Listado.class, Distrito.class);
                            distritos = om.readValue(parser, jt);
                            return distritos;
                        } else {
                            mensajeError = om.readValues(parser, MensajeError.class).next();
                            return mensajeError;
                        }
                    }
                });
    }

    @DebugLog
    public void getPartidos(int offset, int limit, String orderBy,
                            String distrito, String departamento) throws JSONException {

        RequestParams requestParams = new RequestParams();
        requestParams.add(AppConstants.PARAM_OFFSET, String.format("%d", offset));
        requestParams.add(AppConstants.PARAM_LIMIT, String.format("%d", limit));
        requestParams.add(AppConstants.PARAM_ORDER_BY, orderBy);
        requestParams.add(AppConstants.PARAM_DISTRITO, distrito);
        requestParams.add(AppConstants.PARAM_DEPARTAMENTO, departamento);

        EleccionesRestClient.get(context, AppConstants.OPENSHIFT_HOST,
                AppConstants.PATH_PARTIDOS, requestParams,
                new BaseJsonHttpResponseHandler<Object>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        ((SwipeRefreshLayout) getLoadingView()).setRefreshing(true);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ((SwipeRefreshLayout) getLoadingView()).setRefreshing(false);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse,
                                          Object response) {

                        Log.i(TAG, "Status Code: " + statusCode + "\nRaw rawJsonResponse: "
                                + rawJsonResponse);
                        Log.i(TAG, response.toString());
                        try {
                            mListener.onSuccessAction((Listado<Partido>) response);
                        } catch (ClassCastException e) {
                            Log.e(TAG, "No se pudo realizar el cast del objeto: "
                                    + e.getLocalizedMessage());
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                          String rawJsonData, Object errorResponse) {
                        Log.e(TAG, "Status Code: " + statusCode + "\nRaw JSONData: "
                                + rawJsonData);
                        try {
                            MensajeError mensajeError = (MensajeError) errorResponse;
                            if (statusCode == 404) {
                                Snackbar.make(getParentView(), mensajeError.getMensajeUsuario(),
                                        Snackbar.LENGTH_SHORT).show();
                            } else if (statusCode == 500 || statusCode == 503) {
                                Snackbar.make(getParentView(), "El servicio no está disponible, " +
                                                "intente de nuevo más tarde",
                                        Snackbar.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(getParentView(), "Ocurrió un error, " +
                                                "intente de nuevo más tarde",
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        } catch (ClassCastException e) {
                            Log.e(TAG, "No se pudo realizar el cast del mensaje de error: "
                                    + e.getLocalizedMessage());
                        }

                    }

                    @Override
                    protected Object parseResponse(String rawJsonData, boolean isFailure)
                            throws Throwable {
                        Listado<Partido> partidos = null;
                        MensajeError mensajeError = null;

                        ObjectMapper om = new ObjectMapper();
                        JsonParser parser = new JsonFactory().createParser(rawJsonData);

                        if (!isFailure) {
                            JavaType jt = om.getTypeFactory().constructParametrizedType(Listado.class,
                                    Listado.class, Partido.class);
                            partidos = om.readValue(parser, jt);
                            return partidos;
                        } else {
                            mensajeError = om.readValues(parser, MensajeError.class).next();
                            return mensajeError;
                        }
                    }
                });
    }

    @DebugLog
    public void getCandidaturas(String orderBy,
                                String distrito, String departamento, String partido) throws JSONException {

        RequestParams requestParams = new RequestParams();
        requestParams.add(AppConstants.PARAM_ORDER_BY, orderBy);
        requestParams.add(AppConstants.PARAM_DISTRITO, distrito);
        requestParams.add(AppConstants.PARAM_DEPARTAMENTO, departamento);
        requestParams.add(AppConstants.PARAM_PARTIDO, partido);

        EleccionesRestClient.get(context, AppConstants.OPENSHIFT_HOST,
                AppConstants.PATH_CANDIDATURAS, requestParams,
                new BaseJsonHttpResponseHandler<Object>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        ((SwipeRefreshLayout) getLoadingView()).setRefreshing(true);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ((SwipeRefreshLayout) getLoadingView()).setRefreshing(false);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse,
                                          Object response) {

                        Log.i(TAG, "Status Code: " + statusCode + "\nRaw rawJsonResponse: "
                                + rawJsonResponse);
                        Log.i(TAG, response.toString());
                        try {
                            mListener.onSuccessAction((List<String>) response);
                        } catch (ClassCastException e) {
                            Log.e(TAG, "No se pudo realizar el cast del objeto: "
                                    + e.getLocalizedMessage());
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                          String rawJsonData, Object errorResponse) {
                        Log.e(TAG, "Status Code: " + statusCode + "\nRaw JSONData: "
                                + rawJsonData);
                        try {
                            MensajeError mensajeError = (MensajeError) errorResponse;
                            if (statusCode == 404) {
                                Snackbar.make(getParentView(), mensajeError.getMensajeUsuario(),
                                        Snackbar.LENGTH_SHORT).show();
                            } else if (statusCode == 500 || statusCode == 503) {
                                Snackbar.make(getParentView(), "El servicio no está disponible, " +
                                                "intente de nuevo más tarde",
                                        Snackbar.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(getParentView(), "Ocurrió un error, " +
                                                "intente de nuevo más tarde",
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        } catch (ClassCastException e) {
                            Log.e(TAG, "No se pudo realizar el cast del mensaje de error: "
                                    + e.getLocalizedMessage());
                        }

                    }

                    @Override
                    protected Object parseResponse(String rawJsonData, boolean isFailure)
                            throws Throwable {
                        List<String> candidaturas = null;
                        MensajeError mensajeError = null;

                        ObjectMapper om = new ObjectMapper();
                        JsonParser parser = new JsonFactory().createParser(rawJsonData);

                        if (!isFailure) {
                            JavaType jt = om.getTypeFactory().constructCollectionType(List.class,
                                    String.class);
                            candidaturas = om.readValue(parser, jt);
                            return candidaturas;
                        } else {
                            mensajeError = om.readValues(parser, MensajeError.class).next();
                            return mensajeError;
                        }
                    }
                });
    }

    @DebugLog
    public void getCandidatos(int offset, int limit, String orderBy, String nombreApellido,
                              String distrito, String departamento, String partido,
                              String candidatura, String puesto, int lista, int orden) throws JSONException {

        RequestParams requestParams = new RequestParams();
        requestParams.add(AppConstants.PARAM_OFFSET, String.format("%d", offset));
        requestParams.add(AppConstants.PARAM_LIMIT, String.format("%d", limit));
        requestParams.add(AppConstants.PARAM_ORDER_BY, orderBy);
        if (nombreApellido != null && !nombreApellido.isEmpty()) {
            requestParams.add(AppConstants.PARAM_NOMBRE_CANDIDATO, nombreApellido);
        }
        if (distrito != null && !distrito.isEmpty()) {
            requestParams.add(AppConstants.PARAM_DISTRITO, distrito);
        }
        if (departamento != null && !departamento.isEmpty()) {
            requestParams.add(AppConstants.PARAM_DEPARTAMENTO, departamento);
        }
        if (partido != null && !partido.isEmpty()) {
            requestParams.add(AppConstants.PARAM_PARTIDO, partido);
        }
        if (candidatura != null && !candidatura.isEmpty()) {
            requestParams.add(AppConstants.PARAM_CANDIDATURA, candidatura);
        }
        if (puesto != null && !puesto.isEmpty()) {
            requestParams.add(AppConstants.PARAM_PUESTO, puesto);
        }
        if (lista > 0) {
            requestParams.add(AppConstants.PARAM_LISTA, String.format("%d", lista));
        }
        if (orden > 0) {
            requestParams.add(AppConstants.PARAM_ORDEN, String.format("%d", orden));
        }

        EleccionesRestClient.get(context, AppConstants.OPENSHIFT_HOST,
                AppConstants.PATH_CANDIDATOS, requestParams,
                new BaseJsonHttpResponseHandler<Object>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        ((SwipeRefreshLayout) getLoadingView()).setRefreshing(true);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ((SwipeRefreshLayout) getLoadingView()).setRefreshing(false);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse,
                                          Object response) {

                        Log.i(TAG, "Status Code: " + statusCode + "\nRaw rawJsonResponse: "
                                + rawJsonResponse);
                        Log.i(TAG, response.toString());
                        try {
                            mListener.onSuccessAction((Listado<Candidato>) response);
                        } catch (ClassCastException e) {
                            Log.e(TAG, "No se pudo realizar el cast del objeto: "
                                    + e.getLocalizedMessage());
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                          String rawJsonData, Object errorResponse) {
                        Log.e(TAG, "Status Code: " + statusCode + "\nRaw JSONData: "
                                + rawJsonData);
                        try {
                            MensajeError mensajeError = (MensajeError) errorResponse;
                            if (statusCode == 404) {
                                Snackbar.make(getParentView(), mensajeError.getMensajeUsuario(),
                                        Snackbar.LENGTH_SHORT).show();
                            } else if (statusCode == 500 || statusCode == 503) {
                                Snackbar.make(getParentView(), "El servicio no está disponible, " +
                                                "intente de nuevo más tarde",
                                        Snackbar.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(getParentView(), "Ocurrió un error, " +
                                                "intente de nuevo más tarde",
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        } catch (ClassCastException e) {
                            Log.e(TAG, "No se pudo realizar el cast del mensaje de error: "
                                    + e.getLocalizedMessage());
                        } catch (Exception e) {

                        }

                    }

                    @Override
                    protected Object parseResponse(String rawJsonData, boolean isFailure)
                            throws Throwable {
                        Listado<Candidato> candidatos = null;
                        MensajeError mensajeError = null;

                        ObjectMapper om = new ObjectMapper();
                        JsonParser parser = new JsonFactory().createParser(rawJsonData);

                        if (!isFailure) {
                            JavaType jt = om.getTypeFactory().constructParametrizedType(Listado.class,
                                    Listado.class, Candidato.class);
                            candidatos = om.readValue(parser, jt);
                            return candidatos;
                        } else {
                            mensajeError = om.readValues(parser, MensajeError.class).next();
                            return mensajeError;
                        }
                    }
                });
    }

    public interface OnResponseReceived {
        void onSuccessAction(DatosConsultaPadron datosConsultaPadron);

        void onSuccessAction(List list);

        void onSuccessAction(Listado listado);

        void onSuccessAction();

        void onFailureAction(int status);
    }
}
