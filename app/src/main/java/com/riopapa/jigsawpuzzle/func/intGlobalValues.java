package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.MainActivity.allLocked;
import static com.riopapa.jigsawpuzzle.MainActivity.baseX;
import static com.riopapa.jigsawpuzzle.MainActivity.baseY;
import static com.riopapa.jigsawpuzzle.MainActivity.fPhoneSizeX;
import static com.riopapa.jigsawpuzzle.MainActivity.fPhoneSizeY;
import static com.riopapa.jigsawpuzzle.MainActivity.fps;
import static com.riopapa.jigsawpuzzle.MainActivity.jPosX;
import static com.riopapa.jigsawpuzzle.MainActivity.jigCOLUMNs;
import static com.riopapa.jigsawpuzzle.MainActivity.jigROWs;
import static com.riopapa.jigsawpuzzle.MainActivity.offsetC;
import static com.riopapa.jigsawpuzzle.MainActivity.offsetR;
import static com.riopapa.jigsawpuzzle.MainActivity.picGap;
import static com.riopapa.jigsawpuzzle.MainActivity.picHSize;
import static com.riopapa.jigsawpuzzle.MainActivity.picISize;
import static com.riopapa.jigsawpuzzle.MainActivity.picOSize;
import static com.riopapa.jigsawpuzzle.MainActivity.screenX;
import static com.riopapa.jigsawpuzzle.MainActivity.screenY;
import static com.riopapa.jigsawpuzzle.MainActivity.showMaxX;
import static com.riopapa.jigsawpuzzle.MainActivity.showMaxY;
import static com.riopapa.jigsawpuzzle.MainActivity.showShiftX;
import static com.riopapa.jigsawpuzzle.MainActivity.showShiftY;

import android.util.Log;

import java.util.ArrayList;

public class intGlobalValues {
    public intGlobalValues() {

//        float pxVal, dipVal, dipRatio;
//        pxVal = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 1000f,
//                context.getResources().getDisplayMetrics());

        // calculate piece size;

//        recySize = (int) ((float) screenX * dipVal / 2000f / 8f);   // 8f is puzzle size dependency


        // note 20 pxVal=1000.0 dipVal=3000.0 innerSize = 468 ScreenX 1080 x 2316
        // Tab 7   pxVal=1000.0 dipVal=2125.0 innerSize = 331 ScreenX 1600 x 2560

//        Log.w("r25 "+Build.MODEL, "pxVal="+pxVal+", dipVal="+dipVal+", mmVal="+mmVal+" recSz="+recySize);

        jPosX = -1; // prevent drawing without preload

        showMaxX = screenX / picISize - 2;
        if (showMaxX > jigCOLUMNs)
            showMaxX = jigCOLUMNs;

        showMaxY = (int) (screenX * fPhoneSizeY / fPhoneSizeX / picISize - 2);

        if (showMaxY >  screenY/ picISize - 10)
            showMaxY =  screenY / picISize - 10;
        if (showMaxY > jigROWs)
            showMaxY = jigROWs;

        showShiftX = showMaxX * 2 / 3;
        showShiftY = showMaxY * 2 / 3;

        Log.w("show Info", "Jig Cnt="+jigCOLUMNs+" x "+ jigROWs+", showShift "+showShiftX+"x"+showShiftY +
                ", showMax "+showMaxX+"x"+showMaxY);

        offsetC = 0; offsetR = 0;

        fps = new ArrayList<>();

        allLocked = false;
//        new Handler().postDelayed(() -> {
            baseX = (screenX - showMaxX * picISize) / 2 - picGap - picGap;
            baseY = (screenY - showMaxY * picISize) / 2 - picOSize + picGap;
//            baseX = imageAnswer.getLeft() - picGap;
//            baseY = imageAnswer.getTop() - picGap;
            Log.w("r21 sizeCheck","picOSize="+ picOSize +", picISize="+ picISize +
                    ", picHSize ="+picHSize+ " picGap="+picGap);

//        }, 10);

    }

}
