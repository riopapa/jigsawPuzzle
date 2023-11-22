package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowC;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowR;
import static com.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static com.riopapa.jigsawpuzzle.PaintView.nowFp;

import com.riopapa.jigsawpuzzle.model.FloatPiece;

public class AnchorPiece {
    public void move() {
        if (nowFp.anchorId > 0) {
            for (int i = 0; i < gVal.fps.size(); i++) {
                FloatPiece fpT = gVal.fps.get(i);
                if (fpT.anchorId == nowFp.anchorId) {
                    fpT.posX = nowFp.posX - (nowC - fpT.C) * gVal.picISize;
                    fpT.posY = nowFp.posY - (nowR - fpT.R) * gVal.picISize;
                    gVal.fps.set(i, fpT);
                }
            }
        }

    }
}
