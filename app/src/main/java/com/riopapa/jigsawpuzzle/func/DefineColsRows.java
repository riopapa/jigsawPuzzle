package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageHeight;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageWidth;

public class DefineColsRows {

    /**
     * calculate jigCOLUMNs, jigROWs initially by level info
     * @param chosenImageHeight, chosenImageWidth : image Height
     * @return jigCOLUMNs, jigROWs
     **/

    public int col, row;

    public void calc(int level) {

        int [] sizes = {10, 15, 20, 30};
        if (chosenImageWidth > chosenImageHeight) {
            row = sizes[level];
            col = row * chosenImageWidth / chosenImageHeight;  // to avoid over y size
        } else {
            col = sizes[level];
            row = col * chosenImageHeight / chosenImageWidth;  // to avoid over y size
        }

    }
}