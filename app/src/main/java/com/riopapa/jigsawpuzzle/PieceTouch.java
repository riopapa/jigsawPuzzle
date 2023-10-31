package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.MainActivity.aniANCHOR;
import static com.riopapa.jigsawpuzzle.MainActivity.aniTO_PAINT;
import static com.riopapa.jigsawpuzzle.MainActivity.fps;
import static com.riopapa.jigsawpuzzle.MainActivity.hangOn;
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

import java.util.Collections;

public class PieceTouch {


    public void move(float fMovedX, float fMovedY){
        if (hangOn)
            return;
        if (!oneItemSelected)
            return;

        int moveX = (int) fMovedX - picHSize;
        int moveY = (int) fMovedY - picHSize;
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
        } else if (moveY > screenY - recySize - picHSize && fps.size() > 0) {
            hangOn = true;
            Log.w("pchk Check", "fps size="+ fps.size()+" fPIdx="+ nowIdx +" now CR "+ nowC +"x"+nowR);
            fps.remove(nowIdx);
            insert2Recycle.sendEmptyMessage(0);
            oneItemSelected = false;
            hangOn = false;
        }

        // check whether can be anchored to near by pieceImage
        int ancIdx = nearByFloatPiece.anchor(nowIdx, fpNow);
        if (ancIdx != -1) {
            hangOn = true;
            if (fpNow.anchorId == 0) {
                fpNow.anchorId = fpNow.uId;
                fps.set(nowIdx, fpNow);
            }
            long nowId = fpNow.anchorId;
            FloatPiece fpAnchor = fps.get(ancIdx);
            if (fpAnchor.anchorId == 0) {
                fpAnchor.anchorId = nowId;
                fps.set(ancIdx, fpAnchor);
            }
            long ancId = fpAnchor.anchorId;
            jigTables[nowC][nowR].posX =
                    jigTables[fpAnchor.C][fpAnchor.R].posX + (nowC - fpAnchor.C) * picISize;
            jigTables[nowC][nowR].posY =
                    jigTables[fpAnchor.C][fpAnchor.R].posY + (nowR - fpAnchor.R) * picISize;
            for (int i = 0; i < fps.size(); i++) {
                FloatPiece fpT = fps.get(i);
                if (fpT.anchorId == nowId) {
                    fpT.anchorId = ancId;
                    fpT.mode = aniANCHOR; // make it not zero
                    fpT.count = 5;
                    fps.set(i, fpT);
//                    Collections.swap(fps, i, fps.size() - 1);
                }
            }

            hangOn = false;
        }
    }


}
