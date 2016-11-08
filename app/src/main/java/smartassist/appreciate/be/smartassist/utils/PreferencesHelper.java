package smartassist.appreciate.be.smartassist.utils;

import android.content.Context;
import android.content.SharedPreferences;

import smartassist.appreciate.be.smartassist.activities.ScreenSaverActivity;

/**
 * Created by Inneke on 9/02/2015.
 */
public class PreferencesHelper
{
    private static final String PREFERENCES_NAME = "SmartAssistPrefs";

    private static final String PREFERENCE_TIMESTAMP = "timestamp";
    private static final String PREFERENCE_FLAT_ID = "flat_id";
    private static final String PREFERENCE_FLAT_NAME = "flat_name";
    private static final String PREFERENCE_FLAT_PHOTO = "flat_photo";
    private static final String PREFERENCE_RESIDENCE_NAME = "residence_name";
    private static final String PREFERENCE_FLAT_NUMBER = "flat_number";
    private static final String PREFERENCE_HASH = "hash";
    private static final String PREFERENCE_ACCESS_TOKEN = "access_token";
    private static final String PREFERENCE_LOCATION_CITY = "location_city";
    private static final String PREFERENCE_LOCATION_LAT = "location_latitude";
    private static final String PREFERENCE_LOCATION_LONG = "location_longitude";
    public static final String PREFERENCE_UNREAD_NEWS = "news_unread";
    private static final String PREFERENCE_LAST_ALARM_CHECK = "last_alarm_check";
    private static final String PREFERENCE_PIN_ENABLED = "pin_enabled";
    private static final String PREFERENCE_SCREEN_SAVER_MODE = "screen_saver_mode";
    public static final String PREFERENCE_EMERGENCY_DOCTOR_ID = "emergency_doctor_id";
    public static final String PREFERENCE_EMERGENCY_PHARMACY_ID = "emergency_pharmacy_id";
    private static final String PREFERENCE_LAST_CHAT_SYNC = "last_chat_sync";
    private static final String PREFERENCE_RESIDENT_NEWS_PHOTO = "resident_news_photo";
    private static final String PREFERENCE_RESIDENT_INFO_PHOTO = "resident_info_photo";

    public static SharedPreferences getPreferences(Context context)
    {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static void clearConfiguration(Context context)
    {
        SharedPreferences.Editor prefs = PreferencesHelper.getPreferences(context).edit();
        prefs.remove(PREFERENCE_TIMESTAMP);
        prefs.remove(PREFERENCE_FLAT_ID);
        prefs.remove(PREFERENCE_FLAT_NAME);
        prefs.remove(PREFERENCE_FLAT_PHOTO);
        prefs.remove(PREFERENCE_RESIDENCE_NAME);
        prefs.remove(PREFERENCE_FLAT_NUMBER);
        prefs.remove(PREFERENCE_HASH);
        prefs.remove(PREFERENCE_ACCESS_TOKEN);
        prefs.remove(PREFERENCE_LOCATION_CITY);
        prefs.remove(PREFERENCE_LOCATION_LAT);
        prefs.remove(PREFERENCE_LOCATION_LONG);
        prefs.remove(PREFERENCE_UNREAD_NEWS);
        prefs.remove(PREFERENCE_LAST_ALARM_CHECK);
        prefs.remove(PREFERENCE_PIN_ENABLED);
        prefs.remove(PREFERENCE_SCREEN_SAVER_MODE);
        prefs.remove(PREFERENCE_EMERGENCY_DOCTOR_ID);
        prefs.remove(PREFERENCE_EMERGENCY_PHARMACY_ID);
        prefs.remove(PREFERENCE_LAST_CHAT_SYNC);
        prefs.remove(PREFERENCE_RESIDENT_INFO_PHOTO);
        prefs.remove(PREFERENCE_RESIDENT_NEWS_PHOTO);
        prefs.commit();
    }

    public static long getTimestamp(Context context)
    {
        return PreferencesHelper.getPreferences(context).getLong(PREFERENCE_TIMESTAMP, 0);
    }

    public static void saveTimestamp(Context context, long timestamp)
    {
        SharedPreferences.Editor prefs = PreferencesHelper.getPreferences(context).edit();
        prefs.putLong(PREFERENCE_TIMESTAMP, timestamp);
        prefs.commit();
    }

    public static int getFlatId(Context context)
    {
        return PreferencesHelper.getPreferences(context).getInt(PREFERENCE_FLAT_ID, 0);
    }

    public static void saveFlatId(Context context, int flatId)
    {
        SharedPreferences.Editor prefs = PreferencesHelper.getPreferences(context).edit();
        prefs.putInt(PREFERENCE_FLAT_ID, flatId);
        prefs.commit();
    }

    public static String getFlatName(Context context)
    {
        return PreferencesHelper.getPreferences(context).getString(PREFERENCE_FLAT_NAME, null);
    }

    public static void saveFlatName(Context context, String flatName)
    {
        SharedPreferences.Editor prefs = PreferencesHelper.getPreferences(context).edit();
        prefs.putString(PREFERENCE_FLAT_NAME, flatName);
        prefs.commit();
    }

    public static String getFlatPhoto(Context context)
    {
        return PreferencesHelper.getPreferences(context).getString(PREFERENCE_FLAT_PHOTO, null);
    }

    public static void saveFlatPhoto(Context context, String flatPhoto)
    {
        SharedPreferences.Editor prefs = PreferencesHelper.getPreferences(context).edit();
        prefs.putString(PREFERENCE_FLAT_PHOTO, flatPhoto);
        prefs.commit();
    }

    public static String getResidenceName(Context context)
    {
        return PreferencesHelper.getPreferences(context).getString(PREFERENCE_RESIDENCE_NAME, null);
    }

    public static void saveResidenceName(Context context, String residenceName)
    {
        SharedPreferences.Editor prefs = PreferencesHelper.getPreferences(context).edit();
        prefs.putString(PREFERENCE_RESIDENCE_NAME, residenceName);
        prefs.commit();
    }

    public static String getFlatNumber(Context context)
    {
        return PreferencesHelper.getPreferences(context).getString(PREFERENCE_FLAT_NUMBER, null);
    }

    public static void saveFlatNumber(Context context, String flatNumber)
    {
        SharedPreferences.Editor prefs = PreferencesHelper.getPreferences(context).edit();
        prefs.putString(PREFERENCE_FLAT_NUMBER, flatNumber);
        prefs.commit();
    }

    public static String getHash(Context context)
    {
        return PreferencesHelper.getPreferences(context).getString(PREFERENCE_HASH, null);
    }

    public static void saveHash(Context context, String hash)
    {
        SharedPreferences.Editor prefs = PreferencesHelper.getPreferences(context).edit();
        prefs.putString(PREFERENCE_HASH, hash);
        prefs.commit();
    }

    public static String getAccessToken(Context context)
    {
        return PreferencesHelper.getPreferences(context).getString(PREFERENCE_ACCESS_TOKEN, null);
    }

    public static void saveAccessToken(Context context, String accessToken)
    {
        SharedPreferences.Editor prefs = PreferencesHelper.getPreferences(context).edit();
        prefs.putString(PREFERENCE_ACCESS_TOKEN, accessToken);
        prefs.commit();
    }

    public static String getCity(Context context)
    {
        return PreferencesHelper.getPreferences(context).getString(PREFERENCE_LOCATION_CITY, null);
    }

    public static void saveCity(Context context, String city)
    {
        SharedPreferences.Editor prefs = PreferencesHelper.getPreferences(context).edit();
        prefs.putString(PREFERENCE_LOCATION_CITY, city);
        prefs.commit();
    }

    public static float getLatitude(Context context)
    {
        return PreferencesHelper.getPreferences(context).getFloat(PREFERENCE_LOCATION_LAT, 0);
    }

    public static void saveLatitude(Context context, float latitude)
    {
        SharedPreferences.Editor prefs = PreferencesHelper.getPreferences(context).edit();
        prefs.putFloat(PREFERENCE_LOCATION_LAT, latitude);
        prefs.commit();
    }

    public static float getLongitude(Context context)
    {
        return PreferencesHelper.getPreferences(context).getFloat(PREFERENCE_LOCATION_LONG, 0);
    }

    public static void saveLongitude(Context context, float longitude)
    {
        SharedPreferences.Editor prefs = PreferencesHelper.getPreferences(context).edit();
        prefs.putFloat(PREFERENCE_LOCATION_LONG, longitude);
        prefs.commit();
    }

    public static int getUnreadNews(Context context)
    {
        return PreferencesHelper.getPreferences(context).getInt(PREFERENCE_UNREAD_NEWS, 0);
    }

    public static void saveUnreadNews(Context context, int amount)
    {
        SharedPreferences.Editor prefs = PreferencesHelper.getPreferences(context).edit();
        prefs.putInt(PREFERENCE_UNREAD_NEWS, amount);
        prefs.commit();
    }

    public static long getLastAlarmCheck(Context context)
    {
        return PreferencesHelper.getPreferences(context).getLong(PREFERENCE_LAST_ALARM_CHECK, 0);
    }

    public static void saveLastAlarmCheck(Context context, long lastCheck)
    {
        SharedPreferences.Editor prefs = PreferencesHelper.getPreferences(context).edit();
        prefs.putLong(PREFERENCE_LAST_ALARM_CHECK, lastCheck);
        prefs.commit();
    }

    public static boolean isPinEnabled(Context context)
    {
        return PreferencesHelper.getPreferences(context).getBoolean(PREFERENCE_PIN_ENABLED, false);
    }

    public static void savePinEnabled(Context context, boolean pinEnabled)
    {
        SharedPreferences.Editor prefs = PreferencesHelper.getPreferences(context).edit();
        prefs.putBoolean(PREFERENCE_PIN_ENABLED, pinEnabled);
        prefs.commit();
    }

    public static int getScreenSaverMode(Context context)
    {
        return PreferencesHelper.getPreferences(context).getInt(PREFERENCE_SCREEN_SAVER_MODE, ScreenSaverActivity.TYPE_TIME);
    }

    public static void saveScreenSaverMode(Context context, int mode)
    {
        SharedPreferences.Editor prefs = PreferencesHelper.getPreferences(context).edit();
        prefs.putInt(PREFERENCE_SCREEN_SAVER_MODE, mode);
        prefs.commit();
    }

    public static int getEmergencyDoctorId(Context context)
    {
        return PreferencesHelper.getPreferences(context).getInt(PREFERENCE_EMERGENCY_DOCTOR_ID, ScreenSaverActivity.TYPE_TIME);
    }

    public static void saveEmergencyDoctorId(Context context, int id)
    {
        SharedPreferences.Editor prefs = PreferencesHelper.getPreferences(context).edit();
        prefs.putInt(PREFERENCE_EMERGENCY_DOCTOR_ID, id);
        prefs.commit();
    }

    public static int getEmergencyPharmacyId(Context context)
    {
        return PreferencesHelper.getPreferences(context).getInt(PREFERENCE_EMERGENCY_PHARMACY_ID, ScreenSaverActivity.TYPE_TIME);
    }

    public static void saveEmergencyPharmacyId(Context context, int id)
    {
        SharedPreferences.Editor prefs = PreferencesHelper.getPreferences(context).edit();
        prefs.putInt(PREFERENCE_EMERGENCY_PHARMACY_ID, id);
        prefs.commit();
    }

    public static long getLastChatSync(Context context)
    {
        return PreferencesHelper.getPreferences(context).getLong(PREFERENCE_LAST_CHAT_SYNC, 0);
    }

    public static void saveLastChatSync(Context context, long millis)
    {
        SharedPreferences.Editor prefs = PreferencesHelper.getPreferences(context).edit();
        prefs.putLong(PREFERENCE_LAST_CHAT_SYNC, millis);
        prefs.commit();
    }


    public static  void saveResidentNewsPhoto(Context context,String photo)
    {
        SharedPreferences.Editor prefs = PreferencesHelper.getPreferences(context).edit();
        prefs.putString(PREFERENCE_RESIDENT_NEWS_PHOTO, photo);
        prefs.commit();
    }

    public static  String  getResidentNewsPhoto(Context context)
    {
        return PreferencesHelper.getPreferences(context).getString(PREFERENCE_RESIDENT_NEWS_PHOTO, null);
    }

    public static  String  getResidentInfoPhoto(Context context)
    {
        return PreferencesHelper.getPreferences(context).getString(PREFERENCE_RESIDENT_INFO_PHOTO, null);

    }

    public static  void saveResidentInfoPhoto( Context context,String photo)
    {
        SharedPreferences.Editor prefs = PreferencesHelper.getPreferences(context).edit();
        prefs.putString(PREFERENCE_RESIDENT_INFO_PHOTO, photo);
        prefs.commit();
    }

}
