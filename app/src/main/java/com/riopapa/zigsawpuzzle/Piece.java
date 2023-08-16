package com.riopapa.zigsawpuzzle;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

public class Piece {
    int zw, x5, pw;

    public Piece(int zw, int x5, int pw) {
        this.zw = zw;
        this.x5 = x5;
        this.pw = pw;
    }

    public Bitmap makeUpper(Bitmap srcImage, Bitmap mask) {

        Bitmap result = Bitmap.createBitmap(zw, zw, Bitmap.Config.ARGB_8888);
        Canvas tCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        tCanvas.drawBitmap(srcImage, 0, 0, null);
        tCanvas.drawBitmap(mask, 0, 0, paint);
        paint.setXfermode(null);
        return result;
    }

    public Bitmap makeLeft(Bitmap srcImage, Bitmap mask) {

        Bitmap maskMap = Bitmap.createBitmap(zw, zw, Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(maskMap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(srcImage, 0, 0, null);
        mCanvas.drawBitmap(mask, 0, 0, paint);
        paint.setXfermode(null);
        return maskMap;
    }

    public Bitmap makeRight(Bitmap srcImage, Bitmap mask) {

        int maskColor = 0xff44ddff;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Bitmap maskMap = Bitmap.createBitmap(zw,zw, Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(maskMap);
        mCanvas.drawColor(maskColor);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mCanvas.drawBitmap(srcImage, pw, 0, paint);
        mCanvas.drawBitmap(mask, 0,0, paint);
        return maskMap;
    }


    public Bitmap makeDown(Bitmap srcImage, Bitmap mask) {

        int maskColor = 0xff44ddff;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Bitmap maskMap = Bitmap.createBitmap(zw,zw, Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(maskMap);
        mCanvas.drawColor(maskColor);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mCanvas.drawBitmap(srcImage, 0, pw, paint);
        mCanvas.drawBitmap(mask, 0,0, paint);
        return maskMap;
    }

    public Bitmap cropZig(Bitmap srcImage, Bitmap zigSaw) {
        int zWidth = srcImage.getHeight();

        Bitmap cropped = Bitmap.createBitmap(zWidth, zWidth, Bitmap.Config.ARGB_8888);
        Canvas tCanvas = new Canvas(cropped);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        tCanvas.drawBitmap(srcImage, 0, 0, null);
        tCanvas.drawBitmap(zigSaw, 0, 0, paint);
        paint.setXfermode(null);
        return cropped;
    }

    public Bitmap getOutline(Bitmap inMap, int color) {
        int outSize = pw/50+2;
        Bitmap workMap = Bitmap.createBitmap(zw, zw, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(workMap);
        float scale = (zw + 2.0f * outSize) / zw;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale, zw/2,zw/2);
        canvas.drawBitmap(inMap, matrix, null);
        canvas.drawColor(color, PorterDuff.Mode.SRC_ATOP);
        canvas.drawBitmap(inMap, outSize/2, outSize/2, null);
        return workMap;
    }

    public Bitmap makeBig(Bitmap inMap) {
        Bitmap bigMap = Bitmap.createBitmap(zw, zw, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bigMap);
        Matrix m = new Matrix();
        m.setScale(1.1f, 1.1f, zw/2, zw/2);
        canvas.drawBitmap(inMap, m, null);
        return bigMap;
    }

}

//    public void makeTransparent(Bitmap b, boolean right) {
//        xyStack xStack = new xyStack(195200);
//        xyStack yStack = new xyStack(195200);
//        int x = (right) ? zw-5: zw/2;
//        int y = (right) ? zw/2: zw-5;
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
//            for (x = zw-x5; x < zw; x++) {
//                b.setPixel(x, x5, 0);
//                b.setPixel(x, x5+1, 0);
//                b.setPixel(x, zw-x5, 0);
//                b.setPixel(x, zw-x5-1, 0);
//            }
//        } else {
//            for (y = zw-x5; y < zw; y++) {
//                b.setPixel(x5, y, 0);
//                b.setPixel(x5+1, y, 0);
//                b.setPixel(zw-x5, y, 0);
//                b.setPixel(zw-x5-1, y, 0);
//            }
//        }
//
//    }
