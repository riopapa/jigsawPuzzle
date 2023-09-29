package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.MainActivity.jPosX;
import static com.riopapa.jigsawpuzzle.MainActivity.jPosY;
import static com.riopapa.jigsawpuzzle.MainActivity.jigRecycleAdapter;
import static com.riopapa.jigsawpuzzle.MainActivity.jigRecyclePos;
import static com.riopapa.jigsawpuzzle.MainActivity.jigCR;
import static com.riopapa.jigsawpuzzle.MainActivity.jigTables;
import static com.riopapa.jigsawpuzzle.MainActivity.nowC;
import static com.riopapa.jigsawpuzzle.MainActivity.nowR;
import static com.riopapa.jigsawpuzzle.MainActivity.mActivity;
import static com.riopapa.jigsawpuzzle.MainActivity.picHSize;
import static com.riopapa.jigsawpuzzle.MainActivity.picISize;
import static com.riopapa.jigsawpuzzle.MainActivity.picOSize;
import static com.riopapa.jigsawpuzzle.MainActivity.piece;
import static com.riopapa.jigsawpuzzle.MainActivity.recySize;
import static com.riopapa.jigsawpuzzle.MainActivity.recyclerJigs;
import static com.riopapa.jigsawpuzzle.MainActivity.screenY;
import static com.riopapa.jigsawpuzzle.PaintView.inViewC;
import static com.riopapa.jigsawpuzzle.PaintView.inViewMap;
import static com.riopapa.jigsawpuzzle.PaintView.inViewR;
import static com.riopapa.jigsawpuzzle.PaintView.updateViewHandler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;

public class RecycleTouchHelper extends ItemTouchHelper.Callback {

    private final ZItemTouchHelperAdapter mAdapter;
    private final Context mContext;
    private boolean temp = false;

    Paint mClearPaint;
    ColorDrawable mBackground;
    int backgroundColor;
    public RecycleTouchHelper(ZItemTouchHelperAdapter adapter, Context context) {
        mAdapter = adapter;
        mContext = context;

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
//        Log.w("r24 recycle clearView","clearView called");
        viewHolder.itemView.setBackgroundColor(0x0000000);
    }

    long helperDrawTime;

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if(actionState == ItemTouchHelper.ACTION_STATE_DRAG){
            jigRecyclePos = viewHolder.getAbsoluteAdapterPosition();
            jigCR = recyclerJigs.get(jigRecyclePos);
            nowC = jigCR /10000;
            nowR = jigCR - nowC * 10000;
            Log.w("r44 onSelectedChanged", ""+jigCR);
            Bitmap bm = piece.makeBigger(jigTables[nowC][nowR].oLine);
            Drawable d = new BitmapDrawable(mContext.getResources(), bm);
            viewHolder.itemView.setBackground(d);
        }
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.END | ItemTouchHelper.START
        );
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemSwiped(viewHolder.getBindingAdapterPosition());
        Log.w("R28 onSwiped", "Swiped position "+viewHolder.getBindingAdapterPosition());
    }


    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int draggedItemIndex = viewHolder.getAbsoluteAdapterPosition();
        int targetIndex = target.getAbsoluteAdapterPosition();
        Log.w("R27 onMove","from "+draggedItemIndex+" to "+targetIndex);
        Collections.swap(recyclerJigs, draggedItemIndex, targetIndex);
        jigRecycleAdapter.notifyItemMoved(draggedItemIndex, targetIndex);
//        mAdapter.onItemMove(viewHolder.getBindingAdapterPosition(), target.getBindingAdapterPosition());
        return true;
//        Log.w("R27 onMove",  "viewHolder left"+viewHolder.itemView.getLeft()
//                +" tag="+viewHolder.itemView.getTag()+" id="+viewHolder.itemView.getId()
//                +"target tag="+target.itemView.getTag()+" id="+target.itemView.getId()
//        );
//        Log.w("R27 pos","view "+viewHolder.getBindingAdapterPosition()+" target "+target.getBindingAdapterPosition());
//        return true;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        // -- canvas width = screenX, height = recycler height
        long nowTime = System.currentTimeMillis();
        if (nowTime < helperDrawTime + 100)
            return;
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        helperDrawTime = nowTime;
        View pieceView = viewHolder.itemView;

        jigCR = recyclerJigs.get(jigRecyclePos);
        nowC = jigCR / 10000;
        nowR = jigCR - nowC * 10000;

//        c.drawBitmap(jigTables[nowC][nowR].oLine2, 0, 0, null);

        TextView tvLeft = mActivity.findViewById(R.id.debug_left);
        mActivity.runOnUiThread(() -> tvLeft.setText("recyclex "+dX+" x "+dY));

        boolean isCancelled = dX == 0 && !isCurrentlyActive;

        if (isCancelled) {
            clearCanvas(c, pieceView.getRight() + dX, (float) pieceView.getTop(), (float) pieceView.getRight(), (float) pieceView.getBottom());
            return;
        }
//        Log.w("r18 Backgond", "Left "+pieceView.getLeft()+" Right "+pieceView.getRight());
//        Log.w("r20 onChildDrawBack" , " dx="+dX+" dy="+dY+
//                " idx ="+ jigCR);
        if (dY < - picISize/3) {    // if moves up into main plate
            move2PaintView((int) dY, pieceView);
        }
//        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private static void move2PaintView(int dY, View itemView) {
        Log.w("r22 remove", "jig removing "+ nowC +"x"+ nowR);
        jPosY = screenY - recySize + dY - picOSize - picHSize;
        jPosX = itemView.getLeft();

        jigTables[nowC][nowR].outRecycle = true;
        jigTables[nowC][nowR].posX = jPosX;
        jigTables[nowC][nowR].posY = jPosY;

        inViewC.add(nowC); inViewR.add(nowR);
        inViewMap.add(jigTables[nowC][nowR].oLine);
//        inViewMap.add(Bitmap.createScaledBitmap(jigTables[nowC][nowR].oLine, picOSize, picOSize, true));

        recyclerJigs.remove(jigRecyclePos);
        jigRecycleAdapter.notifyItemRemoved(jigRecyclePos);
        updateViewHandler.sendEmptyMessage(0);
    }


    private void clearCanvas(Canvas c, Float left, Float top, Float right, Float bottom) {
//        c.drawRect(left, top, right, bottom, mClearPaint);
        Log.w("recycleTouchHelper","clearCanvas L"+left+" R"+right+" T"+top+" B"+bottom);
    }
}