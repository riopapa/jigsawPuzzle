package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.doNotUpdate;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.dragX;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.dragY;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jPosX;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jPosY;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowCR;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigRecycleAdapter;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigRecyclePos;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowC;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowR;
import static com.riopapa.jigsawpuzzle.ActivityMain.ANI_TO_PAINT;
import static com.riopapa.jigsawpuzzle.ActivityMain.mActivity;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenBottom;
import static com.riopapa.jigsawpuzzle.ActivityMain.vars;
import static com.riopapa.jigsawpuzzle.PaintView.fpNow;
import static com.riopapa.jigsawpuzzle.PaintView.nowIdx;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.riopapa.jigsawpuzzle.databinding.ActivityJigsawBinding;
import com.riopapa.jigsawpuzzle.func.NearByFloatPiece;
import com.riopapa.jigsawpuzzle.func.NearPieceBind;
import com.riopapa.jigsawpuzzle.model.FloatPiece;

import java.util.Collections;

public class JigRecycleCallback extends ItemTouchHelper.Callback {

    private final ZItemTouchHelperListener listener;
    private final Paint nullPaint = new Paint();

    private final ActivityJigsawBinding binding;

    private NearPieceBind nearPieceBind;

    public static boolean nowDragging;

    public JigRecycleCallback(ZItemTouchHelperListener listener, ActivityJigsawBinding binding) {
        this.listener = listener;
        this.binding = binding;
        nearPieceBind = new NearPieceBind();
    }


    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {return true; }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
//        int pos = viewHolder.getAbsoluteAdapterPosition();
//        if (pos < 0)
//            return;
//        Log.w("pc clearView","pos "+pos);
//        int cr = vars.activeRecyclerJigs.get(pos);
//        int c = cr /10000;
//        int r = cr - c * 10000;

//        viewHolder.itemView.setBackgroundColor(0x000FFFF);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if(actionState == ItemTouchHelper.ACTION_STATE_DRAG){

            nowDragging = true;
            // Piece is selected and begun dragging
            recyclerSelected(viewHolder);

        } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
            // Piece dragging is finished
            // if yposition is above recycler then move to fps
            nowDragging = false;
            if (dragY < screenBottom - vars.picHSize) {
                jPosX = dragX; dragX = -1; // no more dragging piece drawing
                jPosY = dragY; dragY = -1;
                Log.w("idle ", "ACTION_STATE_IDLE =" + jPosX + " x " + jPosY);
                doNotUpdate = true;
                vars.debugMode = true;
                removeFromRecycle();
                add2FloatingPiece();
                doNotUpdate = false;
                nearPieceBind.check(jPosX, jPosY);
//                vars.debugMode = false;
            } else {
                dragX = -1;
            }
        }

    }

    private static void recyclerSelected(RecyclerView.ViewHolder viewHolder) {
        fpNow = null;
        jigRecyclePos = viewHolder.getAbsoluteAdapterPosition();
        nowCR = vars.activeRecyclerJigs.get(jigRecyclePos);
        nowC = nowCR / 10000;
        nowR = nowCR - nowC * 10000;
        dragY = screenBottom + vars.picHSize;
        dragX = viewHolder.itemView.getLeft() + vars.picGap;
        Log.w("selected", " CR"+nowCR+ " drag x="+dragX+ " y="+dragY+ " nowPos="+jigRecyclePos
        + " H="+vars.picHSize+" O="+vars.picOSize);
    }
    private void add2FloatingPiece() {

        vars.jigTables[nowC][nowR].posX = jPosX;
        vars.jigTables[nowC][nowR].posY = jPosY; // - vars.picOSize;

        fpNow = new FloatPiece();
        fpNow.C = nowC;
        fpNow.R = nowR;
        fpNow.count = 3;
        fpNow.mode = ANI_TO_PAINT;
        fpNow.uId = System.currentTimeMillis();    // set Unique uId
        fpNow.anchorId = 0;       // let anchorId to itself
        vars.fps.add(fpNow);
        nowIdx = vars.fps.size() - 1;
        Log.w("FPS xy"," info size="+vars.fps.size());
        for (int i = 0; i < vars.fps.size(); i++) {
            Log.w(vars.picGap+" gap "+i, (vars.jigTables[vars.fps.get(i).C][vars.fps.get(i).R].posX-jPosX)
                    + " x "+ (vars.jigTables[vars.fps.get(i).C][vars.fps.get(i).R].posY-jPosY));
        }
    }

    private void removeFromRecycle() {
        Log.w("r2moveCR"+nowCR,"removing from recycler jPos="+jPosX+"x"+jPosY);
        if (jigRecyclePos < vars.activeRecyclerJigs.size()) {
            vars.jigTables[nowC][nowR].outRecycle = true;
            vars.activeRecyclerJigs.remove(jigRecyclePos);
            jigRecycleAdapter.notifyItemRemoved(jigRecyclePos);
            Log.w("r2m move R"+nowCR,"removed from recycler jPos="+jPosX+"x"+jPosY);
        }
    }


    @Override
    public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN
                        | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
                ,
                ItemTouchHelper.END | ItemTouchHelper.START
                        | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        );
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Log.w("p28 onSwiped", "Swiped rightPosition "+viewHolder.getBindingAdapterPosition());
        listener.onItemSwiped(viewHolder.getBindingAdapterPosition());
    }


    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int holderIdx = viewHolder.getAbsoluteAdapterPosition();
        int tgtIdx = target.getAbsoluteAdapterPosition();
        Log.w("p27 onMove","from "+holderIdx+" to "+tgtIdx);
        Collections.swap(vars.activeRecyclerJigs, holderIdx, tgtIdx);
        jigRecycleAdapter.notifyItemMoved(holderIdx, tgtIdx);
//        listener.onItemMove(viewHolder.getBindingAdapterPosition(), target.getBindingAdapterPosition());
        return true;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        // -- canvas width = screenX, height = recycler height
//        long nowTime = System.currentTimeMillis();
//        if (nowTime < helperDrawTime + 50)
//            return;
//
//        helperDrawTime = nowTime;

        if (vars.activeRecyclerJigs.size() == 0)
            return;
        if (jigRecyclePos == vars.activeRecyclerJigs.size()) {
//            Log.e("r tag","activeSize="+vars.activeRecyclerJigs.size()+" vars.jigRecyclePos="+vars.jigRecyclePos);
//            for (int i = 0; i < vars.activeRecyclerJigs.size(); i++)
//                Log.w("active "+i, "pos "+vars.activeRecyclerJigs.get(i));
            return;
        }
        View pieceView = viewHolder.itemView;
//        nowCR = vars.activeRecyclerJigs.get(jigRecyclePos);
//        nowC = nowCR / 10000;
//        nowR = nowCR - nowC * 10000;
        if (dX != 0 && dY != 0 && nowDragging) {
            dragX = (int) pieceView.getX();
            dragY = screenBottom + (int) pieceView.getY();
            vars.jigTables[nowC][nowR].posX = dragX;
            vars.jigTables[nowC][nowR].posY = dragY;
            String txt = "dxDy "+dX+" x "+dY
                    + "\n vars.jPos "+dragX+" x "+dragY;
            mActivity.runOnUiThread(() -> binding.debugLeft.setText(txt));
        }

//        boolean isCancelled = dX == 0 && !isCurrentlyActive;
//

//        if (!isCurrentlyActive) {
//            clearCanvas(c, pieceView.getRight() + dX, (float) pieceView.getTop(), (float) pieceView.getRight(), (float) pieceView.getBottom());
//            return;
//        }

    }


    private void clearCanvas(Canvas c, Float left, Float top, Float right, Float bottom) {
        c.drawRect(left, top, right, bottom, nullPaint);
        Log.w("p recycleTouchHelper","clearCanvas L"+left+" R"+right+" T"+top+" B"+bottom);
    }
}