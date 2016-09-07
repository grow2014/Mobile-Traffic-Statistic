package pro.kinect.traffic.Main;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import pro.kinect.traffic.Receivers.AlarmReceiver;
import pro.kinect.traffic.Retrofit.Calls;
import pro.kinect.traffic.Utils.Prefs;

/**
 * Created by http://kinect.pro © 07.09.16
 * Developer Andrii.Gakhov
 */

public class AppService extends IntentService {
    public static final String PREF_LAST_FLUSH_TIMESTAMP = "PREF_LAST_FLUSH_TIMESTAMP";

    public AppService() {
        super("AppService");
    }

    public AppService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Counters.updateNetTrafficCounters(getApplicationContext(), true);

        final long timestampNow = Calendar.getInstance().getTimeInMillis();
        long timestampLastFlush = Prefs.getLong(PREF_LAST_FLUSH_TIMESTAMP);
        if (timestampLastFlush == 0) {
            timestampLastFlush = timestampNow;
            Prefs.save(PREF_LAST_FLUSH_TIMESTAMP, timestampNow);
        }
        // Is time to flush?
        if (timestampNow > timestampLastFlush + App.TIME_FLUSH_TRAFFIC_COUNTERS) {
            Calls.getInstance().pushDataToServer();
            Prefs.save(PREF_LAST_FLUSH_TIMESTAMP, timestampNow);

        } else {
            Log.e(App.LOG, "AppService.class --->> timestampNow < timestampLastFlush");
            AlarmReceiver.completeWakefulIntent(intent);
        }
    }

    /**
     * Force start service
     *
     * @param context
     * @param intent
     */
    public static void start(Context context, Intent intent) {
        if (intent == null) {
            intent = new Intent(AlarmReceiver.INTENT_ALARM_TRAFFIC_COUNTERS);
        }
        // Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(
                context.getPackageName(),
                AppService.class.getName()
        );
        // Start the service, keeping the device awake while it is launching.
        AlarmReceiver.startWakefulService(context, (intent.setComponent(comp)));
    }
}
