package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityMain.appVersion;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riopapa.jigsawpuzzle.model.History;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class HistoryGetPut {

    final String hist = "history";
    public ArrayList<History> get(Context context) {
        String histFile = hist + appVersion;
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
        String histFile = hist + appVersion;
        SharedPreferences sharedPref = context.getSharedPreferences(histFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedEditor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(histories);
        sharedEditor.putString(hist, json);
        sharedEditor.apply();
    }
}