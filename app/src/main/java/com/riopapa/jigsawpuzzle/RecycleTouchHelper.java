package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.MainActivity.jPosX;
import static com.riopapa.jigsawpuzzle.MainActivity.jPosY;
import static com.riopapa.jigsawpuzzle.MainActivity.jigRecycleAdapter;
import static com.riopapa.jigsawpuzzle.MainActivity.jigRecyclePos;
import static com.riopapa.jigsawpuzzle.MainActivity.jigCR;
import static com.riopapa.jigsawpuzzle.MainActivity.jigTables;
import static com.riopapa.jigsawpuzzle.MainActivity.jigC;
import static com.riopapa.jigsawpuzzle.MainActivity.jigR;
import static com.riopapa.jigsawpuzzle.MainActivity.mActivity;
import static com.riopapa.jigsawpuzzle.MainActivity.picHSize;
import static com.riopapa.jigsawpuzzle.MainActivity.picISize;
import static com.riopapa.jigsawpuzzle.MainActivity.picOSize;
import static com.riopapa.jigsawpuzzle.MainActivity.piece;
import static com.riopapa.jigsawpuzzle.MainActivity.recySize;
import static com.riopapa.jigsawpuzzle.MainActivity.recyclerJigs;
import static com.riopapa.jigsawpuzzle.MainActivity.screenY;
import static com.riopapa.jigsawpuzzle.PaintView.inViewC;
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

public class RecycleTouchHelper extends ItemTouchHelper.Callback {

    private final ZItemTouchHelperAdapter mAdapter;
    private final Context mContext;

    Paint mClearPaint;
    ColorDrawable mBackground;
    int backgroundColor;
    public RecycleTouchHelper(ZItemTouchHelperAdapter adapter, Context context) {
        mAdapter = adapter;
        mContext = context;

    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        Log.w("r24 recycle clearView","clearView called");
        viewHolder.itemView.setBackgroundColor(0x0000000);
    }

    long helperDrawTime;

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if(actionState == ItemTouchHelper.ACTION_STATE_DRAG){
            jigRecyclePos = viewHolder.getAbsoluteAdapterPosition();
            jigCR = recyclerJigs.get(jigRecyclePos);
            jigC = jigCR /10000;
            jigR = jigCR - jigC * 10000;
            Bitmap bm = piece.makeBig(jigTables[jigC][jigR].oLine);
            Drawable d = new BitmapDrawable(mContext.getResources(), bm);
//            viewHolder.itemView.setBackground(d);
//            viewHolder.itemView.setScaleX(1.2f);
//            viewHolder.itemView.setScaleX(1.2f);
//            Log.w("x12 onSelectedChanged", "drawed "+ jigCR);
            //            viewHolder.itemView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.item_selected));
        }
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemSwiped(viewHolder.getBindingAdapterPosition());
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder.getBindingAdapterPosition(), target.getBindingAdapterPosition());
        return true;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        // -- canvas width = screenX, height = recycler height
        long nowTime = System.currentTimeMillis();
        if (nowTime < helperDrawTime + 1000)
            return;
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        helperDrawTime = nowTime;
        View pieceView = viewHolder.itemView;
        Log.w("onChildDraw","pieceView w="+pieceView.getWidth()+" h="+pieceView.getHeight());

        jigCR = recyclerJigs.get(jigRecyclePos);
        jigC = jigCR / 10000;
        jigR = jigCR - jigC * 10000;

        c.drawBitmap(jigTables[jigC][jigR].oLine2, 0, 0, null);

        TextView tvLeft = mActivity.findViewById(R.id.debug_left);
        mActivity.runOnUiThread(() -> tvLeft.setText("recyclex "+dX+" x "+dY));

//        mBackground = new ColorDrawable();
//        backgroundColor = Color.parseColor("#b80f0a");
//        mClearPaint = new Paint();
//        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        boolean isCancelled = dX == 0 && !isCurrentlyActive;

        if (isCancelled) {
            clearCanvas(c, pieceView.getRight() + dX, (float) pieceView.getTop(), (float) pieceView.getRight(), (float) pieceView.getBottom());
//            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            return;
        }

//        mBackground.setColor(mContext.getColor(R.color.purple_500));
//        mBackground.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
//        mBackground.draw(c);
        Log.w("r18 Backgond", "Left "+pieceView.getLeft()+" Right "+pieceView.getRight());
        Log.w("r20 onChildDrawBack" , " dx="+dX+" dy="+dY+
                " idx ="+ jigCR);
        if (dY < - picISize/4) {    // if moves up into main plate
            move2PaintView(dY, pieceView);

        }
//        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private static void move2PaintView(float dY, View itemView) {
        Log.w("r22 remove", "jig removing "+ jigC+"x"+jigR);
        jPosY = screenY - recySize + dY - picISize;
        jPosX = itemView.getLeft() + picISize / 2;

        jigTables[jigC][jigR].outRecycle = true;
        jigTables[jigC][jigR].posX = (int) jPosX - picHSize;
        jigTables[jigC][jigR].posY = (int) jPosY - picOSize;

        inViewC.add(jigC); inViewR.add(jigR);

        recyclerJigs.remove(jigRecyclePos);
        jigRecycleAdapter.notifyItemRemoved(jigRecyclePos);
        updateViewHandler.sendEmptyMessage(0);
    }

    private void clearCanvas(Canvas c, Float left, Float top, Float right, Float bottom) {
//        c.drawRect(left, top, right, bottom, mClearPaint);
        Log.w("recycleTouchHelper","clearCanvas L"+left+" R"+right+" T"+top+" B"+bottom);
    }
}