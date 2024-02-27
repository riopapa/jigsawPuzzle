package biz.riopapa.jigsawpuzzle.images;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.colorOutline;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.currImageMap;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

public class ShowThumbnail {

    static float thumbHeight, thumbWidth, oneSize, rectWidth, rectHeight, xBeg, yBeg, gap;
    static Bitmap thumb;
    public ShowThumbnail() {
    }
    public Bitmap make(Bitmap iMap) {

        int iWidth = iMap.getWidth();
        int iHeight = iMap.getHeight();
        if (iHeight > iWidth) {
            thumbHeight = 800f;
            thumbWidth = thumbHeight * iWidth / iHeight;
            oneSize = thumbWidth / ((float) gVal.colNbr + 0.5f);
        } else {
            thumbWidth = 1000f;
            thumbHeight = thumbWidth * iHeight / iWidth;
            oneSize = thumbHeight / ((float) gVal.rowNbr + 0.5f);
        }
        gap = oneSize * 5/ 24;
        thumb = Bitmap.createScaledBitmap(iMap, (int) thumbWidth, (int) thumbHeight, true);
        rectWidth = oneSize * (float) gVal.showMaxX;
        rectHeight = oneSize * (float) gVal.showMaxY;
        xBeg = oneSize * (float) gVal.offsetC + gap;
        yBeg = oneSize * (float) gVal.offsetR + gap;
        if (thumbHeight < (yBeg + rectHeight))
            rectHeight = thumbHeight - yBeg - 1;
        if (thumbWidth < (xBeg + rectWidth))
            rectWidth = thumbWidth - xBeg - 1;
        Bitmap thumb_copy = thumb.copy(Bitmap.Config.ARGB_8888,true);
        Canvas canvas = new Canvas(thumb_copy);
        Paint pBox = new Paint();
        pBox.setColor(0x8fBBBBBB);
        canvas.drawRect(xBeg, yBeg, xBeg + rectWidth, yBeg + rectHeight, pBox);
        Bitmap boxMap = Bitmap.createBitmap(thumb_copy, (int) xBeg, (int) yBeg,
                (int) rectWidth, (int) rectHeight);
        pBox.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
        canvas.drawBitmap(boxMap, xBeg, yBeg, pBox);

        Paint pLine = new Paint();
        pLine.setColor(colorOutline);
        pLine.setStrokeWidth(20f);

        Paint pDash = new Paint();
        pDash.setColor(colorOutline);
        pDash.setStrokeWidth(20f);
        pDash.setPathEffect(new DashPathEffect(new float[] {40, 40}, 0));

        // top line
        canvas.drawLine(xBeg, yBeg, xBeg + rectWidth, yBeg, (gVal.offsetR == 0)? pLine : pDash);
        // left line
        canvas.drawLine(xBeg, yBeg, xBeg, yBeg + rectHeight, (gVal.offsetC == 0)? pLine : pDash);
        // right line
        canvas.drawLine(xBeg + rectWidth, yBeg, xBeg + rectWidth, yBeg + rectHeight,
                (gVal.offsetC+ gVal.showMaxX == gVal.colNbr)? pLine : pDash);
        // bottom line
        canvas.drawLine(xBeg, yBeg + rectHeight, xBeg + rectWidth, yBeg + rectHeight,
                (gVal.offsetR+ gVal.showMaxY == gVal.rowNbr) ? pLine : pDash);

        return thumb_copy;

    }

}
