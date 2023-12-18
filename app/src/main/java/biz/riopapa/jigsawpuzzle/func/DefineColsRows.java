package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageHeight;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageWidth;

public class DefineColsRows {

    /**
     * calculate jigCOLUMNs, jigROWs initially by level info
     * inp : chosenImageHeight, chosenImageWidth : image Height
     * out : jigCOLUMNs, jigROWs
     **/

    public int col, row;    // return values

    public void calc(int level) {
        int [] sizes = {5, 12, 17, 21};
        double sz = Math.sqrt(Math.sqrt(chosenImageWidth * chosenImageHeight));
        if (chosenImageWidth > chosenImageHeight) {
            row = sizes[level];
            row = (int) Math.sqrt(row * row + sz*(level+1));
            col = row * chosenImageWidth / chosenImageHeight;  // to avoid over y size
        } else {
            col = sizes[level];
            col = (int) Math.sqrt(col * col + sz*(level+1));
            row = col * chosenImageHeight / chosenImageWidth;  // to avoid over y size
        }

    }
}