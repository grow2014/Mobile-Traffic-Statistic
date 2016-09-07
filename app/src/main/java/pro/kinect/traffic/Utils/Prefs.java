package pro.kinect.traffic.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import pro.kinect.traffic.Main.App;

/**
 * Created by http://kinect.pro © 07.09.16
 * Developer Andrii.Gakhov
 */

public class Prefs {
    private static Context mContext;
    private static String	PREF_NAME	= "prefs";

    public static void init(Context context) {
        mContext = context;
    }

    public static void save(String _key, String _value) {
        if (mContext == null)
            return;
        SharedPreferences prefs = mContext.getSharedPreferences(PREF_NAME, 1);
        SharedPreferences.Editor prefsEdit = prefs.edit();

        prefsEdit.putString(_key, _value);
        prefsEdit.commit();
    }

    public static void save(String _key, int _value) {
        if (mContext == null)
            return;
        SharedPreferences prefs = mContext.getSharedPreferences(PREF_NAME, 1);
        SharedPreferences.Editor prefsEdit = prefs.edit();

        prefsEdit.putInt(_key, _value);
        prefsEdit.commit();
    }

    public static void save(String _key, boolean _value) {
        if (mContext == null)
            return;
        SharedPreferences prefs = mContext.getSharedPreferences(PREF_NAME, 1);
        SharedPreferences.Editor prefsEdit = prefs.edit();

        prefsEdit.putBoolean(_key, _value);
        prefsEdit.commit();
    }

    public static void save(String _key, float _value) {
        if (mContext == null)
            return;
        SharedPreferences prefs = mContext.getSharedPreferences(PREF_NAME, 1);
        SharedPreferences.Editor prefsEdit = prefs.edit();

        prefsEdit.putFloat(_key, _value);
        prefsEdit.commit();
    }

    public static void save(String _key, long _value) {
        if (mContext == null)
            return;
        SharedPreferences prefs = mContext.getSharedPreferences(PREF_NAME, 1);
        SharedPreferences.Editor prefsEdit = prefs.edit();

        prefsEdit.putLong(_key, _value);
        prefsEdit.commit();
    }

    public static void save(Context context, String _key, long _value) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, 1);
        SharedPreferences.Editor prefsEdit = prefs.edit();

        prefsEdit.putLong(_key, _value);
        prefsEdit.commit();
    }


    public static String getString(String _key) {
        return getString(_key, "");
    }

    public static String getString(String _key, String _def) {
        if (mContext == null) return _def;

        SharedPreferences prefs = mContext.getSharedPreferences(PREF_NAME, 1);
        String accessToken = prefs.getString(_key, _def);

        return accessToken;
    }


    public static boolean getBoolean(String _key) {
        return getBoolean(_key, false);
    }

    public static boolean getBoolean(String _key, boolean _def) {
        if (mContext == null)
            return false;
        SharedPreferences prefs = mContext.getSharedPreferences(PREF_NAME, 1);
        boolean accessToken = prefs.getBoolean(_key, _def);

        return accessToken;
    }

    public static int getInt(String _key) {
        return getInt(_key, 0);
    }

    public static int getInt(String _key, int _default_value) {
        if (mContext == null)
            return 0;
        SharedPreferences prefs = mContext.getSharedPreferences(PREF_NAME, 1);
        int accessToken = prefs.getInt(_key, _default_value);

        return accessToken;
    }

    public static float getFloat(String _key) {
        return getFloat(_key, 0);
    }

    public static float getFloat(String _key, int _default_value) {
        if (mContext == null)
            return 0;
        SharedPreferences prefs = mContext.getSharedPreferences(PREF_NAME, 1);
        float accessToken = prefs.getFloat(_key, _default_value);

        return accessToken;
    }

    public static long getLong(String _key) {
        return getLong(_key, 0);
    }

    public static long getLong(String _key, long _default_value) {
        if (mContext == null)
            return 0;

        SharedPreferences prefs = mContext.getSharedPreferences(PREF_NAME, 1);
        long accessToken = prefs.getLong(_key, _default_value);

        return accessToken;
    }

    public static long getLong(Context context, String _key, long _default_value) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, 1);
        long accessToken = prefs.getLong(_key, _default_value);

        return accessToken;
    }

}
