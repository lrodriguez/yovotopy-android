package py.com.purpleapps.yovotopy.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by luisrodriguez on 10/11/15.
 */
public class BaseLocationFragment extends Fragment {
    protected BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.filterEquals(new Intent("py.com.purpleapps.yovotopy.LOCATION_UPDATED"))) {
                performOnLocationUpdatedAction();
            } else if (intent.filterEquals(new Intent("py.com.purpleapps.yovotopy.LOCATION_UPDATE_FAILED"))) {
                performOnLocationUpdateFailedAction();
            }
        }
    };
    Location currentLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver,
                new IntentFilter("py.com.purpleapps.yovotopy.LOCATION_UPDATED"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver,
                new IntentFilter("py.com.purpleapps.yovotopy.LOCATION_UPDATE_FAILED"));
    }

    void performOnLocationUpdatedAction() {
    }

    void performOnLocationUpdateFailedAction() {
    }
}
