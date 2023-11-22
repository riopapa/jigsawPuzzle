package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageHeight;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageWidth;
import static com.riopapa.jigsawpuzzle.ActivityMain.gVal;

public class CalcCOLUMN_ROW {

    /**
     * calculate jigCOLUMNs, jigROWs initially by level info
     * @param chosenImageHeight, chosenImageWidth : image Height
     * @return jigCOLUMNs, jigROWs
     **/


    public CalcCOLUMN_ROW(int level) {

        int [] sizes = {10, 20, 30, 40};
        if (chosenImageWidth > chosenImageHeight) {
            gVal.jigCOLs = sizes[level];
            gVal.jigROWs = gVal.jigCOLs * chosenImageHeight / chosenImageWidth;  // to avoid over y size
        } else {
            gVal.jigROWs = sizes[level];
            gVal.jigCOLs = gVal.jigROWs * chosenImageWidth / chosenImageHeight;  // to avoid over y size
        }

    }
}