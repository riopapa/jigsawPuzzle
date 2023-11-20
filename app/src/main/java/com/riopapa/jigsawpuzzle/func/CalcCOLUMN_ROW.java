package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageHeight;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageWidth;
import static com.riopapa.jigsawpuzzle.ActivityMain.GVal;

public class CalcCOLUMN_ROW {

    /**
     * calculate jigCOLUMNs, jigROWs initially by level info
     * @param chosenImageHeight, chosenImageWidth : image Height
     * @return jigCOLUMNs, jigROWs
     **/


    public CalcCOLUMN_ROW(int level) {

        int [] sizes = {10, 20, 30, 40};
        if (chosenImageWidth > chosenImageHeight) {
            GVal.jigCOLs = sizes[level];
            GVal.jigROWs = GVal.jigCOLs * chosenImageHeight / chosenImageWidth;  // to avoid over y size
        } else {
            GVal.jigROWs = sizes[level];
            GVal.jigCOLs = GVal.jigROWs * chosenImageWidth / chosenImageHeight;  // to avoid over y size
        }

    }
}