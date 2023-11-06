package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.Vars.fps;

import com.riopapa.jigsawpuzzle.model.FloatPiece;

import java.util.ArrayList;
import java.util.Collections;

public class RearrangePieces {

    ArrayList<FloatPiece> fp2;

    // function to re-arrange pieces so touched pieces go to top layer
    public RearrangePieces (FloatPiece fpNow, int nowIdx) {

        int fpSize = fps.size() - 1;
        long ancId = fpNow.anchorId;
        if (ancId == 0) {
            Collections.swap(fps, nowIdx, fpSize);
            return;
        }

        fp2 = new ArrayList<>();
        fps.remove(nowIdx);
        for (int i = 0; i < fps.size() - 1;) {
            if (fps.get(i).anchorId == ancId) {
                fp2.add(fps.get(i));
                fps.remove(i);
                if (fps.size() == 0)
                    break;
            } else
                i++;
        }
        fps.addAll(fp2);
        fps.add(fpNow);
        fp2 = null;

    }

}
