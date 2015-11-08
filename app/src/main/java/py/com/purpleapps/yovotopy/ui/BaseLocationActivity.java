package py.com.purpleapps.yovotopy.ui;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by luisrodriguez on 3/11/15.
 */
public class BaseLocationActivity extends AppCompatActivity {
    public static final String TAG = BaseLocationActivity.class.getSimpleName();
    private static final int REQUEST_CHECK_SETTINGS = 0;
    private static final int GOOGLE_PLAY_SERVICES_AVAILABLE = 2;
    public static Location currentLocation;
    public ReactiveLocationProvider reactiveLocationProvider;

    public Observable<Location> lastKnownLocationObservable;

    public Subscription lastKnownLocationSubscription;
    protected int startCount = 0;

    public static Location getCurrentLocation() {
        return currentLocation;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            reactiveLocationProvider = new ReactiveLocationProvider(this);

            setUpLastKnownLocationSettings();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_PLAY_SERVICES_AVAILABLE) {
            if (resultCode == RESULT_OK) {
                if (verifyGooglePlayServicesInstalled()) {

                    reactiveLocationProvider = new ReactiveLocationProvider(this);

                    setUpLastKnownLocationSettings();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        verifyGooglePlayServicesInstalled();
    }

    public void setUpLastKnownLocationSettings() {
        final LocationRequest lastKnownLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        lastKnownLocationObservable = reactiveLocationProvider.checkLocationSettings(
                new LocationSettingsRequest.Builder()
                        .addLocationRequest(lastKnownLocationRequest)
                        .build())
                .doOnNext(new Action1<LocationSettingsResult>() {
                    @Override
                    public void call(LocationSettingsResult locationSettingsResult) {
                        Status status = locationSettingsResult.getStatus();
                        if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                            try {
                                status.startResolutionForResult(BaseLocationActivity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException th) {
                                Log.e(TAG, "Error opening settings activity.", th);
                            }
                        }
                    }
                })
                .flatMap(new Func1<LocationSettingsResult, Observable<Location>>() {
                    @Override
                    public Observable<Location> call(LocationSettingsResult locationSettingsResult) {
                        return reactiveLocationProvider.getLastKnownLocation();
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        if (lastKnownLocationSubscription != null && !lastKnownLocationSubscription.isUnsubscribed()) {
            lastKnownLocationSubscription.unsubscribe();
        }

        super.onStop();

    }

    protected boolean verifyGooglePlayServicesInstalled() {

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int errorCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (apiAvailability.showErrorDialogFragment(this, errorCode, GOOGLE_PLAY_SERVICES_AVAILABLE)) {
            return false;
        }

        doGooglePlayServicesRequiredAction();
        return true;
    }

    protected void doGooglePlayServicesRequiredAction() {

    }
}
