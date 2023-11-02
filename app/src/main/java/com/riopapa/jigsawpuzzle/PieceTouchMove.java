package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.MainActivity.aniANCHOR;
import static com.riopapa.jigsawpuzzle.MainActivity.doNotUpdate;
import static com.riopapa.jigsawpuzzle.MainActivity.fps;
import static com.riopapa.jigsawpuzzle.MainActivity.jigTables;
import static com.riopapa.jigsawpuzzle.MainActivity.nowC;
import static com.riopapa.jigsawpuzzle.MainActivity.nowR;
import static com.riopapa.jigsawpuzzle.MainActivity.picHSize;
import static com.riopapa.jigsawpuzzle.MainActivity.picISize;
import static com.riopapa.jigsawpuzzle.MainActivity.recySize;
import static com.riopapa.jigsawpuzzle.MainActivity.screenY;
import static com.riopapa.jigsawpuzzle.PaintView.fpNow;
import static com.riopapa.jigsawpuzzle.PaintView.nearByFloatPiece;
import static com.riopapa.jigsawpuzzle.PaintView.nearByPieces;
import static com.riopapa.jigsawpuzzle.PaintView.nowIdx;
import static com.riopapa.jigsawpuzzle.PaintView.rightPosition;
import static com.riopapa.jigsawpuzzle.PaintView.wait4Up;
import static com.riopapa.jigsawpuzzle.RecycleJigListener.insert2Recycle;

import android.util.Log;

import com.riopapa.jigsawpuzzle.func.RearrangePieces;
import com.riopapa.jigsawpuzzle.model.FloatPiece;

public class PieceTouchMove {


    void start(float fMovedX, float fMovedY){
        if (doNotUpdate)
            return;

        int moveX = (int) fMovedX - picHSize;
        int moveY = (int) fMovedY - picHSize;

        // check whether go back to recycler
        if (moveY > (screenY - recySize * 2)) {
            // if sole piece then can go back to recycler
            if (fpNow.anchorId == 0) {
                doNotUpdate = true;
                Log.w("pchk Check", "fps size=" + fps.size() + " fPIdx=" + nowIdx + " now CR " + nowC + "x" + nowR);
                fps.remove(nowIdx);
                insert2Recycle.sendEmptyMessage(0);
            }
            // if anchored can not go back to recycle;
            return;
        }

        jigTables[nowC][nowR].posX = moveX;
        jigTables[nowC][nowR].posY = moveY;

        // move anchored pieces too
        if (fpNow.anchorId > 0 && wait4Up == false) {
            for (int i = 0; i < fps.size(); i++) {
                FloatPiece fpT = fps.get(i);
                if (fpT.anchorId == fpNow.anchorId) {
                    jigTables[fpT.C][fpT.R].posX =
                            jigTables[nowC][nowR].posX - (nowC - fpT.C) * picISize;
                    jigTables[fpT.C][fpT.R].posY =
                            jigTables[nowC][nowR].posY - (nowR - fpT.R) * picISize;
                }
            }
        }
        if (nowIdx < fps.size()) {
            new RearrangePieces(fpNow, nowIdx);
            nowIdx = fps.size() - 1;
        }

        // check whether each pieces are in lockable position
        boolean lockable = false;
        long anchorId = fpNow.anchorId;

        for (int i = 0; i < fps.size(); i++) {
            FloatPiece fpTo = fps.get(i);
            if (nearByPieces.lockable(fpTo.C, fpTo.R) && rightPosition.isHere(fpTo.C, fpTo.R,
                            jigTables[fpTo.C][fpTo.R].posX, jigTables[fpTo.C][fpTo.R].posY)) {
                if (fpTo.anchorId == 0) {
                    fpTo.anchorId = -1;
                    fps.set(i, fpTo);
                }
                anchorId = fpTo.anchorId;
                lockable = true;
                break;
            }
        }

        // if pieceImage moved to right rightPosition then lock thi pieceImage
        if (lockable) {
            for (int i = 0; i < fps.size(); ) {
                FloatPiece fpT = fps.get(i);
                if (fpT.anchorId == anchorId) {
                    jigTables[fpT.C][fpT.R].locked = true;
                    jigTables[fpT.C][fpT.R].count = 7;
                    fps.remove(i);
                } else
                    i++;
            }
            return;
        }



        // check whether can be anchored to near by pieceImage

        int ancBase = -1;
        int ancThis = -1;

        FloatPiece fpThis = null, fpBase = null;

        for (int iA = fps.size()-1; iA >= 0;  iA--) {
            fpThis = fps.get(iA);
            ancBase = nearByFloatPiece.isAnchorable(iA, fpThis);
            if (ancBase != -1) {
                ancThis = iA;
                fpBase = fps.get(ancBase);
                break;
            }
        }


        if (ancBase != -1) {
            doNotUpdate = true;

//            Log.w( fpNow.C+"x"+fpNow.R+" fpNow ", "fpNow "+
//                    jigTables[fpNow.C][fpNow.R].posX +"x"+jigTables[fpNow.C][fpNow.R].posY);
//            Log.w( fpBase.C+"x"+fpBase.R+" fpBase ", "fpBase "+
//                    jigTables[fpBase.C][fpBase.R].posX +"x"+jigTables[fpBase.C][fpBase.R].posY);
//            Log.w( fpThis.C+"x"+fpThis.R+" fpThis ", "fpThis "+
//                    jigTables[fpThis.C][fpThis.R].posX +"x"+jigTables[fpThis.C][fpThis.R].posY);

            if (fpBase.anchorId == 0) {
                fpBase.anchorId = fpBase.uId;
                fps.set(ancBase, fpBase);
            }
            long anchorBase = fpBase.anchorId;

            if (fpThis.anchorId == 0) {
                fpThis.anchorId = fpThis.uId;
                fps.set(ancThis, fpThis);
            }
            long anchorThis = fpThis.anchorId;

            for (int i = 0; i < fps.size(); i++) {
                Log.w(fps.get(i).C+"x"+fps.get(i).R+" before "+i, jigTables[fps.get(i).C][fps.get(i).R].posX +"x"+jigTables[fps.get(i).C][fps.get(i).R].posY);
            }

            for (int i = 0; i < fps.size(); i++) {
                FloatPiece fpW = fps.get(i);
                if (fpW.anchorId == anchorThis) {
                    jigTables[fpW.C][fpW.R].posX -= (fpW.C - fpBase.C) * picISize;
                    jigTables[fpW.C][fpW.R].posY -= (fpW.R - fpBase.R) * picISize;
                    fpW.anchorId = anchorBase;
                    fpW.mode = aniANCHOR; // make it not zero
                    fpW.count = 5;
                    fps.set(i, fpW);
//                    Log.w( fpW.C+"x"+fpW.R+" fpW "+i, "fpW "+
//                            jigTables[fpW.C][fpW.R].posX +"x"+jigTables[fpW.C][fpW.R].posY);
                } else if (fpW.anchorId == anchorBase) {
                    fpW.mode = aniANCHOR; // make it not zero
                    fpW.count = 5;
                    fps.set(i, fpW);
                }
            }

            for (int i = 0; i < fps.size(); i++) {
                Log.w(fps.get(i).C+"x"+fps.get(i).R+" after "+i, jigTables[fps.get(i).C][fps.get(i).R].posX +"x"+jigTables[fps.get(i).C][fps.get(i).R].posY);
            }
            wait4Up = true; // wait while retouched
            doNotUpdate = false;
        }
    }


}
