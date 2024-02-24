package biz.riopapa.jigsawpuzzle;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.colorOutline;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigGray;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigLock;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigOLine;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigPic;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.reDrawOLine;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenBottom;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenX;
import static biz.riopapa.jigsawpuzzle.ActivityMain.share_showBack;

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

        if (jigPic == null || jigOLine == null)
            return;
        pGrayed0.setAlpha(LOW_ALPHA);
        for (int c = 0; c < gVal.showMaxX; c++) {
            for (int r = 0; r < gVal.showMaxY; r++) {
                final int ac = c + gVal.offsetC;
                final int ar = r + gVal.offsetR;
                if (jigPic[ac][ar] == null)
                    jigPic[ac][ar] = pieceImage.makePic(ac, ar);
                if (jigOLine[ac][ar] == null)
                    jigOLine[ac][ar] = pieceImage.makeOline(jigPic[ac][ar], ac, ar);
                if (gVal.jigTables[ac][ar].locked)
                    continue;
                if (share_showBack == 0) {
                    canvas.drawBitmap(jigPic[ac][ar],   // later jigShadow
                            gVal.baseX + c * gVal.picISize,
                            gVal.baseY + r * gVal.picISize,
                            pGrayed0);
                } else if (share_showBack == 1) {
                    if (jigGray[ac][ar] == null)
                        jigGray[ac][ar] = pieceImage.makeGray(jigPic[ac][ar],
                                gVal.picOSize,gVal.picOSize);
                    canvas.drawBitmap(jigGray[ac][ar],   // later jigShadow
                            gVal.baseX + c * gVal.picISize,
                            gVal.baseY + r * gVal.picISize,
                            pGrayed1);
                } else {    // showBack == 2
                    if (jigGray[ac][ar] == null)
                        jigGray[ac][ar] = pieceImage.makeGray(jigPic[ac][ar],
                                gVal.picOSize,gVal.picOSize);
                    canvas.drawBitmap(jigGray[ac][ar],   // later jigShadow
                            gVal.baseX + c * gVal.picISize,
                            gVal.baseY + r * gVal.picISize,
                            pGrayed2);
                }
            }
        }
    }

    private void backLocked(Canvas canvas) {

        if (jigOLine == null)
            return;
        for (int c = 0; c < gVal.showMaxX; c++) {
            for (int r = 0; r < gVal.showMaxY; r++) {
                final int ac = c + gVal.offsetC;
                final int ar = r + gVal.offsetR;
                if (gVal.jigTables[ac][ar].locked) {
                    if (reDrawOLine)
                        jigLock[ac][ar] = pieceImage.makeLock(jigPic[ac][ar], jigOLine[ac][ar], ac, ar);
                    canvas.drawBitmap(jigLock[ac][ar],   // later jigShadow
                            gVal.baseX + c * gVal.picISize,
                            gVal.baseY + r * gVal.picISize,
                            pFull);
                    jigGray[ac][ar] = null;
                }
            }
        }
        reDrawOLine = false;
    }
}

