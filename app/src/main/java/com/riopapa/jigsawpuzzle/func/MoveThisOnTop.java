package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityMain.vars;

import com.riopapa.jigsawpuzzle.model.FloatPiece;

import java.util.ArrayList;
import java.util.Collections;

public class MoveThisOnTop {



    // function to re-arrange pieces so touched pieces go to top layer
    public MoveThisOnTop(FloatPiece fpNow, int nowIdx) {

        int fpSize = vars.fps.size() - 1;
        long ancId = fpNow.anchorId;
        if (ancId == 0) {
            Collections.swap(vars.fps, nowIdx, fpSize);
            return;
        }
        ArrayList<FloatPiece> fp2 = new ArrayList<>();
        vars.fps.remove(nowIdx);
        for (int i = 0; i < vars.fps.size() - 1;) {
            if (vars.fps.get(i).anchorId == ancId) {
                fp2.add(vars.fps.get(i));
                vars.fps.remove(i);
                if (vars.fps.size() == 0)
                    break;
            } else
                i++;
        }
        vars.fps.addAll(fp2);
        vars.fps.add(fpNow);
    }

}
