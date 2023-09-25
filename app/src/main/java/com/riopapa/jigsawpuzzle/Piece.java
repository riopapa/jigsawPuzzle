package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.MainActivity.fullImage;
import static com.riopapa.jigsawpuzzle.MainActivity.maskMaps;
import static com.riopapa.jigsawpuzzle.MainActivity.outMaps;
import static com.riopapa.jigsawpuzzle.MainActivity.piece;
import static com.riopapa.jigsawpuzzle.MainActivity.jigTables;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;

import com.riopapa.jigsawpuzzle.model.JigTable;

public class Piece {
    int outerSize, x5, innerSize;
    Paint paintIN, paintOUT;
    int outLineColor;

    public Piece(Context context, int outerSize, int x5, int innerSize) {
        this.outerSize = outerSize;
        this.x5 = x5;
        this.innerSize = innerSize;
        paintIN = new Paint(Paint.ANTI_ALIAS_FLAG);
        outLineColor = context.getColor(R.color.out_line);
        paintIN.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        paintOUT = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintOUT.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    }

    public Bitmap maskOut(Bitmap srcImage, Bitmap mask) {

        Bitmap maskMap = Bitmap.createBitmap(outerSize, outerSize, Bitmap.Config.ARGB_8888);
        Canvas tCanvas = new Canvas(maskMap);
        tCanvas.drawBitmap(srcImage, 0, 0, null);
        tCanvas.drawBitmap(mask, 0, 0, paintIN);
        return maskMap;
    }

    public Bitmap cropZig(Bitmap srcImage, Bitmap maskOut) {

        Bitmap cropped = Bitmap.createBitmap(outerSize, outerSize, Bitmap.Config.ARGB_8888);
        Canvas tCanvas = new Canvas(cropped);
        tCanvas.drawBitmap(srcImage, 0, 0, null);
        tCanvas.drawBitmap(maskOut, 0, 0, paintIN);
        return cropped;
    }

    public Bitmap getOutline(Bitmap inMap, int color) {
        int delta = innerSize /50+2;
        Bitmap workMap = Bitmap.createBitmap(outerSize, outerSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(workMap);
        float scale = (outerSize + 2.0f * delta) / outerSize;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale, outerSize /2, outerSize /2);
        canvas.drawBitmap(inMap, matrix, null);
        canvas.drawColor(color, PorterDuff.Mode.SRC_ATOP);
        canvas.drawBitmap(inMap, delta/2, delta/2, null);
        return workMap;
    }
    public Bitmap makeOutline(Bitmap srcMap, Bitmap outMask) {

        Bitmap workMap = Bitmap.createBitmap(outerSize, outerSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(workMap);
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(outLineColor, PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(outMask, 0,0, paint);
        paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        Matrix matrix = new Matrix();
        canvas.drawBitmap(srcMap, matrix, paint);
        return workMap;
    }

    public Bitmap makeBig(Bitmap inMap) {
        Bitmap bigMap = Bitmap.createBitmap(outerSize, outerSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bigMap);
        Matrix m = new Matrix();
        m.setScale(1.1f, 1.1f, outerSize /2, outerSize /2);
        canvas.drawBitmap(inMap, m, null);
        return bigMap;
    }

    public void make(int x, int y) {
        JigTable z = jigTables[x][y];
        Bitmap srcMap = Bitmap.createBitmap(fullImage, x * innerSize, y * innerSize, outerSize, outerSize);
        Bitmap mask = maskMerge(maskMaps[0][z.lType], maskMaps[1][z.rType],
                maskMaps[2][z.uType], maskMaps[3][z.dType], innerSize, outerSize);
        z.src = piece.cropZig(srcMap, mask);
//        z.oLine = piece.getOutline(z.src, 0xFF8899AA);
        mask = maskMerge(outMaps[0][z.lType], outMaps[1][z.rType],
                outMaps[2][z.uType], outMaps[3][z.dType], innerSize, outerSize);
        z.oLine = piece.makeOutline(z.src, mask);
        z.oLine2 = piece.getOutline(z.src,0xFF667788);
        jigTables[x][y] = z;
    }
    public Bitmap maskMerge(Bitmap maskL, Bitmap maskR, Bitmap maskU, Bitmap maskD, int pw, int zw) {
        Bitmap tMap = Bitmap.createBitmap(zw, zw, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(tMap);
        canvas.drawBitmap(maskL, 0,0, null);
        canvas.drawBitmap(maskR, 0,0, null);
        canvas.drawBitmap(maskU, 0,0, null);
        canvas.drawBitmap(maskD, 0,0, null);
        return tMap;
    }

}

//    public void makeTransparent(Bitmap b, boolean right) {
//        xyStack xStack = new xyStack(195200);
//        xyStack yStack = new xyStack(195200);
//        int x = (right) ? outerSize-5: outerSize/2;
//        int y = (right) ? outerSize/2: outerSize-5;
//        int max = b.getWidth() - 1;
//        int nowColor = b.getPixel(x,y);
//        while (true) {
//            b.setPixel(x, y, 0);
//            if (x > 0 && b.getPixel(x - 1, y) == nowColor) {
//                xStack.push(x - 1);
//                yStack.push(y);
//            }
//            if (x < max && b.getPixel(x + 1, y) == nowColor) {
//                xStack.push(x + 1);
//                yStack.push(y);
//            }
//            if (y > 0 && b.getPixel(x, y - 1) == nowColor) {
//                xStack.push(x);
//                yStack.push(y - 1);
//            }
//            if (y < max && b.getPixel(x, y + 1) == nowColor) {
//                xStack.push(x);
//                yStack.push(y + 1);
//            }
//            x = xStack.pop();
//            y = yStack.pop();
//            if (x == 0)
//                break;
//        }
//        if (right) {
//            for (x = outerSize-x5; x < outerSize; x++) {
//                b.setPixel(x, x5, 0);
//                b.setPixel(x, x5+1, 0);
//                b.setPixel(x, outerSize-x5, 0);
//                b.setPixel(x, outerSize-x5-1, 0);
//            }
//        } else {
//            for (y = outerSize-x5; y < outerSize; y++) {
//                b.setPixel(x5, y, 0);
//                b.setPixel(x5+1, y, 0);
//                b.setPixel(outerSize-x5, y, 0);
//                b.setPixel(outerSize-x5-1, y, 0);
//            }
//        }
//
//    }
