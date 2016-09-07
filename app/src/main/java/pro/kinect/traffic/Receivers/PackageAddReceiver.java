package pro.kinect.traffic.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import pro.kinect.traffic.App;
import pro.kinect.traffic.Counters;

/**
 * Created by http://kinect.pro © 07.09.16
 * Developer Andrii.Gakhov
 */

public class PackageAddReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(App.LOG, "PackageAddReceiver.class -> onReceive()");
        Counters.updateNetTrafficCounters(context, true);
    }
}