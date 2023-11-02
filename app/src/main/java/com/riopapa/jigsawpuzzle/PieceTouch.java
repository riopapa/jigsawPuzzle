package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.MainActivity.aniANCHOR;
import static com.riopapa.jigsawpuzzle.MainActivity.aniTO_PAINT;
import static com.riopapa.jigsawpuzzle.MainActivity.fps;
import static com.riopapa.jigsawpuzzle.MainActivity.doNotUpdate;
import static com.riopapa.jigsawpuzzle.MainActivity.jigTables;
import static com.riopapa.jigsawpuzzle.MainActivity.nowC;
import static com.riopapa.jigsawpuzzle.MainActivity.nowR;
import static com.riopapa.jigsawpuzzle.MainActivity.oneItemSelected;
import static com.riopapa.jigsawpuzzle.MainActivity.picHSize;
import static com.riopapa.jigsawpuzzle.MainActivity.picISize;
import static com.riopapa.jigsawpuzzle.MainActivity.recySize;
import static com.riopapa.jigsawpuzzle.MainActivity.screenY;
import static com.riopapa.jigsawpuzzle.PaintView.fpNow;
import static com.riopapa.jigsawpuzzle.PaintView.nearBy;
import static com.riopapa.jigsawpuzzle.PaintView.nearByFloatPiece;
import static com.riopapa.jigsawpuzzle.PaintView.nowIdx;
import static com.riopapa.jigsawpuzzle.PaintView.rightPosition;
import static com.riopapa.jigsawpuzzle.RecycleJigListener.insert2Recycle;

import android.util.Log;

import com.riopapa.jigsawpuzzle.func.RearrangePieces;
import com.riopapa.jigsawpuzzle.model.FloatPiece;

public class PieceTouch {


    public void move(float fMovedX, float fMovedY){
        if (doNotUpdate)
            return;
        if (!oneItemSelected)
            return;

        int moveX = (int) fMovedX - picHSize;
        int moveY = (int) fMovedY - picHSize;

        if (moveY > (screenY - recySize * 3) && fpNow.anchorId > 0)
            return;

        jigTables[nowC][nowR].posX = moveX;
        jigTables[nowC][nowR].posY = moveY;

        // move anchored pieces too
        if (fpNow.anchorId > 0) {
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
        new RearrangePieces(fpNow, nowIdx);

        // check whether each pieces are in lockable position
        boolean lockable = false;
        long anchorId = -1;

        for (int i = 0; i < fps.size(); i++) {
            FloatPiece fpT = fps.get(i);
            if (nearBy.isLockable(fpT.C, fpT.R) && rightPosition.isHere(fpT.C, fpT.R,
                            jigTables[fpT.C][fpT.R].posX, jigTables[fpT.C][fpT.R].posY)) {
                if (fpT.anchorId == 0) {
                    fpT.anchorId = -1;
                    fps.set(i, fpT);
                }
                anchorId = fpT.anchorId;
                lockable = true;
                break;
            }
        }

        // if pieceImage moved to right rightPosition then lock thi pieceImage
        if (lockable) {
            oneItemSelected = false;
            for (int i = 0; i < fps.size(); ) {
                FloatPiece fpT = fps.get(i);
                if (fpT.anchorId == anchorId) {
                    jigTables[fpT.C][fpT.R].locked = true;
                    jigTables[fpT.C][fpT.R].count = 3;
                    jigTables[fpT.C][fpT.R].lockedTime = System.currentTimeMillis() + aniTO_PAINT;
                    fps.remove(i);
                } else
                    i++;
            }
            return;
        }

        // check whether go back to recycler
        if (moveY > (screenY - recySize * 3)) {
            if (fpNow.anchorId == 0) {
                doNotUpdate = true;
                Log.w("pchk Check", "fps size=" + fps.size() + " fPIdx=" + nowIdx + " now CR " + nowC + "x" + nowR);
                fps.remove(nowIdx);
                insert2Recycle.sendEmptyMessage(0);
                return;
            }
            // if anchored can not go back to recycle;
            return;
        }


        // check whether can be anchored to near by pieceImage

        int ancTo = -1;
        int ancFrom = -1;


        for (int iA = fps.size()-1; iA >= 0;  iA--) {
            FloatPiece fpA = fps.get(iA);
//            for (int j = 0; j < fps.size(); j++) {
                ancTo = nearByFloatPiece.anchor(iA, fpA);
                if (ancTo != -1) {
                    ancFrom = iA;
                    break;
                }
//            }
            if (ancTo != -1)
                break;
        }


        if (ancTo != -1) {
            doNotUpdate = true;

//            if (fpNow.anchorId == 0) {
//                ancFrom = nowIdx;
//            }




            FloatPiece fpTo = fps.get(ancTo);
            if (fpTo.anchorId == 0) {
                fpTo.anchorId = fpTo.uId;
                fps.set(ancTo, fpTo);
            }
            long anchorIdNow = fpTo.anchorId;
            FloatPiece fpFm = fps.get(ancFrom);
            if (fpFm.anchorId == 0) {
                fpFm.anchorId = anchorIdNow;
                fps.set(ancFrom, fpFm);
            }
            long ancId = fpTo.anchorId;

            for (int i = 0; i < fps.size(); i++) {
                FloatPiece fpW = fps.get(i);
                if (fpW.anchorId == anchorIdNow) {
                    jigTables[fpW.C][fpW.R].posX =
                            jigTables[fpW.C][fpW.R].posX + (fpW.C - fpTo.C) * picISize;
                    jigTables[fpW.C][fpW.R].posY =
                            jigTables[fpW.C][fpW.R].posY + (fpW.R - fpTo.R) * picISize;
                    fpW.anchorId = ancId;
                    fpW.mode = aniANCHOR; // make it not zero
                    fpW.count = 5;
                    fps.set(i, fpW);
//                    Collections.swap(fps, i, fps.size() - 1);
                }
            }
            doNotUpdate = false;
        }
    }


}
