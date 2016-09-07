package pro.kinect.traffic;

import android.app.Application;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;

import pro.kinect.traffic.models.AppItem;
import pro.kinect.traffic.receiver.AlarmReceiver;

/**
 * Created by http://kinect.pro © 07.09.16
 * Developer Andrii.Gakhov
 */

public class App extends Application {
    public static final String LOG = "Traffic_LOG";
    public static final int TIME_UPDATE_TRAFFIC_COUNTERS = 1 * 60 * 1000; //1 min
//    public static final int TIME_FLUSH_TRAFFIC_COUNTERS = 15 * 60 * 1000; //15 min
    public static final int TIME_FLUSH_TRAFFIC_COUNTERS = 1 * 60 * 1000; //1 min

    @Override
    public void onCreate() {
        Log.d(App.LOG, "MainActivity.class -> isAppServiceRunning() - true" );
        super.onCreate();

        Configuration.Builder configurationBuilder = new Configuration.Builder(this);
        configurationBuilder.addModelClass(AppItem.class);
        configurationBuilder.setDatabaseVersion(1);
        configurationBuilder.setDatabaseName("traffic.db");

        ActiveAndroid.initialize(configurationBuilder.create());

        // Start alarm
        AlarmReceiver.schedule(getApplicationContext());
    }
}
