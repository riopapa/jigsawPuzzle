package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.Vars.jigCOLUMNs;
import static com.riopapa.jigsawpuzzle.Vars.jigROWs;
import static com.riopapa.jigsawpuzzle.Vars.offsetC;
import static com.riopapa.jigsawpuzzle.Vars.offsetR;
import static com.riopapa.jigsawpuzzle.Vars.selectedHeight;
import static com.riopapa.jigsawpuzzle.Vars.selectedImage;
import static com.riopapa.jigsawpuzzle.Vars.selectedWidth;
import static com.riopapa.jigsawpuzzle.Vars.showMaxX;
import static com.riopapa.jigsawpuzzle.Vars.showMaxY;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;

import com.riopapa.jigsawpuzzle.databinding.ActivityJigsawBinding;

public class ShowThumbnail {
    public ShowThumbnail(ActivityJigsawBinding binding) {
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
