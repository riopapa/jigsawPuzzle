package com.riopapa.jigsawpuzzle.func;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riopapa.jigsawpuzzle.GVal;
import com.riopapa.jigsawpuzzle.model.History;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class HistoryGetPut {

    final String hist = "history";
    public ArrayList<History> get(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(hist,Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPref.getString(hist, "");
        Type type = new TypeToken<History>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void put(ArrayList<History> histories, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(hist, Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedEditor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(histories);
        sharedEditor.putString(hist, json);
        sharedEditor.apply();
    }
}