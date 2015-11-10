package py.com.purpleapps.yovotopy.ui;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import py.com.purpleapps.yovotopy.R;
import py.com.purpleapps.yovotopy.client.EleccionesRestCallback;
import py.com.purpleapps.yovotopy.model.Candidato;
import py.com.purpleapps.yovotopy.model.DatosConsultaPadron;
import py.com.purpleapps.yovotopy.model.DatosVotacion;
import py.com.purpleapps.yovotopy.model.Distrito;
import py.com.purpleapps.yovotopy.model.Listado;
import py.com.purpleapps.yovotopy.util.AppConstants;
import py.com.purpleapps.yovotopy.util.Tracking;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements EleccionesRestCallback.OnResponseReceived {
    public static final String TAG = "HomeFragment";

    public static final long MUNICIPALES_DATE = 1447581600000L;
    public static final long DAY_IN_MILLISECONDS = 86400000L;
    public static final long HOUR_IN_MILLISECONDS = 3600000L;
    public static final long MINUTE_IN_MILLISECONDS = 60000L;
    public static final long SECOND_IN_MILLISECONDS = 1000L;
    View parentView;
    @Bind(R.id.main_content)
    LinearLayout mainContent;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.proximas_elecciones_card)
    CardView proximasEleccionesCard;
    @Bind(R.id.local_votacion_card)
    CardView localVotacionCard;
    @Bind(R.id.datos_votacion_card)
    CardView datosVotacionCard;
    @Bind(R.id.votacion_accesible_card)
    CardView votoAccesibleCard;
    @Bind(R.id.title_proximas_text)
    TextView proximasTitle;
    @Bind(R.id.faltan_text)
    TextView faltan;
    @Bind(R.id.countdown_text)
    TextView countdownText;
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
    @Bind(R.id.title_text)
    TextView titleText;
    @Bind(R.id.map_image_view)
    ImageView mapa;
    @Bind(R.id.ver_mapa_button)
    Button verMapa;
    List<View> candidatosView;
    @BindString(R.string.section_candidatos)
    String seccionCandidatos;
    private String cedula;
    private boolean hasProfile;
    private Double latitudLocal;
    private Double longitudLocal;
    private String distritoCercano;
    private OnFragmentInteractionListener mListener;

    private EleccionesRestCallback restCallback;

    private Location currentLocation;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String cedula, boolean hasProfile) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(AppConstants.ARG_CEDULA, cedula);
        args.putBoolean(AppConstants.ARG_PROFILE, hasProfile);
        fragment.setArguments(args);
        return fragment;
    }

    public static String calculateRemainingTime(Long millisUntilFinished) {
        long dias = TimeUnit.DAYS.convert(millisUntilFinished, TimeUnit.DAYS.MILLISECONDS);

        millisUntilFinished = millisUntilFinished - (dias * DAY_IN_MILLISECONDS);

        long horas = TimeUnit.HOURS.convert(millisUntilFinished, TimeUnit.DAYS.MILLISECONDS);

        millisUntilFinished = millisUntilFinished - (horas * HOUR_IN_MILLISECONDS);

        long minutos = TimeUnit.MINUTES.convert(millisUntilFinished, TimeUnit.DAYS.MILLISECONDS);

        millisUntilFinished = millisUntilFinished - (minutos * MINUTE_IN_MILLISECONDS);

        long segundos = millisUntilFinished / SECOND_IN_MILLISECONDS;

        return String.format("%d d, %d hs, %d min, %d seg", dias, horas, minutos, segundos);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            cedula = getArguments().getString(AppConstants.ARG_CEDULA);
            hasProfile = getArguments().getBoolean(AppConstants.ARG_PROFILE);
        }

        candidatosView = new ArrayList<>();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mListener.showFab(true, 1);
            Tracking.track(getActivity().getApplication(), Tracking.Pantalla.INICIO, Tracking.Accion.VER_PANTALLA);
        } else {
            if (getUserVisibleHint()) {
                mListener.showFab(false, 1);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentView = view;

        final long now = new Date().getTime();
        long timeLeft = MUNICIPALES_DATE - now;

        new CountDownTimer(timeLeft, 1000) {

            public void onTick(long millisUntilFinished) {
                String timeText = calculateRemainingTime(millisUntilFinished);

                countdownText.setText(timeText);
            }

            public void onFinish() {
                if ((now - MUNICIPALES_DATE) < (HOUR_IN_MILLISECONDS * 10)) {
                    proximasTitle.setText("Hoy");
                    faltan.setVisibility(View.GONE);
                    countdownText.setVisibility(View.GONE);
                } else {
                    proximasEleccionesCard.setVisibility(View.GONE);
                }
            }
        }.start();

        refreshLayout.setEnabled(false);

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
                    Log.e(TAG, "No se encontró activity para visualizar mapas: "
                            + e.getLocalizedMessage());
                    Toast.makeText(getActivity(), "Por favor, instalá una aplicación para visualizar mapas " +
                            "como por ej. Google Maps", Toast.LENGTH_LONG).show();
                }

            }
        });

        if (!hasProfile) {
            localVotacionCard.setVisibility(View.GONE);
            datosVotacionCard.setVisibility(View.GONE);
            votoAccesibleCard.setVisibility(View.GONE);
        }

        restCallback = new EleccionesRestCallback(this, getActivity(), refreshLayout, parentView);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (hasProfile) {
            performRequest(1, cedula);
        } else {
            performRequest(3, "");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void performRequest(int idRequest, String cedula) {

        Double latitud = AppConstants.TEST_LATITUDE;
        Double longitud = AppConstants.TEST_LONGITUDE;

        currentLocation = BaseLocationActivity.getCurrentLocation();

        if (currentLocation != null) {
            latitud = currentLocation.getLatitude();
            longitud = currentLocation.getLongitude();
        }

        try {
            switch (idRequest) {
                case 1:
                    restCallback.getConsultaPadron(cedula, latitud, longitud);
                    break;
                case 2:
                    restCallback.getCandidatos(AppConstants.INITIAL_OFFSET, AppConstants.DEFAULT_LIMIT,
                            "lista", "", distritoCercano,
                            "", "", "INTENDENTE", "", 0, 0);
                    break;
                case 3:
                    restCallback.getDistritos(AppConstants.INITIAL_OFFSET, 1, latitud, longitud,
                            "distancia", "", "");
                    break;
            }

        } catch (JSONException e) {
            Log.e(TAG, "Ocurrió un error: " + e.getLocalizedMessage());
        }
    }

    @Override
    public void onSuccessAction(DatosConsultaPadron datosConsultaPadron) {

        if (!datosConsultaPadron.getPuedeVotar()) {
            Snackbar.make(parentView, datosConsultaPadron.getMotivo(), Snackbar.LENGTH_LONG).show();
            localVotacionCard.setVisibility(View.GONE);
            datosVotacionCard.setVisibility(View.GONE);
        } else {
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

            // Seccion de candidatos
            distritoCercano = datosConsultaPadron.getLocalVotacion().getDistrito();
            titleText.setText(String.format(seccionCandidatos, distritoCercano));
            performRequest(2, "");

        }
    }

    private void addCandidatoCard(Candidato candidato) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.card_datos_candidato, null);

        TextView nombre = ButterKnife.findById(view, R.id.nombre_candidato_text);
        TextView candidatura = ButterKnife.findById(view, R.id.candidatura_text);
        TextView lista = ButterKnife.findById(view, R.id.lista_text);
        TextView partido = ButterKnife.findById(view, R.id.partido_text);
        TextView distrito = ButterKnife.findById(view, R.id.distrito_text);
        TextView departamento = ButterKnife.findById(view, R.id.departamento_text);
        TextView puesto = ButterKnife.findById(view, R.id.puesto_text);
        TextView orden = ButterKnife.findById(view, R.id.orden_text);
        TableRow puestoRow = ButterKnife.findById(view, R.id.puesto_row);
        TableRow ordenRow = ButterKnife.findById(view, R.id.orden_row);

        puestoRow.setVisibility(View.GONE);
        ordenRow.setVisibility(View.GONE);

        if (candidato != null) {
            nombre.setText(candidato.getNombreApellido());
            candidatura.setText(candidato.getCandidatura());
            distrito.setText(candidato.getDistrito());
            departamento.setText(candidato.getDepartamento());
            partido.setText(candidato.getPartido());
            lista.setText(String.format("%d", candidato.getLista()));
            if (candidato.getPuesto() != null) {
                puestoRow.setVisibility(View.VISIBLE);
                ordenRow.setVisibility(View.VISIBLE);
                puesto.setText(candidato.getPuesto());
                orden.setText(String.format("%d", candidato.getOrden()));
            }
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        int margins = getResources().getDimensionPixelSize(R.dimen.card_margin);

        layoutParams.setMargins(margins, 0, margins, margins);
        candidatosView.add(view);
        mainContent.addView(view, layoutParams);
    }

    @Override
    public void onSuccessAction(List list) {

    }

    @Override
    public void onSuccessAction(Listado listado) {
        if (listado != null && listado.getContent() != null && listado.getContent().size() > 0) {
            Object object = listado.getContent().get(0);
            if (object instanceof Candidato) {
                // Sacamos las vistas anteriores
                for (View view : candidatosView) {
                    mainContent.removeView(view);
                }

                // Añadimos los candidatos
                for (Candidato candidato : (List<Candidato>) listado.getContent()) {
                    addCandidatoCard(candidato);
                }
            } else if (object instanceof Distrito) {
                // Seccion de candidatos
                distritoCercano = ((Distrito) object).getNombre();
                titleText.setText(String.format(seccionCandidatos, distritoCercano));
                performRequest(2, "");
            }
        }
    }

    @Override
    public void onSuccessAction() {

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

        void showFab(boolean show, int iconId);
    }

}
