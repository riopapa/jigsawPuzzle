package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.MainActivity.fps;
import static com.riopapa.jigsawpuzzle.MainActivity.jPosX;
import static com.riopapa.jigsawpuzzle.MainActivity.jPosY;
import static com.riopapa.jigsawpuzzle.MainActivity.jigTables;
import static com.riopapa.jigsawpuzzle.MainActivity.picGap;
import static com.riopapa.jigsawpuzzle.MainActivity.picISize;

import com.riopapa.jigsawpuzzle.model.FloatPiece;

public class NearByFloatPiece {

    public NearByFloatPiece() {}
    public int anchor(int nowIdx, FloatPiece fpNow) {

        for (int i = 0; i < fps.size(); i++) {
            if (i != nowIdx) {
                FloatPiece fpTemp = fps.get(i);
                if (fpNow.anchorId > 0 && fpNow.anchorId == fpTemp.anchorId) // pass if already anchored group
                    continue;
                int cDelta = fpNow.C - fpTemp.C;
                int rDelta = fpNow.R - fpTemp.R;
                if (Math.abs(cDelta) > 1 || Math.abs(rDelta) > 1)
                    continue;
                if (Math.abs(cDelta) == 1 && Math.abs(rDelta) == 1)
                    continue;
                int delX = Math.abs(fpNow.posX - jigTables[fpTemp.C][fpTemp.R].posX - cDelta*picISize);
                if (delX > picGap)
                    continue;;
                int delY = Math.abs(fpNow.posY - jigTables[fpTemp.C][fpTemp.R].posY - rDelta*picISize);
                if (delY > picGap)
                    continue;;
                return i;   // anchored with i's floatPiece
            }
        }
        return -1;
    }
}
