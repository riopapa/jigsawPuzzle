package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityMain.vars;

public class CalcCOLUMN_ROW {

    /**
     * calculate jigCOLUMNs, jigROWs initially by level info
     * @param selectedHeight, selectedWidth : image Height
     * @param showMaxX, showMaxY : physical phone max showing
     * @return jigCOLUMNs, jigROWs
     **/


    public CalcCOLUMN_ROW(int level) {

        int [] sizes = {15, 20, 30, 40};
        if (vars.selectedWidth > vars.selectedHeight) {
            vars.jigCOLs = sizes[level];
            vars.jigROWs = vars.jigCOLs * vars.selectedHeight / vars.selectedWidth;  // to avoid over y size
        } else {
            vars.jigROWs = sizes[level];
            vars.jigCOLs = vars.jigROWs * vars.selectedWidth / vars.selectedHeight;  // to avoid over y size
        }

    }
}