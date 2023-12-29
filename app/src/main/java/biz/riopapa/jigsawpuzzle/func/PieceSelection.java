package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemC;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemR;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ForeView.topIdx;

import android.util.Log;

import biz.riopapa.jigsawpuzzle.model.FloatPiece;

public class PieceSelection {
    public void check(int iX, int iY){

        /*
         * if new item has been touched in right position then set it to fpNow
         */

        for (int i = gVal.fps.size() - 1; i >= 0; i--) {
            FloatPiece fp = gVal.fps.get(i);
            int c = fp.C;
            int r = fp.R;
            if (Math.abs(fp.posX - iX) > gVal.picHSize)
                continue;
            if (Math.abs(fp.posY - iY) > gVal.picHSize)
                continue;
            itemR = r; itemC = c;
            topIdx = i;
            Log.w("piece","Selected "+c+"x"+r+" idx="+i);
            return;
        }
        topIdx = -1;
    }

}
