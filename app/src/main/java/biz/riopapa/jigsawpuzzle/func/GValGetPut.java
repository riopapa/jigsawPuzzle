package biz.riopapa.jigsawpuzzle.func;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import biz.riopapa.jigsawpuzzle.model.GVal;

public class GValGetPut {

    public GVal get(String gameLevel, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("g_" + gameLevel,
                Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPref.getString("gVal", "");
        Type type = new TypeToken<GVal>() {
        }.getType();
        GVal gval = gson.fromJson(json, type);
        if (gval !=  null) {
            int[] colRow = new DefineColsRows().calc(gval.level, gval.imgFullWidth, gval.imgFullHeight);
            if (gval.colNbr != colRow[0] || gval.rowNbr != colRow[1])
                gval = null;
        }
        return gval;
    }

    public void put(String gameLevel, GVal gVal, Context context) {
        gVal.time = System.currentTimeMillis();
        SharedPreferences sharedPref = context.getSharedPreferences("g_" + gameLevel,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedEditor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(gVal);
        sharedEditor.putString("gVal", json);
        sharedEditor.apply();
    }

}