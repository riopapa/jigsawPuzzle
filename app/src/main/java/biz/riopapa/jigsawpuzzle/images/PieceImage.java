package biz.riopapa.jigsawpuzzle.images;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.colorLocked;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.colorOutline;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.currImageMap;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.outMaskMaps;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.srcMaskMaps;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ActivityMain.showCR;

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

import biz.riopapa.jigsawpuzzle.model.JigTable;

public class PieceImage {
    int orgSizeOut, orgSizeIn;
//    float out2Scale = 1.05f;
    Paint pIN, pOUT, pBright, pWhite, pOutATop, pLockedATop, pOutLine, pShadow, pShadowTop;
    int shadowSize, outLineSz, shadowSz;
    Bitmap mask, maskL, maskR, maskU, maskD;

    Context context;

    public PieceImage(Context context, int imgOutSize, int imgInSize) {
        this.context = context;
        this.orgSizeOut = imgOutSize;
        this.orgSizeIn = imgInSize;
        outLineSz = gVal.picOSize / 60;

        pIN = new Paint(); // Paint.ANTI_ALIAS_FLAG

        pIN.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        pOUT = new Paint(Paint.ANTI_ALIAS_FLAG);
        pOUT.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        pOutATop = new Paint();
        pOutATop.setColorFilter(new PorterDuffColorFilter(colorOutline, PorterDuff.Mode.SRC_ATOP));

        pLockedATop = new Paint();
        pLockedATop.setColorFilter(new PorterDuffColorFilter(colorLocked, PorterDuff.Mode.SRC_ATOP));

        pOutLine = new Paint();
        pOutLine.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

        final float brightContrast  = 1;
        final int BrightBrightness = 50;  // positive is bright
        ColorMatrix brightMatrix = new ColorMatrix(new float[]
                {
                        brightContrast, 0, 0, 0, BrightBrightness,  // Red
                        0, brightContrast, 0, 0, BrightBrightness,  // Green
                        0, 0, brightContrast, 0, BrightBrightness,  // Blue
                        0, 0, 0, 1, 0                   // Alpha
                });
        pBright = new Paint();
        pBright.setColorFilter(new ColorMatrixColorFilter(brightMatrix));

        final float whiteContrast  = 2f;
        final int whiteBrightness = 200;  // positive is bright
        ColorMatrix whiteMatrix = new ColorMatrix(new float[]
                {
                        whiteContrast, 0, 0, 0, whiteBrightness,  // Red
                        0, whiteContrast, 0, 0, whiteBrightness,  // Green
                        0, 0, whiteContrast, 0, whiteBrightness,  // Blue
                        0, 0, 0, 1, 0                   // Alpha
                });
        pWhite = new Paint();
        pWhite.setColorFilter(new ColorMatrixColorFilter(whiteMatrix));

        pShadow = new Paint();
        pShadow.setColor(0xFF333333);
        shadowSize = gVal.picOSize / 90;
        pShadowTop = new Paint();
        pShadowTop.setColorFilter(new PorterDuffColorFilter(0xFF222244, PorterDuff.Mode.SRC_ATOP));

    }

    /**
     * if bitmap image in JigTable is null,
     *    get .src from original image with outerSize
     *    make scaled .pic from .src
     *    make .oLine from .pic
     *    output jigTables[col][row] .src, .pic, .oLine
     */
    public Bitmap makePic(int col, int row) {
        JigTable jig = gVal.jigTables[col][row];
        Bitmap orgPiece = Bitmap.createBitmap(currImageMap,
                col * orgSizeIn, row * orgSizeIn, orgSizeOut, orgSizeOut, null, false);
        Bitmap mask = maskMerge(srcMaskMaps[0][jig.lType], srcMaskMaps[1][jig.rType],
                srcMaskMaps[2][jig.uType], srcMaskMaps[3][jig.dType]);
        Bitmap src = Bitmap.createBitmap(orgSizeOut, orgSizeOut, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(src);
        canvas.drawBitmap(orgPiece, 0, 0, null);
        canvas.drawBitmap(mask, 0, 0, pIN);
        if (showCR) {
            Paint p = new Paint();
            p.setColor(Color.BLACK);
            p.setTextSize(orgSizeOut * 4f / 18f);
            p.setStrokeWidth(orgSizeOut / 15f);
            p.setTextAlign(Paint.Align.CENTER);
            p.setStyle(Paint.Style.STROKE);
            canvas.drawText(col + "." + row, orgSizeOut / 2f, orgSizeOut * 3f / 6f, p);
            p.setStrokeWidth(0);
            p.setColor(Color.WHITE);
            p.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawText(col + "." + row, orgSizeOut / 2f, orgSizeOut * 3f / 6f, p);
        }
        return Bitmap.createScaledBitmap(src, gVal.picOSize, gVal.picOSize, false);
    }

    public Bitmap makeOline(Bitmap pic, int col, int row) {
        JigTable jt = gVal.jigTables[col][row];
        if (jt.locked) {
            shadowSz = 0;
            if (col == 0 || !gVal.jigTables[col-1][row].locked)
                maskL = outMaskMaps[0][jt.lType];
            else
                maskL = srcMaskMaps[0][jt.lType];
            if (col == gVal.colNbr-1 || !gVal.jigTables[col+1][row].locked)
                maskR = outMaskMaps[1][jt.rType];
            else
                maskR = srcMaskMaps[1][jt.rType];
            if (row == 0 || !gVal.jigTables[col][row-1].locked)
                maskU = outMaskMaps[2][jt.uType];
            else
                maskU = srcMaskMaps[2][jt.uType];
            if (row == gVal.rowNbr-1 || !gVal.jigTables[col][row+1].locked)
                maskD = outMaskMaps[3][jt.dType];
            else
                maskD = srcMaskMaps[3][jt.dType];
            mask = maskMerge(maskL, maskR, maskU, maskD);
        } else {
            shadowSz = outLineSz;
            mask = maskMerge(
                    outMaskMaps[0][jt.lType], outMaskMaps[1][jt.rType],
                    outMaskMaps[2][jt.uType], outMaskMaps[3][jt.dType]);
        }
        Bitmap maskScaled = Bitmap.createScaledBitmap(mask,
                gVal.picOSize - shadowSz, gVal.picOSize - shadowSz, true);
        Bitmap picScaled = Bitmap.createScaledBitmap(pic,
                gVal.picOSize - shadowSz, gVal.picOSize - shadowSz, true);
        Bitmap outMap = Bitmap.createBitmap(gVal.picOSize, gVal.picOSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outMap);
        canvas.drawBitmap(maskScaled, shadowSize+ shadowSz, shadowSize+ shadowSz, pShadowTop);
        canvas.drawBitmap(maskScaled, shadowSz /2f, shadowSz /2f, (jt.locked) ? pLockedATop : pOutATop);
        Matrix matrix = new Matrix();
        canvas.drawBitmap(picScaled, matrix, pOutLine);
        return outMap;
    }

    public Bitmap makeGray(Bitmap pic, int wSz, int hSz) {
        Bitmap outMap = Bitmap.createBitmap(wSz, hSz, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outMap);
        Paint p = new Paint();
        for (int c = 0; c < wSz; c++) {
            for (int r = 0; r < hSz; r++) {
                int pxl = pic.getPixel(c, r);
                if (pxl != 0) {
                    int avr = (Color.red(pxl) + Color.green(pxl) + Color.blue(pxl)) / 3;
                    if (avr > 30) {
                        int color = 0xFF000000 | avr << 16 | avr << 8 | avr;
                        p.setColor(color);
                        canvas.drawPoint(c, r, p);
                    }
                }
            }
        }
        return outMap;
    }

    public Bitmap maskSrcMap(Bitmap srcImage, Bitmap mask) {

        Bitmap maskMap = Bitmap.createBitmap(orgSizeOut, orgSizeOut, Bitmap.Config.ARGB_8888);
        Canvas tCanvas = new Canvas(maskMap);
        tCanvas.drawBitmap(srcImage, 0, 0, null);
        tCanvas.drawBitmap(mask, 0, 0, pIN);
        return maskMap;
    }

    /**
     * create masked Map using L,R,U,D masks
     * @param maskL, maskR, maskU, maskD predefined with outerSize
     * @return merged bitmap with outerSize
     */

    public Bitmap maskMerge(Bitmap maskL, Bitmap maskR, Bitmap maskU, Bitmap maskD) {
        Bitmap tMap = Bitmap.createBitmap(orgSizeOut, orgSizeOut, Bitmap.Config.ARGB_8888);
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
        canvasBright.drawBitmap(inMap, 0, 0, pBright);
        return bMap;
    }

    public Bitmap makeWhite(Bitmap inMap) {
        Bitmap bMap = Bitmap.createBitmap(gVal.picOSize, gVal.picOSize, Bitmap.Config.ARGB_8888);
        Canvas canvasBright = new Canvas(bMap);
        canvasBright.drawBitmap(inMap, 0, 0, pWhite);
        return bMap;
    }

}

