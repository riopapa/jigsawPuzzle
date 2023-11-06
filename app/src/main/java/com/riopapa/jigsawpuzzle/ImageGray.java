package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.pieceImage;
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
import android.graphics.Paint;

import com.riopapa.jigsawpuzzle.model.JigTable;

public class ImageGray {

    public void build () {
        new Thread(new Runnable() {
            @Override
            public void run() {
                grayedImage = Bitmap.createBitmap(selectedImage.getWidth(), selectedImage.getHeight(), Bitmap.Config.ARGB_8888);
                Paint paintGray = new Paint();
                paintGray.setAlpha(30);
                Canvas canvasGray = new Canvas(grayedImage);
                canvasGray.drawBitmap(selectedImage, 0, 0, paintGray);
                for (int c = 0; c < jigCOLUMNs; c++) {
                    for (int r = 0; r < jigROWs; r++) {
                        JigTable z = jigTables[c][r];
                        Bitmap mask = pieceImage.maskMerge(maskMaps[0][z.lType], maskMaps[1][z.rType],
                                maskMaps[2][z.uType], maskMaps[3][z.dType]);
                        Bitmap bm = Bitmap.createBitmap(grayedImage, c* jigInnerSize, r* jigInnerSize, jigOuterSize, jigOuterSize);
                        canvasGray.drawBitmap(pieceImage.makeOut2Line(pieceImage.cropSrc(bm, mask, c, r)), c* jigInnerSize, r* jigInnerSize, null);
                    }
                }
            }
        }).start();
    }
}
