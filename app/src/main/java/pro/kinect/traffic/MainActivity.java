package pro.kinect.traffic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(App.LOG, "MainActivity.class -> onCreate()");
        super.onCreate(savedInstanceState);
        finish();
    }


    @Override
    protected void onDestroy() {
        Log.d(App.LOG, "MainActivity.class -> onDestroy()");
        super.onDestroy();
    }
}
