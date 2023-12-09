package biz.riopapa.jigsawpuzzle;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.doNotUpdate;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.dragX;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.activeAdapter;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.activePos;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigRecyclerView;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.nowC;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.nowR;
import static biz.riopapa.jigsawpuzzle.ActivityMain.INVALIDATE_INTERVAL;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenBottom;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ActivityMain.showBack;
import static biz.riopapa.jigsawpuzzle.ActivityMain.showBackCount;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import biz.riopapa.jigsawpuzzle.databinding.ActivityJigsawBinding;
import biz.riopapa.jigsawpuzzle.func.AnchorPiece;
import biz.riopapa.jigsawpuzzle.func.NearByFloatPiece;
import biz.riopapa.jigsawpuzzle.func.PiecePosition;
import biz.riopapa.jigsawpuzzle.func.NearPieceBind;
import biz.riopapa.jigsawpuzzle.func.PieceSelection;
import biz.riopapa.jigsawpuzzle.model.FloatPiece;

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
    public static long invalidateTime;

    ActivityJigsawBinding binding;
    public void init(Activity activity, ActivityJigsawBinding binding){
        this.paintActivity = activity;
        this.binding = binding;
        nowFp = null;
        piecePosition = new PiecePosition(activity);
        pieceDraw = new PieceDraw(binding);
        anchorPiece = new AnchorPiece();
        nearPieceBind = new NearPieceBind();
        nearByFloatPiece = new NearByFloatPiece();
        pieceSelection = new PieceSelection();
        invalidateTime = System.currentTimeMillis() + INVALIDATE_INTERVAL;
        if (showBack == 0)
            showBackCount = 250 * 10;
    }

    protected void onDraw(@NonNull Canvas canvas){
        long nowTime = System.currentTimeMillis();
        if (nowTime < invalidateTime)
            return;
        invalidateTime = nowTime + INVALIDATE_INTERVAL / 2;
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
        nextOKTime = nowTime + 50;

        int x = (int) event.getX() - gVal.picHSize;
        int y = (int) event.getY() - gVal.picHSize;

//        Log.w("px on", x+"x"+y);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                pieceSelection.check(x, y);
                break;

            case MotionEvent.ACTION_MOVE:

                final float MOVE_ALLOWANCE = 20;
                if ((Math.abs(x - xOld) > MOVE_ALLOWANCE ||
                        Math.abs(y - yOld) > MOVE_ALLOWANCE) &&
                        nowFp != null) {
                    xOld = x; yOld = y;
                    if (wannaBack2Recycler(y)) {
//                        doNotUpdate = true;
                        goBack2Recycler();
                        gVal.fps.remove(nowIdx);
                        nowFp = null;
//                        doNotUpdate = false;
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
                performClick();
                break;
        }

        return true;
    }

    @Override
    public boolean performClick() {
        Log.w("perform","Click");
        return super.performClick();
    }

    public void goBack2Recycler() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) jigRecyclerView.getLayoutManager();
        assert layoutManager != null;
        int i = layoutManager.findFirstVisibleItemPosition();
        View v = layoutManager.findViewByPosition(i);
        assert v != null;
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