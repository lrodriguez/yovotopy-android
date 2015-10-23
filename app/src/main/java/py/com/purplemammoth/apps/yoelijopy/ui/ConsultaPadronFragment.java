package py.com.purplemammoth.apps.yoelijopy.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
import py.com.purplemammoth.apps.yoelijopy.util.AppConstants;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConsultaPadronFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConsultaPadronFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultaPadronFragment extends Fragment {
    private static final String TAG = "ConsultaPadron";

    private String cedula;
    private String fechaNacimiento;
    private boolean isProfile;

    private Double latitudLocal;
    private Double longitudLocal;

    private OnFragmentInteractionListener mListener;

    private View parentView;

    private LinearLayout mainContainer;
    private TableRow tipoVotoContainer;
    private ProgressBar progressBar;
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
    public static ConsultaPadronFragment newInstance(String cedula, String fechaNacimiento,
                                                     boolean isProfile) {
        ConsultaPadronFragment fragment = new ConsultaPadronFragment();
        Bundle args = new Bundle();
        args.putString(AppConstants.ARG_CEDULA, cedula);
        args.putString(AppConstants.ARG_FECHA_NAC, fechaNacimiento);
        args.putBoolean(AppConstants.ARG_PROFILE, isProfile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cedula = getArguments().getString(AppConstants.ARG_CEDULA);
            fechaNacimiento = getArguments().getString(AppConstants.ARG_FECHA_NAC);
            isProfile = getArguments().getBoolean(AppConstants.ARG_PROFILE);
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

        mainContainer = (LinearLayout) view.findViewById(R.id.main_content);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

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

        tipoVotoContainer = (TableRow) view.findViewById(R.id.tipo_voto_layout);
        mesa = (TextView) view.findViewById(R.id.mesa_text);
        orden = (TextView) view.findViewById(R.id.orden_text);
        tipoVoto = (TextView) view.findViewById(R.id.tipo_voto_text);

        mainContainer.setVisibility(View.GONE);

        if (isProfile) {
            guardarPredeterminado.setVisibility(View.GONE);
        }

        guardarPredeterminado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity()
                        .getSharedPreferences(AppConstants.PREFS_APP, 0);
                sharedPreferences.edit().putBoolean(AppConstants.PREFS_PROFILE, true).apply();
                sharedPreferences.edit().putString(AppConstants.PREFS_CEDULA, cedula).apply();
                sharedPreferences.edit().putString(AppConstants.PREFS_FECHA_NAC, fechaNacimiento)
                        .apply();

                Snackbar.make(parentView, "Se guardó como predeterminado", Snackbar.LENGTH_SHORT)
                        .show();
            }
        });

        verMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String uriBegin = "geo:" + latitudLocal + "," + longitudLocal;
                    String query = latitudLocal + "," + longitudLocal + "(" + nombreLocal.getText() + ")";
                    String encodedQuery = Uri.encode(query);
                    String uriString = uriBegin + "?q=" + encodedQuery + "&z=14";
                    Uri uri = Uri.parse(uriString);
                    Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                    startActivity(mapIntent);
                } catch (ActivityNotFoundException e) {
                    Log.e("LocalAdapter", "No se encontró activity para visualizar mapas: "
                            + e.getLocalizedMessage());
                    Toast.makeText(getActivity(), "Por favor, instalá una aplicación para visualizar mapas " +
                            "como por ej. Google Maps", Toast.LENGTH_LONG).show();
                }

            }
        });
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

        if (isProfile) {
            try {
                consultaPadron(cedula, fechaNacimiento);
            } catch (JSONException e) {
                Log.e(TAG, "Ocurrió un error: " + e.getLocalizedMessage());
            }
        }
    }

    public void consultaPadron(String cedula, String fechaNacimiento) throws JSONException {

        this.cedula = cedula;
        this.fechaNacimiento = fechaNacimiento;

        RequestParams requestParams = new RequestParams();
        requestParams.add(AppConstants.PARAM_CEDULA, cedula);
        requestParams.add(AppConstants.PARAM_FECHA_NAC, fechaNacimiento);
        requestParams.add(AppConstants.PARAM_LATITUD, AppConstants.TEST_LATITUDE.toString());
        requestParams.add(AppConstants.PARAM_LONGITUD, AppConstants.TEST_LONGITUDE.toString());

        EleccionesRestClient.get(AppConstants.PATH_CONSULTA_PADRON, requestParams,
                new BaseJsonHttpResponseHandler<Object>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse,
                                          Object response) {
                        if (statusCode != 200) {
                            Log.e(TAG, "Status Code: " + statusCode);
                        } else {
                            Log.i(TAG, response.toString());
                            mainContainer.setVisibility(View.VISIBLE);
                            DatosConsultaPadron datosConsultaPadron = (DatosConsultaPadron) response;
                            nombrePersona.setText(datosConsultaPadron.getDatosPersonales().getNombre());
                            sexo.setText(datosConsultaPadron.getDatosPersonales().getSexo());
                            nacionalidad.setText(datosConsultaPadron.getDatosPersonales().getNacionalidad());

                            latitudLocal = datosConsultaPadron.getLocalVotacion().getLatitud();
                            longitudLocal = datosConsultaPadron.getLocalVotacion().getLongitud();
                            String mapsUrl = String.format(AppConstants.URL_MAPS_STATIC_IMAGE, latitudLocal,
                                    longitudLocal, latitudLocal, longitudLocal);
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
                        Log.e(TAG, "Status Code: " + statusCode + " Raw JSONData: "
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
                        } else if (statusCode == 500 || statusCode == 503) {
                            Snackbar.make(parentView, "El servicio no está disponible, " +
                                            "intente de nuevo más tarde",
                                    Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(parentView, "Ocurrió un error, " +
                                            "intente de nuevo más tarde",
                                    Snackbar.LENGTH_SHORT).show();
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
