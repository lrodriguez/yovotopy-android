package py.com.purplemammoth.apps.yoelijopy.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;

import py.com.purplemammoth.apps.yoelijopy.R;
import py.com.purplemammoth.apps.yoelijopy.client.EleccionesRestCallback;
import py.com.purplemammoth.apps.yoelijopy.model.DatosConsultaPadron;
import py.com.purplemammoth.apps.yoelijopy.model.DatosVotacion;
import py.com.purplemammoth.apps.yoelijopy.util.AppConstants;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConsultaPadronFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConsultaPadronFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultaPadronFragment extends Fragment implements EleccionesRestCallback.OnResponseReceived {
    private static final String TAG = "ConsultaPadron";

    // Variables
    private String cedula;
    private boolean isProfile;
    private Double latitudLocal;
    private Double longitudLocal;

    private OnFragmentInteractionListener mListener;
    private EleccionesRestCallback restCallback;

    // Containers
    private View parentView;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayout mainContainer;
    private CardView datosPersonalesCard;
    private CardView localVotacionCard;
    private CardView datosVotacionCard;
    private TableRow tipoVotoContainer;

    // Widgets
    private TextView nombrePersona;
    private TextView apellidoPersona;
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
    public static ConsultaPadronFragment newInstance(String cedula, boolean isProfile) {
        ConsultaPadronFragment fragment = new ConsultaPadronFragment();
        Bundle args = new Bundle();
        args.putString(AppConstants.ARG_CEDULA, cedula);
        args.putBoolean(AppConstants.ARG_PROFILE, isProfile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cedula = getArguments().getString(AppConstants.ARG_CEDULA);
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

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mainContainer = (LinearLayout) view.findViewById(R.id.main_content);
        datosPersonalesCard = (CardView) view.findViewById(R.id.datos_personales_card);
        localVotacionCard = (CardView) view.findViewById(R.id.local_votacion_card);
        datosVotacionCard = (CardView) view.findViewById(R.id.datos_votacion_card);

        nombrePersona = (TextView) view.findViewById(R.id.nombre_persona_text);
        apellidoPersona = (TextView) view.findViewById(R.id.apellido_persona_text);
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

        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.materialAccentColor),
                getResources().getColor(R.color.materialLightAccentColor),
                getResources().getColor(R.color.materialPrimaryColor));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // TODO mientras
                refreshLayout.setRefreshing(false);
            }
        });

        guardarPredeterminado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity()
                        .getSharedPreferences(AppConstants.PREFS_APP, 0);
                sharedPreferences.edit().putBoolean(AppConstants.PREFS_PROFILE, true).apply();
                sharedPreferences.edit().putString(AppConstants.PREFS_CEDULA, cedula).apply();

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

        restCallback = new EleccionesRestCallback(this, getActivity(), refreshLayout, parentView);
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
            performRequest(cedula);
        }
    }

    public void performRequest(String cedula) {
        this.cedula = cedula;
        // TODO update with user location
        try {
            restCallback.consultaPadron(cedula, 0.0, 0.0);
        } catch (JSONException e) {
            Log.e(TAG, "Ocurrió un error: " + e.getLocalizedMessage());
        }
    }

    public SwipeRefreshLayout getRefreshLayout() {
        return refreshLayout;
    }

    @Override
    public void onSuccessAction(DatosConsultaPadron datosConsultaPadron) {

        mainContainer.setVisibility(View.VISIBLE);
        nombrePersona.setText(datosConsultaPadron.getDatosPersonales().getNombre());
        apellidoPersona.setText(datosConsultaPadron.getDatosPersonales().getApellido());
        sexo.setText(datosConsultaPadron.getDatosPersonales().getSexo());
        nacionalidad.setText(datosConsultaPadron.getDatosPersonales().getNacionalidad());

        if (!datosConsultaPadron.getPuedeVotar()) {
            Snackbar.make(parentView, datosConsultaPadron.getMotivo(), Snackbar.LENGTH_LONG).show();
//            datosPersonalesCard.setVisibility(View.VISIBLE);
            localVotacionCard.setVisibility(View.GONE);
            datosVotacionCard.setVisibility(View.GONE);
        } else {
            datosPersonalesCard.setVisibility(View.VISIBLE);
            localVotacionCard.setVisibility(View.VISIBLE);
            datosVotacionCard.setVisibility(View.VISIBLE);

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
        }

    }

    @Override
    public void onFailureAction(int status) {

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
