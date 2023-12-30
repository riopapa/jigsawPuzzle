package biz.riopapa.jigsawpuzzle;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.allLockedMode;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageColor;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.congCount;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.congrats;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemX;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemY;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.fireWorks;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigDones;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigOLine;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigPic;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigWhite;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemC;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemR;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gameMode;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenX;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenY;
import static biz.riopapa.jigsawpuzzle.ForeView.backBlink;
import static biz.riopapa.jigsawpuzzle.ForeView.foreBlink;
import static biz.riopapa.jigsawpuzzle.ForeView.topIdx;
import static biz.riopapa.jigsawpuzzle.ItemMoveCallback.nowDragging;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import java.util.Random;

import biz.riopapa.jigsawpuzzle.databinding.ActivityJigsawBinding;
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
        pathPaint.setColor(chosenImageColor);
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
//        String txt = "onD c" + nowC +" r"+ nowR + "\noffCR "+GVal.offsetC + " x " + GVal.offsetR+"\n calc " + calcC +" x "+ calcR+"\n GVal.fps "+GVal.fps.size();
//        mActivity.runOnUiThread(() -> tvRight.setText(txt));

    }

    private void showJustLocked(Canvas canvas) {
        int lockedCount = 0;

        for (int c = 0; c < gVal.showMaxX; c++) {
            for (int r = 0; r < gVal.showMaxY; r++) {
                final int cc = c + gVal.offsetC;
                final int rr = r + gVal.offsetR;
                if (gVal.jigTables[cc][rr].locked)
                    lockedCount++;
                if (gVal.jigTables[cc][rr].count == 0)
                    continue;
                if (jigWhite[cc][rr] == null)
                    jigWhite[cc][rr] = pieceImage.makeWhite(jigOLine[cc][rr]);
                gVal.jigTables[cc][rr].count -= 1 + rnd.nextInt(2);
                if (gVal.jigTables[cc][rr].count < 0)
                    gVal.jigTables[cc][rr].count = 0;
                if (gVal.jigTables[cc][rr].count == 0) {
                    jigOLine[cc][rr] = pieceImage.makeOline(jigPic[cc][rr], cc, rr);
                    jigWhite[cc][rr] = null;
                    backBlink = true;
                }
                Bitmap bMap ;
                int offset = 0;
                if (gVal.jigTables[cc][rr].count == 0)
                    bMap = jigOLine[cc][rr];
                else if (gVal.jigTables[cc][rr].count % 3 == 1) {
                    bMap = fireWorks[gVal.jigTables[cc][rr].count];
                    offset = -gVal.picGap;
                }
                else if ((gVal.jigTables[cc][rr].count % 2 == 0))
                    bMap = jigOLine[cc][rr];
                else
                    bMap = jigWhite[cc][rr];
                canvas.drawBitmap(bMap,
                        gVal.baseX + c * gVal.picISize + offset,
                        gVal.baseY + r * gVal.picISize + offset, null);

                foreBlink = true;
            }
        }
        if (allLockedMode == 10  && lockedCount == gVal.showMaxX * gVal.showMaxY) {
            congCount = jigDones.length * 4;
            allLockedMode = 20;
            int locked = 0;
            for (int cc = 0; cc < gVal.colNbr; cc++) {
                for (int rr = 0; rr < gVal.rowNbr; rr++) {
                    if (gVal.jigTables[cc][rr].locked)
                        locked++;
                    else
                        break;
                }
            }
            if (locked == gVal.colNbr * gVal.rowNbr) {
                congCount = congrats.length * 4;
                allLockedMode = 30;     // all puzzles are locked
            }
            foreBlink = true;
        }
    }

    private void showFloatingPieces(Canvas fCanvas) {
        Log.w("showFloatingPieces", "fps sz="+gVal.fps.size());
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
        int x = screenX / 7;
        int y = screenY / 3;
        Paint paint = new Paint();
        paint.setAlpha(200);
        int idx = congCount % ((allLockedMode == 30) ? congrats.length : jigDones.length);
        fCanvas.drawBitmap((allLockedMode == 30) ? congrats[idx] : jigDones[idx], x, y, paint);
    }
}
