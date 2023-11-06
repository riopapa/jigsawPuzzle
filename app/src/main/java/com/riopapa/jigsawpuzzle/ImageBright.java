package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.pieceImage;
import static com.riopapa.jigsawpuzzle.Vars.brightImage;
import static com.riopapa.jigsawpuzzle.Vars.grayedImage;
import static com.riopapa.jigsawpuzzle.Vars.jigCOLUMNs;
import static com.riopapa.jigsawpuzzle.Vars.jigInnerSize;
import static com.riopapa.jigsawpuzzle.Vars.jigOuterSize;
import static com.riopapa.jigsawpuzzle.Vars.jigROWs;
import static com.riopapa.jigsawpuzzle.Vars.jigTables;
import static com.riopapa.jigsawpuzzle.Vars.maskMaps;
import static com.riopapa.jigsawpuzzle.Vars.selectedImage;

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
                 * @param brightness -255..255 0 is default
                 * @return new oLine
                 */
                brightImage = Bitmap.createBitmap(selectedImage.getWidth(), selectedImage.getHeight(), Bitmap.Config.ARGB_8888);
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
                canvasBright.drawBitmap(selectedImage, 0, 0, paintBright);
                for (int c = 0; c < jigCOLUMNs; c++) {
                    for (int r = 0; r < jigROWs; r++) {
                        JigTable z = jigTables[c][r];
                        Bitmap mask = pieceImage.maskMerge(maskMaps[0][z.lType], maskMaps[1][z.rType],
                                maskMaps[2][z.uType], maskMaps[3][z.dType]);
                        Bitmap bm = Bitmap.createBitmap(grayedImage, c* jigInnerSize, r* jigInnerSize, jigOuterSize, jigOuterSize);
                        canvasBright.drawBitmap(pieceImage.makeOut2Line(pieceImage.cropSrc(bm, mask, c, r)), c* jigInnerSize, r* jigInnerSize, null);
                    }
                }
            }
        }).start();
    }
}
