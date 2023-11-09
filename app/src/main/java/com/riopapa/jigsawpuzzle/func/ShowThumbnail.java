package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityMain.vars;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.Log;

import com.riopapa.jigsawpuzzle.databinding.ActivityJigsawBinding;

public class ShowThumbnail {
    public ShowThumbnail(ActivityJigsawBinding binding) {

        int h, w;
        float oneSize, rectW, rectH, xOff, yOff;
        if (vars.selectedHeight > vars.selectedWidth) {
            h = 1000;
            w = h * vars.selectedWidth / vars.selectedHeight;
        } else {
            w = 1000;
            h = w * vars.selectedHeight / vars.selectedWidth;
        }
        oneSize = 1000f / (float) vars.jigCOLUMNs;
        Log.w("oneSize", "oneSize="+oneSize);

        rectW =  oneSize * (float) (vars.showMaxX-1);    // 24 to vars.show line boundary
        rectH = oneSize * (float) (vars.showMaxY-1);
        xOff = oneSize * (float) vars.offsetC;
        yOff = oneSize * (float) vars.offsetR;


        Bitmap thumb = Bitmap.createScaledBitmap(vars.selectedImage, w, h, true);
        Canvas canvas = new Canvas(thumb);
        Paint paint = new Paint();
        paint.setColor(0xffff0000);
        paint.setStrokeWidth(40f);
        paint.setPathEffect(new DashPathEffect(new float[] {30, 30}, 0));

        canvas.drawLine(xOff, yOff, xOff+rectW, yOff, paint);
        canvas.drawLine(xOff, yOff, xOff, yOff+rectH, paint);
        canvas.drawLine(xOff+rectW, yOff, xOff+rectW, yOff+rectH, paint);
        canvas.drawLine(xOff, yOff+rectH, xOff+rectW, yOff+rectH, paint);

        binding.thumbnail.setImageBitmap(thumb);

    }

}
