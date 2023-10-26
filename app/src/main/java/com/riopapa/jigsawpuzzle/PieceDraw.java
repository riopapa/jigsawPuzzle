package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.MainActivity.baseX;
import static com.riopapa.jigsawpuzzle.MainActivity.baseY;
import static com.riopapa.jigsawpuzzle.MainActivity.fPs;
import static com.riopapa.jigsawpuzzle.MainActivity.jigTables;
import static com.riopapa.jigsawpuzzle.MainActivity.nowC;
import static com.riopapa.jigsawpuzzle.MainActivity.nowR;
import static com.riopapa.jigsawpuzzle.MainActivity.offsetC;
import static com.riopapa.jigsawpuzzle.MainActivity.offsetR;
import static com.riopapa.jigsawpuzzle.MainActivity.oneItemSelected;
import static com.riopapa.jigsawpuzzle.MainActivity.picGap;
import static com.riopapa.jigsawpuzzle.MainActivity.picHSize;
import static com.riopapa.jigsawpuzzle.MainActivity.picISize;
import static com.riopapa.jigsawpuzzle.MainActivity.piece;
import static com.riopapa.jigsawpuzzle.MainActivity.showMax;
import static com.riopapa.jigsawpuzzle.PaintView.dragging;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.riopapa.jigsawpuzzle.model.FloatPiece;
import com.riopapa.jigsawpuzzle.model.JigTable;

import java.util.Random;

public class PieceDraw {
    boolean allLocked;
    Paint pGrayed;
    public PieceDraw() {
        pGrayed = new Paint();
        pGrayed.setAlpha(100);
    }

    public void draw(Canvas canvas){
        canvas.save();
        allLocked = true;
        for (int c = 0; c < showMax; c++) {
            for (int r = 0; r < showMax; r++) {
                final int cc = c+offsetC; final int rr = r+offsetR;
                if (jigTables[cc][rr].oLine == null)
                    piece.makeAll(c+offsetC, r+offsetR);
                if (jigTables[cc][rr].locked) {
                    if (jigTables[cc][rr].lockedTime == 0)
                        canvas.drawBitmap(jigTables[cc][rr].pic,
                                baseX + c * picISize, baseY + r * picISize, null);
                    else {
                        if (jigTables[cc][rr].lockedTime > System.currentTimeMillis()) {
                            jigTables[cc][rr].count--;
                            canvas.drawBitmap((jigTables[cc][rr].count % 2 == 0) ?
                                            jigTables[cc][rr].picBright : jigTables[cc][rr].pic,
                                    baseX + c * picISize, baseY + r * picISize, null);
                        } else {
                            jigTables[cc][rr].lockedTime = 0;
                            jigTables[cc][rr].picBright = null;
                        }
                    }
                } else {
                    canvas.drawBitmap(jigTables[cc][rr].oLine,
                            baseX + c * picISize, baseY + r * picISize, pGrayed);
                    allLocked = false; // if any piece is unlocked then allLocked = false;
                }
            }
        }


        for (int cnt = 0; cnt < fPs.size(); cnt++) {
            FloatPiece fp = fPs.get(cnt);
            int c = fp.C;
            int r = fp.R;
            JigTable jt = jigTables[c][r];
            if (fp.time == 0) { // time == 0 means normal piece
                if (dragging && oneItemSelected && c == nowC && r == nowR) {
                    canvas.drawBitmap(fp.bigMap, jt.posX, jt.posY, null);
                } else
                    canvas.drawBitmap(fp.oLine, jt.posX, jt.posY, null);

            } else {    // timer to move recycler piece to PieceDraw
                if (fp.count > 0) {
                    fp.count--;
                    jigTables[c][r].posY -= picHSize + new Random().nextInt(5);
                    if (fp.count == 0)
                        fp.time = 0;
                    canvas.drawBitmap(fp.oLine, jt.posX, jt.posY, null);
                }
//                if (System.currentTimeMillis() < fp.time) {
//                    canvas.drawBitmap(fp.brightMap, jt.posX, jt.posY, null);
//                } else {
//                    canvas.drawBitmap(fp.oLine, jt.posX, jt.posY, null);
//                    fPs.remove(cnt);
////                    invalidate();
//                }
            }
        }

        canvas.restore();
//        String txt = "onD c" + nowC +" r"+ nowR + "\noffCR "+offsetC + " x " + offsetR+"\n calc " + calcC +" x "+ calcR+"\n fPs "+fPs.size();
//        mActivity.runOnUiThread(() -> tvRight.setText(txt));

    }

}
