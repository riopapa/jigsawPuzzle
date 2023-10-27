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
import com.riopapa.jigsawpuzzle.func.NearByFloatPiece;
import com.riopapa.jigsawpuzzle.func.RightPosition;
import com.riopapa.jigsawpuzzle.model.FloatPiece;
import com.riopapa.jigsawpuzzle.model.JigTable;

import java.util.ArrayList;
import java.util.Collections;

public class PaintView extends View {

    private static final float TOUCH_TOLERANCE = 20;
    static JigTable nowJig;
    static Bitmap mBitmap;
    public int nowIdx;
    public static int calcC, calcR;
    public static boolean dragging;
    public Activity paintActivity;
    RightPosition rightPosition;
    NearBy nearBy;
    NearByFloatPiece nearByFloatPiece;
    PieceDraw pieceDraw;

    FloatPiece fpNow;

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
        int iX = (int) fX;
        int iY = (int) fY;
        dragging = true;
        oneItemSelected = false;
        for (int i = fps.size() - 1; i >= 0; i--) {
            int c = fps.get(i).C;
            int r = fps.get(i).R;
            JigTable jt = jigTables[c][r];
            if (isPieceSelected(jt, iX, iY)) {
                nowR = r; nowC = c;
                nowIdx = i;
                nowJig = jigTables[c][r];
                jPosX = iX; jPosY = iY;
                oneItemSelected = true;
//                Log.w("pfp ","selected fpidx="+fPIdx+" c="+ nowC +" r="+ nowR
//                + " x y "+jPosX+" x "+jPosY+" fpSize="+fPs.size());
                if (nowIdx != fps.size()-1) { // move current puzzle to top
                    Collections.swap(fps, nowIdx, fps.size() - 1);
                    nowIdx = fps.size() - 1;
                }
                fpNow = fps.get(nowIdx);
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

            if (fpNow.anchorId != 0) {  // anchored with others
                for (int i = 0; i < fps.size(); i++) {
                    FloatPiece fpT = fps.get(i);
                    if (fpT.anchorId == fpNow.anchorId) {
                        jigTables[fpT.C][fpT.R].posX =
                                jigTables[nowC][nowR].posX - (nowC - fpT.C) * picISize;
                        jigTables[fpT.C][fpT.R].posY =
                                jigTables[nowC][nowR].posY - (nowR - fpT.R) * picISize;
                    }
                }
            }


            // if piece moved to right rightPosition then lock thi piece
            if (!jigTables[nowC][nowR].locked && rightPosition.isHere()  && nearBy.isLockable()) {
                hangOn = true;
                oneItemSelected = false;
                jigTables[nowC][nowR].locked = true;
                jigTables[nowC][nowR].count = 2;
                jigTables[nowC][nowR].lockedTime = System.currentTimeMillis() + 953;
                fps.remove(nowIdx);
                hangOn = false;
            } else if (jPosY > screenY - recySize - picHSize && fps.size() > 0) {
                hangOn = true;
                Log.w("pchk Check", "fps size="+ fps.size()+" fPIdx="+ nowIdx +" now CR "+nowC+"x"+nowR);
                fps.remove(nowIdx);
                insert2Recycle.sendEmptyMessage(0);
                oneItemSelected = false;
                hangOn = false;
            }
            // check whether can be anchored to near by piece
            int ancIdx = nearByFloatPiece.anchor(nowIdx, fpNow);
            if (ancIdx != -1) {
                FloatPiece fpAnc = fps.get(ancIdx);
                if (fpAnc.anchorId == 0)
                    fpAnc.anchorId = System.currentTimeMillis();
                if (fpNow.anchorId != fpAnc.anchorId) {
                    fpNow.anchorId = fpAnc.anchorId;
                    jigTables[nowC][nowR].posX =
                            jigTables[fpAnc.C][fpAnc.R].posX + (nowC - fpAnc.C) * picISize;
                    jigTables[nowC][nowR].posY =
                            jigTables[fpAnc.C][fpAnc.R].posY + (nowR - fpAnc.R) * picISize;
                    for (int i = 0; i < fps.size(); i++) {
                        FloatPiece fpT = fps.get(i);
                        if (fpT.anchorId == fpNow.anchorId) {
                            fpT.time = 123; // make it not zero
                            fpT.count = 3;
                            fps.set(i, fpT);
                        }
                    }
                }
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