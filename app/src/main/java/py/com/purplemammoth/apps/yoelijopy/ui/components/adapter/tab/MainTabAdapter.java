package py.com.purplemammoth.apps.yoelijopy.ui.components.adapter.tab;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import py.com.purplemammoth.apps.yoelijopy.ui.ConsultaPadronFragment;
import py.com.purplemammoth.apps.yoelijopy.ui.HomeFragment;
import py.com.purplemammoth.apps.yoelijopy.util.AppConstants;

/**
 * Created by luisrodriguez on 14/10/15.
 */
public class MainTabAdapter extends FragmentStatePagerAdapter {
    private static final int NO_PAGES = 4;

    private SparseArray<Fragment> mPageReferenceMap = new SparseArray<Fragment>();
    private FragmentManager manager;
    private Context context;

    public MainTabAdapter(FragmentManager manager, Context context) {
        super(manager);
        this.manager = manager;
        this.context = context;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);

        mPageReferenceMap.remove(position);
//        if (position <= getCount()) {
//            FragmentManager manager = ((Fragment) object).getFragmentManager();
//            FragmentTransaction trans = manager.beginTransaction();
//            trans.remove((Fragment) object);
//            trans.commit();
//        }
    }

    @Override
    public Fragment getItem(int position) {
        return getFragment(position);
    }

    /*@Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return titles[position];
    }*/

    public Fragment getFragment(int position) {
        Fragment fragment = null;

        if (mPageReferenceMap.get(position) == null) {
            switch (position) {
                case 0:
                    fragment = HomeFragment.newInstance();
                    break;
                case 1:
                    fragment = HomeFragment.newInstance();
                    break;
                case 2:
                    fragment = HomeFragment.newInstance();
                    break;
                case 3:
                    SharedPreferences sharedPreferences = context
                            .getSharedPreferences(AppConstants.PREFS_APP, 0);
                    String cedula = sharedPreferences.getString(AppConstants.PREFS_CEDULA,
                            AppConstants.EMPTY_STRING);
                    String fechaNac = sharedPreferences.getString(AppConstants.PREFS_FECHA_NAC,
                            AppConstants.EMPTY_STRING);
                    boolean hasProfile = sharedPreferences.getBoolean(AppConstants.PREFS_PROFILE,
                            false);

                    fragment = ConsultaPadronFragment.newInstance(cedula, fechaNac, hasProfile);
                    break;
            }

            putFragment(position, fragment);

            return fragment;
        }

        return mPageReferenceMap.get(position);
    }

    public void putFragment(int position, Fragment fragment) {
        mPageReferenceMap.put(position, fragment);
    }

    @Override
    public int getCount() {
        return NO_PAGES;
    }

    @Override
    public int getItemPosition(Object object) {
        /*if (object instanceof UsuarioFragment || object instanceof PremiosHomeFragment) {
            if (replace) {
                return POSITION_NONE;
            }
        }*/
        return POSITION_UNCHANGED;
    }
}
