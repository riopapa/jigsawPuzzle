package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageHeight;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageWidth;

import android.util.Log;

public class DefineColsRows {

    /**
     * calculate jigCOLUMNs, jigROWs initially by level info
     * @param chosenImageHeight, chosenImageWidth : image Height
     * @return jigCOLUMNs, jigROWs
     **/

    public int col, row;

    public void calc(int level) {

        int [] sizes = {10, 14, 18, 25};
        if (chosenImageWidth > chosenImageHeight) {
            row = sizes[level];
            col = row * chosenImageWidth / chosenImageHeight;  // to avoid over y size
        } else {
            col = sizes[level];
            row = col * chosenImageHeight / chosenImageWidth;  // to avoid over y size
        }

    }
}