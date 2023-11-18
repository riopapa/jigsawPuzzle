package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.doNotUpdate;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowC;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowR;
import static com.riopapa.jigsawpuzzle.ActivityMain.vars;
import static com.riopapa.jigsawpuzzle.PaintView.nowFp;
import static com.riopapa.jigsawpuzzle.PaintView.nowIdx;

import android.util.Log;

import com.riopapa.jigsawpuzzle.model.FloatPiece;

public class PieceSelection {
    public void check(int iX, int iY){

        /*
         * if new item has been touched in right position then set it to fpNow
         */

        nowFp = null;
        if (doNotUpdate)
            return;

        for (int i = vars.fps.size() - 1; i >= 0; i--) {
            FloatPiece fp = vars.fps.get(i);
            int c = fp.C;
            int r = fp.R;
            Log.w("PieceSelection","init xy="+iX+"x"+iY + " cr="+c+"x"+r
                    +" pos="+fp.posX+"x"+fp.posY);
            if (Math.abs(fp.posX - iX) > vars.picHSize)
                continue;
            if (Math.abs(fp.posY - iY) > vars.picHSize)
                continue;
            Log.w("mached cr="+c+"x"+r,
                    " fp Pos="+fp.posX+"x"+fp.posY+" hSize="+vars.picHSize+
                    ", gap "+Math.abs(fp.posX - iX)+"x"+Math.abs(fp.posY - iY));
            nowFp = fp;
            nowR = r; nowC = c;
            nowIdx = i;
            break;
        }
    }

}
