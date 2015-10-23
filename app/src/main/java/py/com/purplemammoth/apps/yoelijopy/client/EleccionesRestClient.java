package py.com.purplemammoth.apps.yoelijopy.client;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import py.com.purplemammoth.apps.yoelijopy.util.AppConstants;

/**
 * Created by luisrodriguez on 17/10/15.
 */
public class EleccionesRestClient {

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return AppConstants.SCHEMA + AppConstants.HOST + AppConstants.PORT
                + String.format(AppConstants.BASE_PATH, AppConstants.VERSION)
                + relativeUrl;
    }
}
