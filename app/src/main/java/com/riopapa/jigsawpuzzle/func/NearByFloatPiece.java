package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityMain.GVal;

import com.riopapa.jigsawpuzzle.model.FloatPiece;

public class NearByFloatPiece {

    /*
     * check all Fps to check whether this fp is anchorable
     * return fps idx if anchorable else return -1
     */
    public NearByFloatPiece() {}
    public int isNear(int thisIdx, FloatPiece fpThis) {

        long anchorId = fpThis.anchorId;
        int cc = fpThis.C;
        int rr = fpThis.R;

        for (int i = 0; i < GVal.fps.size(); i++) {
            if (i != thisIdx) {
                FloatPiece fpWork = GVal.fps.get(i);
                // pass if already anchored group
                if (anchorId > 0 && anchorId == fpWork.anchorId)
                    continue;
                int cDelta = cc - fpWork.C;
                int rDelta = rr - fpWork.R;
                if (Math.abs(cDelta) > 1 || Math.abs(rDelta) > 1)
                    continue;
                if (Math.abs(cDelta) == 1 && Math.abs(rDelta) == 1)
                    continue;
                int delX = Math.abs(fpThis.posX - fpWork.posX - cDelta* GVal.picISize);
                if (delX > GVal.picGap)    // around near
                    continue;
                int delY = Math.abs(fpThis.posY - fpWork.posY - rDelta* GVal.picISize);
                if (delY > GVal.picGap)
                    continue;
                return i;   // anchored with i's floatPiece
            }
        }
        return -1;
    }
}
