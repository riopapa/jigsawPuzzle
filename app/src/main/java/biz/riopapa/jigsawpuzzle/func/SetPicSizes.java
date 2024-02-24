package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityMain.fPhoneInchX;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenX;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenY;

public class SetPicSizes {
    public SetPicSizes(int phoneSizeX) {

        /*
        ** calculate picOSize, picISize, picHSize, picGap by phoneSize
         */

        gVal.recSize =  (int) (phoneSizeX / fPhoneInchX / ((fPhoneInchX > 3f)? 1.1f:1.6f));

        gVal.picOSize = gVal.recSize;
        gVal.picISize = gVal.picOSize * 14 / (14+5+5);
        gVal.picHSize = gVal.picOSize / 2;
        gVal.picGap = gVal.picISize * 5 / 24;

        gVal.showMaxX = screenX / gVal.picISize - 2;
        gVal.showMaxY = gVal.showMaxX * screenY / screenX;

    }
}
