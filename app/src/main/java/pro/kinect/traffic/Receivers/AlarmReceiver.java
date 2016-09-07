package pro.kinect.traffic.Receivers;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.Calendar;

import pro.kinect.traffic.Main.App;
import pro.kinect.traffic.Main.AppService;

/**
 * Created by http://kinect.pro © 07.09.16
 * Developer Andrii.Gakhov
 */

public class AlarmReceiver extends WakefulBroadcastReceiver {

    public static final String INTENT_ALARM_TRAFFIC_COUNTERS = "pro.kinect.traffic.ALARM_TRAFFIC_COUNTERS";

    @Override
    public void onReceive(final Context context, Intent intent) {
        AppService.start(context, intent);
        setResultCode(Activity.RESULT_OK);

        // Next alarm
        schedule(context);
    }

    public static void schedule(Context context) {
        Calendar calUpdater = Calendar.getInstance();
        calUpdater.add(Calendar.MILLISECOND, App.TIME_UPDATE_TRAFFIC_COUNTERS);

        AlarmManager service = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intentAlarm = new Intent(INTENT_ALARM_TRAFFIC_COUNTERS);
        PendingIntent pending = PendingIntent.getBroadcast(context, 0, intentAlarm, PendingIntent.FLAG_CANCEL_CURRENT);
        service.set(AlarmManager.RTC_WAKEUP, calUpdater.getTimeInMillis(), pending);
    }
}
