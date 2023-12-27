package biz.riopapa.jigsawpuzzle;


import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.activeAdapter;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.activeJigs;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.activePos;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.dragX;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.dragY;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.nowC;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.nowCR;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.nowR;
import static biz.riopapa.jigsawpuzzle.ActivityMain.ANI_TO_FPS;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ActivityMain.mContext;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenBottom;
import static biz.riopapa.jigsawpuzzle.ActivityMain.vibrate;
import static biz.riopapa.jigsawpuzzle.ForeView.foreBlink;
import static biz.riopapa.jigsawpuzzle.ForeView.nowFp;
import static biz.riopapa.jigsawpuzzle.ForeView.nowIdx;

import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import biz.riopapa.jigsawpuzzle.adaptors.JigsawAdapter;
import biz.riopapa.jigsawpuzzle.func.AnchorPiece;
import biz.riopapa.jigsawpuzzle.func.NearPieceBind;
import biz.riopapa.jigsawpuzzle.func.VibratePhone;
import biz.riopapa.jigsawpuzzle.images.PieceImage;
import biz.riopapa.jigsawpuzzle.model.FloatPiece;

public class ItemMoveCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperContract mAdapter;

    final private AnchorPiece anchorPiece;
    final private NearPieceBind nearPieceBind;
    final private PieceImage pieceImage;

    public static boolean nowDragging;

    public ItemMoveCallback(ItemTouchHelperContract adapter, PieceImage pieceImage) {

        mAdapter = adapter;
        this.pieceImage = pieceImage;
        nearPieceBind = new NearPieceBind();
        anchorPiece = new AnchorPiece();

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
            if (dragY < screenBottom - gVal.picHSize) {
                removeFromRecycle();
                add2FloatingPiece();
//                anchorPiece.move();
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
        if (dragY > screenBottom)
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
        nowFp = null;
        activePos = viewHolder.getAbsoluteAdapterPosition();
        nowCR = activeJigs.get(activePos);
        nowC = nowCR / 10000;
        nowR = nowCR - nowC * 10000;
        dragY = screenBottom;
        dragX = viewHolder.itemView.getLeft();
        Log.w("selected "+ activePos, activePos+" CR"+nowCR + " pieceSz="+activeJigs.size());
        if (vibrate)
            new VibratePhone(mContext);
        nowDragging = true;
    }
    private void add2FloatingPiece() {

        nowFp = new FloatPiece();
        nowFp.C = nowC;
        nowFp.R = nowR;
        nowFp.posX = dragX;
        nowFp.posY = dragY;
        nowFp.count = 9;
        nowFp.mode = ANI_TO_FPS;
        nowFp.uId = System.currentTimeMillis();    // set Unique uId
        nowFp.anchorId = 0;       // let anchorId to itself
        gVal.fps.add(nowFp);
        nowIdx = gVal.fps.size() - 1;
    }

    private void removeFromRecycle() {

        activeJigs.remove(activePos);
        activeAdapter.notifyItemRemoved(activePos);
//        activeAdapter.notifyItemRangeChanged(activePos, activeJigs.size());
        gVal.jigTables[nowC][nowR].fp = true;
        Log.w("r2m move R"+nowCR,"removed from recycler drag="+dragX+"x"+dragY
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
        if (activePos == activeJigs.size()) {
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
            dragX = (int) pieceView.getX();
            dragY = screenBottom + (int) pieceView.getY();
            if (nowFp != null) {
                nowFp.posX = dragX;
                nowFp.posY = dragY;
            }
            foreBlink = true;
//            String txt = "dxDy "+dX+" x "+dY
//                    + "\n GVal.jPos "+dragX+" x "+dragY + "fps size="+ gVal.fps.size();
//            Log.w("screenbottom", txt);
//            foreView.invalidate();
        }
    }

    public interface ItemTouchHelperContract {

        void onRowMoved(int fromPosition, int toPosition);
        void onRowSelected(JigsawAdapter.MyViewHolder myViewHolder);
        void onRowClear(JigsawAdapter.MyViewHolder myViewHolder);

    }

}

