package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.MainActivity.fPs;
import static com.riopapa.jigsawpuzzle.MainActivity.fullHeight;
import static com.riopapa.jigsawpuzzle.MainActivity.fullWidth;
import static com.riopapa.jigsawpuzzle.MainActivity.hangOn;
import static com.riopapa.jigsawpuzzle.MainActivity.jPosX;
import static com.riopapa.jigsawpuzzle.MainActivity.jPosY;
import static com.riopapa.jigsawpuzzle.MainActivity.jigTables;
import static com.riopapa.jigsawpuzzle.MainActivity.nowC;
import static com.riopapa.jigsawpuzzle.MainActivity.nowR;
import static com.riopapa.jigsawpuzzle.MainActivity.oneItemSelected;
import static com.riopapa.jigsawpuzzle.MainActivity.paintView;
import static com.riopapa.jigsawpuzzle.MainActivity.picHSize;
import static com.riopapa.jigsawpuzzle.MainActivity.picISize;
import static com.riopapa.jigsawpuzzle.MainActivity.picOSize;
import static com.riopapa.jigsawpuzzle.MainActivity.recySize;
import static com.riopapa.jigsawpuzzle.MainActivity.screenY;
import static com.riopapa.jigsawpuzzle.RecycleJigListener.insert2Recycle;

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
import com.riopapa.jigsawpuzzle.func.RightPosition;
import com.riopapa.jigsawpuzzle.model.JigTable;

import java.util.ArrayList;
import java.util.Collections;

public class PaintView extends View {

    private static final float TOUCH_TOLERANCE = 20;
    static JigTable nowJig;
    static Bitmap mBitmap;
    public int fPIdx;
    public static int calcC, calcR;
    public static boolean dragging;
    public Activity paintActivity;
    RightPosition rightPosition;
    NearBy nearBy;
    PieceDraw pieceDraw;


    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Activity activity){
        this.paintActivity = activity;
        fPs = new ArrayList<>();
        dragging = false;
        mBitmap = Bitmap.createBitmap(fullWidth, fullHeight, Bitmap.Config.ARGB_8888);
        rightPosition = new RightPosition(activity);
        nearBy = new NearBy(activity);
        pieceDraw = new PieceDraw();

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
        int iX = (int) fX;
        int iY = (int) fY;
        dragging = true;
        oneItemSelected = false;
        for (int i = fPs.size() - 1; i >= 0; i--) {
            int c = fPs.get(i).C;
            int r = fPs.get(i).R;
            JigTable jt = jigTables[c][r];
            if (isPieceSelected(jt, iX, iY)) {
                nowR = r; nowC = c;
                fPIdx = i;
                nowJig = jigTables[c][r];
                jPosX = iX; jPosY = iY;
                oneItemSelected = true;
//                Log.w("pfp ","selected fpidx="+fPIdx+" c="+ nowC +" r="+ nowR
//                + " x y "+jPosX+" x "+jPosY+" fpSize="+fPs.size());
                if (fPIdx != fPs.size()-1) { // move current puzzle to top
                    Collections.swap(fPs, fPIdx, fPs.size() - 1);
                    fPIdx = fPs.size() - 1;
                }
                break;
            }
        }
    }

    private boolean isPieceSelected(JigTable jt, int x, int y) {
        return jt.posX < x && x < (jt.posX + picOSize) &&
                jt.posY < y && y < (jt.posY + picOSize);
    }
    private void paintTouchMove(float fX, float fY){
        if (hangOn)
            return;
        if (!oneItemSelected)
            return;

        if (fX < jPosX - TOUCH_TOLERANCE || fX > jPosX + TOUCH_TOLERANCE ||
           fY < jPosY - TOUCH_TOLERANCE || fY > jPosY + TOUCH_TOLERANCE) {

            jPosX = (int) fX;
            jPosY = (int) fY;
            jigTables[nowC][nowR].posX = jPosX - picISize;
            jigTables[nowC][nowR].posY = jPosY - picISize;

            // if piece moved to right rightPosition then lock thi piece
            if (!jigTables[nowC][nowR].locked && rightPosition.isHere()  && nearBy.isLockable()) {
                hangOn = true;
                oneItemSelected = false;
                jigTables[nowC][nowR].locked = true;
                jigTables[nowC][nowR].count = 2;
                jigTables[nowC][nowR].lockedTime = System.currentTimeMillis() + 953;
                fPs.remove(fPIdx);
                hangOn = false;
            } else if (jPosY > screenY - recySize - picHSize && fPs.size() > 0) {
                hangOn = true;
                Log.w("pchk Check", "fps size="+fPs.size()+" fPIdx="+fPIdx+" now CR "+nowC+"x"+nowR);
                fPs.remove(fPIdx);
                insert2Recycle.sendEmptyMessage(0);
                oneItemSelected = false;
                hangOn = false;
            }
        }
    }

    private void paintTouchUp(){
        dragging = false;
        oneItemSelected = false;
//        Log.w("p83"," touchUp");
    }

    long touchTime = 0, tempTime;
    public boolean onTouchEvent(MotionEvent event) {
        if (hangOn)
            return true;
        float x = event.getX();
        float y = event.getY();
        tempTime = System.currentTimeMillis() + 200;
        if (touchTime > tempTime)
            return true;
        touchTime = tempTime;
//        Log.w("px on TouchEvent", "time="+touchTime);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                paintTouchDown(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                paintTouchMove(x, y);
                break;
            case MotionEvent.ACTION_UP:
                paintTouchUp();
                break;
        }

        return true;
    }

    public final static Handler updateViewHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
//            nowJig = jigTables[nowC][nowR];
//            mapNow = Bitmap.createScaledBitmap(nowJig.src, picOSize, picOSize, true);
//            dragging = true;
//            Log.w("p1x "+ jigCR," call by recycler drawing updateting "+jPosX+" x "+jPosY);
            paintView.invalidate();}
    };

}