package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityMain.GVal;

import com.riopapa.jigsawpuzzle.model.FloatPiece;

import java.util.ArrayList;
import java.util.Collections;

public class MoveThisOnTop {

    // function to re-arrange pieces so touched pieces go to top layer
    public MoveThisOnTop(FloatPiece fpNow, int nowIdx) {

        int fpSize = GVal.fps.size() - 1;
        long ancId = fpNow.anchorId;
        if (ancId == 0) {
            Collections.swap(GVal.fps, nowIdx, fpSize);
            return;
        }
        ArrayList<FloatPiece> fp2 = new ArrayList<>();
        GVal.fps.remove(nowIdx);
        for (int i = 0; i < GVal.fps.size() - 1;) {
            if (GVal.fps.get(i).anchorId == ancId) {
                fp2.add(GVal.fps.get(i));
                GVal.fps.remove(i);
                if (GVal.fps.size() == 0)
                    break;
            } else
                i++;
        }
        GVal.fps.addAll(fp2);
        GVal.fps.add(fpNow);
    }

}
