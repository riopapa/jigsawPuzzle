package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.MainActivity.fps;
import static com.riopapa.jigsawpuzzle.MainActivity.jigTables;
import static com.riopapa.jigsawpuzzle.MainActivity.picHSize;
import static com.riopapa.jigsawpuzzle.MainActivity.picISize;

import android.util.Log;

import com.riopapa.jigsawpuzzle.model.FloatPiece;

public class NearByFloatPiece {

    /*
     * check all Fps to check whether this fp is anchorable
     * return fps idx if anchorable else return -1
     */
    public NearByFloatPiece() {}
    public int isAnchorable(int thisIdx, FloatPiece fpThis) {

        long anchorId = fpThis.anchorId;
        int cc = fpThis.C;
        int rr = fpThis.R;

        for (int i = 0; i < fps.size(); i++) {
            if (i != thisIdx) {
                FloatPiece fpWork = fps.get(i);
                // pass if already anchored group
                if (anchorId > 0 && anchorId == fpWork.anchorId)
                    continue;
                int cDelta = cc - fpWork.C;
                int rDelta = rr - fpWork.R;
                if (Math.abs(cDelta) > 1 || Math.abs(rDelta) > 1)
                    continue;
                if (Math.abs(cDelta) == 1 && Math.abs(rDelta) == 1)
                    continue;
                int delX = Math.abs(jigTables[cc][rr].posX - jigTables[fpWork.C][fpWork.R].posX - cDelta*picISize);
                if (delX > picHSize)    // around near
                    continue;;
                int delY = Math.abs(jigTables[cc][rr].posY - jigTables[fpWork.C][fpWork.R].posY - rDelta*picISize);
                if (delY > picHSize)
                    continue;;
                return i;   // anchored with i's floatPiece
            }
        }
        return -1;
    }
}
