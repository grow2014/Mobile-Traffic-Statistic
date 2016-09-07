package pro.kinect.traffic.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import pro.kinect.traffic.App;
import pro.kinect.traffic.AppService;

/**
 * Created by http://kinect.pro © 07.09.16
 * Developer Andrii.Gakhov
 */

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(App.LOG, "BootReceiver.class -> onReceive()");
        Intent intentService = new Intent(context, AppService.class);
        context.startService(intentService);
    }

}