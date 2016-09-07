package pro.kinect.traffic.receiver;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.Calendar;

import pro.kinect.traffic.App;
import pro.kinect.traffic.AppService;

/**
 * Created by http://kinect.pro © 07.09.16
 * Developer Andrii.Gakhov
 */

public class AlarmReceiver extends WakefulBroadcastReceiver {

    public static final String INTENT_ALARM_TRAFFIC_COUNTERS = "pro.kinect.traffic.ALARM_TRAFFIC_COUNTERS";

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d(App.LOG, "AlarmReceiver.class -> onReceive()");

        AppService.start(context, intent);
        setResultCode(Activity.RESULT_OK);

        // Next alarm
        schedule(context);
    }

    public static void schedule(Context context) {
        Log.d(App.LOG, "AlarmReceiver.class -> schedule()");
        Calendar calUpdater = Calendar.getInstance();
        calUpdater.add(Calendar.MILLISECOND, App.TIME_UPDATE_TRAFFIC_COUNTERS);

        AlarmManager service = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intentAlarm = new Intent(INTENT_ALARM_TRAFFIC_COUNTERS);
        PendingIntent pending = PendingIntent.getBroadcast(context, 0, intentAlarm, PendingIntent.FLAG_CANCEL_CURRENT);
        service.set(AlarmManager.RTC_WAKEUP, calUpdater.getTimeInMillis(), pending);
    }
}
