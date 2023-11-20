package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigBright;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigOLine;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowC;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowCR;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowR;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.pieceImage;
import static com.riopapa.jigsawpuzzle.ActivityMain.GVal;

import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.View;

public class ShadowDraw extends View.DragShadowBuilder{

    public ShadowDraw(View view) {
        super(view);
    }
    public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint){

        shadowSize.set(GVal.picOSize, GVal.picOSize);//사이즈 지정
        shadowTouchPoint.set(GVal.picOSize/2, GVal.picOSize/2);//중심점 지정
    }

   public void onDrawShadow(Canvas canvas){

//        canvas.save();
//        Paint p = new Paint();
//        p.setAlpha(255);
        Log.w("start "+nowCR, "onDrawShadow start");
        for (int i = 0; i < GVal.activeRecyclerJigs.size(); i++) {
            int cr = GVal.activeRecyclerJigs.get(i);
            if (cr == nowCR) {
                nowC = nowCR / 10000;
                nowR = nowCR - nowC * 10000;
                Log.w("found "+nowCR, "onDrawShadow");
                if (jigBright[nowC][nowR] == null)
                    jigBright[nowC][nowR] = pieceImage.makeBright(jigOLine[nowC][nowR]);
                canvas.drawBitmap(jigBright[nowC][nowR], 0, 0, null);
                break;
            }
        }
//        canvas.restore();
    }

}
