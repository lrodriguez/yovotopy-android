package py.com.purplemammoth.apps.yoelijopy.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tab_layout);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.view_pager);

        final MainTabAdapter mAdapter = new MainTabAdapter(getSupportFragmentManager(), this);
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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
