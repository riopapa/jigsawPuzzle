package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.nowC;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.nowR;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ForeView.nowFp;
import static biz.riopapa.jigsawpuzzle.ForeView.nowIdx;

import biz.riopapa.jigsawpuzzle.model.FloatPiece;

public class PieceSelection {
    public void check(int iX, int iY){

        /*
         * if new item has been touched in right position then set it to fpNow
         */

        nowFp = null;

        for (int i = gVal.fps.size() - 1; i >= 0; i--) {
            FloatPiece fp = gVal.fps.get(i);
            int c = fp.C;
            int r = fp.R;
            if (Math.abs(fp.posX - iX) > gVal.picHSize)
                continue;
            if (Math.abs(fp.posY - iY) > gVal.picHSize)
                continue;
            nowFp = fp;
            nowR = r; nowC = c;
            nowIdx = i;
            break;
        }
    }

}
