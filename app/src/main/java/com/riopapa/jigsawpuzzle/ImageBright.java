package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.MainActivity.brightImage;
import static com.riopapa.jigsawpuzzle.MainActivity.fullImage;
import static com.riopapa.jigsawpuzzle.MainActivity.grayedImage;
import static com.riopapa.jigsawpuzzle.MainActivity.innerSize;
import static com.riopapa.jigsawpuzzle.MainActivity.jigCntX;
import static com.riopapa.jigsawpuzzle.MainActivity.jigCntY;
import static com.riopapa.jigsawpuzzle.MainActivity.jigTables;
import static com.riopapa.jigsawpuzzle.MainActivity.maskMaps;
import static com.riopapa.jigsawpuzzle.MainActivity.outerSize;
import static com.riopapa.jigsawpuzzle.MainActivity.piece;

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
                 * @param bmp input bitmap
                 * @param contrast 0..10 1 is default
                 * @param brightness -255..255 0 is default
                 * @return new bitmap
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
                for (int x = 0; x < jigCntX; x++) {
                    for (int y = 0; y < jigCntY; y++) {
                        JigTable z = jigTables[x][y];
                        Bitmap mask = piece.maskMerge(maskMaps[0][z.lType], maskMaps[1][z.rType],
                                maskMaps[2][z.uType], maskMaps[3][z.dType], innerSize, outerSize);
                        Bitmap bm = Bitmap.createBitmap(grayedImage, x*innerSize, y*innerSize, outerSize, outerSize);
                        canvasBright.drawBitmap(piece.getOutline(piece.cropZig(bm, mask), 0x00000000), x*innerSize, y*innerSize, null);
                    }
                }
            }
        }).start();
    }
}
