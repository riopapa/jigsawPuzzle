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

public class ShowThumbnail {

    static float h, w, oneSize, rectW, rectH, xBeg, yBeg;
    static Bitmap thumb;
    public ShowThumbnail() {
    }
    public Bitmap make() {

        if (chosenImageHeight > chosenImageWidth) {
            h = 1000;
            w = h * chosenImageWidth / chosenImageHeight;
            oneSize = 1000f / gVal.colNbr;
        } else {
            w = 1000;
            h = w * chosenImageHeight / chosenImageWidth;
            oneSize = 1000 / gVal.rowNbr;
        }

        thumb = Bitmap.createScaledBitmap(chosenImageMap, (int)w, (int)h, true);

        rectW = oneSize * (float) gVal.showMaxX;    // 24 to GVal.show line boundary
        rectH = oneSize * (float) gVal.showMaxY;
        xBeg = oneSize * (float) gVal.offsetC;
        yBeg = oneSize * (float) gVal.offsetR;
        if (xBeg + rectW > thumb.getWidth())
            xBeg = thumb.getWidth() - rectW;
        if (yBeg + rectH > thumb.getHeight())
            yBeg = thumb.getHeight() - rectH;

        Bitmap thumb_copy = thumb.copy(Bitmap.Config.ARGB_8888,true);
        Canvas canvas = new Canvas(thumb_copy);
        Log.w("x map","thumb "+thumb.getWidth()+" copy "+thumb_copy.getWidth());
        Paint pBox = new Paint();
        pBox.setColor(0x8fBBBBBB);
        canvas.drawRect(xBeg, yBeg, xBeg + rectW, yBeg + rectH, pBox);
        Bitmap boxMap = Bitmap.createBitmap(thumb_copy, (int) xBeg, (int) yBeg,
                (int) rectW,(int) rectH);
        pBox.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
        canvas.drawBitmap(boxMap, xBeg, yBeg, pBox);

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
        canvas.drawLine(xBeg, yBeg, xBeg + rectW, yBeg, (yBeg == 0)? pLine : pDot);
        if (yBeg != 0)
           canvas.drawLine(xBeg, yBeg, xBeg + rectW, yBeg, pDot2);
        // left line
        canvas.drawLine(xBeg, yBeg, xBeg, yBeg + rectH, (xBeg == 0)? pLine : pDot);
        if (xBeg != 0)
            canvas.drawLine(xBeg, yBeg, xBeg, yBeg + rectH, pDot2);
        // right line
        canvas.drawLine(xBeg + rectW, yBeg, xBeg + rectW, yBeg + rectH,
                (gVal.offsetC+ gVal.showMaxX == gVal.colNbr)? pLine : pDot);
        if (gVal.offsetC+ gVal.showMaxX != gVal.colNbr)
            canvas.drawLine(xBeg + rectW, yBeg, xBeg + rectW, yBeg + rectH, pDot2);
        // bottom line
        canvas.drawLine(xBeg, yBeg + rectH, xBeg + rectW, yBeg + rectH,
                (gVal.offsetR+ gVal.showMaxY == gVal.rowNbr) ? pLine : pDot);
        if (gVal.offsetR+ gVal.showMaxY != gVal.rowNbr)
            canvas.drawLine(xBeg, yBeg + rectH, xBeg + rectW, yBeg + rectH, pDot2);

        return thumb_copy;

    }

}
