package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ForeView.nearByFloatPiece;

import biz.riopapa.jigsawpuzzle.ActivityMain;
import biz.riopapa.jigsawpuzzle.model.FloatPiece;

public class PieceBind {

    public boolean update() {

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
                    fpW.mode = ActivityMain.GMode.ANCHOR; // make it not zero
                    fpW.count = 5;
                    gVal.fps.set(i, fpW);
                } else if (fpW.anchorId == anchorBase) {
                    fpW.mode = ActivityMain.GMode.ANCHOR; // make it not zero
                    fpW.count = 5;
                    gVal.fps.set(i, fpW);
                }
            }

            // move anchored pieces too
//            if (nowFp.anchorId > 0) {
//                for (int i = 0; i < gVal.fps.size(); i++) {
//                    FloatPiece fpT = gVal.fps.get(i);
//                    if (fpT.anchorId == nowFp.anchorId) {
//                        fpT.posX = nowFp.posX - (nowC - fpT.C) * gVal.picISize;
//                        fpT.posY = nowFp.posY - (nowR - fpT.R) * gVal.picISize;
//
//                    }
//                }
//            }
        }
        return false;
    }

}
