package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigOLine;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigPic;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageMap;
import static com.riopapa.jigsawpuzzle.ActivityMain.outMaskMaps;
import static com.riopapa.jigsawpuzzle.ActivityMain.srcMaskMaps;
import static com.riopapa.jigsawpuzzle.ActivityMain.vars;

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
    int outerSize, innerSize;
    float out2Scale = 1.05f;
    Paint paintIN, paintOUT, paintBright, paintOutATop, paintOutOver;

    int outLineColor, out2LineColor;
    Matrix matrixOutLine;

    Context context;

    public PieceImage(Context context, int outerSize, int innerSize) {
        this.context = context;
        this.outerSize = outerSize;
        this.innerSize = innerSize;
        matrixOutLine = new Matrix();
        matrixOutLine.setScale(out2Scale, out2Scale);

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

        final float contrast  = 1;
        final int brightness = 50;  // positive is bright
        ColorMatrix colorMatrix = new ColorMatrix(new float[]
                {
                        contrast, 0, 0, 0, brightness,  // Red
                        0, contrast, 0, 0, brightness,  // Green
                        0, 0, contrast, 0, brightness,  // Blue
                        0, 0, 0, 1, 0                   // Alpha
                });
        paintBright = new Paint();
        paintBright.setColorFilter(new ColorMatrixColorFilter(colorMatrix));

    }

    /**
     * if bitmap image in JigTable is null,
     *    get .src from original image with outerSize
     *    make scaled .pic from .src
     *    make .oLine from .pic
     *    output jigTables[col][row] .src, .pic, .oLine
     */

    public void buildOline(int col, int row) {
        JigTable z = vars.jigTables[col][row];
        Bitmap orgPiece = Bitmap.createBitmap(chosenImageMap, col * innerSize, row * innerSize, outerSize, outerSize);
        Bitmap mask = maskMerge(srcMaskMaps[0][z.lType], srcMaskMaps[1][z.rType],
                srcMaskMaps[2][z.uType], srcMaskMaps[3][z.dType]);
        Bitmap src = cropSrc(orgPiece, mask, col,row);   // col, row will be removed later
        jigPic[col][row] = Bitmap.createScaledBitmap(src, vars.picOSize, vars.picOSize, true);
        mask = maskMerge(outMaskMaps[0][z.lType], outMaskMaps[1][z.rType],
                outMaskMaps[2][z.uType], outMaskMaps[3][z.dType]);
        jigOLine[col][row] = makeOutline(src, mask);
        vars.jigTables[col][row] = z;
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
        p.setColor(Color.BLACK);
        p.setTextSize(outerSize*4/18);
        p.setStrokeWidth(outerSize/15);
        p.setTextAlign(Paint.Align.CENTER);
        p.setStyle(Paint.Style.STROKE);
        tCanvas.drawText(c+"."+r, outerSize/2, outerSize*3/6, p);
        p.setStrokeWidth(0);
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        tCanvas.drawText(c+"."+r, outerSize/2, outerSize*3/6, p);
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
        return Bitmap.createScaledBitmap(outMap, vars.picOSize, vars.picOSize, true);

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
        Bitmap bMap = Bitmap.createBitmap(vars.picOSize, vars.picOSize, Bitmap.Config.ARGB_8888);
        Canvas canvasBright = new Canvas(bMap);
        canvasBright.drawBitmap(inMap, 0, 0, paintBright);
        return bMap;
    }

}

