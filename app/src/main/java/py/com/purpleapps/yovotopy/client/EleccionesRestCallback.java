package py.com.purpleapps.yovotopy.client;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import py.com.purpleapps.yovotopy.R;
import py.com.purpleapps.yovotopy.model.AvizorCategoryWrapper;
import py.com.purpleapps.yovotopy.model.AvizorError;
import py.com.purpleapps.yovotopy.model.AvizorResponse;
import py.com.purpleapps.yovotopy.model.DatosConsultaPadron;
import py.com.purpleapps.yovotopy.model.DatosDenuncia;
import py.com.purpleapps.yovotopy.model.MensajeError;
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
        requestParams.add(AppConstants.PARAM_LATITUD, AppConstants.TEST_LATITUDE.toString());
        requestParams.add(AppConstants.PARAM_LONGITUD, AppConstants.TEST_LONGITUDE.toString());

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

    public interface OnResponseReceived {
        void onSuccessAction(DatosConsultaPadron datosConsultaPadron);

        void onSuccessAction(List<AvizorCategoryWrapper> categoryList);

        void onSuccessAction();

        void onFailureAction(int status);
    }
}
