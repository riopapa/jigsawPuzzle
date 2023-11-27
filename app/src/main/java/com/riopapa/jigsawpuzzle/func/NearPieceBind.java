package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.doNotUpdate;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowC;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowR;
import static com.riopapa.jigsawpuzzle.ActivityMain.ANI_ANCHOR;
import static com.riopapa.jigsawpuzzle.ActivityMain.fireWorks;
import static com.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static com.riopapa.jigsawpuzzle.PaintView.nowFp;
import static com.riopapa.jigsawpuzzle.PaintView.nearByFloatPiece;
import static com.riopapa.jigsawpuzzle.PaintView.piecePosition;
import static com.riopapa.jigsawpuzzle.PaintView.nowIdx;

import com.riopapa.jigsawpuzzle.model.FloatPiece;

public class NearPieceBind {

    public void check(){
        if (doNotUpdate)
            return;

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
                    gVal.jigTables[fpT.C][fpT.R].locked = true;
                    gVal.jigTables[fpT.C][fpT.R].count = fireWorks.length;
                    gVal.fps.remove(i);
                } else
                    i++;
            }
            return;
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

//            for (int i = 0; i < GVal.fps.size(); i++) {
//                Log.w(GVal.fps.get(i).C+"x"+GVal.fps.get(i).R+" before "+i, GVal.jigTables[GVal.fps.get(i).C][GVal.fps.get(i).R].posX +"x"+GVal.jigTables[GVal.fps.get(i).C][GVal.fps.get(i).R].posY);
//            }

            for (int i = 0; i < gVal.fps.size(); i++) {
                FloatPiece fpW = gVal.fps.get(i);
                if (fpW.anchorId == anchorThis) {
                    fpW.posX = fpBase.posX + (fpW.C - fpBase.C) * gVal.picISize;
                    fpW.posY = fpBase.posY + (fpW.R - fpBase.R) * gVal.picISize;
                    fpW.anchorId = anchorBase;
                    fpW.mode = ANI_ANCHOR; // make it not zero
                    fpW.count = 5;
                    gVal.fps.set(i, fpW);
//                    Log.w( fpW.C+"x"+fpW.R+" fpW "+i, "fpW "+
//                            GVal.jigTables[fpW.C][fpW.R].posX +"x"+GVal.jigTables[fpW.C][fpW.R].posY);
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
    }



}
