package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityMain.vars;

public class SetPicSizes {
    public SetPicSizes(int phoneSizeX, int phoneSizeY) {

        /*
        ** determin picOSize, picISize, picHSize, picGap by phoneSize
         */

        vars.recySize =  (int) (phoneSizeX / vars.fPhoneInchX / ((vars.fPhoneInchX > 3f)? 1.5f:2f));

        vars.recySize = vars.recySize * 9 / 7;   // while testing only

        vars.picOSize = vars.recySize; //  * 11 / 10;
        vars.picISize = vars.picOSize * 14 / (14+5+5);
        vars.picHSize = vars.picOSize / 2;
        vars.picGap = vars.picISize * 5 / 24;

        vars.showMaxX = vars.screenX / vars.picISize - 2;
        vars.showMaxY = (int) (vars.screenX * vars.fPhoneInchY / vars.fPhoneInchX / vars.picISize - 2);

    }
}
