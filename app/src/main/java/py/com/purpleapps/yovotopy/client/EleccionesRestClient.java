package py.com.purpleapps.yovotopy.client;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.Random;

import cz.msebera.android.httpclient.HttpEntity;
import py.com.purpleapps.yovotopy.util.AppConstants;

/**
 * Created by luisrodriguez on 17/10/15.
 */
public class EleccionesRestClient {

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void setTimeout(int timeout) {
        client.setTimeout(timeout);
    }

    public static void get(Context context, int host, RequestParams params,
                           AsyncHttpResponseHandler responseHandler) {
        client.get(context, getAbsoluteUrl(host, null), params, responseHandler);
    }

    public static void get(Context context, int host, String url, RequestParams params,
                           AsyncHttpResponseHandler responseHandler) {
        client.get(context, getAbsoluteUrl(host, url), params, responseHandler);
    }

    public static void post(Context context, int host, String url, RequestParams params,
                            AsyncHttpResponseHandler responseHandler) {
        client.post(context, getAbsoluteUrl(host, url), params, responseHandler);
    }

    public static void post(Context context, int host, String url, HttpEntity httpEntity, String contentType,
                            AsyncHttpResponseHandler responseHandler) {
        client.post(context, getAbsoluteUrl(host, url), httpEntity, contentType, responseHandler);
    }

    public static void cancelRequests(Context context) {
        client.cancelRequests(context, true);
    }

    public static void cancelRequestByTAG(Object TAG) {
        client.cancelRequestsByTAG(TAG, true);
    }

    private static String getAbsoluteUrl(int host, String relativeUrl) {
        String url = null;

        Random instanceRandom = new Random();
        int randomValue;
        String openshiftName;

        switch (host) {
            case AppConstants.OPENSHIFT_HOST:

                client.setTimeout(30000);
                //randomValue = instanceRandom.nextInt(3);
                randomValue = 6;
                openshiftName = AppConstants.Instance.values()[randomValue].getNombre();

                Log.d("RestClient", "Llamada al web service: " + AppConstants.SCHEMA
                        + openshiftName + AppConstants.HOST + AppConstants.PORT
                        + String.format(AppConstants.BASE_PATH, AppConstants.VERSION)
                        + relativeUrl);
                url = AppConstants.SCHEMA + openshiftName + AppConstants.HOST + AppConstants.PORT
                        + String.format(AppConstants.BASE_PATH, AppConstants.VERSION)
                        + relativeUrl;
                break;
            case AppConstants.AVIZOR_HOST:
                client.setTimeout(30000);
                url = AppConstants.SCHEMA + AppConstants.HOST_AVIZOR + AppConstants.BASE_PATH_AVIZOR;
                break;
            case AppConstants.OPENSHIFT_DENUNCIAS_HOST:
                client.setTimeout(600000);
                randomValue = instanceRandom.nextInt(6 - 3) + 3;
                openshiftName = AppConstants.Instance.values()[randomValue].getNombre();

                Log.d("RestClient", "Llamada al web service: " + AppConstants.SCHEMA
                        + openshiftName + AppConstants.HOST_DENUNCIAS + AppConstants.PORT
                        + String.format(AppConstants.BASE_PATH, AppConstants.VERSION)
                        + relativeUrl);
                url = AppConstants.SCHEMA + openshiftName + AppConstants.HOST_DENUNCIAS + AppConstants.PORT
                        + String.format(AppConstants.BASE_PATH, AppConstants.VERSION)
                        + relativeUrl;
                break;
        }

        return url;
    }
}
