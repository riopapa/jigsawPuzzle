package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.MainActivity.fullImage;
import static com.riopapa.jigsawpuzzle.MainActivity.maskMaps;
import static com.riopapa.jigsawpuzzle.MainActivity.outMaps;
import static com.riopapa.jigsawpuzzle.MainActivity.jigTables;
import static com.riopapa.jigsawpuzzle.MainActivity.picISize;
import static com.riopapa.jigsawpuzzle.MainActivity.picOSize;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;

import com.riopapa.jigsawpuzzle.model.JigTable;

public class Piece {
    int outerSize, pieceGap, innerSize, deltaGap;
    float out2Scale = 1.05f, bigScale = 1.1f;
    Paint paintIN, paintOUT;
    int outLineColor, out2LineColor;
    Matrix matrixOutLine, matrixBig;

    Context context;

    public Piece(Context context, int outerSize, int pieceGap, int innerSize) {
        this.context = context;
        this.outerSize = outerSize;
        this.pieceGap = pieceGap;
        this.innerSize = innerSize;
        this.deltaGap = (int) ((float) picISize * (out2Scale - 1f));

        matrixOutLine = new Matrix();
        matrixOutLine.setScale(out2Scale, out2Scale);

        matrixBig = new Matrix();
        matrixBig.setScale(bigScale, bigScale);
//        matrixOutLine.postScale(out2Scale, out2Scale, outerSize /2, outerSize /2);

        paintIN = new Paint(Paint.ANTI_ALIAS_FLAG);
        outLineColor = context.getColor(R.color.out_line);
        out2LineColor = context.getColor(R.color.out2_line);
        paintIN.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        paintOUT = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintOUT.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    }

    public void makeAll(int col, int row) {
        JigTable z = jigTables[col][row];
        Bitmap orgPiece = Bitmap.createBitmap(fullImage, col * innerSize, row * innerSize, outerSize, outerSize);
        Bitmap mask = maskMerge(maskMaps[0][z.lType], maskMaps[1][z.rType],
                maskMaps[2][z.uType], maskMaps[3][z.dType], innerSize, outerSize);
        z.src = cropSrc(orgPiece, mask);
        z.pic = Bitmap.createScaledBitmap(z.src, picOSize, picOSize, true);
        mask = maskMerge(outMaps[0][z.lType], outMaps[1][z.rType],
                outMaps[2][z.uType], outMaps[3][z.dType], innerSize, outerSize);
        z.oLine = makeOutline(z.src, mask);
        z.oLine2 = makeOut2Line(z.oLine);
        jigTables[col][row] = z;
    }

    public Bitmap maskSrcMap(Bitmap srcImage, Bitmap mask) {

        Bitmap maskMap = Bitmap.createBitmap(outerSize, outerSize, Bitmap.Config.ARGB_8888);
        Canvas tCanvas = new Canvas(maskMap);
        tCanvas.drawBitmap(srcImage, 0, 0, null);
        tCanvas.drawBitmap(mask, 0, 0, paintIN);
        return maskMap;
    }

    public Bitmap cropSrc(Bitmap srcImage, Bitmap maskOut) {

        Bitmap cropped = Bitmap.createBitmap(outerSize, outerSize, Bitmap.Config.ARGB_8888);
        Canvas tCanvas = new Canvas(cropped);
        tCanvas.drawBitmap(srcImage, 0, 0, null);
        tCanvas.drawBitmap(maskOut, 0, 0, paintIN);
        return cropped;
    }

    //   create picOSized outline bitmap from jig.src (outerSize)

    public Bitmap makeOutline(Bitmap srcMap, Bitmap outMask) {

        Bitmap outMap = Bitmap.createBitmap(outerSize, outerSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outMap);
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(outLineColor, PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(outMask, 0,0, paint);
        paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        Matrix matrix = new Matrix();
        canvas.drawBitmap(srcMap, matrix, paint);
        return Bitmap.createScaledBitmap(outMap, picOSize, picOSize, true);

    }

    public Bitmap makeOut2Line(Bitmap inMap) {
        Bitmap outMap = Bitmap.createBitmap(outerSize, outerSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outMap);
        canvas.drawBitmap(inMap, matrixOutLine, null);
        canvas.drawColor(out2LineColor, PorterDuff.Mode.SRC_ATOP); //Color.WHITE is stroke color
        canvas.drawBitmap(inMap, deltaGap, deltaGap, null);
        return Bitmap.createScaledBitmap(outMap, picOSize, picOSize, true);
    }

    // make outline a litter bigger
    public Bitmap makeBigger(Bitmap inMap) {
        Bitmap bigMap = Bitmap.createBitmap(picOSize, picOSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bigMap);
        canvas.drawBitmap(inMap, matrixBig, null);
        return bigMap;
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
//            for (x = outerSize-pieceGap; x < outerSize; x++) {
//                b.setPixel(x, pieceGap, 0);
//                b.setPixel(x, pieceGap+1, 0);
//                b.setPixel(x, outerSize-pieceGap, 0);
//                b.setPixel(x, outerSize-pieceGap-1, 0);
//            }
//        } else {
//            for (y = outerSize-pieceGap; y < outerSize; y++) {
//                b.setPixel(pieceGap, y, 0);
//                b.setPixel(pieceGap+1, y, 0);
//                b.setPixel(outerSize-pieceGap, y, 0);
//                b.setPixel(outerSize-pieceGap-1, y, 0);
//            }
//        }
//
//    }
