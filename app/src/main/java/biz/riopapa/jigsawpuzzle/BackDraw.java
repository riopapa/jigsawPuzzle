package biz.riopapa.jigsawpuzzle;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.colorOutline;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigGray;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigOLine;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigPic;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenBottom;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenX;
import static biz.riopapa.jigsawpuzzle.ActivityMain.showBack;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

import biz.riopapa.jigsawpuzzle.databinding.ActivityJigsawBinding;
import biz.riopapa.jigsawpuzzle.images.PieceImage;

public class BackDraw {
    Paint pFull, pGrayed0, pGrayed1, pGrayed2, lPaint, pathPaint;
    Random rnd;
    int gapSmall, gapTwo;
    final int LOW_ALPHA = 200;
    final int HIDE_ALPHA = 100;
    ActivityJigsawBinding binding;
    PieceImage pieceImage;

    public BackDraw(ActivityJigsawBinding binding, PieceImage pieceImage) {
        this.binding = binding;
        this.pieceImage = pieceImage;
        pFull = new Paint();
        pGrayed0 = new Paint();
        pGrayed1 = new Paint();
        pGrayed1.setAlpha(LOW_ALPHA);
        pGrayed2 = new Paint();
        pGrayed2.setAlpha(HIDE_ALPHA);
        lPaint = new Paint();
        lPaint.setColor(Color.RED);

        pathPaint = new Paint();
        pathPaint.setColor(colorOutline);

        gapTwo = gVal.picGap + gVal.picGap;
        gapSmall = gVal.picGap / 4;
        rnd = new Random(System.currentTimeMillis() & 0xFFFFF);
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.drawLine(gVal.picHSize, screenBottom, screenX - gVal.picHSize,
                screenBottom, lPaint);
        backUnLocked(canvas);
        backLocked(canvas);
        canvas.restore();
    }

    private void backUnLocked(Canvas canvas) {

        pGrayed0.setAlpha(LOW_ALPHA);
        for (int c = 0; c < gVal.showMaxX; c++) {
            for (int r = 0; r < gVal.showMaxY; r++) {
                final int cc = c + gVal.offsetC;
                final int rr = r + gVal.offsetR;
                if (jigPic[cc][rr] == null)
                    jigPic[cc][rr] = pieceImage.makePic(cc, rr);
                if (jigOLine[cc][rr] == null)
                    jigOLine[cc][rr] = pieceImage.makeOline(jigPic[cc][rr], cc, rr);
                if (gVal.jigTables[cc][rr].locked)
                    continue;
                if (showBack == 0) {
                    canvas.drawBitmap(jigPic[cc][rr],   // later jigShadow
                            gVal.baseX + c * gVal.picISize,
                            gVal.baseY + r * gVal.picISize,
                            pGrayed0);
                } else if (showBack == 1) {
                    if (jigGray[cc][rr] == null)
                        jigGray[cc][rr] = pieceImage.makeGray(jigPic[cc][rr],
                                gVal.picOSize,gVal.picOSize);
                    canvas.drawBitmap(jigGray[cc][rr],   // later jigShadow
                            gVal.baseX + c * gVal.picISize,
                            gVal.baseY + r * gVal.picISize,
                            pGrayed1);
                } else {    // showBack == 2
                    if (jigGray[cc][rr] == null)
                        jigGray[cc][rr] = pieceImage.makeGray(jigPic[cc][rr],
                                gVal.picOSize,gVal.picOSize);
                    canvas.drawBitmap(jigGray[cc][rr],   // later jigShadow
                            gVal.baseX + c * gVal.picISize,
                            gVal.baseY + r * gVal.picISize,
                            pGrayed2);
                }
            }
        }
    }

    private void backLocked(Canvas canvas) {

        for (int c = 0; c < gVal.showMaxX; c++) {
            for (int r = 0; r < gVal.showMaxY; r++) {
                final int cc = c + gVal.offsetC;
                final int rr = r + gVal.offsetR;
                if (gVal.jigTables[cc][rr].locked) {
                    canvas.drawBitmap(jigOLine[cc][rr],   // later jigShadow
                            gVal.baseX + c * gVal.picISize,
                            gVal.baseY + r * gVal.picISize,
                            pFull);
                    jigGray[cc][rr] = null;
                }
            }
        }
    }
}

//                } else if (c > 0 && r > 0){
//                        Path path = new Path();
//                        int x0 = gVal.baseX + gapTwo + c * gVal.picISize;
//                        int y0 = gVal.baseY + gapTwo + r * gVal.picISize ;
//                        path.moveTo(x0 - gapSmall, y0);
//                        path.lineTo(x0, y0 - gapSmall);
//                        path.lineTo(x0 + gapSmall, y0);
//                        path.lineTo(x0, y0 + gapSmall);
//                        path.lineTo(x0 - gapSmall, y0);
//                        canvas.drawPath(path, pathPaint);

