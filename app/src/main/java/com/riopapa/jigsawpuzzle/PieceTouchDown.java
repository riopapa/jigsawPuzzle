package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityMain.vars;
import static com.riopapa.jigsawpuzzle.PaintView.fpNow;
import static com.riopapa.jigsawpuzzle.PaintView.nowIdx;

import com.riopapa.jigsawpuzzle.model.FloatPiece;
import com.riopapa.jigsawpuzzle.model.JigTable;

import java.util.Collections;

public class PieceTouchDown {
    void start(float fX, float fY){

        /*
         * if new item has been touched in right position then set it to fpNow
         */

        if (vars.doNotUpdate)
            return;

        int iX = (int) fX - vars.picHSize;
        int iY = (int) fY - vars.picHSize;

        for (int i = vars.fps.size() - 1; i >= 0; i--) {
            int c = vars.fps.get(i).C;
            int r = vars.fps.get(i).R;
            JigTable jt = vars.jigTables[c][r];

            if (Math.abs(jt.posX - iX) < vars.picHSize && Math.abs(jt.posY - iY) < vars.picHSize) {
                vars.nowR = r; vars.nowC = c;
                nowIdx = i;
                if (nowIdx != vars.fps.size()-1) { // move current puzzle to top
                    Collections.swap(vars.fps, nowIdx, vars.fps.size() - 1);
                    nowIdx = vars.fps.size() - 1;
                }
                fpNow = vars.fps.get(nowIdx);
                vars.jPosX = jt.posX; vars.jPosY = jt.posY;

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

}
