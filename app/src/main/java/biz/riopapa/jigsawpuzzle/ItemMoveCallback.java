package biz.riopapa.jigsawpuzzle;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.activeAdapter;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.activeJigs;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemC;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemCR;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemPos;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemR;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemX;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemY;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ActivityMain.mContext;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenBottom;
import static biz.riopapa.jigsawpuzzle.ActivityMain.share_vibrate;
import static biz.riopapa.jigsawpuzzle.ForeView.foreBlink;
import static biz.riopapa.jigsawpuzzle.ForeView.reFresh;
import static biz.riopapa.jigsawpuzzle.ForeView.topIdx;

import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import biz.riopapa.jigsawpuzzle.adaptors.JigsawAdapter;
import biz.riopapa.jigsawpuzzle.func.PieceBind;
import biz.riopapa.jigsawpuzzle.func.PieceLock;
import biz.riopapa.jigsawpuzzle.func.VibratePhone;
import biz.riopapa.jigsawpuzzle.images.PieceImage;
import biz.riopapa.jigsawpuzzle.model.FloatPiece;

public class ItemMoveCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperContract mAdapter;

    public static boolean nowDragging;
    PieceLock pieceLock;
    PieceImage pieceImage;
    PieceBind pieceBind;
    public ItemMoveCallback(ItemTouchHelperContract adapter, PieceLock pieceLock,
                            PieceImage pieceImage, PieceBind pieceBind) {
        mAdapter = adapter;
        this.pieceLock = pieceLock;
        this.pieceImage = pieceImage;
        this.pieceBind = pieceBind;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

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
    public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        Log.w("onMove", "onMove " + viewHolder.getAbsoluteAdapterPosition() + " > " + target.getAbsoluteAdapterPosition() +
                " x delta " + viewHolder.itemView.getLeft() + " > " + target.itemView.getLeft());

        //        if (itemY < screenBottom - gVal.picHSize) {
//            reFresh = false;
//            Log.w("onMove", "onMove " + viewHolder.getAbsoluteAdapterPosition() + " > " + target.getAbsoluteAdapterPosition());
//            mAdapter.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
//            moving = true;
//            reFresh = true;
//            return true;
//        }
        return false;
    }

    RecyclerView.ViewHolder svViewHolder;
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder,
                                  int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        foreBlink = false;
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG){
            topIdx = -1;
            Log.w("state is "+actionState, "START DRAG =");
            // Piece is selected and begun dragging
            svViewHolder = viewHolder;
            recyclerSelected(viewHolder);

        } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof JigsawAdapter.MyViewHolder) {
                JigsawAdapter.MyViewHolder myViewHolder=
                        (JigsawAdapter.MyViewHolder) viewHolder;
                mAdapter.onRowSelected(myViewHolder);
            }

            // Piece dragging is finished
            // if yPosition is above recycler then move to fps
            if (itemY < screenBottom - gVal.picHSize) {
                nowDragging = false;
                reFresh = false;
                removeFromRecycle();
                add2FloatingPiece();
                pieceLock.update(pieceImage);
                pieceBind.update();
                foreBlink = true;
                reFresh = true;
            }
        } else if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            Log.w("state is "+actionState, " ACTION_STATE_SWIPE =");
            // ignore swipe
        } else {
            Log.w("state is "+actionState, " UN known =");
        }

    }
    @Override
    public void clearView(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
//        if (viewHolder instanceof JigsawAdapter.MyViewHolder) {
//            Log.w("clearView","x moving is "+moving+" RowClear");
//            JigsawAdapter.MyViewHolder myViewHolder=
//                    (JigsawAdapter.MyViewHolder) viewHolder;
//            mAdapter.onRowClear(myViewHolder);
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
        if (share_vibrate)
            new VibratePhone(mContext);
    }
    private void add2FloatingPiece() {

        gVal.jigTables[itemC][itemR].fp = true;
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

        activeAdapter.notifyItemRemoved(itemPos);
        activeJigs.remove(itemPos);
//        activeAdapter.notifyItemRangeChanged(activePos, activeJigs.size());
        Log.w("r2m remove "+ itemCR,"removed from recycler drag="+ itemX +"x"+ itemY
                + " pieSZ="+activeJigs.size());
    }

    static long helperDrawTime = 0;
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        if (activeJigs.size() == 0)
            return;
        if (itemPos == activeJigs.size())
            return;
        long nowTime = System.currentTimeMillis();
        if (nowTime < helperDrawTime)
            return;
        helperDrawTime = nowTime + 100;

        if (dX != 0 && dY != 0 && nowDragging) {
            View pieceView = viewHolder.itemView;
            itemX = (int) pieceView.getX();
            itemY = screenBottom + (int) pieceView.getY();
            foreBlink = true;
        }
    }

    public interface ItemTouchHelperContract {

        void onRowMoved(int fromPosition, int toPosition);
        void onRowSelected(JigsawAdapter.MyViewHolder myViewHolder);
        void onRowClear(JigsawAdapter.MyViewHolder myViewHolder);

    }

}

