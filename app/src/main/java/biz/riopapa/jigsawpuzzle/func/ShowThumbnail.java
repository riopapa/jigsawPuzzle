package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageColor;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageHeight;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageMap;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageWidth;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Log;

import biz.riopapa.jigsawpuzzle.databinding.ActivityJigsawBinding;

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
//        int boxColor = (chosenImageColor | 0x00E0E0E0) & 0x6FFFFFFF;
        Paint pBox = new Paint();
        pBox.setColor(0x8fBBBBBB);
        canvas.drawRect(xOff, yOff, xOff+rectW, yOff+rectH, pBox);
        Bitmap boxMap = Bitmap.createBitmap(thumb_copy, (int) xOff, (int) yOff,
                (int) (xOff+rectW),(int) (yOff+rectH));
        pBox.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
        canvas.drawBitmap(boxMap, xOff, yOff, pBox);

        Paint pLine = new Paint();
        pLine.setColor(chosenImageColor);
        pLine.setStrokeWidth(20f);

        Paint pDot = new Paint();
        pDot.setColor(chosenImageColor ^ 0xCCCCCC);
        pDot.setStrokeWidth(20f);
        pDot.setPathEffect(new DashPathEffect(new float[] {50, 50}, 0));

        Paint pDot2 = new Paint();
        pDot2.setColor(chosenImageColor);
        pDot2.setStrokeWidth(20f);
        pDot2.setPathEffect(new DashPathEffect(new float[] {50, 50}, 0));

        // top line
        canvas.drawLine(xOff, yOff, xOff+rectW, yOff, (yOff == 0)? pLine : pDot);
        if (yOff != 0)
           canvas.drawLine(xOff, yOff, xOff+rectW, yOff, pDot2);
        // left line
        canvas.drawLine(xOff, yOff, xOff, yOff+rectH, (xOff == 0)? pLine : pDot);
        if (xOff != 0)
            canvas.drawLine(xOff, yOff, xOff, yOff+rectH, pDot2);
        // right line
        canvas.drawLine(xOff+rectW, yOff, xOff+rectW, yOff+rectH,
                (gVal.offsetC+ gVal.showMaxX == gVal.colNbr)? pLine : pDot);
        if (gVal.offsetC+ gVal.showMaxX != gVal.colNbr)
            canvas.drawLine(xOff+rectW, yOff, xOff+rectW, yOff+rectH, pDot2);
        // bottom line
        canvas.drawLine(xOff, yOff+rectH, xOff+rectW, yOff+rectH,
                (gVal.offsetR+ gVal.showMaxY == gVal.rowNbr) ? pLine : pDot);
        if (gVal.offsetR+ gVal.showMaxY != gVal.rowNbr)
            canvas.drawLine(xOff, yOff+rectH, xOff+rectW, yOff+rectH, pDot2);

        return thumb_copy;

    }

}
