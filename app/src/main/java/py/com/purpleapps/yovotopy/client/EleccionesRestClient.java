package py.com.purpleapps.yovotopy.client;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.HttpEntity;
import py.com.purpleapps.yovotopy.util.AppConstants;

/**
 * Created by luisrodriguez on 17/10/15.
 */
public class EleccionesRestClient {

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(Context context, int host, RequestParams params,
                           AsyncHttpResponseHandler responseHandler) {
        client.get(context, getAbsoluteUrl(host, 0, null), params, responseHandler);
    }

    public static void get(Context context, int host, String url, RequestParams params,
                           AsyncHttpResponseHandler responseHandler) {
        client.get(context, getAbsoluteUrl(host, 0, url), params, responseHandler);
    }

    public static void post(Context context, int host, String url, RequestParams params,
                            AsyncHttpResponseHandler responseHandler) {
        client.post(context, getAbsoluteUrl(host, 0, url), params, responseHandler);
    }

    public static void post(Context context, int host, String url, HttpEntity httpEntity, String contentType,
                            AsyncHttpResponseHandler responseHandler) {
        client.post(context, getAbsoluteUrl(host, 0, url), httpEntity, contentType, responseHandler);
    }

    private static String getAbsoluteUrl(int host, int instance, String relativeUrl) {
        // TODO get instance randomly
        String url = null;

        switch (host) {
            case AppConstants.OPENSHIFT_HOST:
                Log.d("RestClient", "Llamada al web service: " + AppConstants.SCHEMA
                        + AppConstants.INSTANCE_01 + AppConstants.HOST + AppConstants.PORT
                        + String.format(AppConstants.BASE_PATH, AppConstants.VERSION)
                        + relativeUrl);
                url = AppConstants.SCHEMA + AppConstants.INSTANCE_01 + AppConstants.HOST + AppConstants.PORT
                        + String.format(AppConstants.BASE_PATH, AppConstants.VERSION)
                        + relativeUrl;
                break;
            case AppConstants.AVIZOR_HOST:
                url = AppConstants.SCHEMA + AppConstants.HOST_AVIZOR + AppConstants.BASE_PATH_AVIZOR;
                break;
            case AppConstants.OPENSHIFT_DENUNCIAS_HOST:
                url = AppConstants.SCHEMA + AppConstants.INSTANCE_04 + AppConstants.HOST_DENUNCIAS + AppConstants.PORT
                        + String.format(AppConstants.BASE_PATH, AppConstants.VERSION)
                        + relativeUrl;
        }

        return url;
    }
}
