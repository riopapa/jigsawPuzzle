package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityMain.vars;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;

import com.riopapa.jigsawpuzzle.databinding.ActivityJigsawBinding;

public class ShowThumbnail {
    public ShowThumbnail(ActivityJigsawBinding binding) {
        int h, w, rectSize, xOff, yOff;
        if (vars.selectedHeight > vars.selectedWidth) {
            h = 1000;
            w = h * vars.selectedWidth / vars.selectedHeight;
            rectSize = 1000 * (vars.showMaxY) / vars.jigROWs;    // 24 to vars.show line boundary
            xOff = vars.offsetC * 1000  / vars.jigROWs;
            yOff = vars.offsetR * 1000 / vars.jigROWs;
        } else {
            w = 1000;
            h = w * vars.selectedHeight / vars.selectedWidth;
            rectSize = 1000 * (vars.showMaxX) / vars.jigCOLUMNs;
            xOff = vars.offsetC * 1000  / vars.jigCOLUMNs;
            yOff = vars.offsetR * 1000 / vars.jigCOLUMNs;
        }
        if (xOff + rectSize >= w)
            xOff = w - rectSize;
        if (yOff + rectSize >= h)
            yOff = h - rectSize;

        Bitmap thumb = Bitmap.createScaledBitmap(vars.selectedImage, w, h, true);
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
