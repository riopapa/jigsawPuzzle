package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.allLockedMode;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.fireWorks;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.reDrawOLine;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ForeView.foreBlink;
import static biz.riopapa.jigsawpuzzle.ForeView.piecePosition;
import static biz.riopapa.jigsawpuzzle.ForeView.topIdx;

import biz.riopapa.jigsawpuzzle.model.FloatPiece;

public class PieceLock {

    public boolean update() {

        // if this fp lockable then remove to back

        boolean svBlink = foreBlink;
        foreBlink = false;
        for (int i = 0; i < gVal.fps.size(); ) {
            FloatPiece fp = gVal.fps.get(i);
            if (piecePosition.isLockable(fp.C, fp.R, fp.posX, fp.posY)) {
                if (fp.anchorId == 0) {
                    gVal.jigTables[fp.C][fp.R].locked = true;
                    gVal.jigTables[fp.C][fp.R].count = fireWorks.length;
                    gVal.fps.remove(i);
                } else
                    lockSameAnchorIds(fp.anchorId);
                allLockedMode = 10;
                svBlink = true;
                topIdx = -1;
                i = 0;
            } else
                i++;
        }

        return svBlink;
    }

    void lockSameAnchorIds(long lockId) {
        for (int i = gVal.fps.size() - 1; i >= 0; i--) {
            FloatPiece fp = gVal.fps.get(i);
            if (fp.anchorId == lockId) {
                gVal.jigTables[fp.C][fp.R].locked = true;
                gVal.jigTables[fp.C][fp.R].count = fireWorks.length;
                gVal.fps.remove(i);
            }
        }
    }
}
