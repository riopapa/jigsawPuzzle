package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.MainActivity.fullImage;
import static com.riopapa.jigsawpuzzle.MainActivity.maskMaps;
import static com.riopapa.jigsawpuzzle.MainActivity.outMaps;
import static com.riopapa.jigsawpuzzle.MainActivity.jigTables;
import static com.riopapa.jigsawpuzzle.MainActivity.picGap;
import static com.riopapa.jigsawpuzzle.MainActivity.picISize;
import static com.riopapa.jigsawpuzzle.MainActivity.picOSize;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.util.Log;

import com.riopapa.jigsawpuzzle.model.JigTable;

public class Piece {
    int outerSize, pieceGap, innerSize;

    float out2Scale = 1.05f, bigScale = 1.1f, deltaGap;
    Paint paintIN, paintOUT, paintBright, paintOutATop, paintOutOver;

    int outLineColor, out2LineColor;
    Matrix matrixOutLine, matrixBig;

    Context context;

    public Piece(Context context, int outerSize, int pieceGap, int innerSize) {
        this.context = context;
        this.outerSize = outerSize;
        this.pieceGap = pieceGap;
        this.innerSize = innerSize;
        this.deltaGap = picOSize * (out2Scale - 1f) / 2f;
        Log.w("c1 Piece","deltaGap ="+deltaGap);
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

        paintOutATop = new Paint();
        paintOutATop.setColorFilter(new PorterDuffColorFilter(outLineColor, PorterDuff.Mode.SRC_ATOP));

        paintOutOver = new Paint();
        paintOutOver.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

        final int contrast  = 2;
        final int brightness = 60;
        ColorMatrix colorMatrix = new ColorMatrix(new float[]
                {
                        contrast, 0, 0, 0, brightness,
                        0, contrast, 0, 0, brightness,
                        0, 0, contrast, 0, brightness,
                        0, 0, 0, 1, 0
                });
        paintBright = new Paint();
        paintBright.setColorFilter(new ColorMatrixColorFilter(colorMatrix));

    }

    public void makeAll(int col, int row) {
        JigTable z = jigTables[col][row];
        Bitmap orgPiece = Bitmap.createBitmap(fullImage, col * innerSize, row * innerSize, outerSize, outerSize);
        Bitmap mask = maskMerge(maskMaps[0][z.lType], maskMaps[1][z.rType],
                maskMaps[2][z.uType], maskMaps[3][z.dType]);
        z.src = cropSrc(orgPiece, mask, col,row);   // col, row will be removed later
        z.pic = Bitmap.createScaledBitmap(z.src, picOSize, picOSize, true);
        mask = maskMerge(outMaps[0][z.lType], outMaps[1][z.rType],
                outMaps[2][z.uType], outMaps[3][z.dType]);
        z.oLine = makeOutline(z.src, mask);
        jigTables[col][row] = z;
    }

    public void makeOline2(int col, int row) {
        jigTables[col][row].oLine2 = makeOut2Line((jigTables[col][row].oLine));
    }


    public Bitmap maskSrcMap(Bitmap srcImage, Bitmap mask) {

        Bitmap maskMap = Bitmap.createBitmap(outerSize, outerSize, Bitmap.Config.ARGB_8888);
        Canvas tCanvas = new Canvas(maskMap);
        tCanvas.drawBitmap(srcImage, 0, 0, null);
        tCanvas.drawBitmap(mask, 0, 0, paintIN);
        return maskMap;
    }

    public Bitmap cropSrc(Bitmap srcImage, Bitmap maskOut, int c, int r) {

        Bitmap cropped = Bitmap.createBitmap(outerSize, outerSize, Bitmap.Config.ARGB_8888);
        Canvas tCanvas = new Canvas(cropped);
        tCanvas.drawBitmap(srcImage, 0, 0, null);
        tCanvas.drawBitmap(maskOut, 0, 0, paintIN);
        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setTextSize(pieceGap);
        
        tCanvas.drawText(c+"x"+r, outerSize/3, outerSize/2, p);
        return cropped;
    }

    /**
     * create picOSized outline oLine from jig.src (outerSize)
     * @param srcMap input oLine with outerSize
     * @param outMask outline Mask
     * @return outlined bitmawp with picOSize
     */
    public Bitmap makeOutline(Bitmap srcMap, Bitmap outMask) {

        Bitmap outMap = Bitmap.createBitmap(outerSize, outerSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outMap);
        canvas.drawBitmap(outMask, 0,0, paintOutATop);
        Matrix matrix = new Matrix();
        canvas.drawBitmap(srcMap, matrix, paintOutOver);
        return Bitmap.createScaledBitmap(outMap, picOSize, picOSize, true);

    }

    /**
     * create picOSized double outlined oLine from jig.oline
     * @param inMap input outlined with outerSize
     * @return double outlined bitmawp with picOSize
     */
    public Bitmap makeOut2Line(Bitmap inMap) {
        Bitmap outMap = Bitmap.createBitmap(picOSize, picOSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outMap);
        canvas.drawBitmap(inMap, matrixOutLine, null);
        canvas.drawColor(out2LineColor, PorterDuff.Mode.SRC_ATOP); //Color.WHITE is stroke color
        canvas.drawBitmap(inMap, deltaGap, deltaGap, null);
        return outMap;
//        return Bitmap.createScaledBitmap(outMap, picOSize, picOSize, true);
    }

    /**
     * create picOSized outline oLine from jig.oline
     * @param inMap input outlined with outerSize
     * @return bigger oLine with picOSize
     */
    public Bitmap makeBigger(Bitmap inMap) {
        Bitmap bigMap = Bitmap.createBitmap(picOSize, picOSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bigMap);
        canvas.drawBitmap(inMap, matrixBig, null);
        return bigMap;
    }

    /**
     * create masked Map using L,R,U,D masks
     * @param maskL, maskR, maskU, maskD predefined with outerSize
     * @return merged bitmawp with outerSize
     */

    public Bitmap maskMerge(Bitmap maskL, Bitmap maskR, Bitmap maskU, Bitmap maskD) {
        Bitmap tMap = Bitmap.createBitmap(outerSize, outerSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(tMap);
        canvas.drawBitmap(maskL, 0,0, null);
        canvas.drawBitmap(maskR, 0,0, null);
        canvas.drawBitmap(maskU, 0,0, null);
        canvas.drawBitmap(maskD, 0,0, null);
        return tMap;
    }

    /**
     * create white Map for brighter map using ColorMatrix
     * @param inMap positioned picMap
     * @return merged bitmawp with outerSize
     */

    public Bitmap makeBright(Bitmap inMap) {
        Bitmap bMap = Bitmap.createBitmap(picOSize, picOSize, Bitmap.Config.ARGB_8888);
        Canvas canvasBright = new Canvas(bMap);
        canvasBright.drawBitmap(inMap, 0, 0, paintBright);
        return bMap;
    }

}

