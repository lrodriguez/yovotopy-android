package py.com.purplemammoth.apps.yoelijopy.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import org.json.JSONException;

import cz.msebera.android.httpclient.Header;
import py.com.purplemammoth.apps.yoelijopy.R;
import py.com.purplemammoth.apps.yoelijopy.client.EleccionesRestClient;
import py.com.purplemammoth.apps.yoelijopy.model.DatosConsultaPadron;

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

    private OnFragmentInteractionListener mListener;

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
    public static ConsultaPadronFragment newInstance() {
        ConsultaPadronFragment fragment = new ConsultaPadronFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO
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

        mesa = (TextView) view.findViewById(R.id.mesa_text);
        orden = (TextView) view.findViewById(R.id.orden_text);

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
            consultaPadron();
        } catch (JSONException e) {
            Log.e("HomeFragment", e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e("HomeFragment", e.getLocalizedMessage());
        }
    }

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
                            nombrePersona.setText(response.getDatosPersonales().getNombre());
                            sexo.setText(response.getDatosPersonales().getSexo());
                            nacionalidad.setText(response.getDatosPersonales().getNacionalidad());

                            Double latitudLocal = response.getLocalVotacion().getLatitud();
                            Double longitudLocal = response.getLocalVotacion().getLongitud();
                            String mapsUrl = String.format(MAPS_STATIC_URL, latitudLocal, longitudLocal,
                                    latitudLocal, longitudLocal);
                            Glide.with(getActivity()).load(mapsUrl).into(mapa);
                            nombreLocal.setText(response.getLocalVotacion().getNombre());
                            direccion.setText(response.getLocalVotacion().getDireccion());
                            departamento.setText(response.getLocalVotacion().getDepartamento());
                            zona.setText(response.getLocalVotacion().getZona());
                            distrito.setText(response.getLocalVotacion().getDistrito());

                            mesa.setText(String.format("%d", response.getDatosVotacion().getMesa()));
                            orden.setText(String.format("%d", response.getDatosVotacion().getOrden()));

                            // TODO manejar datos duplicados y deshabilitados
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
