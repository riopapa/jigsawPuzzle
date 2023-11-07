package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.pieceImage;
import static com.riopapa.jigsawpuzzle.ActivityMain.vars;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import com.riopapa.jigsawpuzzle.model.JigTable;

public class ImageBright {

    public void build () {
        new Thread(new Runnable() {
            @Override
            public void run() {
                /**
                 *
                 * @param bmp input oLine
                 * @param contrast 0..10 1 is default
                 * @param vars.brightness -255..255 0 is default
                 * @return new oLine
                 */
                Bitmap brightImage = Bitmap.createBitmap(vars.selectedImage.getWidth(), vars.selectedImage.getHeight(), Bitmap.Config.ARGB_8888);
                final int contrast  = 1;
                final int brightness = 120;
                ColorMatrix cm = new ColorMatrix(new float[]
                        {
                                contrast, 0, 0, 0, brightness,
                                0, contrast, 0, 0, brightness,
                                0, 0, contrast, 0, brightness,
                                0, 0, 0, 1, 0
                        });
                Canvas canvasBright = new Canvas(brightImage);
                Paint paintBright = new Paint();
                paintBright.setColorFilter(new ColorMatrixColorFilter(cm));
                canvasBright.drawBitmap(vars.selectedImage, 0, 0, paintBright);
                for (int c = 0; c < vars.jigCOLUMNs; c++) {
                    for (int r = 0; r < vars.jigROWs; r++) {
                        JigTable z = vars.jigTables[c][r];
                        Bitmap mask = pieceImage.maskMerge(vars.maskMaps[0][z.lType],
                                vars.maskMaps[1][z.rType], vars.maskMaps[2][z.uType], vars.maskMaps[3][z.dType]);
                        Bitmap bm = Bitmap.createBitmap(vars.grayedImage, c* vars.jigInnerSize, r* vars.jigInnerSize, vars.jigOuterSize, vars.jigOuterSize);
                        canvasBright.drawBitmap(pieceImage.makeOut2Line(pieceImage.cropSrc(bm, mask, c, r)), c* vars.jigInnerSize, r*vars.jigInnerSize, null);
                    }
                }
            }
        }).start();
    }
}
