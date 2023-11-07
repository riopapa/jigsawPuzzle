package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.pieceImage;
import static com.riopapa.jigsawpuzzle.ActivityMain.vars;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.riopapa.jigsawpuzzle.model.JigTable;

public class ImageGray {

    public void build () {
        new Thread(new Runnable() {
            @Override
            public void run() {
                vars.grayedImage = Bitmap.createBitmap(vars.selectedImage.getWidth(), vars.selectedImage.getHeight(), Bitmap.Config.ARGB_8888);
                Paint paintGray = new Paint();
                paintGray.setAlpha(30);
                Canvas canvasGray = new Canvas(vars.grayedImage);
                canvasGray.drawBitmap(vars.selectedImage, 0, 0, paintGray);
                for (int c = 0; c < vars.jigCOLUMNs; c++) {
                    for (int r = 0; r < vars.jigROWs; r++) {
                        JigTable z = vars.jigTables[c][r];
                        Bitmap mask = pieceImage.maskMerge(vars.maskMaps[0][z.lType],
                                vars.maskMaps[1][z.rType], vars.maskMaps[2][z.uType],
                                vars.maskMaps[3][z.dType]);
                        Bitmap bm = Bitmap.createBitmap(vars.grayedImage, c* vars.jigInnerSize,
                                r* vars.jigInnerSize, vars.jigOuterSize, vars.jigOuterSize);
                        canvasGray.drawBitmap(pieceImage.makeOut2Line(pieceImage.cropSrc(bm, mask, c, r)), c* vars.jigInnerSize, r* vars.jigInnerSize, null);
                    }
                }
            }
        }).start();
    }
}
