package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.MainActivity.baseX;
import static com.riopapa.jigsawpuzzle.MainActivity.baseY;
import static com.riopapa.jigsawpuzzle.MainActivity.aniANCHOR;
import static com.riopapa.jigsawpuzzle.MainActivity.aniTO_PAINT;
import static com.riopapa.jigsawpuzzle.MainActivity.fps;
import static com.riopapa.jigsawpuzzle.MainActivity.jigTables;
import static com.riopapa.jigsawpuzzle.MainActivity.offsetC;
import static com.riopapa.jigsawpuzzle.MainActivity.offsetR;
import static com.riopapa.jigsawpuzzle.MainActivity.picHSize;
import static com.riopapa.jigsawpuzzle.MainActivity.picISize;
import static com.riopapa.jigsawpuzzle.MainActivity.pieceImage;
import static com.riopapa.jigsawpuzzle.MainActivity.showMaxX;
import static com.riopapa.jigsawpuzzle.MainActivity.showMaxY;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.riopapa.jigsawpuzzle.model.FloatPiece;
import com.riopapa.jigsawpuzzle.model.JigTable;

public class PieceDraw {
    Paint pGrayed;
    public PieceDraw() {
        pGrayed = new Paint();
        pGrayed.setAlpha(100);
    }

    public void draw(Canvas canvas){
        canvas.save();

        // draw locked pieces first with .pic
        for (int c = 0; c < showMaxX; c++) {
            for (int r = 0; r < showMaxY; r++) {
                final int cc = c+offsetC; final int rr = r+offsetR;
                if (jigTables[cc][rr].oLine == null)
                    pieceImage.makeAll(c+offsetC, r+offsetR);
                if (jigTables[cc][rr].locked) {
                    if (jigTables[cc][rr].count == 0)
                        canvas.drawBitmap(jigTables[cc][rr].pic,
                                baseX + c * picISize, baseY + r * picISize, null);
                    else {
                        jigTables[cc][rr].count--;
                        canvas.drawBitmap((jigTables[cc][rr].count % 2 == 0) ?
                                        jigTables[cc][rr].picBright : jigTables[cc][rr].pic,
                                baseX + c * picISize, baseY + r * picISize, null);
                        if (jigTables[cc][rr].count == 0) {
                            jigTables[cc][rr].picBright = null;
                        }
                    }
                }
            }
        }
        // then empty pieces with .oline
        for (int c = 0; c < showMaxX; c++) {
            for (int r = 0; r < showMaxY; r++) {
                final int cc = c+offsetC; final int rr = r+offsetR;
                if (!jigTables[cc][rr].locked) {
                    canvas.drawBitmap(jigTables[cc][rr].oLine,
                            baseX + c * picISize, baseY + r * picISize, pGrayed);
                }
            }
        }

        // drawing floating pieces
        for (int cnt = 0; cnt < fps.size(); cnt++) {
            FloatPiece fp = fps.get(cnt);
            int c = fp.C;
            int r = fp.R;
            JigTable jt = jigTables[c][r];
            if (fp.count == 0) { // normal pieceImage
                canvas.drawBitmap(fp.oLine, jt.posX, jt.posY, null);
                continue;
            }
            // animate just anchored
            if (fp.count > 0 && fp.mode == aniANCHOR) {
                fp.count--;
                canvas.drawBitmap((fp.count % 2 == 0) ?
                                jigTables[c][r].picBright : jigTables[c][r].pic,
                        jigTables[c][r].posX, jigTables[c][r].posY, null);
                if (fp.count == 0) {
                    fp.mode = 0;
                }
                fps.set(cnt, fp);
                continue;
            }
            // animate recycler to paint
            if (fp.count > 0 && fp.mode == aniTO_PAINT) {  // animate from recycle to paintView
                fp.count--;
                canvas.drawBitmap((fp.count % 2 == 0) ?
                                jigTables[c][r].picBright : jigTables[c][r].pic,
                        jigTables[c][r].posX, jigTables[c][r].posY, null);
                jigTables[c][r].posY -= picHSize / 2;
                if (fp.count == 0) {
                    fp.mode = 0;
                }
                fps.set(cnt, fp);
            }
        }
        canvas.restore();
//        String txt = "onD c" + nowC +" r"+ nowR + "\noffCR "+offsetC + " x " + offsetR+"\n calc " + calcC +" x "+ calcR+"\n fPs "+fPs.size();
//        mActivity.runOnUiThread(() -> tvRight.setText(txt));

    }

}
