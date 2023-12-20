package biz.riopapa.jigsawpuzzle;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.allLockedMode;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageColor;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.congCount;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.dragX;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.dragY;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigOLine;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigPic;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigWhite;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.nowC;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.nowR;
import static biz.riopapa.jigsawpuzzle.ActivityMain.ANI_ANCHOR;
import static biz.riopapa.jigsawpuzzle.ActivityMain.ANI_TO_FPS;
import static biz.riopapa.jigsawpuzzle.ActivityMain.GAME_COMPLETED;
import static biz.riopapa.jigsawpuzzle.ActivityMain.congrats;
import static biz.riopapa.jigsawpuzzle.ActivityMain.fireWorks;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gameMode;
import static biz.riopapa.jigsawpuzzle.ActivityMain.jigDones;
import static biz.riopapa.jigsawpuzzle.ActivityMain.mContext;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenX;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenY;
import static biz.riopapa.jigsawpuzzle.ForeView.foreBlink;
import static biz.riopapa.jigsawpuzzle.JigRecycleCallback.nowDragging;

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
    int gapSmall, gapTwo;
    final int LOW_ALPHA = 117;

    ActivityJigsawBinding binding;
    PieceImage pieceImage;
    Paint backPaint;
    public ForeDraw(ActivityJigsawBinding binding) {
        this.binding = binding;
        pGrayed0 = new Paint();
        pGrayed1 = new Paint();
        pGrayed1.setAlpha(LOW_ALPHA);
        lPaint = new Paint();
        lPaint.setColor(Color.RED);

        pathPaint = new Paint();
        pathPaint.setColor(chosenImageColor);
        backPaint = new Paint();
        backPaint.setAlpha(100);

        gapTwo = gVal.picGap + gVal.picGap;
        rnd = new Random(System.currentTimeMillis() & 0xFFFFF);
        pieceImage = new PieceImage(mContext, gVal.imgOutSize, gVal.imgInSize);
    }

    public void draw(Canvas fCanvas){
        fCanvas.save();
        showJustLocked(fCanvas);
        showFloatingPieces(fCanvas);
        if (nowDragging) {
            fCanvas.drawBitmap(jigOLine[nowC][nowR],dragX, dragY, null);
//            Log.w("nowDragging", "piece "+dragX+"x"+dragY + " screenBottom="+screenBottom);
        }
        if (congCount > 0)
            showCongrats(fCanvas);

        fCanvas.restore();

//        String txt = "onD c" + nowC +" r"+ nowR + "\noffCR "+GVal.offsetC + " x " + GVal.offsetR+"\n calc " + calcC +" x "+ calcR+"\n GVal.fps "+GVal.fps.size();
//        mActivity.runOnUiThread(() -> tvRight.setText(txt));

    }

    private void showJustLocked(Canvas canvas) {
        int lockedCount = 0;
        // draw locked pieces first with .pic
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

                gVal.jigTables[cc][rr].count -= 1 + rnd.nextInt(4);
                if (gVal.jigTables[cc][rr].count < 0) {
                    gVal.jigTables[cc][rr].count = 0;
                }
                if (gVal.jigTables[cc][rr].count == 0) {
                    jigOLine[cc][rr] = pieceImage.makeOline(jigPic[cc][rr], cc, rr);
                    jigWhite[cc][rr] = null;
                }
                Bitmap bMap ;
                if (gVal.jigTables[cc][rr].count % 3 == 1)
                    bMap = fireWorks[gVal.jigTables[cc][rr].count];
                else if ((gVal.jigTables[cc][rr].count % 2 == 0))
                    bMap = jigOLine[cc][rr];
                else
                    bMap = jigWhite[cc][rr];
                canvas.drawBitmap(bMap,
                            gVal.baseX + c * gVal.picISize - gVal.picGap,
                            gVal.baseY + r * gVal.picISize - gVal.picGap, null);
                foreBlink = true;
            }
        }
        if (allLockedMode == 10  && lockedCount == gVal.showMaxX * gVal.showMaxY) {
            congCount = jigDones.length * 5;
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
        for (int cnt = 0; cnt < gVal.fps.size(); cnt++) {
            FloatPiece fp = gVal.fps.get(cnt);
            int c = fp.C;
            int r = fp.R;
            if (jigOLine[c][r] == null)
                jigOLine[c][r] = pieceImage.makePic(c,r);

            if (fp.count == 0) { // normal pieceImage
                fCanvas.drawBitmap(jigOLine[c][r], fp.posX, fp.posY, null);
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
                fCanvas.drawBitmap(rBitMap, fp.posX, fp.posY, null);
                if (fp.count == 0) {
                    fp.mode = 0;
                }
                gVal.fps.set(cnt, fp);
                foreBlink = true;
                continue;
            }
            // animate recycler to paint
            if (fp.count > 0 && fp.mode == ANI_TO_FPS) {  // animate from recycle to foreView
                fp.count--;
                Matrix matrix = new Matrix();
                matrix.postRotate(3 * (fp.count - 4));
                Bitmap rBitMap = Bitmap.createBitmap(jigOLine[c][r], 0, 0,
                        gVal.picOSize, gVal.picOSize, matrix, true);
                fCanvas.drawBitmap(rBitMap, fp.posX, fp.posY, null);

                if (fp.count == 0) {
                    fp.mode = 0;
                }
                foreBlink = true;
                gVal.fps.set(cnt, fp);
            }
        }
    }

    private void showCongrats(Canvas fCanvas) {
        if (congCount > 0)
            foreBlink = true;
        congCount--;
        int x = screenX / 7;
        int y = screenY / 3;
        Paint paint = new Paint();
        paint.setAlpha(140);
        for (int i = 0; i < congrats.length; i++) {
            int idx = congCount % ((allLockedMode == 30) ? congrats.length : jigDones.length);
            fCanvas.drawBitmap((allLockedMode == 30) ? congrats[idx] : jigDones[idx], x, y, paint);
        }
        if (congCount == 0) {
            if (allLockedMode == 30) {
                gameMode = GAME_COMPLETED;
                allLockedMode = 99;
            } else
                allLockedMode = 2;
        }
    }
}
