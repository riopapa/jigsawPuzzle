package com.riopapa.jigsawpuzzle.func;


import static com.riopapa.jigsawpuzzle.ActivityMain.screenX;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenY;
import static com.riopapa.jigsawpuzzle.ActivityMain.vars;

import android.util.Log;

import java.util.ArrayList;

public class ClearGlobalValues {
    public ClearGlobalValues() {

//        float pxVal, dipVal, dipRatio;
//        pxVal = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 1000f,
//                context.getResources().getDisplayMetrics());

        // calculate piece size;

//        recySize = (int) ((float) vars.screenX * dipVal / 2000f / 8f);   // 8f is puzzle size dependency


        // note 20 pxVal=1000.0 dipVal=3000.0 innerSize = 468 vars.screenX 1080 x 2316
        // Tab 7   pxVal=1000.0 dipVal=2125.0 innerSize = 331 vars.screenX 1600 x 2560

//        Log.w("r25 "+Build.MODEL, "pxVal="+pxVal+", dipVal="+dipVal+", mmVal="+mmVal+" recSz="+recySize);

        vars.showMaxX = screenX / vars.picISize - 2;
        if (vars.showMaxX > vars.jigCOLs)
            vars.showMaxX = vars.jigCOLs;

        vars.showMaxY = screenY / vars.picISize - 6;
        if (vars.showMaxY > vars.jigROWs)
            vars.showMaxY = vars.jigROWs;

        vars.showShiftX = vars.showMaxX * 3 / 4;
        vars.showShiftY = vars.showMaxY * 3 / 4;

        vars.offsetC = 0; vars.offsetR = 0;

        vars.fps = new ArrayList<>();

        vars.allLocked = false;
        vars.baseX = (screenX - vars.showMaxX * vars.picISize) / 2 - vars.picGap - vars.picGap;
        vars.baseY = (screenY - vars.showMaxY * vars.picISize) / 2;

        Log.w("r23 ClearGlobalValues", "Jig Cnt="+vars.jigCOLs +" x "+ vars.jigROWs+", showShift "+vars.showShiftX+"x"+vars.showShiftY +
                ", showMax "+vars.showMaxX+"x"+vars.showMaxY);

    }

}
