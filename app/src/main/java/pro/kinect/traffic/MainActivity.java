package pro.kinect.traffic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import pro.kinect.traffic.Models.AppItem;
import pro.kinect.traffic.Retrofit.Calls;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(App.LOG, "MainActivity.class -> onCreate()");
        super.onCreate(savedInstanceState);
        Counters.updateNetTrafficCounters(this, true);

        List<AppItem> itemList = AppItem.getData();
        if (itemList != null && itemList.size() > 0) {
            for (AppItem item : itemList) {
                Log.d(App.LOG, "AppService.class ->"
                        + ", item.uid: " + item.uid
                        + ", item.count: " + String.valueOf(item.wifiTotal + item.mobileTotal)
                        + " item.name: " + item.name
                        + ", item.packageName: " + item.packageName
                        + ", item.getJSON(): " + item.getJSON()
                );
            }
        }

        Calls.getInstance().pushDataToServer();

        finish();
    }


    @Override
    protected void onDestroy() {
        Log.d(App.LOG, "MainActivity.class -> onDestroy()");
        super.onDestroy();
    }
}
