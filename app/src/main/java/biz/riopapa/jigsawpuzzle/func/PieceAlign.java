package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemC;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemR;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ForeView.foreBlink;
import static biz.riopapa.jigsawpuzzle.ForeView.topIdx;

import biz.riopapa.jigsawpuzzle.model.FloatPiece;

public class PieceAlign {
    public void move() {
        FloatPiece fp = gVal.fps.get(topIdx);
        if (fp.anchorId > 0) {
            for (int i = 0; i < gVal.fps.size(); i++) {
                FloatPiece fpT = gVal.fps.get(i);
                if (fpT.anchorId == fp.anchorId) {
                    fpT.posX = fp.posX - (itemC - fpT.C) * gVal.picISize;
                    fpT.posY = fp.posY - (itemR - fpT.R) * gVal.picISize;
                    gVal.fps.set(i, fpT);
                    foreBlink = true;
                }
            }
        }
    }
}
