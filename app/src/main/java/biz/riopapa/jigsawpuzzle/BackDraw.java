package biz.riopapa.jigsawpuzzle;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.allLockedMode;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.areaMap;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageColor;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.congCount;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigOLine;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigPic;
import static biz.riopapa.jigsawpuzzle.ActivityMain.GAME_COMPLETED;
import static biz.riopapa.jigsawpuzzle.ActivityMain.congrats;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gameMode;
import static biz.riopapa.jigsawpuzzle.ActivityMain.jigDones;
import static biz.riopapa.jigsawpuzzle.ActivityMain.mContext;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenBottom;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenX;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenY;
import static biz.riopapa.jigsawpuzzle.ActivityMain.showBack;
import static biz.riopapa.jigsawpuzzle.ActivityMain.showBackCount;
import static biz.riopapa.jigsawpuzzle.ActivityMain.showBackLoop;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.Random;

import biz.riopapa.jigsawpuzzle.databinding.ActivityJigsawBinding;
import biz.riopapa.jigsawpuzzle.images.PieceImage;

public class BackDraw {
    Paint pFull, pGrayed0, pGrayed1, lPaint, pathPaint;
    Random rnd;
    int gapSmall, gapTwo;
    final int LOW_ALPHA = 117;
    PieceImage pieceImage;
    Paint backPaint;
    ActivityJigsawBinding binding;

    public BackDraw(ActivityJigsawBinding binding) {
        this.binding = binding;
        pFull = new Paint();
        pGrayed0 = new Paint();
        pGrayed1 = new Paint();
        pGrayed1.setAlpha(LOW_ALPHA);
        lPaint = new Paint();
        lPaint.setColor(Color.RED);

        pathPaint = new Paint();
        pathPaint.setColor(chosenImageColor);
        backPaint = new Paint();
        backPaint.setAlpha(100);
        pieceImage = new PieceImage(mContext, gVal.imgOutSize, gVal.imgInSize);

        gapTwo = gVal.picGap + gVal.picGap;
        rnd = new Random(System.currentTimeMillis() & 0xFFFFF);
    }

    public void draw(Canvas canvas){
        canvas.save();
        canvas.drawLine(gVal.picHSize, screenBottom, screenX- gVal.picHSize, screenBottom, lPaint);
        backPieceImages(canvas);
        showPieceDiamondPoint(canvas);
        canvas.restore();
    }

    private void backPieceImages(Canvas canvas) {

        int alpha = 255 * (showBackCount--) / showBackLoop;
        if (alpha < LOW_ALPHA)
            alpha = LOW_ALPHA;
        pGrayed0.setAlpha(alpha);
        for (int c = 0; c < gVal.showMaxX; c++) {
            for (int r = 0; r < gVal.showMaxY; r++) {
                final int cc = c + gVal.offsetC;
                final int rr = r + gVal.offsetR;
                if (jigPic[cc][rr] == null)
                    jigPic[cc][rr] = pieceImage.makePic(cc, rr);
                if (jigOLine[cc][rr] == null)
                    jigOLine[cc][rr] = pieceImage.makeOline(jigPic[cc][rr], cc, rr);
                if (gVal.jigTables[cc][rr].locked) {
                    canvas.drawBitmap(jigOLine[cc][rr],   // later jigShadow
                            gVal.baseX + c * gVal.picISize,
                            gVal.baseY + r * gVal.picISize,
                            pFull);
                } else if (showBack == 0) {
                    canvas.drawBitmap(jigPic[cc][rr],   // later jigShadow
                            gVal.baseX + c * gVal.picISize,
                            gVal.baseY + r * gVal.picISize,
                            pGrayed0);
                } else if (showBack == 1) {
                    canvas.drawBitmap(jigPic[cc][rr],   // later jigShadow
                            gVal.baseX + c * gVal.picISize,
                            gVal.baseY + r * gVal.picISize,
                            pGrayed1);
                }
            }
        }

    }

    private void showPieceDiamondPoint(Canvas canvas) {

        if (showBack == 0 || showBack == 1) {

            gapSmall = gVal.picGap / ((showBack == 0) ? 3 : 4);
            for (int c = 1; c < gVal.showMaxX; c++) {
                for (int r = 1; r < gVal.showMaxY; r++) {
                    Path path = new Path();
                    int x0 = gVal.baseX + gapTwo + c * gVal.picISize;
                    int y0 = gVal.baseY + gapTwo + r * gVal.picISize ;
                    path.moveTo(x0 - gapSmall, y0);
                    path.lineTo(x0, y0 - gapSmall);
                    path.lineTo(x0 + gapSmall, y0);
                    path.lineTo(x0, y0 + gapSmall);
                    path.lineTo(x0 - gapSmall, y0);
                    canvas.drawPath(path, pathPaint);
                }
            }
        }
    }
}
