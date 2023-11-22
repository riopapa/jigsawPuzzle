package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageHeight;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageMap;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageWidth;
import static com.riopapa.jigsawpuzzle.ActivityMain.currLevel;
import static com.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenX;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riopapa.jigsawpuzzle.GVal;
import com.riopapa.jigsawpuzzle.PieceImage;
import com.riopapa.jigsawpuzzle.model.JigTable;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class GValGetPut {

    public GVal get(String key, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPref.getString("gVal", "");
        Type type = new TypeToken<GVal>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public void put(String key, GVal gVal, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedEditor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(gVal);
        sharedEditor.putString("gVal", json);
        sharedEditor.apply();
    }
    public void set(GVal gVal) {
        gVal.gameLevel =  currLevel;
        gVal.fps = new ArrayList<>();

        new SetPicSizes(screenX * (12 - currLevel) / 12);
        new CalcCOLUMN_ROW(currLevel);

        float szW = (float) chosenImageWidth / (float) (gVal.jigCOLs+1);
        float szH = (float) chosenImageHeight / (float) (gVal.jigROWs+1);
        gVal.imgInSize = (szH > szW) ? (int) szW : (int) szH;
        gVal.imgGapSize = gVal.imgInSize * 5 / 24;
        gVal.imgOutSize = gVal.imgInSize + gVal.imgGapSize + gVal.imgGapSize;

        chosenImageMap = Bitmap.createBitmap(chosenImageMap, 0, 0,
                gVal.imgInSize * gVal.jigCOLs + gVal.imgGapSize + gVal.imgGapSize,
                gVal.imgInSize * gVal.jigROWs  + gVal.imgGapSize + gVal.imgGapSize);
        // refine map size
        chosenImageWidth = chosenImageMap.getWidth();
        chosenImageHeight = chosenImageMap.getHeight();
        Log.w("set", " size="+chosenImageWidth+"x"+chosenImageHeight);
        gVal.jigTables = new JigTable[gVal.jigCOLs][gVal.jigROWs];
        new SettleJigTableWall(gVal.jigTables);
        new ClearGlobalValues();

    }


}