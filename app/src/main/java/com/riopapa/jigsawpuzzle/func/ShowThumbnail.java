package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageColor;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageHeight;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageMap;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageWidth;
import static com.riopapa.jigsawpuzzle.ActivityMain.GVal;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;

import com.riopapa.jigsawpuzzle.databinding.ActivityJigsawBinding;

public class ShowThumbnail {
    public ShowThumbnail(ActivityJigsawBinding binding) {

        int h, w;
        float oneSize, rectW, rectH, xOff, yOff;
        if (chosenImageHeight > chosenImageWidth) {
            h = 1000;
            w = h * chosenImageWidth / chosenImageHeight;
            oneSize = GVal.imgInSize  * 1000f / chosenImageHeight;
        } else {
            w = 1000;
            h = w * chosenImageHeight / chosenImageWidth;
            oneSize = GVal.imgInSize  * 1000f / chosenImageWidth;
        }

        rectW = oneSize * ((float) (GVal.showMaxX)+0.05f);    // 24 to GVal.show line boundary
        rectH = oneSize * ((float) (GVal.showMaxY)+0.05f);
        xOff = oneSize * (float) GVal.offsetC;
        yOff = oneSize * (float) GVal.offsetR;


        Bitmap thumb = Bitmap.createScaledBitmap(chosenImageMap, (int) (w-oneSize/24), (int) (h-oneSize/24), true);
        Canvas canvas = new Canvas(thumb);

        Paint pBox = new Paint();
        pBox.setColor(0x7fBBBBBB);

        canvas.drawRect(xOff, yOff, xOff+rectW, yOff+rectH, pBox);

        Paint pDot = new Paint();
        pDot.setColor(chosenImageColor);
        pDot.setStrokeWidth(20f);
        pDot.setPathEffect(new DashPathEffect(new float[] {50, 50}, 0));
        Paint pLine = new Paint();
        pLine.setColor(chosenImageColor);
        pLine.setStrokeWidth(40f);

        // top line
        canvas.drawLine(xOff, yOff, xOff+rectW, yOff, (yOff == 0)? pLine : pDot);
        // left line
        canvas.drawLine(xOff, yOff, xOff, yOff+rectH, (xOff == 0)? pLine : pDot);
        // right line
        canvas.drawLine(xOff+rectW, yOff, xOff+rectW, yOff+rectH,
                (GVal.offsetC+ GVal.showMaxX == GVal.jigCOLs)? pLine : pDot);
        // bottom line
        canvas.drawLine(xOff, yOff+rectH, xOff+rectW, yOff+rectH,
                (GVal.offsetR+ GVal.showMaxY == GVal.jigROWs) ? pLine : pDot);

        binding.thumbnail.setImageBitmap(thumb);

    }

}
