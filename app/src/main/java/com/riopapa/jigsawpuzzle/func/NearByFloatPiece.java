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
    public int anchor(int fPIdx, FloatPiece fpNow) {

        for (int i = 0; i < fps.size(); i++) {
            if (i != fPIdx) {
                FloatPiece fpI = fps.get(i);
                int cDelta = fpNow.C - fpI.C;
                int rDelta = fpNow.R - fpI.R;
                if (Math.abs(cDelta) > 1 || Math.abs(rDelta) > 1)
                    continue;
                int posX = jigTables[fpI.C][fpI.R].posX;
                int posY = jigTables[fpI.C][fpI.R].posY;
                int delX = jPosX - picISize - posX - cDelta*picISize;
                int delY = jPosY - picISize - posY - rDelta*picISize;
//                Log.w("picI "+picISize,"now "+posX+" x "+posY+" jig "+jPosX+" x "+jPosY);
//                Log.w("delta "+picGap, "x=" + delX +
//                        " y="+ delY);
                if (Math.abs(delX) > picGap ||
                        Math.abs(delY) > picGap)
                    continue;
//                fpNow.anchorId = fpI.uId;   // assign ancherId
                return i;   // anchored with i's floatPiece
            }
        }
        return -1;
    }
}
