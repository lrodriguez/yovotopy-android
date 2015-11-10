package py.com.purpleapps.yovotopy.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import py.com.purpleapps.yovotopy.R;
import py.com.purpleapps.yovotopy.model.Candidato;
import py.com.purpleapps.yovotopy.util.AppConstants;

public class DetalleCandidatoActivity extends BaseLocationActivity {

    @Bind(R.id.nombre_candidato_text)
    TextView nombre;
    @Bind(R.id.candidatura_text)
    TextView candidatura;
    @Bind(R.id.lista_text)
    TextView lista;
    @Bind(R.id.partido_text)
    TextView partido;
    @Bind(R.id.distrito_text)
    TextView distrito;
    @Bind(R.id.departamento_text)
    TextView departamento;
    @Bind(R.id.puesto_text)
    TextView puesto;
    @Bind(R.id.orden_text)
    TextView orden;
    @Bind(R.id.puesto_row)
    TableRow puestoRow;
    @Bind(R.id.orden_row)
    TableRow ordenRow;

    Candidato candidato;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_candidato);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null) {
            candidato = getIntent().getExtras().getParcelable(AppConstants.ARG_CANDIDATO);
        }

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
    }

}
