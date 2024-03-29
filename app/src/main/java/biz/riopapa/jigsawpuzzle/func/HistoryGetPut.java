package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityMain.histories;
import static biz.riopapa.jigsawpuzzle.ActivityMain.nowVersion;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import biz.riopapa.jigsawpuzzle.model.GVal;
import biz.riopapa.jigsawpuzzle.model.History;

public class HistoryGetPut {

    final String hist = "history";
    public ArrayList<History> get(Context context) {
        String histFile = hist + nowVersion;
        ArrayList<History> list;
        SharedPreferences sharedPref = context.getSharedPreferences(histFile,Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPref.getString(hist, "");
        if (json.isEmpty()) {
            list = new ArrayList<>();
        } else {
            Type type = new TypeToken<List<History>>() {}.getType();
            list = gson.fromJson(json, type);
        }
        return list;
    }

    public void put(ArrayList<History> histories, Context context) {
        String histFile = hist + nowVersion;
        SharedPreferences sharedPref = context.getSharedPreferences(histFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedEditor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(histories);
        sharedEditor.putString(hist, json);
        sharedEditor.apply();
    }

    public void set(Context context) {

        histories = new ArrayList<>();

        File prefsDir = new File(context.getApplicationInfo().dataDir, "shared_prefs");

        if (prefsDir.exists() && prefsDir.isDirectory()) {
            GValGetPut gValGetPut = new GValGetPut();
            String[] list = prefsDir.list();
            if (list != null) {
                for (String s : list) {
                    if (s.startsWith("g_")) {
                        String gameId = s.substring(2, s.length() - 4); // g_ xxxx .xml
                        GVal gval = gValGetPut.get(gameId, context);
                        if (gval != null && gval.version.equals(nowVersion))
                            add2History(gameId, gval);
                    }
                }
            }
            for (int i = 0; i < histories.size(); i++) {
                History h = histories.get(i);
                long lastTime = 0;
                for (int j = 0; j < 4; j++) {
                    if (h.time[j] > lastTime) {
                        h.latestLvl = j;
                        lastTime = h.time[j];
                    }
                }
                histories.set(i, h);
            }
        }
    }
    void add2History(String gameLevel, GVal gVal) {
        String game = gameLevel.substring(0,gameLevel.length()-1);
        int level = Integer.parseInt(gameLevel.substring(game.length()));
        History h;
        int hIdx = -1;
        for (int i = 0; i < histories.size(); i++) {
            if (histories.get(i).game.equals(game)) {
                hIdx = i;
                break;
            }
        }
        if (hIdx == -1)
            h = new History();
        else
            h = histories.get(hIdx);
        h.game = game;
        h.time[level] = gVal.time;
        int locked = 0;
        for (int ac = 0; ac < gVal.colNbr; ac++) {
            for (int ar = 0; ar < gVal.rowNbr; ar++) {
                if (gVal.jigTables[ac][ar].locked)
                    locked++;
            }
        }
        h.locked[level] = locked;
        h.percent[level] = locked * 100 / (gVal.colNbr * gVal.rowNbr);
        if (hIdx == -1)
            histories.add(h);
        else
            histories.set(hIdx, h);
    }
}