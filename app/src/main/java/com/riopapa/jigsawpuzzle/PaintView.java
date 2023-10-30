package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.MainActivity.activeRecyclerJigs;
import static com.riopapa.jigsawpuzzle.MainActivity.allLocked;
import static com.riopapa.jigsawpuzzle.MainActivity.fps;
import static com.riopapa.jigsawpuzzle.MainActivity.fullHeight;
import static com.riopapa.jigsawpuzzle.MainActivity.fullWidth;
import static com.riopapa.jigsawpuzzle.MainActivity.hangOn;
import static com.riopapa.jigsawpuzzle.MainActivity.jPosX;
import static com.riopapa.jigsawpuzzle.MainActivity.jPosY;
import static com.riopapa.jigsawpuzzle.MainActivity.jigCOLUMNs;
import static com.riopapa.jigsawpuzzle.MainActivity.jigROWs;
import static com.riopapa.jigsawpuzzle.MainActivity.jigTables;
import static com.riopapa.jigsawpuzzle.MainActivity.nowC;
import static com.riopapa.jigsawpuzzle.MainActivity.nowR;
import static com.riopapa.jigsawpuzzle.MainActivity.oneItemSelected;
import static com.riopapa.jigsawpuzzle.MainActivity.paintView;
import static com.riopapa.jigsawpuzzle.MainActivity.picHSize;
import static com.riopapa.jigsawpuzzle.MainActivity.picISize;
import static com.riopapa.jigsawpuzzle.MainActivity.picOSize;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.riopapa.jigsawpuzzle.func.NearBy;
import com.riopapa.jigsawpuzzle.func.NearByFloatPiece;
import com.riopapa.jigsawpuzzle.func.RightPosition;
import com.riopapa.jigsawpuzzle.model.FloatPiece;
import com.riopapa.jigsawpuzzle.model.JigTable;

import java.util.ArrayList;
import java.util.Collections;

public class PaintView extends View {

    static Bitmap mBitmap;
    public static int nowIdx;
    public static boolean dragging;
    public Activity paintActivity;
    public static RightPosition rightPosition;
    public static NearBy nearBy;
    public static NearByFloatPiece nearByFloatPiece;
    PieceDraw pieceDraw;
    PieceTouch pieceTouch;

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
        dragging = false;
        mBitmap = Bitmap.createBitmap(fullWidth, fullHeight, Bitmap.Config.ARGB_8888);
        rightPosition = new RightPosition(activity);
        nearBy = new NearBy(activity);
        pieceDraw = new PieceDraw();
        pieceTouch = new PieceTouch();
        nearByFloatPiece = new NearByFloatPiece();

    }

    //  screenXY 1080 x 2316
    //  image 11232 x 7488, outerSize=802, gapSize=167, innerSize=468
    // picOSize=178, picISize=103 base XY =32 x 537
    // left top corner 121 x 624 ( 85 x  92)

    protected void onDraw(Canvas canvas){
        pieceDraw.draw(canvas);
    }
    private void paintTouchDown(float fX, float fY){

        if (hangOn)
            return;
        if (dragging)
            return;
        int iX = (int) fX - picHSize;
        int iY = (int) fY - picHSize;
        dragging = true;
        oneItemSelected = false;
        for (int i = fps.size() - 1; i >= 0; i--) {
            int c = fps.get(i).C;
            int r = fps.get(i).R;
            JigTable jt = jigTables[c][r];

            Log.w("positions "+i, "i = "+iX+"x"+iY+" jt = "+jt.posX+" x "+jt.posY);
            if (Math.abs(jt.posX - iX) < picHSize && Math.abs(jt.posY - iY) < picHSize) {
                nowR = r; nowC = c;
                nowIdx = i;
                if (nowIdx != fps.size()-1) { // move current puzzle to top
                    Collections.swap(fps, nowIdx, fps.size() - 1);
                    nowIdx = fps.size() - 1;
                }
                oneItemSelected = true;
                fpNow = fps.get(nowIdx);
                fpNow.posX = jt.posX;
                fpNow.posY = jt.posY;
                jPosX = jt.posX; jPosY = jt.posY;
                break;
            }
        }
    }


    private void paintTouchUp(){
        dragging = false;
        oneItemSelected = false;
        allLocked = isPiecesAllLocked();
//        Log.w("p83"," touchUp");
    }

    boolean isPiecesAllLocked() {
        if (activeRecyclerJigs.size() > 0 || fps.size() > 0)
            return false;
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
        if (hangOn)
            return true;
        float x = event.getX();
        float y = event.getY();
        tempTime = System.currentTimeMillis() + 100;
        if (touchTime > tempTime)
            return true;
        touchTime = tempTime;


//        Log.w("px on TouchEvent", "time="+touchTime);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                paintTouchDown(x, y);
                break;

            case MotionEvent.ACTION_MOVE:

                final float TOUCH_TOLERANCE = 20;
                if (Math.abs(x - jPosX) > TOUCH_TOLERANCE || Math.abs(y - jPosY) > TOUCH_TOLERANCE)
                    pieceTouch.move(x, y);
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
//            Log.w("p1x "+ jigCR," call by recycler drawing updateting "+jPosX+" x "+jPosY);
//            paintView.invalidate();}
//    };

}