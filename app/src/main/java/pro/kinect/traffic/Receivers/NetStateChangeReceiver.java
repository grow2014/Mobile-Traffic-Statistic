package pro.kinect.traffic.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import pro.kinect.traffic.Main.App;
import pro.kinect.traffic.Main.Counters;

/**
 * Created by http://kinect.pro © 07.09.16
 * Developer Andrii.Gakhov
 */

public class NetStateChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Counters.updateNetTrafficCounters(context, true);
    }
}
