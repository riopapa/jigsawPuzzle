package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.doNotUpdate;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jPosX;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jPosY;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowC;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowR;
import static com.riopapa.jigsawpuzzle.ActivityMain.vars;
import static com.riopapa.jigsawpuzzle.PaintView.fpNow;
import static com.riopapa.jigsawpuzzle.PaintView.nowIdx;

import android.util.Log;

import com.riopapa.jigsawpuzzle.model.FloatPiece;
import com.riopapa.jigsawpuzzle.model.JigTable;

import java.util.Collections;

public class PieceSelection {
    public void check(int iX, int iY){

        /*
         * if new item has been touched in right position then set it to fpNow
         */

        fpNow = null;
        if (doNotUpdate)
            return;

        for (int i = vars.fps.size() - 1; i >= 0; i--) {
            int c = vars.fps.get(i).C;
            int r = vars.fps.get(i).R;
            JigTable jt = vars.jigTables[c][r];
            Log.w("PieceSelection","init xy="+iX+"x"+iY + " cr="+c+"x"+r+" jtPos="+jt.posX+"x"+jt.posY);
            if (Math.abs(jt.posX - iX) > vars.picHSize)
                continue;
            if (Math.abs(jt.posY - iY) > vars.picHSize)
                continue;
            Log.w("mached cr="+c+"x"+r,
                    " jt Pos="+jt.posX+"x"+jt.posY+" hSize="+vars.picHSize+
                    ", gap "+Math.abs(jt.posX - iX)+"x"+Math.abs(jt.posY - iY));
            nowR = r; nowC = c;
            nowIdx = i;
            if (nowIdx != vars.fps.size()-1) { // move current puzzle to top
                Collections.swap(vars.fps, nowIdx, vars.fps.size() - 1);
                nowIdx = vars.fps.size() - 1;
            }
            fpNow = vars.fps.get(nowIdx);
            jPosX = jt.posX; jPosY = jt.posY;

            // move current pieces with anchored to Top position
            for (int ii = 0; ii < vars.fps.size(); ii++) {
                if (vars.fps.get(ii).anchorId == fpNow.anchorId) {
                    Collections.swap(vars.fps, ii, vars.fps.size() - 1);
                }
            }

            // reset nowIdx to top pieceImage
            for (int ii = 0; i < vars.fps.size(); ii++) {
                FloatPiece fpAnchor = vars.fps.get(ii);
                if (fpAnchor.C == fpNow.C && fpAnchor.R == fpNow.R) {
                    Collections.swap(vars.fps, ii, vars.fps.size() - 1);
                    nowIdx = vars.fps.size() - 1;
                    break;
                }
            }
            fpNow = vars.fps.get(nowIdx);
            break;
        }
    }

}
