package com.riopapa.jigsawpuzzle.func;


import static com.riopapa.jigsawpuzzle.ActivityMain.screenX;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenY;
import static com.riopapa.jigsawpuzzle.ActivityMain.GVal;

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

        GVal.showMaxX = screenX / GVal.picISize - 2;
        if (GVal.showMaxX > GVal.jigCOLs)
            GVal.showMaxX = GVal.jigCOLs;

        GVal.showMaxY = screenY / GVal.picISize - 8;
        if (GVal.showMaxY > GVal.jigROWs)
            GVal.showMaxY = GVal.jigROWs;

        GVal.showShiftX = GVal.showMaxX * 3 / 4;
        GVal.showShiftY = GVal.showMaxY * 3 / 4;

        GVal.offsetC = 0; GVal.offsetR = 0;

        GVal.fps = new ArrayList<>();

        GVal.allLocked = false;
        GVal.baseX = (screenX - GVal.showMaxX * GVal.picISize) / 2 - GVal.picGap - GVal.picGap;
        GVal.baseY = (screenY - GVal.showMaxY * GVal.picISize) / 2 - GVal.picISize - GVal.picISize;

        Log.w("r23 ClearGlobalValues", "Jig Cnt="+ GVal.jigCOLs +" x "+ GVal.jigROWs+", showShift "+ GVal.showShiftX+"x"+ GVal.showShiftY +
                ", showMax "+ GVal.showMaxX+"x"+ GVal.showMaxY);

    }

}
