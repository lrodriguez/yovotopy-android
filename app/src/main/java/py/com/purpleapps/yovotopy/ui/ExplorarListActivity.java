package py.com.purpleapps.yovotopy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.HashMap;

import py.com.purpleapps.yovotopy.R;
import py.com.purpleapps.yovotopy.model.Candidato;
import py.com.purpleapps.yovotopy.model.Departamento;
import py.com.purpleapps.yovotopy.model.Distrito;
import py.com.purpleapps.yovotopy.model.Partido;
import py.com.purpleapps.yovotopy.model.TipoListado;
import py.com.purpleapps.yovotopy.util.AppConstants;

public class ExplorarListActivity extends AppCompatActivity implements ExplorarFragment.OnListFragmentInteractionListener {

    private TipoListado tipoListado;
    private HashMap<String, String> bundleHash;
    private String mTitle;
    private String orderBy;
    private String departamento;
    private String distrito;
    private String partido;
    private String puesto;
    private String candidatura;

    private ExplorarFragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorar_list);


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        if (getIntent().getExtras() != null) {
            mTitle = getIntent().getStringExtra(AppConstants.ARG_ACTIVITY_TITLE);
            bundleHash = (HashMap<String, String>) getIntent()
                    .getSerializableExtra(AppConstants.ARG_ITEM_FILTER);
            tipoListado = TipoListado.valueOf(getIntent()
                    .getStringExtra(AppConstants.ARG_TIPO_LISTADO));
        }

        if (bundleHash != null) {
            partido = bundleHash.get(AppConstants.PARAM_PARTIDO);
            departamento = bundleHash.get(AppConstants.PARAM_DEPARTAMENTO);
            distrito = bundleHash.get(AppConstants.PARAM_DISTRITO);
            orderBy = bundleHash.get(AppConstants.PARAM_ORDER_BY);
            puesto = bundleHash.get(AppConstants.PARAM_PUESTO);
            candidatura = bundleHash.get(AppConstants.PARAM_CANDIDATURA);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (mTitle != null) {
            toolbar.setTitle(mTitle);
        }

        if (tipoListado == null) {
            tipoListado = TipoListado.valueOf(TipoListado.DEPARTAMENTO.name());
            toolbar.setTitle("Departamentos");
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            fragment = ExplorarFragment.newInstance(2, bundleHash, tipoListado.name());

            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .add(R.id.container, fragment, "explorar_fragment")
                    .commit();
        }
    }

    @Override
    public void onListFragmentInteraction(Object item) {
        if (item instanceof Departamento) {
            Intent i = new Intent(this, ExplorarListActivity.class);

            bundleHash = new HashMap<>();
            bundleHash.put(AppConstants.PARAM_DEPARTAMENTO, ((Departamento) item).getNombre());
            bundleHash.put(AppConstants.PARAM_DEPARTAMENTO, ((Departamento) item).getNombre());
            bundleHash.put(AppConstants.PARAM_ORDER_BY, "distancia");

            i.putExtra(AppConstants.ARG_ITEM_FILTER, bundleHash);
            i.putExtra(AppConstants.ARG_TIPO_LISTADO, TipoListado.DISTRITO.name());
            i.putExtra(AppConstants.ARG_ACTIVITY_TITLE, ((Departamento) item).getNombre());
            startActivity(i);
        } else if (item instanceof Distrito) {
            Intent i = new Intent(this, ExplorarListActivity.class);

            bundleHash = new HashMap<>();
            bundleHash.put(AppConstants.PARAM_DEPARTAMENTO, ((Distrito) item).getDepartamento());
            bundleHash.put(AppConstants.PARAM_DISTRITO, ((Distrito) item).getNombre());
            bundleHash.put(AppConstants.PARAM_ORDER_BY, "lista");

            i.putExtra(AppConstants.ARG_ITEM_FILTER, bundleHash);
            i.putExtra(AppConstants.ARG_TIPO_LISTADO, TipoListado.PARTIDO.name());
            i.putExtra(AppConstants.ARG_ACTIVITY_TITLE, ((Distrito) item).getNombre());
            startActivity(i);
        } else if (item instanceof Partido) {
            Intent i = new Intent(this, ExplorarListActivity.class);

            bundleHash = new HashMap<>();
            bundleHash.put(AppConstants.PARAM_DEPARTAMENTO, departamento);
            bundleHash.put(AppConstants.PARAM_DISTRITO, distrito);
            bundleHash.put(AppConstants.PARAM_PARTIDO, ((Partido) item).getNombre());
            bundleHash.put(AppConstants.PARAM_ORDER_BY, "nombre");

            i.putExtra(AppConstants.ARG_ITEM_FILTER, bundleHash);
            i.putExtra(AppConstants.ARG_TIPO_LISTADO, TipoListado.CANDIDATURA.name());
            i.putExtra(AppConstants.ARG_ACTIVITY_TITLE, ((Partido) item).getNombre());
            startActivity(i);
        } else if (item instanceof String) {
            Intent i = new Intent(this, ExplorarListActivity.class);

            String candidatura = ((String) item);

            bundleHash = new HashMap<>();
            bundleHash.put(AppConstants.PARAM_DEPARTAMENTO, departamento);
            bundleHash.put(AppConstants.PARAM_DISTRITO, distrito);
            bundleHash.put(AppConstants.PARAM_PARTIDO, partido);
            bundleHash.put(AppConstants.PARAM_CANDIDATURA, candidatura);

            if (candidatura.equalsIgnoreCase("JUNTA MUNICIPAL")) {
                bundleHash.put(AppConstants.PARAM_ORDER_BY, "puesto DESC, orden");
            }


            i.putExtra(AppConstants.ARG_ITEM_FILTER, bundleHash);
            i.putExtra(AppConstants.ARG_TIPO_LISTADO, TipoListado.CANDIDATO.name());
            i.putExtra(AppConstants.ARG_ACTIVITY_TITLE, ((String) item));
            startActivity(i);

        } else if (item instanceof Candidato) {

            Intent i = new Intent(this, DetalleCandidatoActivity.class);

            i.putExtra(AppConstants.ARG_CANDIDATO, (Candidato) item);
            startActivity(i);
        }
    }
}
