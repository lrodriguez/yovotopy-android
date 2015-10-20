package py.com.purplemammoth.apps.yoelijopy.client;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by luisrodriguez on 17/10/15.
 */
public class EleccionesRestClient {
    private static final String SCHEMA = "http://";
    private static final String HOST = "municipales2015-openshiftpublic.rhcloud.com";
    private static final String PORT = ":80";
    private static final String BASE_PATH = "/municipales2015-%s/rest/";
    private static final String VERSION = "v0.1";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return SCHEMA + HOST + PORT + String.format(BASE_PATH, VERSION) + relativeUrl;
    }
}
