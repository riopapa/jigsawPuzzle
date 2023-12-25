package biz.riopapa.jigsawpuzzle;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.activeAdapter;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.activeJigs;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.activePos;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.congCount;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.dragX;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigRecyclerView;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.nowC;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.nowR;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenBottom;

import android.annotation.SuppressLint;
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
import biz.riopapa.jigsawpuzzle.func.NearPieceBind;
import biz.riopapa.jigsawpuzzle.func.PiecePosition;
import biz.riopapa.jigsawpuzzle.func.PieceSelection;
import biz.riopapa.jigsawpuzzle.images.PieceImage;
import biz.riopapa.jigsawpuzzle.model.FloatPiece;

public class ForeView extends View {

    public static int nowIdx;
    public static NearByFloatPiece nearByFloatPiece;
    public static PiecePosition piecePosition;
    ForeDraw foreDraw;
    AnchorPiece anchorPiece;
    NearPieceBind nearPieceBind;
    PieceSelection pieceSelection;
    PieceImage pieceImage;

    public static FloatPiece nowFp;
    public static boolean foreBlink, backBlink;

    public ForeView(Context context) {
        this(context, null);
    }

    public ForeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    ActivityJigsawBinding binding;

    public void init(ActivityJigsawBinding binding, PieceImage pieceImage){
        this.binding = binding;
        this.pieceImage = pieceImage;
        nowFp = null;
        piecePosition = new PiecePosition();
        anchorPiece = new AnchorPiece();
        nearPieceBind = new NearPieceBind();
        nearByFloatPiece = new NearByFloatPiece();
        pieceSelection = new PieceSelection();
        foreDraw = new ForeDraw(binding, pieceImage);
    }

    protected void onDraw(@NonNull Canvas fCanvas){

        foreBlink = false;
        foreDraw.draw(fCanvas);
        if (congCount > 0) {
            foreBlink = true;
        }
    }

    private void paintTouchUp(){
//        gVal.allLocked = isPiecesAllLocked();
    }

    long nextOKTime = 0, nowTime;
    static int xOld, yOld;
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        nowTime = System.currentTimeMillis();
        if (nextOKTime > nowTime)
            return true;
        nextOKTime = nowTime + 50;

        int x = (int) event.getX() - gVal.picHSize;
        int y = (int) event.getY() - gVal.picHSize;

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
                        goBack2Recycler();
                        gVal.fps.remove(nowIdx);
                        nowFp = null;
                        dragX = -1;
                    } else if (y < screenBottom) {
                        nowFp.posX = x;
                        nowFp.posY = y;
                        Log.w("nowFp", nowFp.C+" x "+nowFp.R+" "+gVal.jigTables[nowFp.C][nowFp.R].locked);
                        anchorPiece.move();
                        nearPieceBind.check(pieceImage);
                    } else {
                        y -= gVal.picOSize;
                        nowFp.posX = x;
                        nowFp.posY = y;
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                paintTouchUp();
                performClick();
                break;
        }

        return true;
    }

    public void goBack2Recycler() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) jigRecyclerView.getLayoutManager();
        assert layoutManager != null;
        int i = layoutManager.findFirstVisibleItemPosition();
        int xPos = 0;
        View v = layoutManager.findViewByPosition(i);
        if (v != null)
            xPos = (int) v.getX();
        activePos = i + (dragX - xPos) / gVal.picOSize;
        gVal.jigTables[nowC][nowR].outRecycle = false;
        if (activePos < activeJigs.size()-1) {
            activeJigs.add(activePos, nowC * 10000 + nowR);
            activeAdapter.notifyItemInserted(activePos);
        } else {
            activeJigs.add(nowC * 10000 + nowR);
            activeAdapter.notifyItemInserted(activeJigs.size()-1);
        }
    }

    public boolean wannaBack2Recycler(int moveY) {

        // if sole piece then can go back to recycler
        return nowFp.anchorId == 0 && moveY > screenBottom - gVal.picHSize && gVal.fps.size() > 0;
    }
}