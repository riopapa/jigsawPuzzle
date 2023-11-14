package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.doNotUpdate;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.dragX;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.dragY;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jPosX;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jPosY;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigBright;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowCR;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigPic;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigRecycleAdapter;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigRecyclePos;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowC;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowR;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.pieceImage;
import static com.riopapa.jigsawpuzzle.ActivityMain.ANI_TO_PAINT;
import static com.riopapa.jigsawpuzzle.ActivityMain.mActivity;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenBottom;
import static com.riopapa.jigsawpuzzle.ActivityMain.vars;
import static com.riopapa.jigsawpuzzle.JigsawAdapter.removeFrmRecycle;
import static com.riopapa.jigsawpuzzle.PaintView.fpNow;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.riopapa.jigsawpuzzle.databinding.ActivityJigsawBinding;
import com.riopapa.jigsawpuzzle.model.FloatPiece;

import java.util.Collections;

public class PaintViewTouchCallback extends ItemTouchHelper.Callback {

    private final ZItemTouchHelperListener listener;
    private final Paint nullPaint = new Paint();

    private final ActivityJigsawBinding binding;

    private NearPieceMerge nearPieceMerge;

    public PaintViewTouchCallback(ZItemTouchHelperListener listener, ActivityJigsawBinding binding) {
        this.listener = listener;
        this.binding = binding;
        nearPieceMerge = new NearPieceMerge();
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
            // Piece is selected and begun dragging
            setNowPiece(viewHolder);
        } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
            // Piece dragging is finished
            // if yposition is above recycler then move to fps
            if (dragY < screenBottom) {
                jPosX = dragX;
                jPosY = dragY + vars.picHSize - vars.picGap - vars.picGap;
                dragX = -1;
                Log.w("idle ", "ACTION_STATE_IDLE =" + jPosX + " x " + jPosY);
                doNotUpdate = true;
                removeFrmRecycle.sendEmptyMessage(0);
                add2FloatingPiece();
                nearPieceMerge.check(jPosX, jPosY);
            }

        }


    }

    private static void setNowPiece(RecyclerView.ViewHolder viewHolder) {
        jigRecyclePos = viewHolder.getAbsoluteAdapterPosition();
        nowCR = vars.activeRecyclerJigs.get(jigRecyclePos);
        nowC = nowCR /10000;
        nowR = nowCR - nowC * 10000;
        if (jigBright[nowC][nowR] == null)
            jigBright[nowC][nowR] = pieceImage.makeBright(jigPic[nowC][nowR]);

        dragY = screenBottom + vars.picHSize;
        dragX = viewHolder.itemView.getLeft() + vars.picGap;
        Log.w("selectedChg CR"+nowCR, "Drag stated nowPos="+jigRecyclePos);

    }
    void add2FloatingPiece() {

        vars.jigTables[nowC][nowR].posX = jPosX;
        vars.jigTables[nowC][nowR].posY = jPosY; // - vars.picOSize;

        FloatPiece fp = new FloatPiece();
        fp.C = nowC;
        fp.R = nowR;
        fp.count = 2;
        fp.mode = ANI_TO_PAINT;
        fp.uId = System.currentTimeMillis();    // set Unique uId
        fp.anchorId = 0;       // let anchorId to itself
        vars.fps.add(fp);
        fpNow = fp;
        doNotUpdate = false;
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
        nowCR = vars.activeRecyclerJigs.get(jigRecyclePos);
        nowC = nowCR / 10000;
        nowR = nowCR - nowC * 10000;
        if (dX != 0 && dY != 0) {
//            Log.w("PieceView",pieceView.getX()+" x "+pieceView.getY()+" delta="+dX+"x"+dY);
//            dragX = pieceView.getLeft() + (int) dX;
//            dragY = (int) (pieceView.getY()+ (int) dY);
            dragX = (int) pieceView.getX();
            dragY = screenBottom + (int) pieceView.getY();
            vars.jigTables[nowC][nowR].posX = dragX;
            vars.jigTables[nowC][nowR].posY = dragY;
        }

        String txt = "dxDy "+dX+" x "+dY
                + "\n vars.jPos "+dragX+" x "+dragY;
        mActivity.runOnUiThread(() -> binding.debugLeft.setText(txt));
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