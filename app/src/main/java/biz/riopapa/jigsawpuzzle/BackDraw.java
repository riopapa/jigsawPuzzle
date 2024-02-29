package biz.riopapa.jigsawpuzzle;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.colorOutline;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigGray;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigLock;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigOLine;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigPic;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.pieceImage;
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

    public BackDraw(ActivityJigsawBinding binding) {
        this.binding = binding;
        pFull = new Paint();
        pGrayed0 = new Paint();
        pGrayed1 = new Paint();
        pGrayed1.setAlpha(LOW_ALPHA);
        pGrayed2 = new Paint();
        pGrayed2.setAlpha(HIDE_ALPHA);
        lPaint = new Paint();
        lPaint.setColor(Color.RED);
        pGrayed0.setAlpha(LOW_ALPHA);
//        pGrayed0.setAlpha(LOW_ALPHA);

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
        for (int c = 0; c < gVal.showMaxX; c++) {
            for (int r = 0; r < gVal.showMaxY; r++) {
                final int ac = c + gVal.offsetC;
                final int ar = r + gVal.offsetR;
                if (jigPic[ac][ar] == null)
                    jigPic[ac][ar] = pieceImage.makePic(ac, ar);
                if (jigOLine[ac][ar] == null)
                    jigOLine[ac][ar] = pieceImage.makeOline(jigPic[ac][ar], ac, ar);
                if (gVal.jigTables[ac][ar].locked)
                    drawLocked(canvas, c, r, ac, ar);
                else
                    drawUnLocked(canvas, c, r, ac, ar);
            }
        }
        canvas.restore();
    }

    private void drawUnLocked(Canvas canvas, int c, int r, int ac, int ar) {
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

    private void drawLocked(Canvas canvas, int c, int r, int ac, int ar) {
        jigLock[ac][ar] = pieceImage.makeLock(jigPic[ac][ar], jigOLine[ac][ar], ac, ar);
        canvas.drawBitmap(jigLock[ac][ar],   // later jigShadow
                gVal.baseX + c * gVal.picISize,
                gVal.baseY + r * gVal.picISize,
                pFull);
        jigGray[ac][ar] = null;
    }
}

