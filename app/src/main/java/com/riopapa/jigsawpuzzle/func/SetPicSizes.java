package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityMain.fPhoneInchX;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenBottom;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenX;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenY;
import static com.riopapa.jigsawpuzzle.ActivityMain.gVal;

public class SetPicSizes {
    public SetPicSizes(int phoneSizeX) {

        /*
        ** calculate picOSize, picISize, picHSize, picGap by phoneSize
         */

        gVal.recSize =  (int) (phoneSizeX / fPhoneInchX / ((fPhoneInchX > 3f)? 1.1f:1.5f));

//        GVal.recSize = GVal.recSize * 9 / 7;   // while testing only

        gVal.picOSize = gVal.recSize;
        gVal.picISize = gVal.picOSize * 14 / (14+5+5);
        gVal.picHSize = gVal.picOSize / 2;
        gVal.picGap = gVal.picISize * 5 / 24;

        gVal.showMaxX = screenX / gVal.picISize - 2;
        gVal.showMaxY = gVal.showMaxX * screenY / screenX;

        screenBottom = screenY - gVal.recSize - gVal.recSize + gVal.picGap;
        if (fPhoneInchX > 3f)
            screenBottom += gVal.picHSize;
    }
}
