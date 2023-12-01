package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageColor;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageHeight;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageMap;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageWidth;
import static com.riopapa.jigsawpuzzle.ActivityMain.gVal;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.Log;

import com.riopapa.jigsawpuzzle.databinding.ActivityJigsawBinding;

public class ShowThumbnail {

    float h, w, oneSize, rectW, rectH, xOff, yOff;
    Bitmap thumb;
    public ShowThumbnail() {

        if (chosenImageHeight > chosenImageWidth) {
            h = 1000;
            w = h * chosenImageWidth / chosenImageHeight;
            oneSize = gVal.imgInSize  * 1000f / chosenImageHeight;
        } else {
            w = 1000;
            h = w * chosenImageHeight / chosenImageWidth;
            oneSize = gVal.imgInSize  * 1000f / chosenImageWidth;
        }

        thumb = Bitmap.createScaledBitmap(chosenImageMap, (int)w, (int)h, true);

    }
    public Bitmap make() {

        rectW = oneSize * ((float) (gVal.showMaxX)+0.05f);    // 24 to GVal.show line boundary
        rectH = oneSize * ((float) (gVal.showMaxY)+0.05f);
        xOff = oneSize * (float) gVal.offsetC;
        yOff = oneSize * (float) gVal.offsetR;


        Bitmap thumb_copy = thumb.copy(Bitmap.Config.ARGB_8888,true);
        Canvas canvas = new Canvas(thumb_copy);

        new calcImageColor();
        int boxColor = (chosenImageColor | 0x00E0E0E0) & 0x6FFFFFFF;
        Paint pBox = new Paint();
        pBox.setColor(boxColor);
        canvas.drawRect(xOff, yOff, xOff+rectW, yOff+rectH, pBox);

        Paint pDot = new Paint();
        pDot.setColor(chosenImageColor);
        pDot.setStrokeWidth(20f);
        pDot.setPathEffect(new DashPathEffect(new float[] {50, 50}, 0));

        int color = chosenImageColor ^ 0xCCCCCC;
        Paint pDot2 = new Paint();
        pDot2.setColor(color);
        pDot2.setStrokeWidth(20f);
        pDot2.setPathEffect(new DashPathEffect(new float[] {25, 250}, 0));

        Paint pLine = new Paint();
        pLine.setColor(chosenImageColor);
        pLine.setStrokeWidth(20f);

        // top line
        canvas.drawLine(xOff, yOff, xOff+rectW, yOff, (yOff == 0)? pLine : pDot);
        canvas.drawLine(xOff, yOff, xOff+rectW, yOff, (yOff == 0)? pLine : pDot2);
        // left line
        canvas.drawLine(xOff, yOff, xOff, yOff+rectH, (xOff == 0)? pLine : pDot);
        canvas.drawLine(xOff, yOff, xOff, yOff+rectH, (xOff == 0)? pLine : pDot2);
        // right line
        canvas.drawLine(xOff+rectW, yOff, xOff+rectW, yOff+rectH,
                (gVal.offsetC+ gVal.showMaxX == gVal.colNbr)? pLine : pDot);
        canvas.drawLine(xOff+rectW, yOff, xOff+rectW, yOff+rectH,
                (gVal.offsetC+ gVal.showMaxX == gVal.colNbr)? pLine : pDot2);
        // bottom line
        canvas.drawLine(xOff, yOff+rectH, xOff+rectW, yOff+rectH,
                (gVal.offsetR+ gVal.showMaxY == gVal.rowNbr) ? pLine : pDot);
        canvas.drawLine(xOff, yOff+rectH, xOff+rectW, yOff+rectH,
                (gVal.offsetR+ gVal.showMaxY == gVal.rowNbr) ? pLine : pDot2);

        return thumb_copy;

    }

}
