package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.MainActivity.jigCOLUMNs;
import static com.riopapa.jigsawpuzzle.MainActivity.jigROWs;
import static com.riopapa.jigsawpuzzle.MainActivity.offsetC;
import static com.riopapa.jigsawpuzzle.MainActivity.offsetR;
import static com.riopapa.jigsawpuzzle.MainActivity.selectedHeight;
import static com.riopapa.jigsawpuzzle.MainActivity.selectedImage;
import static com.riopapa.jigsawpuzzle.MainActivity.selectedWidth;
import static com.riopapa.jigsawpuzzle.MainActivity.showMaxX;
import static com.riopapa.jigsawpuzzle.MainActivity.showMaxY;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;

import com.riopapa.jigsawpuzzle.databinding.ActivityMainBinding;

public class ShowThumbnail {
    public ShowThumbnail(ActivityMainBinding binding) {
        int h, w, rectSize, xOff, yOff;
        if (selectedHeight > selectedWidth) {
            h = 1000;
            w = h * selectedWidth / selectedHeight;
            rectSize = 1000 * (showMaxY) / jigROWs;    // 24 to show line boundary
            xOff = offsetC * 1000  / jigROWs;
            yOff = offsetR * 1000 / jigROWs;
        } else {
            w = 1000;
            h = w * selectedHeight / selectedWidth;
            rectSize = 1000 * (showMaxX) / jigCOLUMNs;
            xOff = offsetC * 1000  / jigCOLUMNs;
            yOff = offsetR * 1000 / jigCOLUMNs;
        }
        if (xOff + rectSize >= w)
            xOff = w - rectSize;
        if (yOff + rectSize >= h)
            yOff = h - rectSize;

        Bitmap thumb = Bitmap.createScaledBitmap(selectedImage, w, h, true);
        Canvas canvas = new Canvas(thumb);
        Paint paint = new Paint();
        paint.setColor(0xffff0000);
        paint.setStrokeWidth(40f);
        paint.setPathEffect(new DashPathEffect(new float[] {30, 30}, 0));


        canvas.drawLine(xOff, yOff, xOff+rectSize, yOff, paint);
        canvas.drawLine(xOff, yOff, xOff, yOff+rectSize, paint);
        canvas.drawLine(xOff+rectSize, yOff, xOff+rectSize, yOff+rectSize, paint);
        canvas.drawLine(xOff, yOff+rectSize, xOff+rectSize, yOff+rectSize, paint);

        binding.thumbnail.setImageBitmap(thumb);

    }

}
