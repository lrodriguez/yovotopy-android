package py.com.purpleapps.yovotopy.ui;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import py.com.purpleapps.yovotopy.R;
import py.com.purpleapps.yovotopy.client.EleccionesRestCallback;
import py.com.purpleapps.yovotopy.model.DatosConsultaPadron;
import py.com.purpleapps.yovotopy.model.DatosVotacion;
import py.com.purpleapps.yovotopy.model.Listado;
import py.com.purpleapps.yovotopy.util.AppConstants;
import py.com.purpleapps.yovotopy.util.Tracking;

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
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.main_content)
    LinearLayout mainContainer;
    @Bind(R.id.datos_personales_card)
    CardView datosPersonalesCard;
    @Bind(R.id.local_votacion_card)
    CardView localVotacionCard;
    @Bind(R.id.datos_votacion_card)
    CardView datosVotacionCard;
    @Bind(R.id.votacion_accesible_card)
    CardView votoAccesibleCard;
    // Widgets
    @Bind(R.id.nombre_persona_text)
    TextView nombrePersona;
    @Bind(R.id.apellido_persona_text)
    TextView apellidoPersona;
    @Bind(R.id.sexo_persona_text)
    TextView sexo;
    @Bind(R.id.nacionalidad_persona_text)
    TextView nacionalidad;
    @Bind(R.id.nombre_local_text)
    TextView nombreLocal;
    @Bind(R.id.direccion_local_text)
    TextView direccion;
    @Bind(R.id.departamento_text)
    TextView departamento;
    @Bind(R.id.zona_text)
    TextView zona;
    @Bind(R.id.distrito_text)
    TextView distrito;
    @Bind(R.id.mesa_text)
    TextView mesa;
    @Bind(R.id.orden_text)
    TextView orden;
    @Bind(R.id.voto_accesible_text)
    TextView tipoVoto;
    @Bind(R.id.map_image_view)
    ImageView mapa;
    @Bind(R.id.guardar_button)
    Button guardarPredeterminado;
    @Bind(R.id.ver_mapa_button)
    Button verMapa;
    View emptyView;
    // Variables
    private String cedula;
    private boolean isProfile;
    private Double latitudLocal;
    private Double longitudLocal;
    private OnFragmentInteractionListener mListener;
    private EleccionesRestCallback restCallback;
    // Containers
    private View parentView;
    private Location currentLocation;


    // Para el tracking de analytics
    private Tracking.Pantalla pantalla = Tracking.Pantalla.CONSULTA_PADRON;

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
        View view = inflater.inflate(R.layout.fragment_consultapadron, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentView = view;

        datosPersonalesCard.setVisibility(View.GONE);
        datosVotacionCard.setVisibility(View.GONE);
        localVotacionCard.setVisibility(View.GONE);
        votoAccesibleCard.setVisibility(View.GONE);


        if (isProfile) {
            guardarPredeterminado.setVisibility(View.GONE);
        } else {
            if (getActivity() instanceof MainTabActivity) {
                View emptyView = LayoutInflater.from(getActivity())
                        .inflate(R.layout.view_empty_profile, null);
                mainContainer.addView(emptyView);
            }
        }

        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.materialAccentColor),
                getResources().getColor(R.color.materialLightAccentColor),
                getResources().getColor(R.color.materialPrimaryColor));

        refreshLayout.setEnabled(false);

        guardarPredeterminado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity()
                        .getSharedPreferences(AppConstants.PREFS_APP, 0);
                sharedPreferences.edit().putBoolean(AppConstants.PREFS_PROFILE, true).apply();
                sharedPreferences.edit().putString(AppConstants.PREFS_CEDULA, cedula).apply();

                Tracking.track(ConsultaPadronFragment.this.getActivity().getApplication(),
                        pantalla, Tracking.Accion.GUARDAR_PREDETERMINADO);

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

                    Tracking.track(ConsultaPadronFragment.this.getActivity().getApplication(),
                            pantalla, Tracking.Accion.VER_MAPA);

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
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

        Double latitud = AppConstants.TEST_LATITUDE;
        Double longitud = AppConstants.TEST_LONGITUDE;

        currentLocation = BaseLocationActivity.getCurrentLocation();

        if (currentLocation != null) {
            latitud = currentLocation.getLatitude();
            longitud = currentLocation.getLongitude();
        }

        try {
            restCallback.getConsultaPadron(cedula, latitud, longitud);
        } catch (JSONException e) {
            Log.e(TAG, "Ocurrió un error: " + e.getLocalizedMessage());
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            Tracking.track(ConsultaPadronFragment.this.getActivity().getApplication(),
                    pantalla,
                    Tracking.Accion.VER_PANTALLA);
        }
    }

    public SwipeRefreshLayout getRefreshLayout() {
        return refreshLayout;
    }

    @Override
    public void onSuccessAction(DatosConsultaPadron datosConsultaPadron) {

        nombrePersona.setText(datosConsultaPadron.getDatosPersonales().getNombre());
        apellidoPersona.setText(datosConsultaPadron.getDatosPersonales().getApellido());
        sexo.setText(datosConsultaPadron.getDatosPersonales().getSexo());
        nacionalidad.setText(datosConsultaPadron.getDatosPersonales().getNacionalidad());

        if (!datosConsultaPadron.getPuedeVotar()) {
            Snackbar.make(parentView, datosConsultaPadron.getMotivo(), Snackbar.LENGTH_LONG).show();
            datosPersonalesCard.setVisibility(View.VISIBLE);
            votoAccesibleCard.setVisibility(View.GONE);
            localVotacionCard.setVisibility(View.GONE);
            datosVotacionCard.setVisibility(View.GONE);
            if (emptyView != null) {
                mainContainer.removeView(emptyView);
            }
        } else {
            if (emptyView != null) {
                mainContainer.removeView(emptyView);
            }

            datosPersonalesCard.setVisibility(View.VISIBLE);

            if (datosConsultaPadron.getDatosVotacion().getTipoVotoAccesible() != null) {
                votoAccesibleCard.setVisibility(View.VISIBLE);
                tipoVoto.setText(DatosVotacion.TipoVoto
                        .valueOf(datosConsultaPadron.getDatosVotacion()
                                .getTipoVotoAccesible()).getDescripcion());
                if (DatosVotacion.TipoVoto.VOTO_CASA.name().equalsIgnoreCase(datosConsultaPadron.getDatosVotacion()
                        .getTipoVotoAccesible())) {
                    localVotacionCard.setVisibility(View.GONE);
                    datosVotacionCard.setVisibility(View.GONE);
                    return;
                }
            } else {
                votoAccesibleCard.setVisibility(View.GONE);
            }

            localVotacionCard.setVisibility(View.VISIBLE);
            datosVotacionCard.setVisibility(View.VISIBLE);

            latitudLocal = datosConsultaPadron.getLocalVotacion().getLatitud();
            longitudLocal = datosConsultaPadron.getLocalVotacion().getLongitud();
            String mapsUrl = String.format(AppConstants.URL_MAPS_STATIC_IMAGE, latitudLocal,
                    longitudLocal, latitudLocal, longitudLocal);
            Glide.with(this).load(mapsUrl).into(mapa);
            nombreLocal.setText(datosConsultaPadron.getLocalVotacion().getNombre());
            direccion.setText(datosConsultaPadron.getLocalVotacion().getDireccion());
            departamento.setText(datosConsultaPadron.getLocalVotacion().getDepartamento());
            zona.setText(datosConsultaPadron.getLocalVotacion().getZona());
            distrito.setText(datosConsultaPadron.getLocalVotacion().getDistrito());

            mesa.setText(String.format("%d", datosConsultaPadron.getDatosVotacion()
                    .getMesa()));
            orden.setText(String.format("%d", datosConsultaPadron.getDatosVotacion()
                    .getOrden()));

        }

    }

    @Override
    public void onSuccessAction() {

    }

    @Override
    public void onSuccessAction(List list) {

    }

    @Override
    public void onSuccessAction(Listado listado) {

    }

    @Override
    public void onFailureAction(int status) {

    }

    public void setPantalla(Tracking.Pantalla pantalla) {
        this.pantalla = pantalla;
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
