package com.riopapa.jigsawpuzzle.func;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riopapa.jigsawpuzzle.GVal;

import java.lang.reflect.Type;


public class VarsGetPut {

    public GVal get(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("saved", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPref.getString("GVal", "");
        Type type = new TypeToken<GVal>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public void put(GVal GVal, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("saved", Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedEditor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(GVal);
        sharedEditor.putString("GVal", json);
        sharedEditor.apply();
    }
}