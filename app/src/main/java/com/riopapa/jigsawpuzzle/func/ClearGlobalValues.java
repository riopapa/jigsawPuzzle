package com.riopapa.jigsawpuzzle.func;


import static com.riopapa.jigsawpuzzle.ActivityMain.screenX;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenY;
import static com.riopapa.jigsawpuzzle.ActivityMain.gVal;

import android.util.Log;

import java.util.ArrayList;

public class ClearGlobalValues {
    public ClearGlobalValues() {

//        float pxVal, dipVal, dipRatio;
//        pxVal = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 1000f,
//                context.getResources().getDisplayMetrics());

        // calculate piece size;

//        recySize = (int) ((float) GVal.screenX * dipVal / 2000f / 8f);   // 8f is puzzle size dependency


        // note 20 pxVal=1000.0 dipVal=3000.0 innerSize = 468 GVal.screenX 1080 x 2316
        // Tab 7   pxVal=1000.0 dipVal=2125.0 innerSize = 331 GVal.screenX 1600 x 2560

//        Log.w("r25 "+Build.MODEL, "pxVal="+pxVal+", dipVal="+dipVal+", mmVal="+mmVal+" recSz="+recySize);

        gVal.showMaxX = screenX / gVal.picISize - 2;
        if (gVal.showMaxX > gVal.jigCOLs)
            gVal.showMaxX = gVal.jigCOLs;

        gVal.showMaxY = screenY / gVal.picISize - 8;
        if (gVal.showMaxY > gVal.jigROWs)
            gVal.showMaxY = gVal.jigROWs;

        gVal.showShiftX = gVal.showMaxX * 3 / 4;
        gVal.showShiftY = gVal.showMaxY * 3 / 4;

        gVal.offsetC = 0; gVal.offsetR = 0;

        gVal.allLocked = false;
        gVal.baseX = (screenX - gVal.showMaxX * gVal.picISize) / 2 - gVal.picGap - gVal.picGap;
        gVal.baseY = (screenY - gVal.showMaxY * gVal.picISize) / 2 - gVal.picISize - gVal.picISize;

        Log.w("r23 ClearGlobalValues", "Jig Cnt="+ gVal.jigCOLs +" x "+ gVal.jigROWs+", showShift "+ gVal.showShiftX+"x"+ gVal.showShiftY +
                ", showMax "+ gVal.showMaxX+"x"+ gVal.showMaxY);

    }

}
