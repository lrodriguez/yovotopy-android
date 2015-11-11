package py.com.purpleapps.yovotopy.ui;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import py.com.purpleapps.yovotopy.R;
import py.com.purpleapps.yovotopy.client.EleccionesRestCallback;
import py.com.purpleapps.yovotopy.client.EleccionesRestClient;
import py.com.purpleapps.yovotopy.model.DatosConsultaPadron;
import py.com.purpleapps.yovotopy.model.Listado;
import py.com.purpleapps.yovotopy.model.TipoListado;
import py.com.purpleapps.yovotopy.ui.components.ItemOffsetDecoration;
import py.com.purpleapps.yovotopy.util.AppConstants;
import py.com.purpleapps.yovotopy.util.Tracking;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ExplorarFragment extends BaseLocationFragment implements EleccionesRestCallback.OnResponseReceived {
    public static final String TAG = "ExplorarFragment";
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.list)
    RecyclerView recyclerView;
    private boolean loading = true, paginated = true;
    private int startCount = 0;
    private int firstVisibleItems, visibleItemCount, totalItemCount, previousTotal = 0, visibleThreshold = 3;
    private int count = 0, offset = AppConstants.INITIAL_OFFSET, limit = AppConstants.DEFAULT_LIMIT;
    // TODO: Customize parameters
    private int mColumnCount = 2;
    private TipoListado tipoListado;
    private HashMap<String, String> bundleHash;
    private String orderBy;
    private String departamento;
    private String distrito;
    private String partido;
    private String puesto;
    private String candidatura;
    private OnListFragmentInteractionListener mListener;
    private View parentView;
    private MyExplorarRecyclerViewAdapter mAdapter;
    private EleccionesRestCallback restCallback;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ExplorarFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ExplorarFragment newInstance(int columnCount, HashMap<String, String> bundleHash, String tipoListado) {
        ExplorarFragment fragment = new ExplorarFragment();
        Bundle args = new Bundle();
        args.putInt(AppConstants.ARG_COLUMN_COUNT, columnCount);
        args.putSerializable(AppConstants.ARG_ITEM_FILTER, bundleHash);
        args.putString(AppConstants.ARG_TIPO_LISTADO, tipoListado);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(AppConstants.ARG_COLUMN_COUNT);
            bundleHash = (HashMap<String, String>) getArguments().getSerializable(AppConstants.ARG_ITEM_FILTER);
            tipoListado = TipoListado.valueOf(getArguments()
                    .getString(AppConstants.ARG_TIPO_LISTADO));
        }

        if (bundleHash != null) {
            partido = bundleHash.get(AppConstants.PARAM_PARTIDO);
            departamento = bundleHash.get(AppConstants.PARAM_DEPARTAMENTO);
            distrito = bundleHash.get(AppConstants.PARAM_DISTRITO);
            orderBy = bundleHash.get(AppConstants.PARAM_ORDER_BY);
            puesto = bundleHash.get(AppConstants.PARAM_PUESTO);
            candidatura = bundleHash.get(AppConstants.PARAM_CANDIDATURA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explorar_list, container, false);
        parentView = view;

        ButterKnife.bind(this, view);

        restCallback = new EleccionesRestCallback(this, getActivity(), refreshLayout, parentView);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = view.getContext();

        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            ItemOffsetDecoration decoration = new ItemOffsetDecoration(context, R.dimen.items_offset);
            recyclerView.addItemDecoration(decoration);
        }

        mAdapter = new MyExplorarRecyclerViewAdapter(getActivity(), new ArrayList<>(), mListener);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (paginated) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    firstVisibleItems = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();

                   /* Log.d(TAG, "visibleItemCount: " + visibleItemCount + "\ntotalItemCount: "
                            + totalItemCount + "\nfirstVisibleItems: " + firstVisibleItems);*/

                    if (loading && totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }

                    if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItems + visibleThreshold)) {
                        if ((offset + limit) <= count) {
                            performRequest(offset + limit, orderBy);
                            loading = true;
                        }
                    }
                }
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resetFragment();
                performRequest(offset, orderBy);
            }
        });

        trackEvent();
    }

    public void resetFragment() {
        count = 0;
        firstVisibleItems = 0;
        visibleItemCount = 0;
        totalItemCount = 0;
        previousTotal = 0;
        loading = true;
        offset = AppConstants.INITIAL_OFFSET;
        mAdapter.removeItems();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onStart() {
        super.onStart();

//        Log.d(TAG, "onStart called");
        if (startCount < 1) {
            performOnLocationUpdatedAction();
            startCount++;
        }
    }

    @Override
    public void onStop() {
        switch (tipoListado.getId()) {
            case 1:
                EleccionesRestClient.cancelRequestByTAG(AppConstants.PATH_DEPARTAMENTOS);
                break;
            case 2:
                EleccionesRestClient.cancelRequestByTAG(AppConstants.PATH_DISTRITOS);
                break;
            case 3:
                EleccionesRestClient.cancelRequestByTAG(AppConstants.PATH_PARTIDOS);
                break;
            case 4:
                EleccionesRestClient.cancelRequestByTAG(AppConstants.PATH_CANDIDATURAS);
                break;
            case 5:
                EleccionesRestClient.cancelRequestByTAG(AppConstants.PATH_CANDIDATOS);
                break;
        }
        super.onStop();
    }

    @Override
    void performOnLocationUpdatedAction() {
        resetFragment();
        currentLocation = BaseLocationActivity.getCurrentLocation();
        if (currentLocation != null) {
            performRequest(offset, orderBy);
        }
    }

    private void trackEvent() {
        Tracking.Pantalla explorar = Tracking.Pantalla.EXPLORAR;
        Tracking.Pantalla explorarJerarquia = Tracking.Pantalla.EXPLORAR_JERARQUIAS;
        Application app = ExplorarFragment.this.getActivity().getApplication();

        switch (tipoListado.getId()) {
            case 1:
                break;
            case 2:
                Tracking.track(app, explorar, Tracking.Accion.VER_DEPARTAMENTO, departamento);
                Tracking.track(app, explorarJerarquia, departamento);
                break;
            case 3:
                Tracking.track(app, explorar, Tracking.Accion.VER_DISTRITO, distrito);
                Tracking.track(app, explorarJerarquia, departamento + "/" + distrito);
                break;
            case 4:
                Tracking.track(app, explorar, Tracking.Accion.VER_PARTIDO, partido);
                Tracking.track(app, explorarJerarquia, departamento + "/" + distrito + "/" + partido);
                break;
            case 5:
                Tracking.track(app, explorar, Tracking.Accion.VER_CANDIDATURA, candidatura);
                Tracking.track(app, explorarJerarquia, departamento + "/" + distrito + "/" + partido + "/" + candidatura);
                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            Tracking.track(getActivity().getApplication(),
                    Tracking.Pantalla.EXPLORAR,
                    Tracking.Accion.VER_PANTALLA);
        }
    }

    private void performRequest(int offset, String orderBy) {
        Double latitud = currentLocation.getLatitude();
        Double longitud = currentLocation.getLongitude();


        try {
            switch (tipoListado.getId()) {
                case 1:
                    paginated = false;
                    restCallback.getDepartamentos(latitud, longitud, orderBy, "");
                    break;
                case 2:
                    restCallback.getDistritos(offset, limit, latitud, longitud, orderBy, "", departamento);
                    break;
                case 3:
                    restCallback.getPartidos(offset, limit, orderBy, distrito, departamento);
                    break;
                case 4:
                    paginated = false;
                    restCallback.getCandidaturas(orderBy, distrito, departamento, partido);
                    break;
                case 5:
                    restCallback.getCandidatos(offset, limit, orderBy, "", distrito, departamento,
                            partido, candidatura, puesto, 0, 0);
                    break;
            }

        } catch (JSONException e) {
            Log.e(TAG, "Ocurrió un error: " + e.getLocalizedMessage());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
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

    @Override
    public void onSuccessAction(DatosConsultaPadron datosConsultaPadron) {

    }

    @Override
    public void onSuccessAction(List list) {
        mAdapter.addItems(list);
    }

    @Override
    public void onSuccessAction(Listado listado) {
        count = listado.getTotal();
        offset = listado.getOffset();

        switch (tipoListado.getId()) {
            case 2:
                mAdapter.addItems(listado.getContent());
                break;
            case 3:
                mAdapter.addItems(listado.getContent());
                break;
            case 5:
                mAdapter.addItems(listado.getContent());
                break;
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
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Object item);
    }
}
