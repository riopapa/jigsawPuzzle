package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.MainActivity.brightImage;
import static com.riopapa.jigsawpuzzle.MainActivity.fullImage;
import static com.riopapa.jigsawpuzzle.MainActivity.grayedImage;
import static com.riopapa.jigsawpuzzle.MainActivity.innerSize;
import static com.riopapa.jigsawpuzzle.MainActivity.jigCOLUMNs;
import static com.riopapa.jigsawpuzzle.MainActivity.jigROWs;
import static com.riopapa.jigsawpuzzle.MainActivity.jigTables;
import static com.riopapa.jigsawpuzzle.MainActivity.maskMaps;
import static com.riopapa.jigsawpuzzle.MainActivity.outerSize;
import static com.riopapa.jigsawpuzzle.MainActivity.pieceImage;

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
                brightImage = Bitmap.createBitmap(fullImage.getWidth(), fullImage.getHeight(), Bitmap.Config.ARGB_8888);
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
                canvasBright.drawBitmap(fullImage, 0, 0, paintBright);
                for (int c = 0; c < jigCOLUMNs; c++) {
                    for (int r = 0; r < jigROWs; r++) {
                        JigTable z = jigTables[c][r];
                        Bitmap mask = pieceImage.maskMerge(maskMaps[0][z.lType], maskMaps[1][z.rType],
                                maskMaps[2][z.uType], maskMaps[3][z.dType]);
                        Bitmap bm = Bitmap.createBitmap(grayedImage, c*innerSize, r*innerSize, outerSize, outerSize);
                        canvasBright.drawBitmap(pieceImage.makeOut2Line(pieceImage.cropSrc(bm, mask, c, r)), c*innerSize, r*innerSize, null);
                    }
                }
            }
        }).start();
    }
}
