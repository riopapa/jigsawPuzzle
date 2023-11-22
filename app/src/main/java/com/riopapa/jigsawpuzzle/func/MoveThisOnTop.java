package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityMain.gVal;

import com.riopapa.jigsawpuzzle.model.FloatPiece;

import java.util.ArrayList;
import java.util.Collections;

public class MoveThisOnTop {

    // function to re-arrange pieces so touched pieces go to top layer
    public MoveThisOnTop(FloatPiece fpNow, int nowIdx) {

        int fpSize = gVal.fps.size() - 1;
        long ancId = fpNow.anchorId;
        if (ancId == 0) {
            Collections.swap(gVal.fps, nowIdx, fpSize);
            return;
        }
        ArrayList<FloatPiece> fp2 = new ArrayList<>();
        gVal.fps.remove(nowIdx);
        for (int i = 0; i < gVal.fps.size() - 1;) {
            if (gVal.fps.get(i).anchorId == ancId) {
                fp2.add(gVal.fps.get(i));
                gVal.fps.remove(i);
                if (gVal.fps.size() == 0)
                    break;
            } else
                i++;
        }
        gVal.fps.addAll(fp2);
        gVal.fps.add(fpNow);
    }

}
