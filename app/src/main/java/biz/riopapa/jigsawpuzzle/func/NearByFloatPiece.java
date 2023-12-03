package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;

import biz.riopapa.jigsawpuzzle.model.FloatPiece;

public class NearByFloatPiece {

    /*
     * check all Fps to check whether this fp is anchorable
     * return fps idx if anchorable else return -1
     */
    int gapAllowed;
    public NearByFloatPiece() {
        gapAllowed = gVal.picGap + gVal.picGap;
    }
    public int isNear(int thisIdx, FloatPiece fpThis) {

        long anchorId = fpThis.anchorId;
        int cc = fpThis.C;
        int rr = fpThis.R;

        for (int i = 0; i < gVal.fps.size(); i++) {
            if (i != thisIdx) {
                FloatPiece fpWork = gVal.fps.get(i);
                // pass if already anchored group
                if (anchorId > 0 && anchorId == fpWork.anchorId)
                    continue;
                int cDelta = cc - fpWork.C;
                int rDelta = rr - fpWork.R;
                if (Math.abs(cDelta) > 1 || Math.abs(rDelta) > 1)
                    continue;
                if (Math.abs(cDelta) == 1 && Math.abs(rDelta) == 1)
                    continue;
                int delX = Math.abs(fpThis.posX - fpWork.posX - cDelta* gVal.picISize);
                if (delX > gapAllowed)    // around near
                    continue;
                int delY = Math.abs(fpThis.posY - fpWork.posY - rDelta* gVal.picISize);
                if (delY > gapAllowed)
                    continue;
                return i;   // anchored with i's floatPiece
            }
        }
        return -1;
    }
}
