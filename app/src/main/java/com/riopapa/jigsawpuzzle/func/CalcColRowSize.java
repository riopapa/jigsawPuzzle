package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.Vars.jigCOLUMNs;
import static com.riopapa.jigsawpuzzle.Vars.jigROWs;
import static com.riopapa.jigsawpuzzle.Vars.selectedHeight;
import static com.riopapa.jigsawpuzzle.Vars.selectedWidth;
import static com.riopapa.jigsawpuzzle.Vars.showMaxX;
import static com.riopapa.jigsawpuzzle.Vars.showMaxY;

public class CalcColRowSize {

    /**
     * calculate jigCOLUMNs, jigROWs initially by level info
     * @param selectedHeight, selectedWidth : image Height
     * @param showMaxX, showMaxY : physical phone max showing
     * @return jigCOLUMNs, jigROWs
     **/


    public CalcColRowSize(int level) {

        if (selectedWidth > selectedHeight) {
            if (level == 0) {
                jigCOLUMNs = showMaxX;
            } else if (level == 1) {
                jigCOLUMNs = showMaxX * 13 / 10;
            } else if (level == 2) {
                jigCOLUMNs = showMaxX * 16 / 10;
            } else {
                jigCOLUMNs = showMaxX * 20 / 10;
            }
            jigROWs = jigCOLUMNs * selectedHeight / selectedWidth;  // to avoid over y size
        } else {
            if (level == 0) {
                jigROWs = showMaxY;
            } else if (level == 1) {
                jigROWs = showMaxY * 14 / 10;
            } else if (level == 2) {
                jigROWs = showMaxY * 18 / 10;
            } else {
                jigROWs = showMaxY * 24 / 10;
            }
            jigCOLUMNs = jigROWs * selectedWidth / selectedHeight;  // to avoid over y size
        }

    }
}