package pro.kinect.traffic.Models;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.provider.BaseColumns;
import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pro.kinect.traffic.Main.App;

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

    public static AppItem createFromApplicationInfo(Context context, ApplicationInfo applicationInfo) {
        AppItem appItem = new AppItem();
        appItem.uid = applicationInfo.uid;
        appItem.name = getApplicationLabel(context, applicationInfo, Locale.getDefault());
        appItem.packageName = applicationInfo.packageName;
        return appItem;
    }

    private static String getApplicationLabel(Context context, ApplicationInfo applicationInfo, Locale locale) {
        return applicationInfo.name != null && !applicationInfo.name.isEmpty() ?
                applicationInfo.name :
                context.getPackageManager().getApplicationLabel(applicationInfo).toString()
                ;
    }


    public static List<AppItem> getFullData() {
        return new Select().from(AppItem.class)
                .where("last_absolute > 0")
                .orderBy("wifi_total + mobile_total DESC")
                .limit(1000).execute();
    }

    public static List<AppItem> getDataForServer() {
        return new Select().from(AppItem.class)
                .where("mobile_counter > 0")
                .or("wifi_counter > 0")
                .orderBy("wifi_total + mobile_total DESC")
                .limit(1000).execute();
    }

    public static void clearItem(String uid) {
        new Delete().from(AppItem.class).where("uid = ?", uid).execute();
    }
}