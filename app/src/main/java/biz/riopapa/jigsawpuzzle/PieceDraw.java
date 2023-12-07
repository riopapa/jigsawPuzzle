package biz.riopapa.jigsawpuzzle;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageColor;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.dragX;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.dragY;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigOLine;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigPic;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigWhite;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.nowC;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.nowR;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.pieceImage;
import static biz.riopapa.jigsawpuzzle.ActivityMain.ANI_ANCHOR;
import static biz.riopapa.jigsawpuzzle.ActivityMain.ANI_TO_FPS;
import static biz.riopapa.jigsawpuzzle.ActivityMain.fireWorks;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ActivityMain.mContext;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenBottom;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenX;
import static biz.riopapa.jigsawpuzzle.ActivityMain.showBack;
import static biz.riopapa.jigsawpuzzle.JigRecycleCallback.nowDragging;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.Random;

import biz.riopapa.jigsawpuzzle.model.FloatPiece;

public class PieceDraw {
    Paint pGrayed0, pGrayed1, lPaint, pathPaint;
    Random rnd;
    int gapSmall, gapTwo;
    public PieceDraw() {
        pGrayed0 = new Paint();
        pGrayed0.setAlpha(160);
        pGrayed1 = new Paint();
        pGrayed1.setAlpha(100);
        lPaint = new Paint();
        lPaint.setColor(Color.RED);

        pathPaint = new Paint();
        pathPaint.setColor(chosenImageColor);
        gapSmall = gVal.picGap / 3;
        gapTwo = gVal.picGap + gVal.picGap;
        rnd = new Random(System.currentTimeMillis() & 0xFFFFF);
    }

    public void draw(Canvas canvas){
        canvas.save();
        canvas.drawLine(gVal.picHSize, screenBottom, screenX- gVal.picHSize, screenBottom, lPaint);

        // draw locked pieces first with .pic
        for (int c = 0; c < gVal.showMaxX; c++) {
            for (int r = 0; r < gVal.showMaxY; r++) {
                final int cc = c+ gVal.offsetC;
                final int rr = r+ gVal.offsetR;
                if (jigPic[cc][rr] == null)
                    jigPic[cc][rr] = pieceImage.makePic(cc, rr);

                if (jigOLine[cc][rr] == null)
                    jigOLine[cc][rr] = pieceImage.makeOline(jigPic[cc][rr], cc, rr);

                if (gVal.jigTables[cc][rr].locked) {
                    if (gVal.jigTables[cc][rr].count == 0)
                        canvas.drawBitmap((showBack == 0) ? jigOLine[cc][rr] : jigPic[cc][rr],
                                gVal.baseX + c * gVal.picISize, gVal.baseY + r * gVal.picISize, null);
                    else {
                        if (jigWhite[cc][rr] == null)
                            jigWhite[cc][rr] = pieceImage.makeWhite(jigOLine[cc][rr]);

                        gVal.jigTables[cc][rr].count -= 1 + rnd.nextInt(4);
                        if (gVal.jigTables[cc][rr].count < 0)
                            gVal.jigTables[cc][rr].count = 0;

                        canvas.drawBitmap((gVal.jigTables[cc][rr].count % 2 == 0)?
                                jigOLine[cc][rr] : jigWhite[cc][rr],
                                    gVal.baseX + c * gVal.picISize, gVal.baseY + r * gVal.picISize, null);
                        if (gVal.jigTables[cc][rr].count % 3 == 0)
                            canvas.drawBitmap(fireWorks[gVal.jigTables[cc][rr].count],
                                    gVal.baseX + c * gVal.picISize - gVal.picGap,
                                    gVal.baseY + r * gVal.picISize - gVal.picGap, null);
                        if (gVal.jigTables[cc][rr].count == 0) {
                            jigOLine[cc][rr] = pieceImage.makeOline(jigPic[cc][rr], cc, rr);
                            jigWhite[cc][rr] = null;
                            System.gc();
                        }
                    }
                }
            }
        }
        // then empty pieces

        if (showBack == 0) {
            for (int c = 0; c < gVal.showMaxX; c++) {
                for (int r = 0; r < gVal.showMaxY; r++) {
                    final int cc = c + gVal.offsetC;
                    final int rr = r + gVal.offsetR;
                    if (!gVal.jigTables[cc][rr].locked) {
                        canvas.drawBitmap(jigPic[cc][rr],   // later jigShadow
                                gVal.baseX + c * gVal.picISize,
                                gVal.baseY + r * gVal.picISize,
                                pGrayed0);
                    }
                }
            }
        } else if (showBack == 1) {
            for (int c = 0; c < gVal.showMaxX; c++) {
                for (int r = 0; r < gVal.showMaxY; r++) {
                    final int cc = c + gVal.offsetC;
                    final int rr = r + gVal.offsetR;
                    if (!gVal.jigTables[cc][rr].locked) {
                        canvas.drawBitmap(jigPic[cc][rr],
                                gVal.baseX + c * gVal.picISize,
                                gVal.baseY + r * gVal.picISize,
                                pGrayed1);
                    }
                }
            }
        } else {
            int xSz = gVal.picISize * gVal.showMaxX;
            int ySz = gVal.picISize * gVal.showMaxY;
            int xBase = gVal.baseX + gVal.picGap + gVal.picGap/2;
            int yBase = gVal.baseY + gVal.picGap + gVal.picGap/2;
            Paint pBox = new Paint();
            pBox.setColor(mContext.getColor(R.color.jigsaw_background));
            pBox.setStrokeWidth(5);
            canvas.drawLine(xBase, yBase, xBase+xSz, yBase, pBox);
            canvas.drawLine(xBase, yBase, xBase, yBase+ySz, pBox);
            canvas.drawLine(xBase+xSz, yBase, xBase+xSz, yBase+ySz, pBox);
            canvas.drawLine(xBase, yBase+ySz, xBase+xSz, yBase+ySz, pBox);
        }
        // draw hint points
        if (showBack == 0 || showBack == 1) {
            for (int c = 1; c < gVal.showMaxX; c++) {
                for (int r = 1; r < gVal.showMaxY; r++) {
                    Path path = new Path();
                    path.moveTo(gVal.baseX + gapTwo + c * gVal.picISize,
                            gVal.baseY + gapTwo + r * gVal.picISize - gapSmall);
                    path.lineTo(gVal.baseX + gVal.picGap + c * gVal.picISize + gapSmall,
                            gVal.baseY + gapTwo + r * gVal.picISize);
                    path.lineTo(gVal.baseX + gapTwo + c * gVal.picISize,
                            gVal.baseY + gapTwo + r * gVal.picISize + gapSmall);
                    path.lineTo(gVal.baseX + gapTwo + c * gVal.picISize - gapSmall,
                            gVal.baseY + gapTwo + r * gVal.picISize);
                    canvas.drawPath(path, pathPaint);
                }
            }
        }
        // drawing floating pieces
        for (int cnt = 0; cnt < gVal.fps.size(); cnt++) {
            FloatPiece fp = gVal.fps.get(cnt);
            int c = fp.C;
            int r = fp.R;
            if (jigOLine[c][r] == null)
                jigOLine[c][r] = pieceImage.makePic(c,r);

            if (fp.count == 0) { // normal pieceImage
                canvas.drawBitmap(jigOLine[c][r], fp.posX, fp.posY, null);
                continue;
            }
            // animate just anchored
            if (fp.count > 0 && fp.mode == ANI_ANCHOR) {
                fp.count--;
                Bitmap oMap = pieceImage.makeBright(jigOLine[c][r]);
                Matrix matrix = new Matrix();
                matrix.postRotate(2 * (2 - fp.count / 2f + rnd.nextInt(4)));
                Bitmap rBitMap = Bitmap.createBitmap(oMap, 0, 0,
                        gVal.picOSize, gVal.picOSize, matrix, true);
                canvas.drawBitmap(rBitMap, fp.posX, fp.posY, null);
                if (fp.count == 0) {
                    fp.mode = 0;
                }
                gVal.fps.set(cnt, fp);
                continue;
            }
            // animate recycler to paint
            if (fp.count > 0 && fp.mode == ANI_TO_FPS) {  // animate from recycle to paintView
                fp.count--;
                Matrix matrix = new Matrix();
                matrix.postRotate(3 * (fp.count - 4));
                Bitmap rBitMap = Bitmap.createBitmap(jigOLine[c][r], 0, 0,
                        gVal.picOSize, gVal.picOSize, matrix, true);
                canvas.drawBitmap(rBitMap, fp.posX, fp.posY, null);

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

        canvas.restore();
//        String txt = "onD c" + nowC +" r"+ nowR + "\noffCR "+GVal.offsetC + " x " + GVal.offsetR+"\n calc " + calcC +" x "+ calcR+"\n GVal.fps "+GVal.fps.size();
//        mActivity.runOnUiThread(() -> tvRight.setText(txt));

    }

}
