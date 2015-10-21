package py.com.purplemammoth.apps.yoelijopy.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;

import cz.msebera.android.httpclient.Header;
import py.com.purplemammoth.apps.yoelijopy.R;
import py.com.purplemammoth.apps.yoelijopy.client.EleccionesRestClient;
import py.com.purplemammoth.apps.yoelijopy.model.DatosConsultaPadron;
import py.com.purplemammoth.apps.yoelijopy.model.DatosVotacion;
import py.com.purplemammoth.apps.yoelijopy.model.MensajeError;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConsultaPadronFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConsultaPadronFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultaPadronFragment extends Fragment {
    public static final String MAPS_STATIC_URL = "http://maps.google.com/maps/api/staticmap?center" +
            "=%f,%f&zoom=16&size=480x240&markers=color:blue|%f,%f&sensor=false";
    private static final String CONSULTA_PADRON_URI = "consultas-padron";
    private static final String ARG_CEDULA = "arg_cedula";
    private static final String ARG_FECHA_NAC = "arg_fecha_nac";
    private static final Double TEST_LATITUDE = -25.325367;
    private static final Double TEST_LONGITUDE = -57.567217;

    private String cedula;
    private String fechaNacimiento;

    private OnFragmentInteractionListener mListener;

    private View parentView;

    private LinearLayout tipoVotoContainer;
    private TextView nombrePersona;
    private TextView sexo;
    private TextView nacionalidad;
    private TextView nombreLocal;
    private TextView direccion;
    private TextView departamento;
    private TextView zona;
    private TextView distrito;
    private TextView mesa;
    private TextView orden;
    private TextView tipoVoto;
    private ImageView mapa;
    private Button guardarPredeterminado;
    private Button verMapa;

    public ConsultaPadronFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConsultaPadronFragment newInstance(String cedula, String fechaNacimiento) {
        ConsultaPadronFragment fragment = new ConsultaPadronFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CEDULA, cedula);
        args.putString(ARG_FECHA_NAC, fechaNacimiento);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cedula = getArguments().getString(ARG_CEDULA);
            fechaNacimiento = getArguments().getString(ARG_FECHA_NAC);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_consultapadron, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentView = view;

        nombrePersona = (TextView) view.findViewById(R.id.nombre_persona_text);
        sexo = (TextView) view.findViewById(R.id.sexo_persona_text);
        nacionalidad = (TextView) view.findViewById(R.id.nacionalidad_persona_text);
        guardarPredeterminado = (Button) view.findViewById(R.id.guardar_button);

        mapa = (ImageView) view.findViewById(R.id.map_image_view);
        nombreLocal = (TextView) view.findViewById(R.id.nombre_local_text);
        direccion = (TextView) view.findViewById(R.id.direccion_local_text);
        departamento = (TextView) view.findViewById(R.id.departamento_text);
        zona = (TextView) view.findViewById(R.id.zona_text);
        distrito = (TextView) view.findViewById(R.id.distrito_text);
        verMapa = (Button) view.findViewById(R.id.ver_mapa_button);

        tipoVotoContainer = (LinearLayout) view.findViewById(R.id.tipo_voto_layout);
        mesa = (TextView) view.findViewById(R.id.mesa_text);
        orden = (TextView) view.findViewById(R.id.orden_text);
        tipoVoto = (TextView) view.findViewById(R.id.tipo_voto_text);

        // TODO onclicklisteners
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();

        try {
            consultaPadron(cedula, fechaNacimiento);
        } catch (JSONException e) {
            Log.e("HomeFragment", e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e("HomeFragment", e.getLocalizedMessage());
        }
    }

    public void consultaPadron(String cedula, String fechaNacimiento) throws JSONException {
        RequestParams requestParams = new RequestParams();
        requestParams.add("ci", cedula);
        requestParams.add("fechaNacimiento", fechaNacimiento);
        requestParams.add("latitud", TEST_LATITUDE.toString());
        requestParams.add("longitud", TEST_LONGITUDE.toString());

        EleccionesRestClient.get(CONSULTA_PADRON_URI, requestParams,
                new BaseJsonHttpResponseHandler<Object>() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse,
                                          Object response) {
                        if (statusCode != 200) {
                            Log.e("Consulta Padrón", "Status Code: " + statusCode);
                        } else {
                            Log.i("Consulta Padrón", response.toString());
                            DatosConsultaPadron datosConsultaPadron = (DatosConsultaPadron) response;
                            nombrePersona.setText(datosConsultaPadron.getDatosPersonales().getNombre());
                            sexo.setText(datosConsultaPadron.getDatosPersonales().getSexo());
                            nacionalidad.setText(datosConsultaPadron.getDatosPersonales().getNacionalidad());

                            Double latitudLocal = datosConsultaPadron.getLocalVotacion().getLatitud();
                            Double longitudLocal = datosConsultaPadron.getLocalVotacion().getLongitud();
                            String mapsUrl = String.format(MAPS_STATIC_URL, latitudLocal, longitudLocal,
                                    latitudLocal, longitudLocal);
                            Glide.with(getActivity()).load(mapsUrl).into(mapa);
                            nombreLocal.setText(datosConsultaPadron.getLocalVotacion().getNombre());
                            direccion.setText(datosConsultaPadron.getLocalVotacion().getDireccion());
                            departamento.setText(datosConsultaPadron.getLocalVotacion().getDepartamento());
                            zona.setText(datosConsultaPadron.getLocalVotacion().getZona());
                            distrito.setText(datosConsultaPadron.getLocalVotacion().getDistrito());

                            mesa.setText(String.format("%d", datosConsultaPadron.getDatosVotacion()
                                    .getMesa()));
                            orden.setText(String.format("%d", datosConsultaPadron.getDatosVotacion()
                                    .getOrden()));
                            if (datosConsultaPadron.getDatosVotacion().getTipoVotoAccesible() != null) {
                                tipoVotoContainer.setVisibility(View.VISIBLE);
                                tipoVoto.setText(DatosVotacion.TipoVoto
                                        .valueOf(datosConsultaPadron.getDatosVotacion()
                                                .getTipoVotoAccesible()).getDescripcion());
                            } else {
                                tipoVotoContainer.setVisibility(View.GONE);
                            }

                            // TODO manejar datos duplicados y deshabilitados
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                          String rawJsonData, Object errorResponse) {
                        Log.e("ConsultaPadron", "Status Code: " + statusCode + "Raw JSONData: "
                                + rawJsonData);
                        MensajeError mensajeError = (MensajeError) errorResponse;
                        if (statusCode == 404) {
                            /*try {

                                MensajeError mensajeError = new ObjectMapper()
                                        .readValues(new JsonFactory().createParser(rawJsonData),
                                                MensajeError.class).next();*/
                            Snackbar.make(parentView, mensajeError.getMensajeUsuario(),
                                    Snackbar.LENGTH_SHORT).show();
                            /*} catch (IOException e) {
                                Log.e("ConsultaPadron", "Error: " + e.getLocalizedMessage());
                            }*/
                        }
                    }

                    @Override
                    protected Object parseResponse(String rawJsonData, boolean isFailure)
                            throws Throwable {
                        DatosConsultaPadron datosConsultaPadron = null;
                        MensajeError mensajeError = null;
                        if (!isFailure) {
                            datosConsultaPadron = new ObjectMapper().readValues(new JsonFactory().createParser(rawJsonData),
                                    DatosConsultaPadron.class).next();
                            return datosConsultaPadron;
                        } else {
                            mensajeError = new ObjectMapper()
                                    .readValues(new JsonFactory().createParser(rawJsonData),
                                            MensajeError.class).next();
                            return mensajeError;
                        }
                    }
                });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
