package pro.kinect.traffic.Screens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import pro.kinect.traffic.Main.App;
import pro.kinect.traffic.Retrofit.Calls;
import pro.kinect.traffic.Main.Counters;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Counters.updateNetTrafficCounters(this, true);
        Calls.getInstance().pushDataToServer();

        finish();
    }
}
