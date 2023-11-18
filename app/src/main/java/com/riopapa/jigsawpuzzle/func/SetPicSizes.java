package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityMain.fPhoneInchX;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenBottom;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenX;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenY;
import static com.riopapa.jigsawpuzzle.ActivityMain.vars;

public class SetPicSizes {
    public SetPicSizes(int phoneSizeX) {

        /*
        ** calculate picOSize, picISize, picHSize, picGap by phoneSize
         */

        vars.recSize =  (int) (phoneSizeX / fPhoneInchX / ((fPhoneInchX > 3f)? 1.1f:1.5f));

//        vars.recSize = vars.recSize * 9 / 7;   // while testing only

        vars.picOSize = vars.recSize;
        vars.picISize = vars.picOSize * 14 / (14+5+5);
        vars.picHSize = vars.picOSize / 2;
        vars.picGap = vars.picISize * 5 / 24;

        vars.showMaxX = screenX / vars.picISize - 2;
        vars.showMaxY = vars.showMaxX * screenY / screenX;

        screenBottom = screenY - vars.recSize - vars.recSize + vars.picGap;

    }
}
