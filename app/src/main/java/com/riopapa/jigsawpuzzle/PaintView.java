package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.doNotUpdate;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.dragX;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.activeAdapter;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.activePos;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigRecyclerView;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowC;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowR;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenBottom;
import static com.riopapa.jigsawpuzzle.ActivityMain.gVal;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.riopapa.jigsawpuzzle.func.AnchorPiece;
import com.riopapa.jigsawpuzzle.func.NearByFloatPiece;
import com.riopapa.jigsawpuzzle.func.PiecePosition;
import com.riopapa.jigsawpuzzle.func.NearPieceBind;
import com.riopapa.jigsawpuzzle.func.PieceSelection;
import com.riopapa.jigsawpuzzle.model.FloatPiece;

public class PaintView extends View {

    public static int nowIdx;
    public Activity paintActivity;
    public static PiecePosition piecePosition;
    public static NearByFloatPiece nearByFloatPiece;
    PieceDraw pieceDraw;
    AnchorPiece anchorPiece;
    NearPieceBind nearPieceBind;
    PieceSelection pieceSelection;

    public static FloatPiece nowFp;

    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Activity activity){
        this.paintActivity = activity;
        nowFp = null;
        piecePosition = new PiecePosition(activity);
        pieceDraw = new PieceDraw();
        anchorPiece = new AnchorPiece();
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
        gVal.allLocked = isPiecesAllLocked();
    }

    boolean isPiecesAllLocked() {
        if (gVal.activeJigs.size() > 0 || gVal.fps.size() > 0) {
            return false;
        }
        for (int c = 0; c < gVal.colNbr; c++) {
            for (int r = 0; r < gVal.rowNbr; r++) {
                if (!gVal.jigTables[c][r].locked)
                    return false;
            }
        }
        return true;
    }
    long nextOKTime = 0, nowTime;
    static int xOld, yOld;
    public boolean onTouchEvent(MotionEvent event) {
        if (doNotUpdate)
            return false;
        nowTime = System.currentTimeMillis();
        if (nextOKTime > nowTime)
            return true;
        nextOKTime = nowTime + 100;

        int x = (int) event.getX() - gVal.picHSize;
        int y = (int) event.getY() - gVal.picHSize;

//        Log.w("px on", x+"x"+y);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                pieceSelection.check(x, y);
                break;

            case MotionEvent.ACTION_MOVE:

                final float MOVING = 30;
                if ((Math.abs(x - xOld) > MOVING || Math.abs(y - yOld) > MOVING) &&
                    nowFp != null) {
                    xOld = x; yOld = y;
                    if (wannaBack2Recycler(y)) {
                        doNotUpdate = true;
                        Log.w("pchk Check", "GVal.fps size=" + gVal.fps.size() + " fPIdx=" + nowIdx + " now CR " + nowC + "x" + nowR);
                        goBack2Recycler();
                        gVal.fps.remove(nowIdx);
                        nowFp = null;
                        doNotUpdate = false;
                        dragX = -1;
                    } else if (y < screenBottom) {
                        nowFp.posX = x;
                        nowFp.posY = y;
                        anchorPiece.move();
                        nearPieceBind.check();
                    } else {
                        y -= gVal.picOSize;
                        nowFp.posX = x;
                        nowFp.posY = y;
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
        activePos = i + (dragX - (int) v.getX()) / gVal.picOSize;
        gVal.jigTables[nowC][nowR].outRecycle = false;
        if (activePos < gVal.activeJigs.size()-1) {
            gVal.activeJigs.add(activePos, nowC * 10000 + nowR);
            activeAdapter.notifyItemInserted(activePos);
        } else {
            gVal.activeJigs.add(nowC * 10000 + nowR);
            activeAdapter.notifyItemInserted(gVal.activeJigs.size()-1);
        }
    }

    public boolean wannaBack2Recycler(int moveY) {

        // if sole piece then can go back to recycler
        if (nowFp.anchorId == 0 && moveY > screenBottom - gVal.picHSize && gVal.fps.size() > 0) {
            nowFp = null;
            return true;
        }
        return false;
    }
}