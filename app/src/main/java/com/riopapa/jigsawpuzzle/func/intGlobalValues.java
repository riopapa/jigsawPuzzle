package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.MainActivity.allLocked;
import static com.riopapa.jigsawpuzzle.MainActivity.baseX;
import static com.riopapa.jigsawpuzzle.MainActivity.baseY;
import static com.riopapa.jigsawpuzzle.MainActivity.fps;
import static com.riopapa.jigsawpuzzle.MainActivity.fullHeight;
import static com.riopapa.jigsawpuzzle.MainActivity.fullWidth;
import static com.riopapa.jigsawpuzzle.MainActivity.gapSize;
import static com.riopapa.jigsawpuzzle.MainActivity.innerSize;
import static com.riopapa.jigsawpuzzle.MainActivity.jPosX;
import static com.riopapa.jigsawpuzzle.MainActivity.jigCOLUMNs;
import static com.riopapa.jigsawpuzzle.MainActivity.jigROWs;
import static com.riopapa.jigsawpuzzle.MainActivity.offsetC;
import static com.riopapa.jigsawpuzzle.MainActivity.offsetR;
import static com.riopapa.jigsawpuzzle.MainActivity.outerSize;
import static com.riopapa.jigsawpuzzle.MainActivity.picGap;
import static com.riopapa.jigsawpuzzle.MainActivity.picHSize;
import static com.riopapa.jigsawpuzzle.MainActivity.picISize;
import static com.riopapa.jigsawpuzzle.MainActivity.picOSize;
import static com.riopapa.jigsawpuzzle.MainActivity.piece;
import static com.riopapa.jigsawpuzzle.MainActivity.puzzleSize;
import static com.riopapa.jigsawpuzzle.MainActivity.recySize;
import static com.riopapa.jigsawpuzzle.MainActivity.screenX;
import static com.riopapa.jigsawpuzzle.MainActivity.screenY;
import static com.riopapa.jigsawpuzzle.MainActivity.showMax;
import static com.riopapa.jigsawpuzzle.MainActivity.showShift;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import com.riopapa.jigsawpuzzle.Piece;

import java.util.ArrayList;

public class intGlobalValues {
    public intGlobalValues(Context context, Activity activity) {

//        float pxVal, dipVal, dipRatio;
//        pxVal = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 1000f,
//                context.getResources().getDisplayMetrics());
        float dipVal = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                1000f, context.getResources().getDisplayMetrics());
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        screenX = metrics.widthPixels;
        screenY = metrics.heightPixels;
        Log.w("r23 Main","screenXY "+screenX+" x "+screenY);

        int szW = fullWidth / (jigCOLUMNs+1);
        int szH = fullHeight / (jigROWs+1);
        innerSize = Math.min(szW, szH);
        outerSize = innerSize * (14+5+5) / 14;
        gapSize = innerSize *5/14;

        recySize = (int) ((float) screenX * dipVal / 2000f / 8f);   // 8f is puzzle size dependency

        picOSize = recySize; //  * 11 / 10;
        picISize = picOSize * 14 / (14+5+5);
        picHSize = picOSize / 2;
        picGap = picISize * 5 / 24;

        // note 20 pxVal=1000.0 dipVal=3000.0 innerSize = 468 ScreenX 1080 x 2316
        // Tab 7   pxVal=1000.0 dipVal=2125.0 innerSize = 331 ScreenX 1600 x 2560

//        Log.w("r25 "+Build.MODEL, "pxVal="+pxVal+", dipVal="+dipVal+", mmVal="+mmVal+" recSz="+recySize);

        jPosX = -1; // prevent drawing without preload

        showMax = screenX / picISize - 1;
        if (showMax > jigCOLUMNs)
            showMax = jigCOLUMNs;

        if (showMax > jigROWs)
            showMax = jigROWs;

        showShift = showMax / 2;

        puzzleSize = showMax * picISize;

        offsetC = 0; offsetR = 0;

        fps = new ArrayList<>();

        allLocked = false;
        piece = new Piece(context, outerSize, gapSize, innerSize);
//        new Handler().postDelayed(() -> {
            baseX = (screenX - puzzleSize) / 2 - picGap - picGap;
            baseY = (screenY - puzzleSize) / 2 - picOSize + picGap;
//            baseX = imageAnswer.getLeft() - picGap;
//            baseY = imageAnswer.getTop() - picGap;
            Log.w("r21 sizeCheck","image "+ fullWidth +" x "+ fullHeight +", outerSize="+ outerSize +", gapSize="+ gapSize +", innerSize="+ innerSize);
            Log.w("r21 sizeCheck","picOSize="+ picOSize +", picISize="+ picISize +
                    ", base XY ="+baseX+" x "+ baseY + " picGap="+picGap);

//        }, 10);

    }

}
