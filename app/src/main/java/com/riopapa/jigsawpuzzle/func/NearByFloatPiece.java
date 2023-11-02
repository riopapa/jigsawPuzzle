package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.MainActivity.fps;
import static com.riopapa.jigsawpuzzle.MainActivity.jigTables;
import static com.riopapa.jigsawpuzzle.MainActivity.picGap;
import static com.riopapa.jigsawpuzzle.MainActivity.picHSize;
import static com.riopapa.jigsawpuzzle.MainActivity.picISize;

import com.riopapa.jigsawpuzzle.model.FloatPiece;

public class NearByFloatPiece {

    /*
     * check all Fps to check whether this fp is anchorable
     * return fps idx if anchorable else return -1
     */
    public NearByFloatPiece() {}
    public int anchor(int thisIdx, FloatPiece fpThis) {

        long anchorId = fpThis.anchorId;

        for (int i = 0; i < fps.size(); i++) {
            if (i != thisIdx) {
                FloatPiece fpTemp = fps.get(i);
                // pass if already anchored group
                if (anchorId > 0 && anchorId == fpTemp.anchorId)
                    continue;
                int cDelta = fpThis.C - fpTemp.C;
                int rDelta = fpThis.R - fpTemp.R;
                if (Math.abs(cDelta) > 1 || Math.abs(rDelta) > 1)
                    continue;
                if (Math.abs(cDelta) == 1 && Math.abs(rDelta) == 1)
                    continue;
                int delX = Math.abs(fpThis.posX - jigTables[fpTemp.C][fpTemp.R].posX - cDelta*picISize);
                if (delX > picHSize)    // around near
                    continue;;
                int delY = Math.abs(fpThis.posY - jigTables[fpTemp.C][fpTemp.R].posY - rDelta*picISize);
                if (delY > picHSize)
                    continue;;
                return i;   // anchored with i's floatPiece
            }
        }
        return -1;
    }
}
