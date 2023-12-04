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

    public int col, row;    // return values

    public void calc(int level) {

        int [] sizes = {5, 12, 17, 21};
        if (chosenImageWidth > chosenImageHeight) {
            row = sizes[level];
            row = (int) Math.sqrt(row * row + Math.sqrt(chosenImageWidth + chosenImageHeight));
            col = row * chosenImageWidth / chosenImageHeight;  // to avoid over y size
        } else {
            col = sizes[level];
            col = (int) Math.sqrt(col * col + Math.sqrt(chosenImageHeight + chosenImageHeight));
            row = col * chosenImageHeight / chosenImageWidth;  // to avoid over y size
        }

    }
}