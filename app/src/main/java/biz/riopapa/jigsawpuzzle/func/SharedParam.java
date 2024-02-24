package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityMain.share_appVersion;
import static biz.riopapa.jigsawpuzzle.ActivityMain.share_backColor;
import static biz.riopapa.jigsawpuzzle.ActivityMain.share_installDate;
import static biz.riopapa.jigsawpuzzle.ActivityMain.share_showBack;
import static biz.riopapa.jigsawpuzzle.ActivityMain.share_sound;
import static biz.riopapa.jigsawpuzzle.ActivityMain.share_vibrate;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedParam {
    final String params = "params";
    public void get (Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(params, Context.MODE_PRIVATE);

        share_installDate = sharedPref.getLong("installDate", System.currentTimeMillis() / 24 / 60 / 60 / 1000);
        share_showBack = sharedPref.getInt("showBack", 1);
        share_vibrate = sharedPref.getBoolean("vibrate", true);
        share_sound = sharedPref.getBoolean("sound", true);
        share_backColor = sharedPref.getInt("backColor", 0);
        share_appVersion = sharedPref.getString("appVersion", "none");
    }

    public void put(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(params, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putLong("installDate", share_installDate);
        editor.putInt("showBack", share_showBack);
        editor.putBoolean("vibrate", share_vibrate);
        editor.putBoolean("sound", share_sound);
        editor.putInt("backColor", share_backColor);
        editor.putString("appVersion", share_appVersion);
        editor.apply();
    }
}
