package py.com.purpleapps.yovotopy.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.BindDrawable;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;
import py.com.purpleapps.yovotopy.R;
import py.com.purpleapps.yovotopy.model.Departamento;
import py.com.purpleapps.yovotopy.model.TipoListado;
import py.com.purpleapps.yovotopy.ui.components.adapter.tab.MainTabAdapter;
import py.com.purpleapps.yovotopy.util.AppConstants;
import rx.functions.Action1;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class MainTabActivity extends BaseLocationActivity implements
        HomeFragment.OnFragmentInteractionListener,
        ExplorarFragment.OnListFragmentInteractionListener,
        DenunciasFragment.OnFragmentInteractionListener,
        ConsultaPadronFragment.OnFragmentInteractionListener {
    private static final String SHOWCASE_ID = "showcase yovotopy";
    private static final int[] imageResId = {
            R.drawable.ic_home_white_24dp,
            R.drawable.ic_explore_white_24dp,
            R.drawable.ic_announcement_white_24dp,
            R.drawable.ic_account_circle_white_24dp
    };

    private static final String[] titles = {
            "Inicio",
            "Explorar",
            "Denuncias",
            "Perfil"
    };

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    @BindDrawable(R.drawable.ic_search_white_24dp)
    Drawable searchIcon;
    @BindDrawable(R.drawable.ic_send_white_24dp)
    Drawable sendIcon;

    private MainTabAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        mAdapter = new MainTabAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.view_tab, null);
            ImageView tabImage = ButterKnife.findById(view, R.id.tab_image);
            Drawable drawable = ContextCompat.getDrawable(this, imageResId[i]);
            tabImage.setImageDrawable(drawable);
            mTabLayout.getTabAt(i).setCustomView(tabImage);
//            mTabLayout.getTabAt(i).setIcon(imageResId[i]);
            mTabLayout.getTabAt(i).setContentDescription(titles[i]);
        }
        fab.hide();

        presentShowcaseSequence();
    }

    @OnClick(R.id.fab)
    void onFabClick() {
        if (mViewPager.getCurrentItem() == 0) {
            updateDatosPadron();
        } else if (mViewPager.getCurrentItem() == 2) {
            // llamar API denuncias
            DenunciasFragment denunciasFragment =
                    (DenunciasFragment) mAdapter.getFragment(2);
            denunciasFragment.validateForm();
        }
    }

    @OnPageChange(value = R.id.view_pager, callback = OnPageChange.Callback.PAGE_SELECTED)
    void onPageChanged(int position) {
        if (position == 0) {
            fab.show();
            fab.setImageDrawable(searchIcon);
        } else if (position == 2) {
            fab.show();
            fab.setImageDrawable(sendIcon);
        } else {
            fab.hide();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        lastKnownLocationSubscription = lastKnownLocationObservable
                .subscribe(new Action1<Location>() {
                    @Override
                    public void call(Location location) {
                        Log.i(TAG, "lastKnownLocationSuscription callback is running");
                        if (location != null) {
                            currentLocation = location;
                            Log.i(TAG, "Se obtuvo la localización: "
                                    + currentLocation.getLatitude() + ";"
                                    + currentLocation.getLongitude() + "("
                                    + currentLocation.getAccuracy() + ")");
                        } else {
                            Log.i(TAG, "Reactive Location: no se obtuvo la localización");
                        }
                    }
                });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void updateDatosPadron() {
        Intent i = new Intent(this, ConsultaPadronActivity.class);
        startActivity(i);
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
            /*Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);*/
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_about) {
            AppConstants.showAboutDialog(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(Object item) {
        if (item instanceof Departamento) {
            Intent i = new Intent(this, ExplorarListActivity.class);

            HashMap<String, String> bundleHash = new HashMap<>();
            bundleHash.put(AppConstants.PARAM_DEPARTAMENTO, ((Departamento) item).getNombre());
            bundleHash.put(AppConstants.PARAM_ORDER_BY, "distancia");

            i.putExtra(AppConstants.ARG_ITEM_FILTER, bundleHash);
            i.putExtra(AppConstants.ARG_TIPO_LISTADO, TipoListado.DISTRITO.name());
            i.putExtra(AppConstants.ARG_ACTIVITY_TITLE, ((Departamento) item).getNombre());
            startActivity(i);
        }
    }

    @Override
    public void showFab(boolean show, int iconId) {
        switch (iconId) {
            case 1:
                fab.setImageDrawable(searchIcon);
                break;
            case 2:
                fab.setImageDrawable(sendIcon);
                break;
        }

        if (show) {
            fab.show();
        } else {
            fab.hide();
        }
    }

    private void presentShowcaseSequence() {

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view
        config.setMaskColor(Color.argb(150, 103, 58, 183));

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);

        sequence.setConfig(config);

        MaterialShowcaseView targetView = new MaterialShowcaseView.Builder(this)
                .setTarget(mTabLayout.getTabAt(0).getCustomView())
                .setDismissText("ENTENDIDO")
                .setContentText("Inicio:\nAquí podrás ver informaciones útiles sobre las elecciones, " +
                        "dónde estás habilitado para votar así también como ver los candidatos en tu ciudad.")
                .setUseAutoRadius(false)
                .setRadius(100)
                .setMaskColour(Color.argb(150, 103, 58, 183))
                .build();
        sequence.addSequenceItem(targetView);

        targetView = new MaterialShowcaseView.Builder(this)
                .setTarget(mTabLayout.getTabAt(1).getCustomView())
                .setDismissText("ENTENDIDO")
                .setContentText("Explorar: \nAquí podrás consultar los candidatos por distrito, partido y lista")
                .setUseAutoRadius(false)
                .setRadius(100)
                .setMaskColour(Color.argb(150, 103, 58, 183))
                .build();
        sequence.addSequenceItem(targetView);

        targetView = new MaterialShowcaseView.Builder(this)
                .setTarget(mTabLayout.getTabAt(2).getCustomView())
                .setDismissText("ENTENDIDO")
                .setContentText("Denuncias: \nAquí podés enviar un reporte de una denuncia, " +
                        "describiendo el hecho irregular con posibilidad de adjuntar fotos y links")
                .setUseAutoRadius(false)
                .setRadius(100)
                .setMaskColour(Color.argb(150, 103, 58, 183))
                .build();
        sequence.addSequenceItem(targetView);

        targetView = new MaterialShowcaseView.Builder(this)
                .setTarget(mTabLayout.getTabAt(3).getCustomView())
                .setDismissText("ENTENDIDO")
                .setContentText("Perfil: \nUna vez que hayas guardado tu consulta al padrón, " +
                        "podrás visualizar aquí los datos referentes a tu votación para que no " +
                        "tengas que volver a realizar otra consulta")
                .setUseAutoRadius(false)
                .setRadius(100)
                .setMaskColour(Color.argb(150, 103, 58, 183))
                .build();
        sequence.addSequenceItem(targetView);

        sequence.addSequenceItem(fab, "Tocando aquí podrás consultar los datos del padrón electoral", "ENTENDIDO");

        sequence.start();

    }
}
