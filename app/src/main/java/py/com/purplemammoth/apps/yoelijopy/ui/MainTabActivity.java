package py.com.purplemammoth.apps.yoelijopy.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import org.json.JSONException;

import py.com.purplemammoth.apps.yoelijopy.R;
import py.com.purplemammoth.apps.yoelijopy.ui.components.adapter.tab.MainTabAdapter;

public class MainTabActivity extends AppCompatActivity implements
        HomeFragment.OnFragmentInteractionListener,
        ConsultaPadronFragment.OnFragmentInteractionListener {
    private static final int[] imageResId = {
            R.drawable.ic_home_white_24dp,
            R.drawable.ic_search_white_24dp,
            R.drawable.ic_announcement_white_24dp,
            R.drawable.ic_account_circle_white_24dp
    };
    private static final String[] titles = {
            "Inicio",
            "Búsqueda",
            "Denuncias",
            "Perfil"
    };
    private String cedulaText;
    private String fechaNacText;

    private MainTabAdapter mAdapter;

    private ViewPager mViewPager;
    private EditText cedula;
    private DatePicker fechaNacimiento;
    DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // TODO Validar cedula
            cedulaText = cedula.getText().toString();
            fechaNacText = fechaNacimiento.getDayOfMonth() + "/" + (fechaNacimiento.getMonth() + 1)
                    + "/" + (fechaNacimiento.getYear() % 100);

            // TODO comunicar al fragment de consultar
            Snackbar.make(mViewPager, "Cedula: " + cedulaText + "\nFecha de Nacimiento: "
                    + fechaNacText, Snackbar.LENGTH_LONG).setAction("Action", null).show();

            updateDatosPadron();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tab_layout);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mAdapter = new MainTabAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            mTabLayout.getTabAt(i).setIcon(imageResId[i]);
            mTabLayout.getTabAt(i).setContentDescription(titles[i]);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View dialogView = createDialog();
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainTabActivity.this)
                        .setView(dialogView)
                        .setTitle("Consultar datos de padrón")
                        .setPositiveButton("Consultar", onClickListener)
                        .setNegativeButton("Cancelar", null);
                dialog.show();
            }
        });
    }

    public View createDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_datos_consulta, null);

        cedula = (EditText) view.findViewById(R.id.editText);
        fechaNacimiento = (DatePicker) view.findViewById(R.id.datePicker);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            fechaNacimiento.setCalendarViewShown(false);
        }

        return view;
    }

    private void updateDatosPadron() {
        // TODO esto es temporal
        mViewPager.setCurrentItem(0, true);
        ConsultaPadronFragment consultaPadronFragment =
                (ConsultaPadronFragment) mAdapter.getFragment(0);

        try {
            consultaPadronFragment.consultaPadron(cedulaText, fechaNacText);
        } catch (JSONException e) {
            Log.e("MainTabActivity", "Ocurrió un error: " + e.getLocalizedMessage());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
