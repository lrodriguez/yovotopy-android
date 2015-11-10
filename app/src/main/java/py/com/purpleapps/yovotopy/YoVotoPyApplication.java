package py.com.purpleapps.yovotopy;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by luisrodriguez on 26/10/15.
 */
public class YoVotoPyApplication extends Application {
    public static GoogleAnalytics analytics;

    // Google Analytics
    public static Tracker tracker;

    @Override
    public void onCreate() {
        super.onCreate();
        setupGATracker();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }

    public Tracker getTracker() {
        return tracker;
    }

    private void setupGATracker() {
        analytics = GoogleAnalytics.getInstance(this);

        analytics.setDryRun(false);
        analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);

        tracker = analytics.newTracker(R.xml.app_tracker); // Replace with actual tracker/property Id
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);
    }


}
