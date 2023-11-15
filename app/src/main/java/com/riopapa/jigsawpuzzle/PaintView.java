package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.doNotUpdate;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jPosX;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jPosY;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowC;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowR;
import static com.riopapa.jigsawpuzzle.ActivityMain.vars;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.riopapa.jigsawpuzzle.func.NearByFloatPiece;
import com.riopapa.jigsawpuzzle.func.NearByPieces;
import com.riopapa.jigsawpuzzle.func.NearPieceBind;
import com.riopapa.jigsawpuzzle.func.PieceSelection;
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
    NearPieceBind nearPieceBind;
    PieceSelection pieceSelection;

    public static FloatPiece fpNow;

    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Activity activity){
        this.paintActivity = activity;
        vars.fps = new ArrayList<>();
        fpNow = null;

        rightPosition = new RightPosition(activity);
        nearByPieces = new NearByPieces(activity);
        pieceDraw = new PieceDraw();
        nearPieceBind = new NearPieceBind();
        nearByFloatPiece = new NearByFloatPiece();
        pieceSelection = new PieceSelection();

    }

    //  screenXY 1080 x 2316
    //  image 11232 x 7488, outerSize=802, gapSize=167, innerSize=468
    // picOSize=178, picISize=103 base XY =32 x 537
    // left top corner 121 x 624 ( 85 x  92)

    protected void onDraw(Canvas canvas){
        pieceDraw.draw(canvas);
    }


    private void paintTouchUp(){
        vars.allLocked = isPiecesAllLocked();
    }

    boolean isPiecesAllLocked() {
        if (vars.activeRecyclerJigs.size() > 0 || vars.fps.size() > 0) {
            return false;
        }
        for (int c = 0; c < vars.jigCOLs; c++) {
            for (int r = 0; r < vars.jigROWs; r++) {
                if (!vars.jigTables[c][r].locked)
                    return false;
            }
        }
        return true;
    }
    long nextOKTime = 0, nowTime;
    public boolean onTouchEvent(MotionEvent event) {
        if (doNotUpdate)
            return true;
        nowTime = System.currentTimeMillis();
        if (nextOKTime > nowTime)
            return true;
        nextOKTime = nowTime + 100;

        int x = (int) event.getX() - vars.picHSize;
        int y = (int) event.getY() - vars.picHSize;

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
//                Log.w("px on ACTION_DOWN", x+"x"+y);
                pieceSelection.check(x, y);
                break;

            case MotionEvent.ACTION_MOVE:

                final float MOVING = 30;
                if (fpNow != null &&
                    (Math.abs(x - jPosX) > MOVING || Math.abs(y - jPosY) > MOVING)) {
                    vars.jigTables[nowC][nowR].posX = x;
                    vars.jigTables[nowC][nowR].posY = y;
                    nearPieceBind.check(x, y);
                }
                break;
            case MotionEvent.ACTION_UP:
                paintTouchUp();
                break;
        }

        return true;
    }

//    public final static Handler updateViewHandler = new Handler(Looper.getMainLooper()) {
//        public void handleMessage(Message msg) {
//            nowJig = vars.jigTables[nowC][nowR];
//            mapNow = Bitmap.createScaledBitmap(nowJig.src, picOSize, picOSize, true);
//            dragging = true;
//            Log.w("p1x "+ jigCR," call by recycler drawing updating "+jPosX+" x "+jPosY);
//            paintView.invalidate();}
//    };

}