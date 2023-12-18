package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageHeight;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageMap;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageWidth;
import static biz.riopapa.jigsawpuzzle.ActivityMain.currGame;
import static biz.riopapa.jigsawpuzzle.ActivityMain.currLevel;
import static biz.riopapa.jigsawpuzzle.ActivityMain.nowVersion;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenX;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import biz.riopapa.jigsawpuzzle.model.GVal;
import biz.riopapa.jigsawpuzzle.model.JigTable;


public class GValGetPut {

    public GVal get(String key, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("game_" + key,
                Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPref.getString("gVal", "");
        Type type = new TypeToken<GVal>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public void put(String key, GVal gVal, Context context) {
        gVal.game = currGame;
        gVal.version = nowVersion;
        gVal.level =  currLevel;
        gVal.time = System.currentTimeMillis();
        SharedPreferences sharedPref = context.getSharedPreferences("game_" + key,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedEditor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(gVal);
        sharedEditor.putString("gVal", json);
        sharedEditor.apply();
    }
    public void set(GVal gVal, int col, int row) {
        gVal.game = currGame;
        gVal.version = nowVersion;
        gVal.level =  currLevel;
        gVal.time = System.currentTimeMillis();
        gVal.fps = new ArrayList<>();
        gVal.colNbr = col;
        gVal.rowNbr = row;
        new SetPicSizes(screenX * (12 - currLevel) / 12);

        float szW = (float) chosenImageWidth / (float) (gVal.colNbr +1);
        float szH = (float) chosenImageHeight / (float) (gVal.rowNbr +1);
        gVal.imgInSize = (szH > szW) ? (int) szW : (int) szH;
        gVal.imgGapSize = gVal.imgInSize * 5 / 14;
        gVal.imgOutSize = gVal.imgInSize + gVal.imgGapSize + gVal.imgGapSize;
        chosenImageMap = Bitmap.createBitmap(chosenImageMap, 0, 0,
                gVal.imgInSize * gVal.colNbr + gVal.imgGapSize + gVal.imgGapSize,
                gVal.imgInSize * gVal.rowNbr + gVal.imgGapSize + gVal.imgGapSize);
        // refine map size
        chosenImageWidth = chosenImageMap.getWidth();
        chosenImageHeight = chosenImageMap.getHeight();
        gVal.jigTables = new JigTable[gVal.colNbr][gVal.rowNbr];
        new DefineTableWalls(gVal.jigTables);
        new ClearGValValues();
        new DefineFullRecyclePieces();
    }


}