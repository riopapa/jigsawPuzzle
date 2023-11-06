package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.Vars.activeRecyclerJigs;
import static com.riopapa.jigsawpuzzle.Vars.allLocked;
import static com.riopapa.jigsawpuzzle.Vars.doNotUpdate;
import static com.riopapa.jigsawpuzzle.Vars.fps;
import static com.riopapa.jigsawpuzzle.Vars.jPosX;
import static com.riopapa.jigsawpuzzle.Vars.jPosY;
import static com.riopapa.jigsawpuzzle.Vars.jigCOLUMNs;
import static com.riopapa.jigsawpuzzle.Vars.jigROWs;
import static com.riopapa.jigsawpuzzle.Vars.jigTables;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.riopapa.jigsawpuzzle.func.NearByFloatPiece;
import com.riopapa.jigsawpuzzle.func.NearByPieces;
import com.riopapa.jigsawpuzzle.func.RightPosition;
import com.riopapa.jigsawpuzzle.model.FloatPiece;

import java.util.ArrayList;

public class PaintView extends View {

    public static int nowIdx;
    public Activity paintActivity;
    public static RightPosition rightPosition;
    public static NearByPieces nearByPieces;
    public static NearByFloatPiece nearByFloatPiece;
    PieceDraw pieceDraw;
    PieceTouchMove pieceTouchMove;
    PieceTouchDown pieceTouchDown;

    public static FloatPiece fpNow;

    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Activity activity){
        this.paintActivity = activity;
        fps = new ArrayList<>();

        rightPosition = new RightPosition(activity);
        nearByPieces = new NearByPieces(activity);
        pieceDraw = new PieceDraw();
        pieceTouchMove = new PieceTouchMove();
        nearByFloatPiece = new NearByFloatPiece();
        pieceTouchDown = new PieceTouchDown();

    }

    //  screenXY 1080 x 2316
    //  image 11232 x 7488, outerSize=802, gapSize=167, innerSize=468
    // picOSize=178, picISize=103 base XY =32 x 537
    // left top corner 121 x 624 ( 85 x  92)

    protected void onDraw(Canvas canvas){
        pieceDraw.draw(canvas);
    }


    private void paintTouchUp(){
        allLocked = isPiecesAllLocked();
    }

    boolean isPiecesAllLocked() {
        if (activeRecyclerJigs.size() > 0 || fps.size() > 0) {
            return false;
        }
        for (int c = 0; c < jigCOLUMNs; c++) {
            for (int r = 0; r < jigROWs; r++) {
                if (!jigTables[c][r].locked)
                    return false;
            }
        }
        return true;
    }
    long touchTime = 0, tempTime;
    public boolean onTouchEvent(MotionEvent event) {
        if (doNotUpdate)
            return true;
        float x = event.getX();
        float y = event.getY();
        tempTime = System.currentTimeMillis() + 100;
        if (touchTime > tempTime)
            return true;
        touchTime = tempTime;
        Log.w("onTouchEvent", "doNotUpdate ="+doNotUpdate);

//        Log.w("px on TouchEvent", "time="+touchTime);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                pieceTouchDown.start(x, y);
                break;

            case MotionEvent.ACTION_MOVE:

                final float TOUCH_TOLERANCE = 30;
                if (Math.abs(x - jPosX) > TOUCH_TOLERANCE || Math.abs(y - jPosY) > TOUCH_TOLERANCE)
                    pieceTouchMove.start(x, y);
                break;
            case MotionEvent.ACTION_UP:
                paintTouchUp();
                break;
        }

        return true;
    }

//    public final static Handler updateViewHandler = new Handler(Looper.getMainLooper()) {
//        public void handleMessage(Message msg) {
//            nowJig = jigTables[nowC][nowR];
//            mapNow = Bitmap.createScaledBitmap(nowJig.src, picOSize, picOSize, true);
//            dragging = true;
//            Log.w("p1x "+ jigCR," call by recycler drawing updating "+jPosX+" x "+jPosY);
//            paintView.invalidate();}
//    };

}