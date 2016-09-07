package pro.kinect.traffic;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

import pro.kinect.traffic.Models.AppItem;
import pro.kinect.traffic.Receivers.AlarmReceiver;

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
        Log.d(App.LOG, "AppService.class -> onHandleIntent()");

        Counters.updateNetTrafficCounters(getApplicationContext(), false);

        final long timestampNow = Calendar.getInstance().getTimeInMillis();
        long timestampLastFlush = Prefs.getLong(PREF_LAST_FLUSH_TIMESTAMP);
        if (timestampLastFlush == 0) {
            timestampLastFlush = timestampNow;
            Prefs.save(PREF_LAST_FLUSH_TIMESTAMP, timestampNow);
        }
        // Is time to flush?
        if (timestampNow > timestampLastFlush + App.TIME_FLUSH_TRAFFIC_COUNTERS) {
            // Sleep to let updater complete async db operations
            try {
                Thread.sleep(1000); // TODO: Make sure that ActiveAndroid will not sleep with this thread
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Log.d(App.LOG, "AppService.class --->> Flush traffic counters");

            //// TODO: 07.09.16  тут отправляем инфу на сервер и когда 200 - сохраняем таймстамп + чистим базу
            List<AppItem> itemList = AppItem.getData();
            if (itemList != null && itemList.size() > 0) {
                for (AppItem item : itemList) {
                    Log.d(App.LOG, "AppService.class ->"
//                            + " item.getId()" + item.getId()
//                            + ", item.uid: " + item.uid
//                            + ", item.packageName: " + item.packageName
//                            + " item.name: " + item.name
                            + ", item.count: " + String.valueOf(item.wifiTotal + item.mobileTotal)
                            + ", item.getJSON(): " + item.getJSON()
                    );
                }
            }

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
        Log.d(App.LOG, "AppService.class -> start()");
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
