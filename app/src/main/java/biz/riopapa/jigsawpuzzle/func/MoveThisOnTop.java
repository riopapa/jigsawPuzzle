package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ForeView.foreBlink;
import biz.riopapa.jigsawpuzzle.model.FloatPiece;

import java.util.ArrayList;
import java.util.Collections;

public class MoveThisOnTop {

    // function to re-arrange pieces so touched pieces go to top layer
    public MoveThisOnTop(int nowIdx) {

        foreBlink = false;
        FloatPiece fp = gVal.fps.get(nowIdx);
        int fpSize = gVal.fps.size() - 1;
        Collections.swap(gVal.fps, nowIdx, fpSize);
        long ancId = fp.anchorId;
        if (ancId == 0)
            return;

        ArrayList<FloatPiece> fp1 = new ArrayList<>();
        ArrayList<FloatPiece> fp2 = new ArrayList<>();
        for (int i = 0; i < gVal.fps.size(); i++) {
            if (gVal.fps.get(i).anchorId == ancId) {
                fp2.add(gVal.fps.get(i));
            } else
                fp1.add(gVal.fps.get(i));
        }
        gVal.fps = new ArrayList<>();
        gVal.fps.addAll(fp1);
        gVal.fps.addAll(fp2);
        foreBlink = true;
    }

}
