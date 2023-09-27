package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.MainActivity.jPosX;
import static com.riopapa.jigsawpuzzle.MainActivity.jPosY;
import static com.riopapa.jigsawpuzzle.MainActivity.jigRecyclePos;
import static com.riopapa.jigsawpuzzle.MainActivity.jigX00Y;
import static com.riopapa.jigsawpuzzle.MainActivity.jigTables;
import static com.riopapa.jigsawpuzzle.MainActivity.jigX;
import static com.riopapa.jigsawpuzzle.MainActivity.jigY;
import static com.riopapa.jigsawpuzzle.MainActivity.mActivity;
import static com.riopapa.jigsawpuzzle.MainActivity.picOSize;
import static com.riopapa.jigsawpuzzle.MainActivity.piece;
import static com.riopapa.jigsawpuzzle.MainActivity.recySize;
import static com.riopapa.jigsawpuzzle.MainActivity.recyclerJigs;
import static com.riopapa.jigsawpuzzle.MainActivity.screenY;
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
        Log.w("RecycleTouchHelper", "got mAdapter");
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
        viewHolder.itemView.setBackgroundColor(0x0000000);
    }

    long helperDrawTime;
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if(actionState == ItemTouchHelper.ACTION_STATE_DRAG){
            jigRecyclePos = viewHolder.getAbsoluteAdapterPosition();
            jigX00Y = recyclerJigs.get(jigRecyclePos);
            jigX = jigX00Y /10000;
            jigY = jigX00Y - jigX * 10000;
            Bitmap bm = piece.makeBig(jigTables[jigX][jigY].oLine2);
            Drawable d = new BitmapDrawable(mContext.getResources(), bm);
            viewHolder.itemView.setBackground(d);
            Log.w("onSelectedChanged", "drawed "+ jigX00Y);
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
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        long nowTime = System.currentTimeMillis();
        if (nowTime < helperDrawTime + 400)
            return;
        helperDrawTime = nowTime;

        View itemView = viewHolder.itemView;
        jigX00Y = recyclerJigs.get(viewHolder.getAbsoluteAdapterPosition());
        jigX = jigX00Y / 10000;
        jigY = jigX00Y - jigX * 10000;
        c.drawBitmap(piece.makeBig(jigTables[jigX][jigY].oLine), 0, 0, null);

        TextView tvLeft = mActivity.findViewById(R.id.debug_left);
        mActivity.runOnUiThread(() -> tvLeft.setText("recyclex "+dX+" x "+dY));

//        mBackground = new ColorDrawable();
//        backgroundColor = Color.parseColor("#b80f0a");
//        mClearPaint = new Paint();
//        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        boolean isCancelled = dX == 0 && !isCurrentlyActive;

        if (isCancelled) {
            clearCanvas(c, itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            return;
        }

//        mBackground.setColor(mContext.getColor(R.color.purple_500));
//        mBackground.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
//        mBackground.draw(c);
        Log.w("Backgond", "Left "+itemView.getLeft()+" Right "+itemView.getRight()+" , delta ");
        Log.w("onChildDrawBack" , " dx="+dX+" dy="+dY+
                " idx ="+ jigX00Y);
        if (dY < -50) {
            jPosY = screenY - recySize - picOSize;
            jPosX = itemView.getLeft() + picOSize /2;
            updateViewHandler.sendEmptyMessage(0);
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private void clearCanvas(Canvas c, Float left, Float top, Float right, Float bottom) {
//        c.drawRect(left, top, right, bottom, mClearPaint);

    }
}