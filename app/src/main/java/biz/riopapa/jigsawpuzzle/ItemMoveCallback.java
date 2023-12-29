package biz.riopapa.jigsawpuzzle;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.activeAdapter;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.activeJigs;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemPos;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemX;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemY;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemC;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemCR;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemR;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ActivityMain.mContext;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenBottom;
import static biz.riopapa.jigsawpuzzle.ActivityMain.vibrate;
import static biz.riopapa.jigsawpuzzle.ForeView.foreBlink;
import static biz.riopapa.jigsawpuzzle.ForeView.topIdx;

import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import biz.riopapa.jigsawpuzzle.adaptors.JigsawAdapter;
import biz.riopapa.jigsawpuzzle.func.VibratePhone;
import biz.riopapa.jigsawpuzzle.model.FloatPiece;

public class ItemMoveCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperContract mAdapter;

    public static boolean nowDragging;

    public ItemMoveCallback(ItemTouchHelperContract adapter) {

        mAdapter = adapter;
    }


//    @Override
//    public boolean isLongPressDragEnabled() {
//        return true;
//    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN
                        | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
                ,
                ItemTouchHelper.END | ItemTouchHelper.START
                        | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        );
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        Log.w("onMove", "onMove " +viewHolder.getAbsoluteAdapterPosition()+" > "+target.getAbsoluteAdapterPosition());
        mAdapter.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    RecyclerView.ViewHolder svViewHolder;
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder,
                                  int actionState) {
        foreBlink = false;
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG){
            topIdx = -1;
            Log.w("state is "+actionState, "START DRAG =");
            // Piece is selected and begun dragging
            svViewHolder = viewHolder;
            recyclerSelected(viewHolder);

        } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
//            Log.w("state is "+actionState, " ACTION_STATE_IDLE =");
            if (viewHolder instanceof JigsawAdapter.MyViewHolder) {
                JigsawAdapter.MyViewHolder myViewHolder=
                        (JigsawAdapter.MyViewHolder) viewHolder;
                mAdapter.onRowSelected(myViewHolder);
            }

            // Piece dragging is finished
            // if yPosition is above recycler then move to fps
            if (itemY < screenBottom - gVal.picHSize) {
                removeFromRecycle();
                add2FloatingPiece();
                nowDragging = false;
                foreBlink = true;
            }
        } else if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            Log.w("state is "+actionState, " ACTION_STATE_SWIPE =");
            // ignore swipe
        } else {
            Log.w("state is "+actionState, " UN KNOWEN =");
        }

//        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
//
//        }

        super.onSelectedChanged(viewHolder, actionState);
    }
    @Override
    public void clearView(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (itemY > screenBottom)
            return;
        if (viewHolder instanceof JigsawAdapter.MyViewHolder) {
            JigsawAdapter.MyViewHolder myViewHolder=
                    (JigsawAdapter.MyViewHolder) viewHolder;
            mAdapter.onRowClear(myViewHolder);
        }
//        if (dragY < screenBottom - gVal.picHSize && ) {
//            viewHolder.itemView.setAlpha(0);
//        }

    }

    private void recyclerSelected(RecyclerView.ViewHolder viewHolder) {
        itemPos = viewHolder.getAbsoluteAdapterPosition();
        itemCR = activeJigs.get(itemPos) - 10000;
        itemC = itemCR / 100;
        itemR = itemCR - itemC * 100;
        itemY = screenBottom;
        itemX = viewHolder.itemView.getLeft();
        Log.w("selected "+ itemPos, itemPos +" CR"+ itemCR + " pieceSz="+activeJigs.size());
        nowDragging = true;
        if (vibrate)
            new VibratePhone(mContext);
    }
    private void add2FloatingPiece() {

        FloatPiece itemFp = new FloatPiece();
        itemFp.C = itemC;
        itemFp.R = itemR;
        itemFp.posX = itemX;
        itemFp.posY = itemY;
        itemX = -1;
        itemY = -1;
        itemFp.count = 9;
        itemFp.mode = ActivityMain.GMode.TO_FPS;
        itemFp.uId = System.currentTimeMillis();    // set Unique uId
        itemFp.anchorId = 0;       // let anchorId to itself
        gVal.fps.add(itemFp);
        topIdx = gVal.fps.size() - 1;
    }

    private void removeFromRecycle() {

        activeJigs.remove(itemPos);
        activeAdapter.notifyItemRemoved(itemPos);
//        activeAdapter.notifyItemRangeChanged(activePos, activeJigs.size());
        gVal.jigTables[itemC][itemR].fp = true;
        Log.w("r2m move R"+ itemCR,"removed from recycler drag="+ itemX +"x"+ itemY
                + " pieSZ="+activeJigs.size());
    }

    static long helperDrawTime = 0;
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        long nowTime = System.currentTimeMillis();
        if (nowTime < helperDrawTime)
            return;

        helperDrawTime = nowTime + 150;

        if (activeJigs.size() == 0)
            return;
        if (itemPos == activeJigs.size()) {
//            Log.e("r tag","activeSize="+GVal.activeRecyclerJigs.size()+" GVal.jigRecyclePos="+GVal.jigRecyclePos);
//            for (int i = 0; i < GVal.activeRecyclerJigs.size(); i++)
//                Log.w("active "+i, "pos "+GVal.activeRecyclerJigs.get(i));
            return;
        }

        // dX, dY is valuable only when piece is dragged from recycler view
//        nowCR = GVal.activeRecyclerJigs.get(jigRecyclePos);
//        nowC = nowCR / 10000;
//        nowR = nowCR - nowC * 10000;
        if (dX != 0 && dY != 0 && nowDragging) {
            View pieceView = viewHolder.itemView;
            itemX = (int) pieceView.getX();
            itemY = screenBottom + (int) pieceView.getY();
            Log.w("dragXY "+topIdx, itemPos + " dragging "+ itemX +"/"+ itemY);
//            if (gVal.fps.size() > 0) {    // not effect to fp pools
//                gVal.fps.get(topIdx).posX = dragX;
//                gVal.fps.get(topIdx).posY = dragY;
//            }
            foreBlink = true;
        }
    }

    public interface ItemTouchHelperContract {

        void onRowMoved(int fromPosition, int toPosition);
        void onRowSelected(JigsawAdapter.MyViewHolder myViewHolder);
        void onRowClear(JigsawAdapter.MyViewHolder myViewHolder);

    }

}

