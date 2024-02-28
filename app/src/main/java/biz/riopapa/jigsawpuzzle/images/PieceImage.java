package biz.riopapa.jigsawpuzzle.images;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.colorLocked;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.colorOutline;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.currImageMap;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.outMaskMaps;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.srcMaskMaps;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ActivityMain.mContext;
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
import android.util.Log;

import biz.riopapa.jigsawpuzzle.R;
import biz.riopapa.jigsawpuzzle.model.JigTable;

public class PieceImage {
    int orgSizeOut, orgSizeIn;
//    float out2Scale = 1.05f;
    Paint pNORM, pIN, pOUT, pCUT, pBright, pWhite, pOutATop, pLockedATop, pOutLine, pShadow, pShadowTop;
    int darkSz, outLineSz;
    Bitmap mask;
    Bitmap maskScale, picSmall, mapLocked, mapUnLocked;
    Bitmap part_up, part_dn, part_le, part_ri;
    Canvas canvasUn, canvasLk;
    Drawable2bitmap dMap;
    Matrix matrix;

    Context context;

    public PieceImage(Context context, int imgOutSize, int imgInSize) {
        this.context = context;
        this.orgSizeOut = imgOutSize;
        this.orgSizeIn = imgInSize;
        outLineSz = gVal.picOSize / 60;
        darkSz = gVal.picOSize / 80;

        pNORM = new Paint(Paint.ANTI_ALIAS_FLAG);

        pIN = new Paint(Paint.ANTI_ALIAS_FLAG);
        pIN.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        pOUT = new Paint(Paint.ANTI_ALIAS_FLAG);
        pOUT.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        pOutATop = new Paint();
        pOutATop.setColorFilter(new PorterDuffColorFilter(colorOutline, PorterDuff.Mode.SRC_ATOP));

        pLockedATop = new Paint();
        pLockedATop.setColorFilter(new PorterDuffColorFilter(colorLocked, PorterDuff.Mode.SRC_ATOP));

        pOutLine = new Paint();
        pOutLine.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

        pCUT = new Paint();
        pCUT.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));

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
        pShadowTop = new Paint();
        pShadowTop.setColorFilter(new PorterDuffColorFilter(0xFF222244, PorterDuff.Mode.SRC_ATOP));
        matrix = new Matrix();

        dMap = new Drawable2bitmap(mContext, gVal.picOSize);

        part_up = Bitmap.createScaledBitmap(dMap.make(R.drawable.part_up),
                gVal.picOSize, gVal.picOSize, true);;
        part_dn = Bitmap.createScaledBitmap(dMap.make(R.drawable.part_dn),
                gVal.picOSize, gVal.picOSize, true);;
        part_le = Bitmap.createScaledBitmap(dMap.make(R.drawable.part_le),
                gVal.picOSize, gVal.picOSize, true);;
        part_ri = Bitmap.createScaledBitmap(dMap.make(R.drawable.part_ri),
                gVal.picOSize, gVal.picOSize, true);;

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
        Bitmap mask = maskMerge(srcMaskMaps[0][jig.le], srcMaskMaps[1][jig.ri],
                srcMaskMaps[2][jig.up], srcMaskMaps[3][jig.dn]);
        Bitmap src = Bitmap.createBitmap(orgSizeOut, orgSizeOut, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(src);
        canvas.drawBitmap(orgPiece, 0, 0, null);
        canvas.drawBitmap(mask, 0, 0, pIN);
        if (showCR) {
            Paint p = new Paint();
            p.setColor(Color.BLACK);
            p.setTextSize(orgSizeOut * 4f / 20f);
            p.setStrokeWidth(orgSizeOut / 20f);
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
        mask = maskMerge(
                outMaskMaps[0][jt.le], outMaskMaps[1][jt.ri],
                outMaskMaps[2][jt.up], outMaskMaps[3][jt.dn]);
        maskScale = Bitmap.createScaledBitmap(mask, gVal.picOSize, gVal.picOSize, true);
        picSmall = Bitmap.createScaledBitmap(pic,
                gVal.picOSize- outLineSz, gVal.picOSize - outLineSz, true);
        Bitmap outMap = Bitmap.createBitmap(gVal.picOSize, gVal.picOSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outMap);
        canvas.drawBitmap(maskScale, darkSz + outLineSz, darkSz + outLineSz, pShadowTop);
        canvas.drawBitmap(maskScale, 0, 0, (jt.locked) ? pLockedATop : pOutATop);
        canvas.drawBitmap(picSmall, outLineSz, outLineSz, pOutLine);
        return outMap;
    }

    public Bitmap makeLock(Bitmap pic, Bitmap oLine, int col, int row) {

        boolean lockL = col == 0 || gVal.jigTables[col-1][row].locked;
        boolean lockR = col == gVal.colNbr-1 || gVal.jigTables[col+1][row].locked;
        boolean lockU = row == 0 || gVal.jigTables[col][row-1].locked;
        boolean lockD = row == gVal.rowNbr-1 || gVal.jigTables[col][row+1].locked;

        if (lockL && lockR && lockU && lockD)
            return pic;

        mask = maskMerge((lockL) ? part_le : null, (lockR) ? part_ri : null,
                (lockU) ? part_up : null, (lockD) ? part_dn : null);
        mapLocked = Bitmap.createBitmap(gVal.picOSize, gVal.picOSize, Bitmap.Config.ARGB_8888);
        canvasLk = new Canvas(mapLocked);
        canvasLk.drawBitmap(pic, 0, 0, pNORM);
        canvasLk.drawBitmap(mask, 0, 0, pIN);

        mask = maskMerge((lockL) ? null : part_le, (lockR) ? null : part_ri,
                (lockU) ? null : part_up, (lockD) ? null : part_dn);
        mapUnLocked = Bitmap.createBitmap(gVal.picOSize, gVal.picOSize, Bitmap.Config.ARGB_8888);
        canvasUn = new Canvas(mapUnLocked);
        canvasUn.drawBitmap(oLine, 0, 0, pNORM);
        canvasUn.drawBitmap(mask, 0, 0, pIN);

        canvasLk.drawBitmap(mapUnLocked, 0, 0, pNORM);
        return mapLocked;
    }

    public Bitmap makeGray(Bitmap pic, int wSz, int hSz) {
        Bitmap outMap = Bitmap.createBitmap(wSz, hSz, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outMap);
        Paint p = new Paint();
        for (int c = 0; c < wSz; c++) {
            for (int r = 0; r < hSz; r++) {
                int pxl = pic.getPixel(c, r);
                if (pxl != 0) {
                    int red = Color.red(pxl); int gre = Color.green(pxl); int blu = Color.blue(pxl);
                    int avr = ( red + gre + blu) / 3;
                    red = (red+avr+avr) / 3; gre = (gre+avr+avr) / 3; blu = (blu+avr+avr) / 3;
                    int color = Color.argb(0xA0, red, gre, blu);
                    p.setColor(color);
                    canvas.drawPoint(c, r, p);
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
        if (maskL != null)
            canvas.drawBitmap(maskL, 0,0, null);
        if (maskR != null)
            canvas.drawBitmap(maskR, 0,0, null);
        if (maskU != null)
            canvas.drawBitmap(maskU, 0,0, null);
        if (maskD != null)
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

