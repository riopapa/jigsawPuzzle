package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.MainActivity.doNotUpdate;
import static com.riopapa.jigsawpuzzle.MainActivity.fps;
import static com.riopapa.jigsawpuzzle.MainActivity.jPosX;
import static com.riopapa.jigsawpuzzle.MainActivity.jPosY;
import static com.riopapa.jigsawpuzzle.MainActivity.jigTables;
import static com.riopapa.jigsawpuzzle.MainActivity.nowC;
import static com.riopapa.jigsawpuzzle.MainActivity.nowR;
import static com.riopapa.jigsawpuzzle.MainActivity.picHSize;
import static com.riopapa.jigsawpuzzle.PaintView.fpNow;
import static com.riopapa.jigsawpuzzle.PaintView.nowIdx;

import com.riopapa.jigsawpuzzle.model.FloatPiece;
import com.riopapa.jigsawpuzzle.model.JigTable;

import java.util.Collections;

public class PieceTouchDown {
    void start(float fX, float fY){

        if (doNotUpdate)
            return;

        int iX = (int) fX - picHSize;
        int iY = (int) fY - picHSize;

        for (int i = fps.size() - 1; i >= 0; i--) {
            int c = fps.get(i).C;
            int r = fps.get(i).R;
            JigTable jt = jigTables[c][r];

            if (Math.abs(jt.posX - iX) < picHSize && Math.abs(jt.posY - iY) < picHSize) {
                nowR = r; nowC = c;
                nowIdx = i;
                if (nowIdx != fps.size()-1) { // move current puzzle to top
                    Collections.swap(fps, nowIdx, fps.size() - 1);
                    nowIdx = fps.size() - 1;
                }
                fpNow = fps.get(nowIdx);
                jPosX = jt.posX; jPosY = jt.posY;

                // move current pieces with anchored to Top position
                for (int ii = 0; ii < fps.size(); ii++) {
                    if (fps.get(ii).anchorId == fpNow.anchorId) {
                        Collections.swap(fps, ii, fps.size() - 1);
                    }
                }

                // reset nowIdx to top pieceImage
                for (int ii = 0; i < fps.size(); ii++) {
                    FloatPiece fpAnchor = fps.get(ii);
                    if (fpAnchor.C == fpNow.C && fpAnchor.R == fpNow.R) {
                        Collections.swap(fps, ii, fps.size() - 1);
                        nowIdx = fps.size() - 1;
                        break;
                    }
                }
                fpNow = fps.get(nowIdx);
                break;
            }
        }
    }

}
