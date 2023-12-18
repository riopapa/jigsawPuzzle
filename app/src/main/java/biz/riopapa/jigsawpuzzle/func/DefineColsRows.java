package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageHeight;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageWidth;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;

import android.util.Log;

public class DefineColsRows {

    /**
     * calculate jigCOLUMNs, jigROWs initially by level info
     * inp : chosenImageHeight, chosenImageWidth : image Height
     * out : colNbr, rowNbr
     **/

    public int col, row;    // return values

    public void calc(int level) {
        int [] sizes = {5, 12, 17, 21};
        double sz = Math.sqrt(Math.sqrt(chosenImageWidth * chosenImageHeight));
        if (chosenImageWidth > chosenImageHeight) {
            row = sizes[level];
            row = (int) Math.sqrt(row * row + sz*(level+1));
            col = row * chosenImageWidth / chosenImageHeight + 1;  // one more possible?
            float szH = (float) chosenImageHeight / (float) (row);
            int col2 = (int) ((float) chosenImageWidth / szH);
            if (col != col2)
                Log.e("defineCol","lvl="+level+" game="+gVal.game+" "+col + " vs "+col2);
            if (col2 > col + 2)
                col++;
        } else {
            col = sizes[level];
            col = (int) Math.sqrt(col * col + sz*(level+1));
            row = col * chosenImageHeight / chosenImageWidth + 1;  // to avoid over y size
            float szW = (float) chosenImageWidth / (float) (col);
            int row2 = (int) ((float) chosenImageHeight / szW);
            if (row != row2)
                Log.e("defineRow","lvl="+level+" game="+gVal.game+" "+row + " vs "+row2);
            if (row2 > row + 2)
                row++;
        }
    }
}