package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.nowC;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.nowR;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ForeView.nowFp;

import biz.riopapa.jigsawpuzzle.model.FloatPiece;

public class AnchorPiece {
    public boolean move() {
        boolean updated = false;
        if (nowFp.anchorId > 0) {
            for (int i = 0; i < gVal.fps.size(); i++) {
                FloatPiece fpT = gVal.fps.get(i);
                if (fpT.anchorId == nowFp.anchorId) {
                    fpT.posX = nowFp.posX - (nowC - fpT.C) * gVal.picISize;
                    fpT.posY = nowFp.posY - (nowR - fpT.R) * gVal.picISize;
                    gVal.fps.set(i, fpT);
                    updated = true;
                }
            }
        }
        return updated;
    }
}
