package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.allLockedMode;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.fireWorks;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigOLine;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigPic;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.nowC;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.nowR;
import static biz.riopapa.jigsawpuzzle.ActivityMain.ANI_ANCHOR;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ForeView.nearByFloatPiece;
import static biz.riopapa.jigsawpuzzle.ForeView.nowFp;
import static biz.riopapa.jigsawpuzzle.ForeView.nowIdx;
import static biz.riopapa.jigsawpuzzle.ForeView.piecePosition;

import biz.riopapa.jigsawpuzzle.images.PieceImage;
import biz.riopapa.jigsawpuzzle.model.FloatPiece;

public class NearPieceBind {

    public boolean check(PieceImage pieceImage){

        if (nowIdx < gVal.fps.size() -1) {
            new MoveThisOnTop(nowFp, nowIdx);
            nowIdx = gVal.fps.size() - 1;
        }

        // check whether each pieces are in lockable position
        boolean lockable = false;
        long anchorId = nowFp.anchorId;

        for (int i = 0; i < gVal.fps.size(); i++) {
            FloatPiece fpTo = gVal.fps.get(i);
            if (piecePosition.isLockable(fpTo.C, fpTo.R, fpTo.posX, fpTo.posY)) {
                if (fpTo.anchorId == 0) {
                    fpTo.anchorId = -1;
                    gVal.fps.set(i, fpTo);
                }
                anchorId = fpTo.anchorId;
                lockable = true;
                break;
            }
        }

        // if pieceImage moved to right Position then lock thi pieceImage
        if (lockable) {
            for (int i = 0; i < gVal.fps.size(); ) {
                FloatPiece fpT = gVal.fps.get(i);
                if (fpT.anchorId == anchorId) {
                    gVal.fps.remove(i);
                    gVal.jigTables[fpT.C][fpT.R].locked = true;
                    gVal.jigTables[fpT.C][fpT.R].count = fireWorks.length;
                    jigOLine[fpT.C][fpT.R] = pieceImage.makeOline(jigPic[fpT.C][fpT.R], fpT.C, fpT.R);
                } else
                    i++;
            }
            allLockedMode = 10;
            return true;
        }


        // check whether can be anchored to near by pieceImage

        int ancBase = -1;
        int ancThis = -1;

        FloatPiece fpThis = null, fpBase = null;

        for (int iA = gVal.fps.size()-1; iA >= 0; iA--) {
            fpThis = gVal.fps.get(iA);
            ancBase = nearByFloatPiece.isNear(iA, fpThis);
            if (ancBase != -1) {
                ancThis = iA;
                fpBase = gVal.fps.get(ancBase);
                break;
            }
        }

        if (ancBase != -1) {

            if (fpBase.anchorId == 0) {
                fpBase.anchorId = fpBase.uId;
                gVal.fps.set(ancBase, fpBase);
            }
            long anchorBase = fpBase.anchorId;

            if (fpThis.anchorId == 0) {
                fpThis.anchorId = fpThis.uId;
                gVal.fps.set(ancThis, fpThis);
            }
            long anchorThis = fpThis.anchorId;
            for (int i = 0; i < gVal.fps.size(); i++) {
                FloatPiece fpW = gVal.fps.get(i);
                if (fpW.anchorId == anchorThis) {
                    fpW.posX = fpBase.posX + (fpW.C - fpBase.C) * gVal.picISize;
                    fpW.posY = fpBase.posY + (fpW.R - fpBase.R) * gVal.picISize;
                    fpW.anchorId = anchorBase;
                    fpW.mode = ANI_ANCHOR; // make it not zero
                    fpW.count = 5;
                    gVal.fps.set(i, fpW);
                } else if (fpW.anchorId == anchorBase) {
                    fpW.mode = ANI_ANCHOR; // make it not zero
                    fpW.count = 5;
                    gVal.fps.set(i, fpW);
                }
            }

            // move anchored pieces too
            if (nowFp.anchorId > 0) {
                for (int i = 0; i < gVal.fps.size(); i++) {
                    FloatPiece fpT = gVal.fps.get(i);
                    if (fpT.anchorId == nowFp.anchorId) {
                        fpT.posX = nowFp.posX - (nowC - fpT.C) * gVal.picISize;
                        fpT.posY = nowFp.posY - (nowR - fpT.R) * gVal.picISize;

                    }
                }
            }
        }
        return false;
    }

}
