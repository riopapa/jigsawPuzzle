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

    static float h, w, oneSize, rectW, rectH, xBeg, yBeg, gap;
    static Bitmap thumb;
    public ShowThumbnail() {
    }
    public Bitmap make() {

        if (chosenImageHeight > chosenImageWidth) {
            h = 800f;
            w = h * chosenImageWidth / chosenImageHeight;
            oneSize = 1000f / ((float) gVal.rowNbr + 0.5f);
        } else {
            w = 1000f;
            h = w * chosenImageHeight / chosenImageWidth;
            oneSize = 1000f / ((float) gVal.colNbr + 0.5f);
        }
        gap = oneSize * 5/ 24;
        thumb = Bitmap.createScaledBitmap(chosenImageMap, (int)w, (int)h, true);
        rectW = oneSize * (float) gVal.showMaxX - gap;    // 24 to GVal.show line boundary
        rectH = oneSize * (float) gVal.showMaxY - gap;
        xBeg = oneSize * (float) gVal.offsetC;
        yBeg = oneSize * (float) gVal.offsetR;
        if (xBeg + rectW > thumb.getWidth())
            xBeg = thumb.getWidth() - rectW;
        if (yBeg + rectH > thumb.getHeight())
            yBeg = thumb.getHeight() - rectH;
        if (xBeg < gap)
            xBeg = gap;
        if (yBeg < gap)
            yBeg = gap;
        Bitmap thumb_copy = thumb.copy(Bitmap.Config.ARGB_8888,true);
        Canvas canvas = new Canvas(thumb_copy);
        Paint pBox = new Paint();
        pBox.setColor(0x8fBBBBBB);
        canvas.drawRect(xBeg, yBeg, xBeg + rectW, yBeg + rectH, pBox);
        Bitmap boxMap = Bitmap.createBitmap(thumb_copy, (int) xBeg, (int) yBeg,
                (int) rectW, (int) rectH);
        pBox.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
        canvas.drawBitmap(boxMap, xBeg, yBeg, pBox);

        Paint pLine = new Paint();
        pLine.setColor(chosenImageColor);
        pLine.setStrokeWidth(20f);

        Paint pDash = new Paint();
        pDash.setColor(chosenImageColor);
        pDash.setStrokeWidth(20f);
        pDash.setPathEffect(new DashPathEffect(new float[] {40, 40}, 0));

        // top line
        canvas.drawLine(xBeg, yBeg, xBeg + rectW, yBeg, (gVal.offsetR == 0)? pLine : pDash);
        // left line
        canvas.drawLine(xBeg, yBeg, xBeg, yBeg + rectH, (gVal.offsetC == 0)? pLine : pDash);
        // right line
        canvas.drawLine(xBeg + rectW, yBeg, xBeg + rectW, yBeg + rectH,
                (gVal.offsetC+ gVal.showMaxX == gVal.colNbr)? pLine : pDash);
        // bottom line
        canvas.drawLine(xBeg, yBeg + rectH, xBeg + rectW, yBeg + rectH,
                (gVal.offsetR+ gVal.showMaxY == gVal.rowNbr) ? pLine : pDash);

        return thumb_copy;

    }

}
