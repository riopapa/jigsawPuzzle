package biz.riopapa.jigsawpuzzle;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.allLockedMode;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.congCount;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageColor;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.dragX;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.dragY;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigOLine;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigPic;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigWhite;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.nowC;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.nowR;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.pieceImage;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.areaMap;
import static biz.riopapa.jigsawpuzzle.ActivityMain.ANI_ANCHOR;
import static biz.riopapa.jigsawpuzzle.ActivityMain.ANI_TO_FPS;
import static biz.riopapa.jigsawpuzzle.ActivityMain.congrats;
import static biz.riopapa.jigsawpuzzle.ActivityMain.fireWorks;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ActivityMain.mContext;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenBottom;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenX;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenY;
import static biz.riopapa.jigsawpuzzle.ActivityMain.showBack;
import static biz.riopapa.jigsawpuzzle.ActivityMain.showBackCount;
import static biz.riopapa.jigsawpuzzle.JigRecycleCallback.nowDragging;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.Random;

import biz.riopapa.jigsawpuzzle.databinding.ActivityJigsawBinding;
import biz.riopapa.jigsawpuzzle.model.FloatPiece;

public class PieceDraw {
    Paint pGrayed0, pGrayed1, lPaint, pathPaint;
    Random rnd;
    int gapSmall, gapTwo;
    final int LOW_ALPHA = 117;

    ActivityJigsawBinding binding;
    public PieceDraw(ActivityJigsawBinding binding) {
        this.binding = binding;
        pGrayed0 = new Paint();
        pGrayed1 = new Paint();
        pGrayed1.setAlpha(LOW_ALPHA);
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

        readyPieceImages();
        showPieceImage(canvas);
//        showUnlockedImages(canvas);
        showLockedPieces(canvas);
        showPiecePoint(canvas);
        showFloatingPieces(canvas);
        if (nowDragging) {
            canvas.drawBitmap(jigOLine[nowC][nowR],dragX, dragY, null);
//            Log.w("nowDragging", "piece "+dragX+"x"+dragY + " screenBottom="+screenBottom);
        }
        if (congCount > 0)
            showCongrats(canvas);

        canvas.restore();

//        String txt = "onD c" + nowC +" r"+ nowR + "\noffCR "+GVal.offsetC + " x " + GVal.offsetR+"\n calc " + calcC +" x "+ calcR+"\n GVal.fps "+GVal.fps.size();
//        mActivity.runOnUiThread(() -> tvRight.setText(txt));

    }


    private void readyPieceImages() {
        // draw locked pieces first with .pic
        for (int c = 0; c < gVal.showMaxX; c++) {
            for (int r = 0; r < gVal.showMaxY; r++) {
                final int cc = c + gVal.offsetC;
                final int rr = r + gVal.offsetR;
                if (jigPic[cc][rr] == null)
                    jigPic[cc][rr] = pieceImage.makePic(cc, rr);
                if (jigOLine[cc][rr] == null) {
                    jigOLine[cc][rr] = pieceImage.makeOline(jigPic[cc][rr], cc, rr);
                }
            }
        }
    }

    private void showPieceImage(Canvas canvas) {

        if (showBack == 0) {
            pGrayed0.setAlpha(255 * showBackCount / (250 * 10));
            canvas.drawBitmap(areaMap, gVal.baseX + gVal.picGap + gVal.picGap/2f,
                    gVal.baseY + gVal.picGap + gVal.picGap/2f, pGrayed0);
            showBackCount--;
            if (showBackCount < LOW_ALPHA * 10) {
                showBack = 1;
                binding.showBack.setImageResource(R.drawable.z_eye_half);
            }
        } else if (showBack == 1) {
            canvas.drawBitmap(areaMap, gVal.baseX + gVal.picGap + gVal.picGap/2f,
                    gVal.baseY + gVal.picGap + gVal.picGap/2f, pGrayed1);
        }

    }

    private void showUnlockedImages(Canvas canvas) {
        if (showBack == 0) {
            pGrayed0.setAlpha(255 * showBackCount / (250 * 10));
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
            showBackCount--;
            if (showBackCount < LOW_ALPHA * 10) {
                showBack = 1;
                binding.showBack.setImageResource(R.drawable.z_eye_half);
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
                    } else {
                        canvas.drawBitmap(jigPic[cc][rr],
                                gVal.baseX + c * gVal.picISize,
                                gVal.baseY + r * gVal.picISize,
                                null);

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
    }

    private void showLockedPieces(Canvas canvas) {
        int lockedCount = 0;
        // draw locked pieces first with .pic
        for (int c = 0; c < gVal.showMaxX; c++) {
            for (int r = 0; r < gVal.showMaxY; r++) {
                final int cc = c+ gVal.offsetC;
                final int rr = r+ gVal.offsetR;
                if (!gVal.jigTables[cc][rr].locked)
                    continue;
                lockedCount++;
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
        if (allLockedMode == 10  && lockedCount == gVal.showMaxX * gVal.showMaxY) {
            congCount = congrats.length * 4;
            allLockedMode = 20;
        }

    }

    private void showFloatingPieces(Canvas canvas) {
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
    }

    private void showPiecePoint(Canvas canvas) {
        if (showBack == 0 || showBack == 1) {
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

    private void showCongratsX(Canvas canvas) {
        congCount--;
        for (int i = 0; i < 6; i++) {
            int x = rnd.nextInt(screenX * 5 / 8);
            int y = screenY / 8 + rnd.nextInt(screenY * 5 / 8);
            int idx = rnd.nextInt(congrats.length-1);
            canvas.drawBitmap(congrats[idx], x, y, null);
        }
        if (congCount == 0)
            allLockedMode = 2;
    }

    private void showCongrats(Canvas canvas) {
        congCount--;

        int x = screenX / 8;
        int y = screenY / 3;
        for (int i = 0; i < congrats.length; i++) {
            int idx = congCount % congrats.length;
            canvas.drawBitmap(congrats[idx], x, y, null);
        }
        if (congCount == 0)
            allLockedMode = 2;
    }
}
