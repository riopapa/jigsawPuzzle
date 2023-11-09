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

        if (vars.selectedWidth > vars.selectedHeight) {
            if (level == 0) {
                vars.jigCOLUMNs = vars.showMaxX * 12 / 10;
            } else if (level == 1) {
                vars.jigCOLUMNs = vars.showMaxX * 20 / 10;
            } else if (level == 2) {
                vars.jigCOLUMNs = vars.showMaxX * 30 / 10;
            } else {
                vars.jigCOLUMNs = vars.showMaxX * 45 / 10;
            }
            vars.jigROWs = vars.jigCOLUMNs * vars.selectedHeight / vars.selectedWidth;  // to avoid over y size
        } else {
            if (level == 0) {
                vars.jigROWs = vars.showMaxY * 12 / 10;
            } else if (level == 1) {
                vars.jigROWs = vars.showMaxY * 20 / 10;
            } else if (level == 2) {
                vars.jigROWs = vars.showMaxY * 30 / 10;
            } else {
                vars.jigROWs = vars.showMaxY * 45 / 10;
            }
            vars.jigCOLUMNs = vars.jigROWs * vars.selectedWidth / vars.selectedHeight;  // to avoid over y size
        }

    }
}