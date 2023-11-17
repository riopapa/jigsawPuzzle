package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageHeight;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageWidth;
import static com.riopapa.jigsawpuzzle.ActivityMain.vars;

public class CalcCOLUMN_ROW {

    /**
     * calculate jigCOLUMNs, jigROWs initially by level info
     * @param selectedHeight, selectedWidth : image Height
     * @param showMaxX, showMaxY : physical phone max showing
     * @return jigCOLUMNs, jigROWs
     **/


    public CalcCOLUMN_ROW(int level) {

        int [] sizes = {10, 20, 30, 40};
        if (chosenImageWidth > chosenImageHeight) {
            vars.jigCOLs = sizes[level];
            vars.jigROWs = vars.jigCOLs * chosenImageHeight / chosenImageWidth;  // to avoid over y size
        } else {
            vars.jigROWs = sizes[level];
            vars.jigCOLs = vars.jigROWs * chosenImageWidth / chosenImageHeight;  // to avoid over y size
        }

    }
}