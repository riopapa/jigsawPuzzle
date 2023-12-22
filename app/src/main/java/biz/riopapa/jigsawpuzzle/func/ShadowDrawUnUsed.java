package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigPic;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.nowC;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.nowCR;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.nowR;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;

import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.View;

public class ShadowDrawUnUsed extends View.DragShadowBuilder{

    public ShadowDrawUnUsed(View view) {
        super(view);
    }
    public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint){

        shadowSize.set(gVal.picOSize, gVal.picOSize);//사이즈 지정
        shadowTouchPoint.set(gVal.picOSize/2, gVal.picOSize/2);//중심점 지정
    }

   public void onDrawShadow(Canvas canvas){

//        canvas.save();
//        Paint p = new Paint();
//        p.setAlpha(255);
        Log.w("start "+nowCR, "onDrawShadow start");
        for (int i = 0; i < gVal.activeJigs.size(); i++) {
            int cr = gVal.activeJigs.get(i);
            if (cr == nowCR) {
                nowC = nowCR / 10000;
                nowR = nowCR - nowC * 10000;
                Log.w("found "+nowCR, "onDrawShadow");
                canvas.drawBitmap(jigPic[nowC][nowR], 0, 0, null);
                break;
            }
        }
//        canvas.restore();
    }

}