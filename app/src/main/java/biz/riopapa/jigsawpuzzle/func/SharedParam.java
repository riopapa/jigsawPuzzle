package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityMain.nowVersion;
import static biz.riopapa.jigsawpuzzle.ActivityMain.share_appVersion;
import static biz.riopapa.jigsawpuzzle.ActivityMain.share_backColor;
import static biz.riopapa.jigsawpuzzle.ActivityMain.share_installDate;
import static biz.riopapa.jigsawpuzzle.ActivityMain.share_showBack;
import static biz.riopapa.jigsawpuzzle.ActivityMain.share_sound;
import static biz.riopapa.jigsawpuzzle.ActivityMain.share_vibrate;
import static biz.riopapa.jigsawpuzzle.ActivityMain.share_download;
import static biz.riopapa.jigsawpuzzle.ActivityMain.share_today;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedParam {

    final String shared = "shared";
    public void get (Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(shared, Context.MODE_PRIVATE);

        share_showBack = sharedPref.getInt("showBack", 1);
        share_vibrate = sharedPref.getBoolean("vibrate", true);
        share_sound = sharedPref.getBoolean("sound", true);
        share_backColor = sharedPref.getInt("backColor", 0);
    }

    public void put(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(shared, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("showBack", share_showBack);
        editor.putBoolean("vibrate", share_vibrate);
        editor.putBoolean("sound", share_sound);
        editor.putInt("backColor", share_backColor);
        editor.apply();
    }

    public void getBase (Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(shared, Context.MODE_PRIVATE);
        share_installDate = sharedPref.getLong("installDate", System.currentTimeMillis() / 24 / 60 / 60 / 1000);
        share_appVersion = sharedPref.getString("appVersion", nowVersion);
    }
    public void putBase(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(shared, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("installDate", share_installDate);
        editor.putString("appVersion", share_appVersion);
        editor.apply();
    }

    public void getToday (Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(shared, Context.MODE_PRIVATE);
        share_today = sharedPref.getLong("today",0);
        share_download = sharedPref.getInt("download", 0);
    }
    public void putToday(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(shared, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("today", share_today);
        editor.putInt("download", share_download);
        editor.apply();
    }
}
