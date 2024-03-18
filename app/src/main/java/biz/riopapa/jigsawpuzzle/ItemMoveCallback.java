package biz.riopapa.jigsawpuzzle;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.activeAdapter;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.activeJigs;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemC;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemCR;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemPos;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemR;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemX;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemY;
import static biz.riopapa.jigsawpuzzle.ActivityMain.fPhoneInchX;
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

import biz.riopapa.jigsawpuzzle.adaptors.PiecesAdapter;
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
//        Log.w("onMove", "onMove " + viewHolder.getAbsoluteAdapterPosition() + " > " + target.getAbsoluteAdapterPosition() +
//                " x delta " + viewHolder.itemView.getLeft() + " > " + target.itemView.getLeft());

//        if (itemY > screenBottom - gVal.picHSize) {
//            moveFrom = viewHolder.getAbsoluteAdapterPosition();
//            moveTo = target.getAbsoluteAdapterPosition();
//            Log.w("onMove", "Moving? "+moveFrom+" > "+moveTo);
////            Log.w("onMove", "onMove " + viewHolder.getAbsoluteAdapterPosition()+" "+viewHolder.itemView.findViewById(R.id.recycler_jigsaw).getTag().toString()
////                    + " > " + target.getAbsoluteAdapterPosition()+" "+target.itemView.findViewById(R.id.recycler_jigsaw).getTag().toString());
////            mAdapter.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
//        }
        return true;
    }

    RecyclerView.ViewHolder svViewHolder;
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder,
                                  int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        foreBlink = false;
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG){
            topIdx = -1;
            // Piece is selected and begun dragging
            svViewHolder = viewHolder;
            recyclerSelected(viewHolder);

        } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof PiecesAdapter.MyViewHolder) {
//                PiecesAdapter.MyViewHolder myViewHolder=
//                        (PiecesAdapter.MyViewHolder) viewHolder;
//                mAdapter.onRowSelected(myViewHolder);
            }
            nowDragging = false;

            // Piece dragging is finished
            // if yPosition is above recycler then move to fps
            if (itemY < screenBottom - gVal.picOSize) {
                reFresh = false;
                removeFromRecycle();
                add2FloatingPiece();

                pieceLock.update();
                pieceBind.update();
                foreBlink = true;
                reFresh = true;
                if (fPhoneInchX > 3f) {
                    svViewHolder.itemView.findViewById(R.id.recycler_jigsaw).setAlpha(0);
                }
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

        //        if (viewHolder instanceof PiecesAdapter.MyViewHolder) {
//            Log.w("clearView","x moving is "+moving+" RowClear");
//            PiecesAdapter.MyViewHolder myViewHolder=
//                    (PiecesAdapter.MyViewHolder) viewHolder;
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
        void onRowSelected(PiecesAdapter.MyViewHolder myViewHolder);
        void onRowClear(PiecesAdapter.MyViewHolder myViewHolder);

    }

}

