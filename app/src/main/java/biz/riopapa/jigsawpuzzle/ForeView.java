package biz.riopapa.jigsawpuzzle;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.activeAdapter;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.activeJigs;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemPos;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.congCount;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemX;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigRecyclerView;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemC;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemR;
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
import biz.riopapa.jigsawpuzzle.func.PieceAlign;
import biz.riopapa.jigsawpuzzle.func.MoveThisOnTop;
import biz.riopapa.jigsawpuzzle.func.NearByFloatPiece;
import biz.riopapa.jigsawpuzzle.func.PieceBind;
import biz.riopapa.jigsawpuzzle.func.PieceLock;
import biz.riopapa.jigsawpuzzle.func.PiecePosition;
import biz.riopapa.jigsawpuzzle.func.PieceSelection;
import biz.riopapa.jigsawpuzzle.images.PieceImage;
import biz.riopapa.jigsawpuzzle.model.FloatPiece;

public class ForeView extends View {

    public static int topIdx = -1;
    public static NearByFloatPiece nearByFloatPiece;
    public static PiecePosition piecePosition;
    ForeDraw foreDraw;
    PieceAlign pieceAlign;
    PieceLock pieceLock;
    PieceBind pieceBind;
    PieceSelection pieceSelection;
    PieceImage pieceImage;

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
        piecePosition = new PiecePosition();
        pieceAlign = new PieceAlign();
        pieceLock = new PieceLock();
        pieceBind = new PieceBind();
        nearByFloatPiece = new NearByFloatPiece();
        pieceSelection = new PieceSelection();
        foreDraw = new ForeDraw(binding, pieceImage);
        topIdx = -1;
    }

    protected void onDraw(@NonNull Canvas fCanvas){

        foreBlink = false;
        foreDraw.draw(fCanvas);
        if (congCount > 0)
            foreBlink = true;
    }

//    private void paintTouchUp(){
////        gVal.allLocked = isPiecesAllLocked();
//    }

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
                if (topIdx == -1) {
//                    itemX = -1;
//                    itemY = -1;
                    Log.w("ACTION_DOWN", "none selected");
                } else {
                    if (topIdx < gVal.fps.size() - 1) {
                        new MoveThisOnTop(topIdx);
                        topIdx = gVal.fps.size() - 1;
                    }
                    FloatPiece fp = gVal.fps.get(topIdx);
                    Log.w("ACTION_DOWN", topIdx+" fp Selected "+fp.C+" "+fp.R);
                }

                break;

            case MotionEvent.ACTION_MOVE:

                final float MOVE_ALLOWANCE = 20;
                if ((Math.abs(x - xOld) > MOVE_ALLOWANCE ||
                        Math.abs(y - yOld) > MOVE_ALLOWANCE) &&
                        topIdx != -1) {
                    xOld = x; yOld = y;
                    Log.w("Action", "is idx="+topIdx + " Moving "+x+"x"+y+" fps Sz="+gVal.fps.size());
                    if (wannaBack2Recycler(y)) {
                        goBack2Recycler();
                        gVal.fps.remove(topIdx);
                        topIdx = -1;
                    } else if (y < screenBottom) {
                        gVal.fps.get(topIdx).posX = x;
                        gVal.fps.get(topIdx).posY = y;
                        Log.w("vstate", topIdx+"  "+ gVal.fps.get(topIdx).C+" x "+gVal.fps.get(topIdx).R
                            + " xy = "+x+"x"+y);
                        pieceAlign.move();
                        foreBlink = pieceLock.update(pieceImage);

//                        if (foreBlink)
//                            topIdx = gVal.fps.size() - 1;
                        foreBlink |= pieceBind.update();
                    } else if (gVal.fps.get(topIdx).anchorId == 0){
                        y -= gVal.picOSize;
                        gVal.fps.get(topIdx).posX = x;
                        gVal.fps.get(topIdx).posY = y;
                        foreBlink = true;
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.w("Action","UP ");
//                paintTouchUp();
//                performClick();
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
        Log.w("goback2", "xPos= "+xPos);
//        itemPos = i + (xOld - xPos) / gVal.picOSize;
        itemPos = i + xOld / gVal.picOSize + 1;
        gVal.jigTables[itemC][itemR].fp = false;
        if (itemPos < activeJigs.size()-1) {
            activeJigs.add(itemPos, 10000 + itemC * 100 + itemR);
            activeAdapter.notifyItemInserted(itemPos);
        } else {
            activeJigs.add(10000 + itemC * 100 + itemR);
            activeAdapter.notifyItemInserted(activeJigs.size()-1);
        }
        Log.w("goback2 "+itemPos, itemC+"x"+itemR);
    }

    public boolean wannaBack2Recycler(int moveY) {
        // if sole piece then can go back to recycler
//        Log.w("back2", "topidx="+topIdx+" fps sz="+gVal.fps.size());
        return gVal.fps.get(topIdx).anchorId == 0 && moveY > screenBottom - gVal.picHSize;
    }
}