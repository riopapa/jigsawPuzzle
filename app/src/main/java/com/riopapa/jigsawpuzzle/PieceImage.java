package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageMap;
import static com.riopapa.jigsawpuzzle.ActivityMain.debugMode;
import static com.riopapa.jigsawpuzzle.ActivityMain.outMaskMaps;
import static com.riopapa.jigsawpuzzle.ActivityMain.srcMaskMaps;
import static com.riopapa.jigsawpuzzle.ActivityMain.gVal;

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

import com.riopapa.jigsawpuzzle.model.JigTable;

public class PieceImage {
    int imgOutSize, imgInSize;
//    float out2Scale = 1.05f;
    Paint paintIN, paintOUT, paintBright, paintWhite, paintOutATop, paintOutLine;

    int outLineColor, out2LineColor;
    Matrix matrixOutLine;

    Context context;

    public PieceImage(Context context, int imgOutSize, int imgInSize) {
        this.context = context;
        this.imgOutSize = imgOutSize;
        this.imgInSize = imgInSize;

        paintIN = new Paint(Paint.ANTI_ALIAS_FLAG);
        outLineColor = context.getColor(R.color.out_line);
        out2LineColor = context.getColor(R.color.out2_line);
        paintIN.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        paintOUT = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintOUT.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        paintOutATop = new Paint();
        paintOutATop.setColorFilter(new PorterDuffColorFilter(outLineColor, PorterDuff.Mode.SRC_ATOP));

        paintOutLine = new Paint();
        paintOutLine.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

        final float brightContrast  = 1;
        final int BrightBrightness = 50;  // positive is bright
        ColorMatrix brightMatrix = new ColorMatrix(new float[]
                {
                        brightContrast, 0, 0, 0, BrightBrightness,  // Red
                        0, brightContrast, 0, 0, BrightBrightness,  // Green
                        0, 0, brightContrast, 0, BrightBrightness,  // Blue
                        0, 0, 0, 1, 0                   // Alpha
                });
        paintBright = new Paint();
        paintBright.setColorFilter(new ColorMatrixColorFilter(brightMatrix));

        final float whiteContrast  = 2f;
        final int whiteBrightness = 200;  // positive is bright
        ColorMatrix whiteMatrix = new ColorMatrix(new float[]
                {
                        whiteContrast, 0, 0, 0, whiteBrightness,  // Red
                        0, whiteContrast, 0, 0, whiteBrightness,  // Green
                        0, 0, whiteContrast, 0, whiteBrightness,  // Blue
                        0, 0, 0, 1, 0                   // Alpha
                });
        paintWhite = new Paint();
        paintWhite.setColorFilter(new ColorMatrixColorFilter(whiteMatrix));

    }

    /**
     * if bitmap image in JigTable is null,
     *    get .src from original image with outerSize
     *    make scaled .pic from .src
     *    make .oLine from .pic
     *    output jigTables[col][row] .src, .pic, .oLine
     */
    public Bitmap buildPic(int col, int row) {
        JigTable z = gVal.jigTables[col][row];
        Bitmap orgPiece = Bitmap.createBitmap(chosenImageMap, col * imgInSize, row * imgInSize, imgOutSize, imgOutSize);
        Bitmap mask = maskMerge(srcMaskMaps[0][z.lType], srcMaskMaps[1][z.rType],
                srcMaskMaps[2][z.uType], srcMaskMaps[3][z.dType]);
        Bitmap src = Bitmap.createBitmap(imgOutSize, imgOutSize, Bitmap.Config.ARGB_8888);
        Canvas tCanvas = new Canvas(src);
        tCanvas.drawBitmap(orgPiece, 0, 0, null);
        tCanvas.drawBitmap(mask, 0, 0, paintIN);
        if (debugMode) {
            Paint p = new Paint();
            p.setColor(Color.BLACK);
            p.setTextSize(imgOutSize * 4f / 18f);
            p.setStrokeWidth(imgOutSize / 15f);
            p.setTextAlign(Paint.Align.CENTER);
            p.setStyle(Paint.Style.STROKE);
            tCanvas.drawText(col + "." + row, imgOutSize / 2f, imgOutSize * 3f / 6f, p);
            p.setStrokeWidth(0);
            p.setColor(Color.WHITE);
            p.setStyle(Paint.Style.FILL_AND_STROKE);
            tCanvas.drawText(col + "." + row, imgOutSize / 2f, imgOutSize * 3f / 6f, p);
        }
        return Bitmap.createScaledBitmap(src, gVal.picOSize, gVal.picOSize, true);
    }

    public Bitmap buildOline(Bitmap pic, int col, int row) {
        JigTable z = gVal.jigTables[col][row];
        Bitmap mask = maskMerge(outMaskMaps[0][z.lType], outMaskMaps[1][z.rType],
                outMaskMaps[2][z.uType], outMaskMaps[3][z.dType]);
        Bitmap maskScaled = Bitmap.createScaledBitmap(mask, gVal.picOSize, gVal.picOSize, true);
        Bitmap outMap = Bitmap.createBitmap(gVal.picOSize, gVal.picOSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outMap);
        canvas.drawBitmap(maskScaled, 0,0, paintOutATop);
        Matrix matrix = new Matrix();
        canvas.drawBitmap(pic, matrix, paintOutLine);
        return outMap;
    }


    public Bitmap maskSrcMap(Bitmap srcImage, Bitmap mask) {

        Bitmap maskMap = Bitmap.createBitmap(imgOutSize, imgOutSize, Bitmap.Config.ARGB_8888);
        Canvas tCanvas = new Canvas(maskMap);
        tCanvas.drawBitmap(srcImage, 0, 0, null);
        tCanvas.drawBitmap(mask, 0, 0, paintIN);
        return maskMap;
    }

    /**
     * create masked Map using L,R,U,D masks
     * @param maskL, maskR, maskU, maskD predefined with outerSize
     * @return merged bitmap with outerSize
     */

    public Bitmap maskMerge(Bitmap maskL, Bitmap maskR, Bitmap maskU, Bitmap maskD) {
        Bitmap tMap = Bitmap.createBitmap(imgOutSize, imgOutSize, Bitmap.Config.ARGB_8888);
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
     * @return merged bitmap with outerSize
     */

    public Bitmap makeBright(Bitmap inMap) {
        Bitmap bMap = Bitmap.createBitmap(gVal.picOSize, gVal.picOSize, Bitmap.Config.ARGB_8888);
        Canvas canvasBright = new Canvas(bMap);
        canvasBright.drawBitmap(inMap, 0, 0, paintBright);
        return bMap;
    }

    public Bitmap makeWhite(Bitmap inMap) {
        Bitmap bMap = Bitmap.createBitmap(gVal.picOSize, gVal.picOSize, Bitmap.Config.ARGB_8888);
        Canvas canvasBright = new Canvas(bMap);
        canvasBright.drawBitmap(inMap, 0, 0, paintWhite);
        return bMap;
    }

}

