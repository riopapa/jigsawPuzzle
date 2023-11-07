package com.riopapa.jigsawpuzzle.func;


import static com.riopapa.jigsawpuzzle.ActivityMain.vars;

import android.util.Log;

import java.util.ArrayList;

public class intGlobalValues {
    public intGlobalValues() {

//        float pxVal, dipVal, dipRatio;
//        pxVal = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 1000f,
//                context.getResources().getDisplayMetrics());

        // calculate piece size;

//        recySize = (int) ((float) vars.vars.screenX * dipVal / 2000f / 8f);   // 8f is puzzle size dependency


        // note 20 pxVal=1000.0 dipVal=3000.0 innerSize = 468 vars.vars.screenX 1080 x 2316
        // Tab 7   pxVal=1000.0 dipVal=2125.0 innerSize = 331 vars.vars.screenX 1600 x 2560

//        Log.w("r25 "+Build.MODEL, "pxVal="+pxVal+", dipVal="+dipVal+", mmVal="+mmVal+" recSz="+recySize);

        vars.showMaxX = vars.screenX / vars.picISize - 2;
        if (vars.showMaxX > vars.jigCOLUMNs)
            vars.showMaxX = vars.jigCOLUMNs;

        vars.showMaxY = (int) (vars.screenX * vars.fPhoneSizeY / vars.fPhoneSizeX / vars.picISize - 2);

        if (vars.showMaxY >  vars.screenY/ vars.picISize - 8)
            vars.showMaxY =  vars.screenY / vars.picISize - 8;
        if (vars.showMaxY > vars.jigROWs)
            vars.showMaxY = vars.jigROWs;

        vars.showShiftX = vars.showMaxX * 3 / 4;
        vars.showShiftY = vars.showMaxY * 3 / 4;

        Log.w("show Info", "Jig Cnt="+vars.jigCOLUMNs+" x "+ vars.jigROWs+", showShift "+vars.showShiftX+"x"+vars.showShiftY +
                ", showMax "+vars.showMaxX+"x"+vars.showMaxY);

        vars.offsetC = 0; vars.offsetR = 0;

        vars.fps = new ArrayList<>();

        vars.allLocked = false;
//        new Handler().postDelayed(() -> {
            vars.baseX = (vars.screenX - vars.showMaxX * vars.picISize) / 2 - vars.picGap - vars.picGap;
            vars.baseY = (vars.screenY - vars.showMaxY * vars.picISize) / 2 - vars.picOSize + vars.picGap;
//            baseX = imageAnswer.getLeft() - vars.picGap;
//            baseY = imageAnswer.getTop() - vars.picGap;
            Log.w("r21 sizeCheck","vars.picOSize="+ vars.picOSize +", vars.picISize="+ vars.picISize +
                    ", picHSize ="+vars.picHSize+ " vars.picGap="+vars.picGap);

//        }, 10);

    }

}
