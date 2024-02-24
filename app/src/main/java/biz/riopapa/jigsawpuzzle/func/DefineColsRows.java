package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.currImageHeight;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.currImageWidth;

public class DefineColsRows {

    /**
     * calculate jigCOLUMNs, jigROWs initially by level info
     * inp : chosenImageHeight, chosenImageWidth : image Height
     * out : colNbr, rowNbr
     **/

    public int col, row;    // return values

    public void calc(int level) {
        int [] sizes = {2, 5, 7, 9};
        double sz = Math.sqrt(Math.sqrt(currImageWidth * currImageHeight));
        if (currImageWidth > currImageHeight) {
            row = sizes[level];
            row = (int) Math.sqrt(row * row + sz*(level+3)) / 2;
            col = row * currImageWidth / currImageHeight + 1;  // one more possible?
            float szH = (float) currImageHeight / (float) (row);
            int col2 = (int) ((float) currImageWidth / szH);
            if (col2 > col + 2)
                col++;
        } else {
            col = sizes[level];
            col = (int) Math.sqrt(col * col + sz*(level+3)) / 2;
            row = col * currImageHeight / currImageWidth + 1;  // to avoid over y size
            float szW = (float) currImageWidth / (float) (col);
            int row2 = (int) ((float) currImageHeight / szW);
            if (row2 > row + 2)
                row++;
        }
    }
}