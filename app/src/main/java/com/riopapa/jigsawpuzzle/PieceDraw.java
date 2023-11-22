package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.dragX;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.dragY;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigBright;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigOLine;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigPic;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigWhite;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowC;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowR;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.pieceImage;
import static com.riopapa.jigsawpuzzle.ActivityMain.ANI_ANCHOR;
import static com.riopapa.jigsawpuzzle.ActivityMain.ANI_TO_FPS;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenBottom;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenX;
import static com.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static com.riopapa.jigsawpuzzle.JigRecycleCallback.nowDragging;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.riopapa.jigsawpuzzle.model.FloatPiece;

import java.util.Random;

public class PieceDraw {
    Paint pGrayed, lPaint;
    Random rnd;
    public PieceDraw() {
        pGrayed = new Paint();
        pGrayed.setAlpha(50);
        lPaint = new Paint();
        lPaint.setColor(Color.RED);
        rnd = new Random(System.currentTimeMillis() & 0xFFFFF);
    }

    public void draw(Canvas canvas){
        canvas.save();

        // draw locked pieces first with .pic
        for (int c = 0; c < gVal.showMaxX; c++) {
            for (int r = 0; r < gVal.showMaxY; r++) {
                final int cc = c+ gVal.offsetC; final int rr = r+ gVal.offsetR;
                if (jigOLine[cc][rr] == null)
                    pieceImage.buildOline(c+ gVal.offsetC, r+ gVal.offsetR);
                if (jigWhite[cc][rr] == null)
                    jigWhite[cc][rr] = pieceImage.makeWhite(jigPic[cc][rr]);
                if (gVal.jigTables[cc][rr].locked) {
                    if (gVal.jigTables[cc][rr].count == 0)
                        canvas.drawBitmap(jigPic[cc][rr],
                                gVal.baseX + c * gVal.picISize, gVal.baseY + r * gVal.picISize, null);
                    else {
                        gVal.jigTables[cc][rr].count--;
                        canvas.drawBitmap((gVal.jigTables[cc][rr].count % 2 == 0) ?
                                        jigWhite[cc][rr] : jigPic[cc][rr],
                                gVal.baseX + c * gVal.picISize, gVal.baseY + r * gVal.picISize, null);
                        if (gVal.jigTables[cc][rr].count == 0) {
                            jigWhite[cc][rr] = null;
                        }
                    }
                }
            }
        }
        // then empty pieces with .oline
        for (int c = 0; c < gVal.showMaxX; c++) {
            for (int r = 0; r < gVal.showMaxY; r++) {
                final int cc = c+ gVal.offsetC; final int rr = r+ gVal.offsetR;
                if (!gVal.jigTables[cc][rr].locked) {
                    canvas.drawBitmap(jigOLine[cc][rr],
                            gVal.baseX + c * gVal.picISize, gVal.baseY + r * gVal.picISize,
                            pGrayed);
                }
            }
        }

        // drawing floating pieces
        for (int cnt = 0; cnt < gVal.fps.size(); cnt++) {
            FloatPiece fp = gVal.fps.get(cnt);
            int c = fp.C;
            int r = fp.R;

            if (fp.count == 0) { // normal pieceImage
                canvas.drawBitmap(jigOLine[c][r], fp.posX, fp.posY, null);
                continue;
            }
            // animate just anchored
            if (fp.count > 0 && fp.mode == ANI_ANCHOR) {
                fp.count--;
                if (jigBright[c][r] == null)
                    jigBright[c][r] = pieceImage.makeBright(jigOLine[c][r]);
                canvas.drawBitmap((fp.count % 2 == 0) ?
                        jigBright[c][r] : jigOLine[c][r],
                        fp.posX + rnd.nextInt(4) - 2,
                        fp.posY + rnd.nextInt(4) - 2, null);
                if (fp.count == 0) {
                    fp.mode = 0;
                }
                gVal.fps.set(cnt, fp);
                continue;
            }
            // animate recycler to paint
            if (fp.count > 0 && fp.mode == ANI_TO_FPS) {  // animate from recycle to paintView
                fp.count--;
                if (jigBright[c][r] == null)
                    jigBright[c][r] = pieceImage.makeBright(jigOLine[c][r]);
                canvas.drawBitmap((fp.count % 2 == 0) ?
                        jigBright[c][r] : jigOLine[c][r],
                        fp.posX + rnd.nextInt(4) - 2,
                        fp.posY + rnd.nextInt(4) - 2, null);
//                GVal.jigTables[c][r].posY -= GVal.picISize / 4;
                if (fp.count == 0) {
                    fp.mode = 0;
                }
                gVal.fps.set(cnt, fp);
            }
        }
        if (nowDragging) {
            canvas.drawBitmap(jigOLine[nowC][nowR],dragX, dragY, null);
//            Log.w("nowDragging", "piece "+dragX+"x"+dragY + " screenBottom="+screenBottom);
        }

        canvas.drawLine(gVal.picHSize, screenBottom, screenX- gVal.picHSize, screenBottom, lPaint);


        canvas.restore();
//        String txt = "onD c" + nowC +" r"+ nowR + "\noffCR "+GVal.offsetC + " x " + GVal.offsetR+"\n calc " + calcC +" x "+ calcR+"\n GVal.fps "+GVal.fps.size();
//        mActivity.runOnUiThread(() -> tvRight.setText(txt));

    }

}
