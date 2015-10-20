package py.com.purplemammoth.apps.yoelijopy.client;

import android.util.Log;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import org.json.JSONException;

import cz.msebera.android.httpclient.Header;
import py.com.purplemammoth.apps.yoelijopy.model.DatosConsultaPadron;

/**
 * Created by luisrodriguez on 17/10/15.
 */
public class EleccionesRestCallback {

    public void consultaPadron() throws JSONException {
        EleccionesRestClient.get("consultas-padron?ci=4232966&fechaNacimiento=1/9/88&latitud=-25.325367&longitud=-57.567217",
                null, new BaseJsonHttpResponseHandler<DatosConsultaPadron>() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse,
                                          DatosConsultaPadron response) {
                        if (statusCode != 200) {
                            Log.e("Consulta Padrón", "Status Code: " + statusCode);
                        } else {
                            Log.i("Consulta Padrón", response.toString());
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                          String rawJsonData, DatosConsultaPadron errorResponse) {

                    }

                    @Override
                    protected DatosConsultaPadron parseResponse(String rawJsonData, boolean isFailure)
                            throws Throwable {
                        DatosConsultaPadron datosConsultaPadron = null;
                        if (!isFailure) {
                            datosConsultaPadron = new ObjectMapper().readValues(new JsonFactory().createParser(rawJsonData),
                                    DatosConsultaPadron.class).next();
                        }
                        return datosConsultaPadron;
                    }
                });
    }
}
