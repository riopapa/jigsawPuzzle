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
            oneSize = vars.imgInSize  * 1000f / vars.selectedHeight;
        } else {
            w = 1000;
            h = w * vars.selectedHeight / vars.selectedWidth;
            oneSize = vars.imgInSize  * 1000f / vars.selectedWidth;
        }

        rectW = oneSize * (float) (vars.showMaxX);    // 24 to vars.show line boundary
        rectH = oneSize * (float) (vars.showMaxY);
        xOff = oneSize * (float) vars.offsetC;
        yOff = oneSize * (float) vars.offsetR;


        Bitmap thumb = Bitmap.createScaledBitmap(vars.selectedImage, w, h, true);
        Canvas canvas = new Canvas(thumb);


        Paint pBox = new Paint();
        pBox.setColor(0x7fCCCCCC);

        canvas.drawRect(xOff, yOff, xOff+rectW, yOff+rectH, pBox);

        Paint pDot = new Paint();
        pDot.setColor(0xffff0000);
        pDot.setStrokeWidth(20f);
        pDot.setPathEffect(new DashPathEffect(new float[] {30, 30}, 0));
        Paint pLine = new Paint();
        pLine.setColor(0xffff0000);
        pLine.setStrokeWidth(40f);

        // top line
        canvas.drawLine(xOff, yOff, xOff+rectW, yOff, (yOff == 0)? pLine : pDot);
        // left line
        canvas.drawLine(xOff, yOff, xOff, yOff+rectH, (xOff == 0)? pLine : pDot);
        // right line
        canvas.drawLine(xOff+rectW, yOff, xOff+rectW, yOff+rectH,
                (vars.offsetC+vars.showMaxX == vars.jigCOLs)? pLine : pDot);
        // bottom line
        canvas.drawLine(xOff, yOff+rectH, xOff+rectW, yOff+rectH,
                (vars.offsetR+vars.showMaxY == vars.jigROWs) ? pLine : pDot);

        Log.w("q1 hw="+h+"x"+w, "C="+vars.offsetC+" max="+vars.showMaxX+" cols="+vars.jigCOLs);

        binding.thumbnail.setImageBitmap(thumb);

    }

}
