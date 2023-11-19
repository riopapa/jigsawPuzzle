package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.doNotUpdate;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.dragX;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.dragY;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigRecycleAdapter;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigRecyclePos;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowC;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowCR;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowR;
import static com.riopapa.jigsawpuzzle.ActivityMain.ANI_TO_FPS;
import static com.riopapa.jigsawpuzzle.ActivityMain.mActivity;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenBottom;
import static com.riopapa.jigsawpuzzle.ActivityMain.vars;
import static com.riopapa.jigsawpuzzle.PaintView.nowFp;
import static com.riopapa.jigsawpuzzle.PaintView.nowIdx;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.riopapa.jigsawpuzzle.databinding.ActivityJigsawBinding;
import com.riopapa.jigsawpuzzle.func.NearPieceBind;
import com.riopapa.jigsawpuzzle.model.FloatPiece;

import java.util.Collections;

public class JigRecycleCallback extends ItemTouchHelper.Callback {

    private final ZItemTouchHelperListener listener;
    private final Paint nullPaint = new Paint();

    private final ActivityJigsawBinding binding;

    private final NearPieceBind nearPieceBind;

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
        viewHolder.itemView.setBackgroundColor(0x000FFFF);

//        int pos = viewHolder.getAbsoluteAdapterPosition();
//        if (pos < 0)
//            return;
//        Log.w("pc clearView","pos "+pos);
//        int cr = vars.activeRecyclerJigs.get(pos);
//        int c = cr /10000;
//        int r = cr - c * 10000;

    }

    RecyclerView.ViewHolder svViewHolder;
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if(actionState == ItemTouchHelper.ACTION_STATE_DRAG){

            nowDragging = true;
            // Piece is selected and begun dragging
            svViewHolder = viewHolder;
            recyclerSelected(viewHolder);

        } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
            // Piece dragging is finished
            // if yposition is above recycler then move to fps
            if (dragY < screenBottom - vars.picHSize) {
//                dragX = dragX; dragX = -1; // no more dragging piece drawing
//                dragY = dragY; dragY = -1;
                Log.w("idle ", "ACTION_STATE_IDLE =" + dragX + " x " + dragY);
                doNotUpdate = true;
                vars.debugMode = true;
                removeFromRecycle(svViewHolder);
                add2FloatingPiece();
                doNotUpdate = false;
                nearPieceBind.check(dragX, dragY);
//                vars.debugMode = false;
                nowDragging = false;
            }
        }

    }

    private void recyclerSelected(RecyclerView.ViewHolder viewHolder) {
        nowFp = null;
        jigRecyclePos = viewHolder.getAbsoluteAdapterPosition();
        nowCR = vars.activeRecyclerJigs.get(jigRecyclePos);
        nowC = nowCR / 10000;
        nowR = nowCR - nowC * 10000;
        dragY = screenBottom;
        dragX = viewHolder.itemView.getLeft();
        Log.w("selected "+jigRecyclePos, " CR"+nowCR+ " drag x="+dragX+ " y="+dragY);
    }
    private void add2FloatingPiece() {

//        vars.jigTables[nowC][nowR].posX = dragX;
//        vars.jigTables[nowC][nowR].posY = dragY; // - vars.picOSize;

        nowFp = new FloatPiece();
        nowFp.C = nowC;
        nowFp.R = nowR;
        nowFp.posX = dragX;
        nowFp.posY = dragY;
        nowFp.count = 2;
        nowFp.mode = ANI_TO_FPS;
        nowFp.uId = System.currentTimeMillis();    // set Unique uId
        nowFp.anchorId = 0;       // let anchorId to itself
        vars.fps.add(nowFp);
        nowIdx = vars.fps.size() - 1;
//        Log.w("FPS xy"," info size="+vars.fps.size());
//        for (int i = 0; i < vars.fps.size(); i++) {
//            Log.w(vars.picGap+" gap "+i, (vars.fps.get(i).posX-dragX)
//                    + " x "+ (vars.fps.get(i).posY-dragY));
//        }
    }

    private void removeFromRecycle(RecyclerView.ViewHolder viewHolder) {
        viewHolder.itemView.setAlpha(0);  // let this piece GONE first
        vars.activeRecyclerJigs.remove(jigRecyclePos);
        jigRecycleAdapter.notifyItemRemoved(jigRecyclePos);
        vars.jigTables[nowC][nowR].outRecycle = true;
        Log.w("r2m move R"+nowCR,"removed from recycler jPos="+dragX+"x"+dragY);
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
            if (nowFp != null) {
                nowFp.posX = dragX;
                nowFp.posY = dragY;
            }
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