package com.riopapa.zigsawpuzzle;

import static com.riopapa.zigsawpuzzle.MainActivity.fullImage;
import static com.riopapa.zigsawpuzzle.MainActivity.grayedImage;
import static com.riopapa.zigsawpuzzle.MainActivity.innerSize;
import static com.riopapa.zigsawpuzzle.MainActivity.jigCntX;
import static com.riopapa.zigsawpuzzle.MainActivity.jigCntY;
import static com.riopapa.zigsawpuzzle.MainActivity.jigTables;
import static com.riopapa.zigsawpuzzle.MainActivity.maskMaps;
import static com.riopapa.zigsawpuzzle.MainActivity.outerSize;
import static com.riopapa.zigsawpuzzle.MainActivity.piece;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.riopapa.zigsawpuzzle.model.JigTable;

public class ImageGray {

    public void build () {
        new Thread(new Runnable() {
            @Override
            public void run() {
                grayedImage = Bitmap.createBitmap(fullImage.getWidth(), fullImage.getHeight(), Bitmap.Config.ARGB_8888);
                Paint paintGray = new Paint();
                paintGray.setAlpha(30);
                Canvas canvasGray = new Canvas(grayedImage);
                canvasGray.drawBitmap(fullImage, 0, 0, paintGray);
                for (int x = 0; x < jigCntX; x++) {
                    for (int y = 0; y < jigCntY; y++) {
                        JigTable z = jigTables[x][y];
                        Bitmap mask = piece.maskMerge(maskMaps[0][z.lType], maskMaps[1][z.rType],
                                maskMaps[2][z.uType], maskMaps[3][z.dType], innerSize, outerSize);
                        Bitmap bm = Bitmap.createBitmap(grayedImage, x*innerSize, y*innerSize, outerSize, outerSize);
                        canvasGray.drawBitmap(piece.getOutline(piece.cropZig(bm, mask), 0x00000000), x*innerSize, y*innerSize, null);
                    }
                }
            }
        }).start();
    }
}
