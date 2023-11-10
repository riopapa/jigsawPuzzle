package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.doNotUpdate;
import static com.riopapa.jigsawpuzzle.ActivityMain.ANI_ANCHOR;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenY;
import static com.riopapa.jigsawpuzzle.ActivityMain.vars;
import static com.riopapa.jigsawpuzzle.PaintView.fpNow;
import static com.riopapa.jigsawpuzzle.PaintView.nearByFloatPiece;
import static com.riopapa.jigsawpuzzle.PaintView.nearByPieces;
import static com.riopapa.jigsawpuzzle.PaintView.nowIdx;
import static com.riopapa.jigsawpuzzle.PaintView.rightPosition;
import static com.riopapa.jigsawpuzzle.RecycleJigListener.insert2Recycle;

import android.util.Log;

import com.riopapa.jigsawpuzzle.func.RearrangePieces;
import com.riopapa.jigsawpuzzle.model.FloatPiece;

public class PieceTouchMove {


    void start(float fMovedX, float fMovedY){
        if (doNotUpdate)
            return;

        int moveX = (int) fMovedX - vars.picHSize;
        int moveY = (int) fMovedY - vars.picHSize;

        // check whether go back to recycler
        if (moveY > (screenY - vars.recSize)) {
            // if sole piece then can go back to recycler
            if (fpNow.anchorId == 0 && vars.fps.size() > 0) {
                doNotUpdate = true;
                Log.w("pchk Check", "vars.fps size=" + vars.fps.size() + " fPIdx=" + nowIdx + " now CR " + vars.nowC + "x" + vars.nowR);
                vars.fps.remove(nowIdx);
                insert2Recycle.sendEmptyMessage(0);
            }
            // if anchored can not go back to recycle;
            return;
        }

        vars.jigTables[vars.nowC][vars.nowR].posX = moveX;
        vars.jigTables[vars.nowC][vars.nowR].posY = moveY;

        // move anchored pieces too
        if (fpNow.anchorId > 0) {
            for (int i = 0; i < vars.fps.size(); i++) {
                FloatPiece fpT = vars.fps.get(i);
                if (fpT.anchorId == fpNow.anchorId) {
                    vars.jigTables[fpT.C][fpT.R].posX =
                            vars.jigTables[vars.nowC][vars.nowR].posX - (vars.nowC - fpT.C) * vars.picISize;
                    vars.jigTables[fpT.C][fpT.R].posY =
                            vars.jigTables[vars.nowC][vars.nowR].posY - (vars.nowR - fpT.R) * vars.picISize;
                }
            }
        }
        if (nowIdx < vars.fps.size()) {
            new RearrangePieces(fpNow, nowIdx);
            nowIdx = vars.fps.size() - 1;
        }

        // check whether each pieces are in lockable position
        boolean lockable = false;
        long anchorId = fpNow.anchorId;

        for (int i = 0; i < vars.fps.size(); i++) {
            FloatPiece fpTo = vars.fps.get(i);
            if (nearByPieces.lockable(fpTo.C, fpTo.R) && rightPosition.isHere(fpTo.C, fpTo.R,
                            vars.jigTables[fpTo.C][fpTo.R].posX, vars.jigTables[fpTo.C][fpTo.R].posY)) {
                if (fpTo.anchorId == 0) {
                    fpTo.anchorId = -1;
                    vars.fps.set(i, fpTo);
                }
                anchorId = fpTo.anchorId;
                lockable = true;
                break;
            }
        }

        // if pieceImage moved to right rightPosition then lock thi pieceImage
        if (lockable) {
            for (int i = 0; i < vars.fps.size(); ) {
                FloatPiece fpT = vars.fps.get(i);
                if (fpT.anchorId == anchorId) {
                    vars.jigTables[fpT.C][fpT.R].locked = true;
                    vars.jigTables[fpT.C][fpT.R].count = 7;
                    vars.fps.remove(i);
                } else
                    i++;
            }
            return;
        }



        // check whether can be anchored to near by pieceImage

        int ancBase = -1;
        int ancThis = -1;

        FloatPiece fpThis = null, fpBase = null;

        for (int iA = vars.fps.size()-1; iA >= 0;  iA--) {
            fpThis = vars.fps.get(iA);
            ancBase = nearByFloatPiece.isAnchorable(iA, fpThis);
            if (ancBase != -1) {
                ancThis = iA;
                fpBase = vars.fps.get(ancBase);
                break;
            }
        }


        if (ancBase != -1) {
            doNotUpdate = true;

//            Log.w( fpNow.C+"x"+fpNow.R+" fpNow ", "fpNow "+
//                    vars.jigTables[fpNow.C][fpNow.R].posX +"x"+vars.jigTables[fpNow.C][fpNow.R].posY);
//            Log.w( fpBase.C+"x"+fpBase.R+" fpBase ", "fpBase "+
//                    vars.jigTables[fpBase.C][fpBase.R].posX +"x"+vars.jigTables[fpBase.C][fpBase.R].posY);
//            Log.w( fpThis.C+"x"+fpThis.R+" fpThis ", "fpThis "+
//                    vars.jigTables[fpThis.C][fpThis.R].posX +"x"+vars.jigTables[fpThis.C][fpThis.R].posY);

            if (fpBase.anchorId == 0) {
                fpBase.anchorId = fpBase.uId;
                vars.fps.set(ancBase, fpBase);
            }
            long anchorBase = fpBase.anchorId;

            if (fpThis.anchorId == 0) {
                fpThis.anchorId = fpThis.uId;
                vars.fps.set(ancThis, fpThis);
            }
            long anchorThis = fpThis.anchorId;

//            for (int i = 0; i < vars.fps.size(); i++) {
//                Log.w(vars.fps.get(i).C+"x"+vars.fps.get(i).R+" before "+i, vars.jigTables[vars.fps.get(i).C][vars.fps.get(i).R].posX +"x"+vars.jigTables[vars.fps.get(i).C][vars.fps.get(i).R].posY);
//            }

            for (int i = 0; i < vars.fps.size(); i++) {
                FloatPiece fpW = vars.fps.get(i);
                if (fpW.anchorId == anchorThis) {
//                    vars.jigTables[fpW.C][fpW.R].posX -= (fpW.C - fpBase.C) * vars.picISize;
//                    vars.jigTables[fpW.C][fpW.R].posY -= (fpW.R - fpBase.R) * vars.picISize;
                    fpW.anchorId = anchorBase;
                    fpW.mode = ANI_ANCHOR; // make it not zero
                    fpW.count = 5;
                    vars.fps.set(i, fpW);
//                    Log.w( fpW.C+"x"+fpW.R+" fpW "+i, "fpW "+
//                            vars.jigTables[fpW.C][fpW.R].posX +"x"+vars.jigTables[fpW.C][fpW.R].posY);
                } else if (fpW.anchorId == anchorBase) {
                    fpW.mode = ANI_ANCHOR; // make it not zero
                    fpW.count = 5;
                    vars.fps.set(i, fpW);
                }
            }

            // move anchored pieces too
            if (fpNow.anchorId > 0) {
                for (int i = 0; i < vars.fps.size(); i++) {
                    FloatPiece fpT = vars.fps.get(i);
                    if (fpT.anchorId == fpNow.anchorId) {
                        vars.jigTables[fpT.C][fpT.R].posX =
                                vars.jigTables[vars.nowC][vars.nowR].posX - (vars.nowC - fpT.C) * vars.picISize;
                        vars.jigTables[fpT.C][fpT.R].posY =
                                vars.jigTables[vars.nowC][vars.nowR].posY - (vars.nowR - fpT.R) * vars.picISize;
                    }
                }
            }
            doNotUpdate = false;
        }
    }


}
