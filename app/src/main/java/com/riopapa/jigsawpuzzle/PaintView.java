package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.doNotUpdate;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.dragX;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jPosX;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jPosY;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigRecycleAdapter;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigRecyclePos;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigRecyclerView;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowC;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowR;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenBottom;
import static com.riopapa.jigsawpuzzle.ActivityMain.vars;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

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
                if ((Math.abs(x - jPosX) > MOVING || Math.abs(y - jPosY) > MOVING) &&
                    fpNow != null) {
                    if (wannaBack2Recycler(y)) {
                        doNotUpdate = true;
                        Log.w("pchk Check", "vars.fps size=" + vars.fps.size() + " fPIdx=" + nowIdx + " now CR " + nowC + "x" + nowR);
                        vars.fps.remove(nowIdx);
                        goBack2Recycler();
                        fpNow = null;
                        dragX = -1;
                    } else if (y < screenBottom) {
                        vars.jigTables[nowC][nowR].posX = x;
                        vars.jigTables[nowC][nowR].posY = y;
                        nearPieceBind.check(x, y);
                    } else {
                        y -= vars.picOSize;
                        vars.jigTables[nowC][nowR].posX = x;
                        vars.jigTables[nowC][nowR].posY = y;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                paintTouchUp();
                break;
        }

        return true;
    }

    public void goBack2Recycler() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) jigRecyclerView.getLayoutManager();
        int i = layoutManager.findFirstVisibleItemPosition();
        View v = layoutManager.findViewByPosition(i);
        jigRecyclePos = i + (jPosX + vars.picOSize - (int) v.getX()) / vars.picOSize;
        vars.jigTables[nowC][nowR].outRecycle = false;
        if (jigRecyclePos < vars.activeRecyclerJigs.size()-1) {
            vars.activeRecyclerJigs.add(jigRecyclePos, nowC * 10000 + nowR);
            jigRecycleAdapter.notifyItemInserted(jigRecyclePos);
        } else {
            vars.activeRecyclerJigs.add(nowC * 10000 + nowR);
            jigRecycleAdapter.notifyItemInserted(vars.activeRecyclerJigs.size()-1);
        }
        doNotUpdate = false;
    }

    public boolean wannaBack2Recycler(int moveY) {

        // if sole piece then can go back to recycler
        if (fpNow.anchorId == 0 && moveY > (screenBottom - vars.picHSize) &&
                vars.fps.size() > 0) {
            fpNow = null;
            dragX = -1;
            return true;
        }
        return false;
    }
}