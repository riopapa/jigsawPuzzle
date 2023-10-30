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

import com.riopapa.jigsawpuzzle.model.FloatPiece;

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

        // check whether each pieces are in lockable position
        boolean lockable = false;
        long anchorId = -1;

        for (int i = 0; i < fps.size(); i++) {
            FloatPiece fpT = fps.get(i);
            if (rightPosition.isHere(fpT.C, fpT.R,
                    jigTables[fpT.C][fpT.R].posX, jigTables[fpT.C][fpT.R].posY)
                    && nearBy.isLockable(fpT.C, fpT.R)) {
                if (fpT.anchorId == 0) {
                    fpT.anchorId = -1;
                    fps.set(i, fpT);
                }
                anchorId = fpT.anchorId;
                lockable = true;
                break;
            }
        }

        // if piece moved to right rightPosition then lock thi piece
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

        // check whether can be anchored to near by piece
        if (fpNow.anchorId == 0) {  // this is separated piece
            int ancIdx = nearByFloatPiece.anchor(nowIdx, fpNow);
            if (ancIdx != -1) {
                FloatPiece fpAnc = fps.get(ancIdx);
                if (fpAnc.anchorId == 0)
                    fpAnc.anchorId = fpAnc.uId;
                fpNow.anchorId = fpAnc.anchorId;
                fps.set(nowIdx, fpNow);
                jigTables[nowC][nowR].posX =
                        jigTables[fpAnc.C][fpAnc.R].posX + (nowC - fpAnc.C) * picISize;
                jigTables[nowC][nowR].posY =
                        jigTables[fpAnc.C][fpAnc.R].posY + (nowR - fpAnc.R) * picISize;
                for (int i = 0; i < fps.size(); i++) {
                    FloatPiece fpT = fps.get(i);
                    if (fpT.anchorId == fpNow.anchorId) {
                        fpT.mode = aniANCHOR; // make it not zero
                        fpT.count = 3;
                        fps.set(i, fpT);
                    }
                }
            }
        }
    }

}
