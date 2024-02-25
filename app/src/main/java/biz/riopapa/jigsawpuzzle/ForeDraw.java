package biz.riopapa.jigsawpuzzle;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.allLockedMode;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.colorOutline;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.congCount;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.congrats;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemX;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemY;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.fireWorks;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigFinishes;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigOLine;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigPic;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigWhite;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemC;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemR;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.save_History;
import static biz.riopapa.jigsawpuzzle.ActivityMain.currGameLevel;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gameMode;
import static biz.riopapa.jigsawpuzzle.ActivityMain.mContext;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenX;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenY;
import static biz.riopapa.jigsawpuzzle.ForeView.backBlink;
import static biz.riopapa.jigsawpuzzle.ForeView.foreBlink;
import static biz.riopapa.jigsawpuzzle.ItemMoveCallback.nowDragging;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import java.util.Random;

import biz.riopapa.jigsawpuzzle.databinding.ActivityJigsawBinding;
import biz.riopapa.jigsawpuzzle.func.GValGetPut;
import biz.riopapa.jigsawpuzzle.images.PieceImage;
import biz.riopapa.jigsawpuzzle.model.FloatPiece;

public class ForeDraw {
    Paint pGrayed0, pGrayed1, lPaint, pathPaint;
    Random rnd;
    int gapTwo;
    final int LOW_ALPHA = 117;
    PieceImage pieceImage;

    ActivityJigsawBinding binding;
    Paint backPaint;
    public ForeDraw(ActivityJigsawBinding binding, PieceImage pieceImage) {
        this.binding = binding;
        this.pieceImage = pieceImage;

        pGrayed0 = new Paint();
        pGrayed1 = new Paint();
        pGrayed1.setAlpha(LOW_ALPHA);
        lPaint = new Paint();
        lPaint.setColor(Color.RED);

        pathPaint = new Paint();
        pathPaint.setColor(colorOutline);
        backPaint = new Paint();
        backPaint.setAlpha(160);

        gapTwo = gVal.picGap + gVal.picGap;
        rnd = new Random(System.currentTimeMillis() & 0xFFFFF);
    }

    public void draw(Canvas canvas){
            canvas.save();
            showJustLocked(canvas);
            showFloatingPieces(canvas);
            if (nowDragging && itemX > 0)
                canvas.drawBitmap(jigOLine[itemC][itemR], itemX, itemY, null);
            if (congCount > 0)
                showCongrats(canvas);
            canvas.restore();
    }

    private void showJustLocked(Canvas canvas) {
        int lockedCount = 0;

        for (int c = 0; c < gVal.showMaxX; c++) {
            for (int r = 0; r < gVal.showMaxY; r++) {
                final int ac = c + gVal.offsetC;
                final int ar = r + gVal.offsetR;
                if (gVal.jigTables[ac][ar].locked)
                    lockedCount++;
                if (gVal.jigTables[ac][ar].count == 0)
                    continue;
                if (jigWhite[ac][ar] == null)
                    jigWhite[ac][ar] = pieceImage.makeWhite(jigOLine[ac][ar]);
                gVal.jigTables[ac][ar].count -= 1 + rnd.nextInt(2);
                if (gVal.jigTables[ac][ar].count < 0)
                    gVal.jigTables[ac][ar].count = 0;
                if (gVal.jigTables[ac][ar].count == 0) {
                    jigOLine[ac][ar] = pieceImage.makeOline(jigPic[ac][ar], ac, ar);
                    jigWhite[ac][ar] = null;
                    backBlink = true;
                }
                Bitmap bMap ;
                int offset = 0;
                if (gVal.jigTables[ac][ar].count == 0)
                    bMap = jigOLine[ac][ar];
                else if (gVal.jigTables[ac][ar].count % 3 == 1) {
                    bMap = fireWorks[gVal.jigTables[ac][ar].count];
                    offset = -gVal.picGap;
                }
                else if ((gVal.jigTables[ac][ar].count % 2 == 0))
                    bMap = jigOLine[ac][ar];
                else
                    bMap = jigWhite[ac][ar];
                canvas.drawBitmap(bMap,
                        gVal.baseX + c * gVal.picISize + offset,
                        gVal.baseY + r * gVal.picISize + offset, null);

                foreBlink = true;
            }
        }
        if (allLockedMode == 10  && lockedCount == gVal.showMaxX * gVal.showMaxY) {
            congCount = jigFinishes.length * 3;
            allLockedMode = 20;
            int locked = 0;
            for (int ac = 0; ac < gVal.colNbr; ac++) {
                for (int ar = 0; ar < gVal.rowNbr; ar++) {
                    if (gVal.jigTables[ac][ar].locked)
                        locked++;
                    else
                        break;
                }
            }
            if (locked == gVal.colNbr * gVal.rowNbr) {
                congCount = congrats.length * 3;
                save_History();
            }
            new GValGetPut().put(currGameLevel, gVal, mContext);
            foreBlink = true;
        }
    }

    private void showFloatingPieces(Canvas fCanvas) {
        for (int cnt = 0; cnt < gVal.fps.size(); cnt++) {
            FloatPiece fp = gVal.fps.get(cnt);
            int c = fp.C;
            int r = fp.R;
            if (jigOLine[c][r] == null)
                jigOLine[c][r] = pieceImage.makePic(c,r);

            if (fp.mode == null) { // normal pieceImage
                fCanvas.drawBitmap(jigOLine[c][r], fp.posX, fp.posY, null);
//                Log.w(topIdx+" fp ("+cnt,fp.C+"x"+fp.R+" pos = "+fp.posX+" x "+fp.posY);
                continue;
            }
            // animate just anchored
            if (fp.count > 0) {
                fp.count--;
                if (fp.mode == ActivityMain.GMode.ANCHOR) {
                    Bitmap oMap = pieceImage.makeBright(jigOLine[c][r]);
                    Matrix matrix = new Matrix();
                    if (fp.count > 0)
                        matrix.postRotate(2 * (2 - fp.count / 2f + rnd.nextInt(4)));
                    Bitmap rBitMap = Bitmap.createBitmap(oMap, 0, 0,
                            gVal.picOSize, gVal.picOSize, matrix, true);
                    fCanvas.drawBitmap(rBitMap, fp.posX, fp.posY, null);
                } else if (fp.mode == ActivityMain.GMode.TO_FPS) {  // animate from recycle to foreView
                    Matrix matrix = new Matrix();
                    if (fp.count > 0)
                        matrix.postRotate(3 * (fp.count - 4));
                    Bitmap rBitMap = Bitmap.createBitmap(jigOLine[c][r], 0, 0,
                            gVal.picOSize, gVal.picOSize, matrix, true);
                    fCanvas.drawBitmap(rBitMap, fp.posX, fp.posY, null);
                } else {
                    Log.e("fp Mode","Mode Error "+fp.mode);
                }
                if (fp.count == 0)
                    fp.mode = null;
                else
                    foreBlink = true;
                gVal.fps.set(cnt, fp);
//            } else {
//                fCanvas.drawBitmap(jigOLine[c][r], fp.posX, fp.posY, null);
            }
            foreBlink = true;
        }
    }

    private void showCongrats(Canvas fCanvas) {
        foreBlink = true;
        congCount--;
        if (congCount == 0) {
            if (allLockedMode == 30) {
                gameMode = ActivityMain.GMode.ALL_DONE;
                allLockedMode = 99;
            } else
                allLockedMode = 2;
            return;
        }
        int x = screenX * 16 / 100 + rnd.nextInt(6);
        int y = screenY * 35 / 100 + rnd.nextInt(6);
        Paint paint = new Paint();
        paint.setAlpha(200);
        int idx = congCount % ((allLockedMode == 30) ? congrats.length : jigFinishes.length);
        fCanvas.drawBitmap((allLockedMode == 30) ? congrats[idx] : jigFinishes[idx], x, y, paint);
    }
}
