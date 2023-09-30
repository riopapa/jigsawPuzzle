package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.MainActivity.iv1;
import static com.riopapa.jigsawpuzzle.MainActivity.jPosX;
import static com.riopapa.jigsawpuzzle.MainActivity.jPosY;
import static com.riopapa.jigsawpuzzle.MainActivity.jigCR;
import static com.riopapa.jigsawpuzzle.MainActivity.jigRecycleAdapter;
import static com.riopapa.jigsawpuzzle.MainActivity.jigRecyclePos;
import static com.riopapa.jigsawpuzzle.MainActivity.jigTables;
import static com.riopapa.jigsawpuzzle.MainActivity.mActivity;
import static com.riopapa.jigsawpuzzle.MainActivity.nowC;
import static com.riopapa.jigsawpuzzle.MainActivity.nowR;
import static com.riopapa.jigsawpuzzle.MainActivity.paintView;
import static com.riopapa.jigsawpuzzle.MainActivity.picISize;
import static com.riopapa.jigsawpuzzle.MainActivity.picOSize;
import static com.riopapa.jigsawpuzzle.MainActivity.piece;
import static com.riopapa.jigsawpuzzle.MainActivity.recySize;
import static com.riopapa.jigsawpuzzle.MainActivity.recyclerJigs;
import static com.riopapa.jigsawpuzzle.MainActivity.screenY;
import static com.riopapa.jigsawpuzzle.PaintView.fPs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.riopapa.jigsawpuzzle.model.FloatPiece;

import java.util.Collections;

public class RecycleTouchHelper extends ItemTouchHelper.Callback {

    private final ZItemTouchHelperAdapter mAdapter;
    private final Context mContext;


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
        int pos = viewHolder.getAbsoluteAdapterPosition();
        Log.w("clearView","pos "+pos);
        if (pos < 0)
            return;
        int cr = recyclerJigs.get(pos);
        int c = cr /10000;
        int r = cr - c * 10000;

        Log.w("r24 recycle clearView","clearView called "+cr+" "+c+"x"+r);
//        Drawable d = new BitmapDrawable(mContext.getResources(),
//                jigTables[c][r].oLine);
//        viewHolder.itemView.setForeground(d);
        viewHolder.itemView.setBackgroundColor(0x000FFFF);
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
            if (jigTables[nowC][nowR].picSel == null)
                jigTables[nowC][nowR].picSel = piece.makeBright(jigTables[nowC][nowR].pic);
            Drawable d = new BitmapDrawable(mContext.getResources(),
                    jigTables[nowC][nowR].picSel);
            viewHolder.itemView.setBackground(d);
            iv1.setImageBitmap(jigTables[nowC][nowR].picSel);
//            viewHolder.itemView.setForeground(d);
            Log.w("r29 onSelected "+jigRecyclePos,jigCR+" selected");
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
        int holderIdx = viewHolder.getAbsoluteAdapterPosition();
        int tgtIdx = target.getAbsoluteAdapterPosition();
        Log.w("R27 onMove","from "+holderIdx+" to "+tgtIdx);
        Collections.swap(recyclerJigs, holderIdx, tgtIdx);
        jigRecycleAdapter.notifyItemMoved(holderIdx, tgtIdx);
//        mAdapter.onItemMove(viewHolder.getBindingAdapterPosition(), target.getBindingAdapterPosition());
        return true;
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
        mActivity.runOnUiThread(() -> tvLeft.setText("recycle "+dX+" x "+dY));

//        boolean isCancelled = dX == 0 && !isCurrentlyActive;
//
//        if (isCancelled) {
//            clearCanvas(c, pieceView.getRight() + dX, (float) pieceView.getTop(), (float) pieceView.getRight(), (float) pieceView.getBottom());
//            return;
//        }

        if (dY < - picISize/2) {    // if moves up into main plate
            move2PaintView((int) dY, pieceView);
//            for (int i = 0; i < 16; i+=2) {
//                jigCR = recyclerJigs.get(i);
//                nowC = jigCR / 10000;
//                nowR = jigCR - nowC * 10000;
//                move2PaintView((int) dY - nowC* 10 - nowR * 10 -i * i*60, pieceView);
//            }
        }
    }

    private static void move2PaintView(int dY, View itemView) {
        Log.w("r22 remove", "jig removing "+ nowC +"x"+ nowR);
        jPosY = screenY - recySize + dY - picOSize - picISize;
        jPosX = itemView.getLeft();

        jigTables[nowC][nowR].outRecycle = true;
        jigTables[nowC][nowR].posX = jPosX;
        jigTables[nowC][nowR].posY = jPosY;

        FloatPiece fp = new FloatPiece();
        fp.C = nowC; fp.R = nowR;
        fp.bitmap = jigTables[nowC][nowR].oLine;
        fp.bigMap = piece.makeBigger(fp.bitmap);
        fPs.add(fp);
        recyclerJigs.remove(jigRecyclePos);
        jigRecycleAdapter.notifyItemRemoved(jigRecyclePos);
        paintView.invalidate();
//        updateViewHandler.sendEmptyMessage(0);
    }


//    private void clearCanvas(Canvas c, Float left, Float top, Float right, Float bottom) {
////        c.drawRect(left, top, right, bottom, mClearPaint);
//        Log.w("recycleTouchHelper","clearCanvas L"+left+" R"+right+" T"+top+" B"+bottom);
//    }
}