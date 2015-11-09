package py.com.purpleapps.yovotopy.ui;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.HashMap;

import py.com.purpleapps.yovotopy.R;
import py.com.purpleapps.yovotopy.model.Departamento;
import py.com.purpleapps.yovotopy.model.TipoListado;
import py.com.purpleapps.yovotopy.ui.components.adapter.tab.MainTabAdapter;
import py.com.purpleapps.yovotopy.util.AppConstants;
import rx.functions.Action1;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainTabActivity extends BaseLocationActivity implements
        HomeFragment.OnFragmentInteractionListener,
        ExplorarFragment.OnListFragmentInteractionListener,
        DenunciasFragment.OnFragmentInteractionListener,
        ConsultaPadronFragment.OnFragmentInteractionListener {
    private static final int[] imageResId = {
            R.drawable.ic_home_white_24dp,
            R.drawable.ic_explore_white_24dp,
            R.drawable.ic_announcement_white_24dp,
            R.drawable.ic_account_circle_white_24dp
    };
    private static final String[] titles = {
            "Inicio",
            "Búsqueda",
            "Denuncias",
            "Perfil"
    };

    private MainTabAdapter mAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TabLayout mTabLayout = (TabLayout) findViewById(R.id.tab_layout);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mAdapter = new MainTabAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    fab.show();
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_search_white_24dp));
                } else if (position == 2) {
                    fab.show();
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_send_white_24dp));
                } else {
                    fab.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            mTabLayout.getTabAt(i).setIcon(imageResId[i]);
            mTabLayout.getTabAt(i).setContentDescription(titles[i]);
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewPager.getCurrentItem() == 0) {
                    updateDatosPadron();
                } else if (mViewPager.getCurrentItem() == 2) {
                    // llamar API denuncias
                    DenunciasFragment denunciasFragment =
                            (DenunciasFragment) mAdapter.getFragment(2);
                    denunciasFragment.validateForm();
                }
            }
        });
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
            /*Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);*/
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
}
