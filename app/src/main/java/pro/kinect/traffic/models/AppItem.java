package pro.kinect.traffic.models;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.provider.BaseColumns;
import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pro.kinect.traffic.App;

/**
 * Created by http://kinect.pro © 07.09.16
 * Developer Andrii.Gakhov
 */

@Table(name = "apps",  id = BaseColumns._ID)
public class AppItem extends Model {

    @Column(name = "uid")
    public int uid;

    @Column(name = "name")
    public String name;

    @Column(name = "package_name")
    public String packageName;

    /**
     * Usage date without time e.g. getTimestamp(2014-09-05 00:00:00)
     */
    @Column(name = "timestamp")
    public long timestamp;

    @Column(name = "mobile_counter")
    public long mobileCounter;

    @Column(name = "wifi_counter")
    public long wifiCounter;

    @Column(name = "mobile_total")
    public long mobileTotal;

    @Column(name = "wifi_total")
    public long wifiTotal;

    @Column(name = "last_absolute")
    public long lastAbsolute;

    public static AppItem createFromApplicationInfo(Context context, ApplicationInfo applicationInfo, long timestamp) {
        Log.d(App.LOG, "AppItem.class -> createFromApplicationInfo()");
        AppItem appItem = new AppItem();
        appItem.uid = applicationInfo.uid;
        appItem.name = getApplicationLabel(context, applicationInfo, Locale.getDefault());
        appItem.packageName = applicationInfo.packageName;
        appItem.timestamp = timestamp;
        return appItem;
    }

    private static String getApplicationLabel(Context context, ApplicationInfo applicationInfo, Locale locale) {
        Log.d(App.LOG, "AppItem.class -> getApplicationLabel()");
        return applicationInfo.name != null && !applicationInfo.name.isEmpty() ?
                applicationInfo.name :
                context.getPackageManager().getApplicationLabel(applicationInfo).toString()
                ;
    }

    public JSONObject getJSON() {
        Log.d(App.LOG, "AppItem.class -> getJSON()");
        JSONObject result = new JSONObject();
        try {
            result.put("package", packageName);
            result.put("name", name);
            result.put("date", new java.sql.Date(timestamp).toString());
            result.put("wifi", wifiCounter);
            result.put("mobile", mobileCounter);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }


    public static List<AppItem> getData() {
        List<AppItem> result = new Select().from(AppItem.class).where("last_absolute > 0").limit(1000).execute();
        if (result == null) result = new ArrayList<>();
        return result;
    }

}