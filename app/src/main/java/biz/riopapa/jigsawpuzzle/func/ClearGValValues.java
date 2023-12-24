package biz.riopapa.jigsawpuzzle.func;


import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenX;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenY;

import android.util.Log;

public class ClearGValValues {
    public ClearGValValues() {

//        float pxVal, dipVal, dipRatio;
//        pxVal = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 1000f,
//                context.getResources().getDisplayMetrics());

        // calculate piece size;

        // note 20 pxVal=1000.0 dipVal=3000.0 innerSize = 468 GVal.screenX 1080 x 2316
        // Tab 7   pxVal=1000.0 dipVal=2125.0 innerSize = 331 GVal.screenX 1600 x 2560

        gVal.showMaxX = screenX / gVal.picISize - 2;
        if (gVal.showMaxX > gVal.colNbr)
            gVal.showMaxX = gVal.colNbr;

        gVal.showMaxY = screenY / gVal.picISize - 8;
        if (gVal.showMaxY > gVal.rowNbr)
            gVal.showMaxY = gVal.rowNbr;

        gVal.showShiftX = gVal.showMaxX * 3 / 4;
        gVal.showShiftY = gVal.showMaxY * 3 / 4;

        gVal.offsetC = 0; gVal.offsetR = 0;

        gVal.allLocked = false;
        gVal.baseX = (screenX - gVal.showMaxX * gVal.picISize) / 2 - gVal.picGap - gVal.picGap;
        gVal.baseY = (screenY - gVal.showMaxY * gVal.picISize) * 20 / 30 - gVal.picOSize;

        Log.w("r23GValValues", "Jig Cnt="+ gVal.colNbr +"x"+ gVal.rowNbr +", showShift "+ gVal.showShiftX+"x"+ gVal.showShiftY +
                ", showMax "+ gVal.showMaxX+"x"+ gVal.showMaxY);

    }

}
