package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigBright;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigPic;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigRecycleAdapter;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.pieceImage;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.tvLeft;
import static com.riopapa.jigsawpuzzle.ActivityMain.mActivity;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenY;
import static com.riopapa.jigsawpuzzle.ActivityMain.vars;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;

public class PaintViewTouchCallback extends ItemTouchHelper.Callback {

    private final ZItemTouchHelperListener listener;
    private final Paint nullPaint = new Paint();

    public PaintViewTouchCallback(ZItemTouchHelperListener listener, Context context) {
        this.listener = listener;
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
            vars.jigRecyclePos = viewHolder.getAbsoluteAdapterPosition();
            Log.w("onS sta", "size=" + vars.activeRecyclerJigs.size()+" nowPos="+vars.jigRecyclePos);
            vars.jigCR = vars.activeRecyclerJigs.get(vars.jigRecyclePos);
            vars.nowC = vars.jigCR /10000;
            vars.nowR = vars.jigCR - vars.nowC * 10000;
            if (jigBright[vars.nowC][vars.nowR] == null)
                jigBright[vars.nowC][vars.nowR] = pieceImage.makeBright(jigPic[vars.nowC][vars.nowR]);

            vars.jPosY = screenY - vars.recSize;
            vars.jPosX = viewHolder.itemView.getLeft();
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
        if (vars.jigRecyclePos == vars.activeRecyclerJigs.size()) {
//            Log.e("r tag","activeSize="+vars.activeRecyclerJigs.size()+" vars.jigRecyclePos="+vars.jigRecyclePos);
//            for (int i = 0; i < vars.activeRecyclerJigs.size(); i++)
//                Log.w("active "+i, "pos "+vars.activeRecyclerJigs.get(i));
            return;
        }
        View pieceView = viewHolder.itemView;
        vars.jigCR = vars.activeRecyclerJigs.get(vars.jigRecyclePos);
        vars.nowC = vars.jigCR / 10000;
        vars.nowR = vars.jigCR - vars.nowC * 10000;
        if (dX != 0 && dY != 0) {
            vars.jPosX = pieceView.getLeft() + (int) dX;
            vars.jPosY = screenY - vars.recSize - vars.picISize + (int) dY;
            vars.jigTables[vars.nowC][vars.nowR].posX = vars.jPosX;
            vars.jigTables[vars.nowC][vars.nowR].posY = vars.jPosY;
        }

        String txt = "dxDy "+dX+" x "+dY
                + "\n vars.jPos "+vars.jPosX+" x "+vars.jPosY;
        mActivity.runOnUiThread(() -> tvLeft.setText(txt));
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