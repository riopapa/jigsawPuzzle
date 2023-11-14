package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigBright;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigOLine;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigPic;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowC;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowCR;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowR;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.pieceImage;
import static com.riopapa.jigsawpuzzle.ActivityMain.vars;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;

public class ShadowDraw extends View.DragShadowBuilder{

    public ShadowDraw(View view) {
        super(view);
    }
    public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint){

        shadowSize.set(vars.picOSize, vars.picOSize);//사이즈 지정
        shadowTouchPoint.set(vars.picOSize/2, vars.picOSize/2);//중심점 지정
    }

   public void xonDrawShadow(Canvas canvas){

        canvas.save();
        Log.w("found "+nowCR, "onDrawShadow start");
        for (int i = 0; i < vars.activeRecyclerJigs.size(); i++) {
            int cr = vars.activeRecyclerJigs.get(i);
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
        canvas.restore();
    }

}
