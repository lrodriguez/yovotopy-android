package py.com.purpleapps.yovotopy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.HashMap;

import py.com.purpleapps.yovotopy.R;
import py.com.purpleapps.yovotopy.model.Candidato;
import py.com.purpleapps.yovotopy.model.Departamento;
import py.com.purpleapps.yovotopy.model.Distrito;
import py.com.purpleapps.yovotopy.model.TipoListado;
import py.com.purpleapps.yovotopy.util.AppConstants;

public class ExplorarListActivity extends AppCompatActivity implements ExplorarFragment.OnListFragmentInteractionListener {

    private TipoListado tipoListado;
    private HashMap<String, String> bundleHash;
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null) {
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
            if (orderBy == null || orderBy.isEmpty()) {
                bundleHash.put(AppConstants.PARAM_ORDER_BY, "distancia");
            } else {
                bundleHash.put(AppConstants.PARAM_ORDER_BY, orderBy);
            }

            i.putExtra(AppConstants.ARG_ITEM_FILTER, bundleHash);
            startActivity(i);
        } else if (item instanceof Distrito) {
            Intent i = new Intent(this, ExplorarListActivity.class);

            bundleHash = new HashMap<>();
            bundleHash.put(AppConstants.PARAM_DEPARTAMENTO, ((Distrito) item).getDepartamento());
            bundleHash.put(AppConstants.PARAM_DISTRITO, ((Distrito) item).getNombre());
            if (orderBy == null || orderBy.isEmpty() || orderBy.equalsIgnoreCase("distancia")) {
                bundleHash.put(AppConstants.PARAM_ORDER_BY, "nombre");
            } else {
                bundleHash.put(AppConstants.PARAM_ORDER_BY, orderBy);
            }

            i.putExtra(AppConstants.ARG_ITEM_FILTER, bundleHash);
            i.putExtra(AppConstants.ARG_TIPO_LISTADO, TipoListado.PARTIDO.name());
            startActivity(i);
        } else if (item instanceof String) {
            Intent i;
            switch (tipoListado.getId()) {
                case 3:
                    i = new Intent(this, ExplorarListActivity.class);

                    bundleHash = new HashMap<>();
                    bundleHash.put(AppConstants.PARAM_DEPARTAMENTO, departamento);
                    bundleHash.put(AppConstants.PARAM_DISTRITO, distrito);
                    bundleHash.put(AppConstants.PARAM_PARTIDO, ((String) item));
                    if (orderBy == null || orderBy.isEmpty()) {
                        bundleHash.put(AppConstants.PARAM_ORDER_BY, "nombre");
                    } else {
                        bundleHash.put(AppConstants.PARAM_ORDER_BY, orderBy);
                    }

                    i.putExtra(AppConstants.ARG_ITEM_FILTER, bundleHash);
                    i.putExtra(AppConstants.ARG_TIPO_LISTADO, TipoListado.CANDIDATURA.name());
                    startActivity(i);
                    break;
                case 4:
                    i = new Intent(this, ExplorarListActivity.class);

                    String candidatura = ((String) item);

                    bundleHash = new HashMap<>();
                    bundleHash.put(AppConstants.PARAM_DEPARTAMENTO, departamento);
                    bundleHash.put(AppConstants.PARAM_DISTRITO, distrito);
                    bundleHash.put(AppConstants.PARAM_PARTIDO, partido);
                    bundleHash.put(AppConstants.PARAM_CANDIDATURA, candidatura);

                    if (candidatura.equalsIgnoreCase("JUNTA MUNICIPAL")) {
                        bundleHash.put(AppConstants.PARAM_ORDEN, "1");
                    }

                    if (orderBy == null || orderBy.isEmpty() || orderBy.equalsIgnoreCase("nombre")) {
                        bundleHash.put(AppConstants.PARAM_ORDER_BY, "lista");
                    } else {
                        bundleHash.put(AppConstants.PARAM_ORDER_BY, orderBy);
                    }


                    i.putExtra(AppConstants.ARG_ITEM_FILTER, bundleHash);
                    i.putExtra(AppConstants.ARG_TIPO_LISTADO, TipoListado.CANDIDATO.name());
                    startActivity(i);
                    break;
            }

        } else if (item instanceof Candidato) {
            Toast.makeText(this, "Mostrar detalle de candidato", Toast.LENGTH_SHORT).show();
        }
    }
}
