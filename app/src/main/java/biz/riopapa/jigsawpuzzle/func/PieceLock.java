package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.allLockedMode;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.fireWorks;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigOLine;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigPic;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ForeView.foreBlink;
import static biz.riopapa.jigsawpuzzle.ForeView.piecePosition;
import static biz.riopapa.jigsawpuzzle.ForeView.topIdx;

import biz.riopapa.jigsawpuzzle.images.PieceImage;
import biz.riopapa.jigsawpuzzle.model.FloatPiece;

public class PieceLock {

    public boolean update(PieceImage pieceImage) {

        // if this fp lockable then remove to back
        boolean svBlink = foreBlink;
        foreBlink = false;
        for (int i = gVal.fps.size()-1; i >= 0;  i--) {
            FloatPiece fpT = gVal.fps.get(i);
            if (piecePosition.isLockable(fpT.C, fpT.R, fpT.posX, fpT.posY)) {
                gVal.jigTables[fpT.C][fpT.R].locked = true;
                gVal.jigTables[fpT.C][fpT.R].count = fireWorks.length;
                jigOLine[fpT.C][fpT.R] = pieceImage.makeOline(jigPic[fpT.C][fpT.R], fpT.C, fpT.R);
                gVal.fps.remove(i);
                allLockedMode = 10;
                svBlink = true;
                topIdx = -1;
            }
        }

        return svBlink;
    }
}
