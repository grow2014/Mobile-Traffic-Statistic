package pro.kinect.traffic;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.TrafficStats;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import pro.kinect.traffic.Models.AppItem;

/**
 * Created by http://kinect.pro © 07.09.16
 * Developer Andrii.Gakhov
 */

public class Counters {
    private final static String PACKAGE_NAME_SYSTEM = "android";
    private final static String PACKAGE_NAME_GOOGLE = "com.google.android.gms";
    private final static String PACKAGE_NAME_DOWNLOADS = "com.android.providers.downloads";
    public final static String PREF_MOBIL_STATE_LAST = "PREF_MOBIL_STATE_LAST";
    private static HashMap<String, Integer> sysUids;
    public static AppItem globalCounters = new AppItem();

    public static List<AppItem> updateNetTrafficCounters(Context context, boolean isForcePackageSync) {

        Log.d(App.LOG, "Counters.class -> updateNetTrafficCounters()");

        // Init
        if (sysUids == null) {
            initSystemUids(context);
        }

        // Get current isMobile state
        boolean isMobileCurrent = Networks.isConnectedMobile(context);
        // Use previously saved isMobile state for counters updating
        boolean isMobileLast = Counters.updateIsMobile(context, isMobileCurrent);

        // Load today's apps from db
//        List<AppItem> appsList = new Select()
//                .from(AppItem.class)
//                .where("timestamp >= ?", getDateTimestamp())
//                .orderBy("wifi_total + mobile_total DESC")
//                .execute();

        List<AppItem> appsList = AppItem.getData();

        // Fill list at first launch / new day or sync apps if new one was installed by user
        if (appsList.isEmpty() || isForcePackageSync) {
            syncApplicationList(context, appsList);
        }
        // Update counters
        synchronized (Counters.class) {
            for (AppItem appItem : appsList) {
                long currentAbsolute = TrafficStats.getUidRxBytes(appItem.uid) + TrafficStats.getUidTxBytes(appItem.uid);
                long delta = currentAbsolute - appItem.lastAbsolute;
                if (delta > 0) {
                    if (isMobileLast) {
                        appItem.mobileCounter += delta;
                        appItem.mobileTotal += delta;
                    } else {
                        appItem.wifiCounter += delta;
                        appItem.wifiTotal += delta;
                    }
                    appItem.lastAbsolute = currentAbsolute;
                    appItem.save();
                }
            }
        }
        // Update global counters
        long currentAbsolute = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes();
        long delta = currentAbsolute - globalCounters.lastAbsolute;
        if (isMobileLast) {
            globalCounters.mobileCounter += delta;
            globalCounters.mobileTotal += delta;
        } else {
            globalCounters.wifiCounter += delta;
            globalCounters.wifiTotal += delta;
        }
        globalCounters.lastAbsolute = currentAbsolute;

        return appsList;
    }


    public static String usageTrafficFormat(long bytes) {
        return
                bytes >= 107374182400l ? String.format("%.0f GB", (float) bytes / 1073741824l) : (
                        bytes >= 10737418240l ? String.format("%.1f GB", (float) bytes / 1073741824l) : (
                                bytes >= 1073741824l ? String.format("%.2f GB", (float) bytes / 1073741824l) : (
                                        bytes >= 104857600 ? String.format("%.0f MB", (float) bytes / 1048576) : (
                                                bytes >= 10485760 ? String.format("%.1f MB", (float) bytes / 1048576) : (
                                                        bytes >= 1048576 ? String.format("%.2f MB", (float) bytes / 1048576) : (
                                                                bytes >= 102400 ? String.format("%.0f KB", (float) bytes / 1024) : (
                                                                        bytes >= 10240 ? String.format("%.1f KB", (float) bytes / 1024) : (
                                                                                bytes >= 1024 ? String.format("%.2f KB", (float) bytes / 1024) :
                                                                                        String.format("%d", (int) bytes)
                                                                        ))))))));
    }

    /**
     * Synchronize given application list with installed/systems applications
     * <p/>
     * Note: During synchronization saves newly added records to db
     *
     * @param appsList Application list being synced
     */
    private static void syncApplicationList(Context context, List<AppItem> appsList) {
        Log.d(App.LOG, "Counters.class -> syncApplicationList()");

        // Index by package name
        HashMap<String, AppItem> appsHashMap = new HashMap<String, AppItem>();
        for (AppItem appItem : appsList) {
            appsHashMap.put(appItem.packageName, appItem);
        }

        // Get applications list
        List<ApplicationInfo> appsInfoList = getSystemApplications(); // system apps
        appsInfoList.addAll(context.getPackageManager().getInstalledApplications(0)); // installed apps

        // Append new day records or newly installed apps to db
        for (final ApplicationInfo appInfo : appsInfoList) {
            // skip system applications
            if (
                    (sysUids.get(PACKAGE_NAME_SYSTEM) != null) && (appInfo.uid == sysUids.get(PACKAGE_NAME_SYSTEM) && !PACKAGE_NAME_SYSTEM.equals(appInfo.packageName))
                            ||
                            (sysUids.get(PACKAGE_NAME_GOOGLE) != null) && (appInfo.uid == sysUids.get(PACKAGE_NAME_GOOGLE) && !PACKAGE_NAME_GOOGLE.equals(appInfo.packageName))
                            ||
                            (sysUids.get(PACKAGE_NAME_DOWNLOADS) != null) && (appInfo.uid == sysUids.get(PACKAGE_NAME_DOWNLOADS) && !PACKAGE_NAME_DOWNLOADS.equals(appInfo.packageName))
                    ) {
                continue;
            }
            // Create if not exists in db
            if (appsHashMap.get(appInfo.packageName) == null) {
                AppItem appItem = AppItem.createFromApplicationInfo(context, appInfo);
                // First time get current absolute callCounter
                appItem.lastAbsolute = TrafficStats.getUidRxBytes(appItem.uid) + TrafficStats.getUidTxBytes(appItem.uid);
                appItem.save();
                appsList.add(appItem);
            }
        }
    }

    /**
     * Updates value in shared prefs and returns previous one
     */
    private static boolean updateIsMobile(Context context, boolean isMobileCurrent) {
        Prefs.init(context);
        boolean isMobileLast = Prefs.getBoolean(PREF_MOBIL_STATE_LAST, isMobileCurrent);
        if (isMobileCurrent != isMobileLast) {
            Prefs.save(PREF_MOBIL_STATE_LAST, isMobileCurrent);
        }
        return isMobileLast;
    }


    public static long getDateTimestamp() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }

    /**
     * UIDs that have more than one application installed
     */
    private static void initSystemUids(Context context) {
        sysUids = new HashMap<String, Integer>();
        for (ApplicationInfo appInfo : context.getPackageManager().getInstalledApplications(0)) {
            if (
                    appInfo.packageName.equals(PACKAGE_NAME_SYSTEM)
                            ||
                            appInfo.packageName.equals(PACKAGE_NAME_GOOGLE)
                            ||
                            appInfo.packageName.equals(PACKAGE_NAME_DOWNLOADS)
                    ) {
                sysUids.put(appInfo.packageName, appInfo.uid);
            }
        }
    }

    /**
     * Hardcoded list of system UIDs
     */
    private static List<ApplicationInfo> getSystemApplications() {
        return new ArrayList<ApplicationInfo>() {{
            add(new ApplicationInfo() {{
                uid = 0;
                packageName = "root";
                name = "Root";
            }});
            add(new ApplicationInfo() {{
                uid = 1000;
                packageName = "system";
                name = "System services";
            }});
            add(new ApplicationInfo() {{
                uid = 1001;
                packageName = "radio";
                name = "Radio";
            }});
            add(new ApplicationInfo() {{
                uid = 1002;
                packageName = "bluetooth";
                name = "Bluetooth";
            }});
            add(new ApplicationInfo() {{
                uid = 1003;
                packageName = "graphics";
                name = "Core graphics";
            }});
            add(new ApplicationInfo() {{
                uid = 1004;
                packageName = "input";
                name = "Input method service";
            }});
            add(new ApplicationInfo() {{
                uid = 1005;
                packageName = "audio";
                name = "Audio services";
            }});
            add(new ApplicationInfo() {{
                uid = 1006;
                packageName = "camera";
                name = "Camera";
            }});
            add(new ApplicationInfo() {{
                uid = 1007;
                packageName = "log";
                name = "System logger";
            }});
            add(new ApplicationInfo() {{
                uid = 1008;
                packageName = "compass";
                name = "Compass service";
            }});
            add(new ApplicationInfo() {{
                uid = 1009;
                packageName = "mount";
                name = "Storage mount service";
            }});
            add(new ApplicationInfo() {{
                uid = 1010;
                packageName = "wifi";
                name = "WiFi";
            }});
            add(new ApplicationInfo() {{
                uid = 1011;
                packageName = "adb";
                name = "ADB";
            }});
            add(new ApplicationInfo() {{
                uid = 1012;
                packageName = "install";
                name = "Android installer";
            }});
            add(new ApplicationInfo() {{
                uid = 1013;
                packageName = "media";
                name = "Media streaming service";
            }});
            add(new ApplicationInfo() {{
                uid = 1014;
                packageName = "dhcp";
                name = "DHCP";
            }});
        }};
    }
}
