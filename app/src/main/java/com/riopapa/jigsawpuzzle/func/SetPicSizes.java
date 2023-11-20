package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityMain.fPhoneInchX;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenBottom;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenX;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenY;
import static com.riopapa.jigsawpuzzle.ActivityMain.GVal;

public class SetPicSizes {
    public SetPicSizes(int phoneSizeX) {

        /*
        ** calculate picOSize, picISize, picHSize, picGap by phoneSize
         */

        GVal.recSize =  (int) (phoneSizeX / fPhoneInchX / ((fPhoneInchX > 3f)? 1.1f:1.5f));

//        GVal.recSize = GVal.recSize * 9 / 7;   // while testing only

        GVal.picOSize = GVal.recSize;
        GVal.picISize = GVal.picOSize * 14 / (14+5+5);
        GVal.picHSize = GVal.picOSize / 2;
        GVal.picGap = GVal.picISize * 5 / 24;

        GVal.showMaxX = screenX / GVal.picISize - 2;
        GVal.showMaxY = GVal.showMaxX * screenY / screenX;

        screenBottom = screenY - GVal.recSize - GVal.recSize + GVal.picGap;
        if (fPhoneInchX > 3f)
            screenBottom += GVal.picHSize;
    }
}
