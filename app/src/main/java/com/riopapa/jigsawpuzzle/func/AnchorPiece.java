package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowC;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowR;
import static com.riopapa.jigsawpuzzle.ActivityMain.GVal;
import static com.riopapa.jigsawpuzzle.PaintView.nowFp;

import com.riopapa.jigsawpuzzle.model.FloatPiece;

public class AnchorPiece {
    public void move() {
        if (nowFp.anchorId > 0) {
            for (int i = 0; i < GVal.fps.size(); i++) {
                FloatPiece fpT = GVal.fps.get(i);
                if (fpT.anchorId == nowFp.anchorId) {
                    fpT.posX = nowFp.posX - (nowC - fpT.C) * GVal.picISize;
                    fpT.posY = nowFp.posY - (nowR - fpT.R) * GVal.picISize;
                    GVal.fps.set(i, fpT);
                }
            }
        }

    }
}
