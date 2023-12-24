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
import static biz.riopapa.jigsawpuzzle.ForeView.backBlink;
import static biz.riopapa.jigsawpuzzle.ForeView.foreBlink;
import static biz.riopapa.jigsawpuzzle.ForeView.nowFp;
import static biz.riopapa.jigsawpuzzle.ForeView.nowIdx;

import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import biz.riopapa.jigsawpuzzle.func.AnchorPiece;
import biz.riopapa.jigsawpuzzle.func.NearPieceBind;
import biz.riopapa.jigsawpuzzle.func.VibratePhone;
import biz.riopapa.jigsawpuzzle.images.PieceImage;
import biz.riopapa.jigsawpuzzle.model.FloatPiece;

public class JigRecycleCallback extends ItemTouchHelper.Callback {

    final private ItemTouchHelperListener listener;
    final private AnchorPiece anchorPiece;
    final private NearPieceBind nearPieceBind;
    final private PieceImage pieceImage;
    public static boolean nowDragging;

    public JigRecycleCallback(ItemTouchHelperListener listener, PieceImage pieceImage) {
        this.listener = listener;
        this.pieceImage = pieceImage;
        nearPieceBind = new NearPieceBind();
        anchorPiece = new AnchorPiece();
    }

    // this clearView removes piece shadow
    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (dragY < screenBottom - gVal.picHSize) {
            viewHolder.itemView.setAlpha(0);
        }

    }

    RecyclerView.ViewHolder svViewHolder;
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {

        if(actionState == ItemTouchHelper.ACTION_STATE_DRAG){
            Log.w("state is "+actionState, "START DRAG =");
            // Piece is selected and begun dragging
            svViewHolder = viewHolder;
            recyclerSelected(viewHolder);
            nowDragging = true;

        } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
            nowDragging = false;
            // Piece dragging is finished
            // if yposition is above recycler then move to fps
            if (dragY < screenBottom - gVal.picHSize) {
                removeFromRecycle();
                add2FloatingPiece();
                anchorPiece.move();
                foreBlink = true;
                if (nearPieceBind.check(pieceImage)) {
                    backBlink = true;
                }
            }
        } else if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // ignore swipe
        } else {
            Log.e("xx onSelec", "Helper "+actionState);
        }
        super.onSelectedChanged(viewHolder, actionState);

    }

    private void recyclerSelected(RecyclerView.ViewHolder viewHolder) {
        nowFp = null;
        activePos = viewHolder.getAbsoluteAdapterPosition();
        nowCR = activeJigs.get(activePos);
        nowC = nowCR / 10000;
        nowR = nowCR - nowC * 10000;
        dragY = screenBottom;
        dragX = viewHolder.itemView.getLeft();
        Log.w("selected "+ activePos, " CR"+nowCR+ " drag x="+dragX+ " y="+dragY
                +" pieceSz="+activeJigs.size());
        if (vibrate)
            new VibratePhone(mContext);
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
//
        gVal.jigTables[nowC][nowR].outRecycle = true;
        activeJigs.remove(activePos);
        activeAdapter.notifyItemRemoved(activePos);
        activeAdapter.notifyItemRangeChanged(activePos, activeJigs.size());
//        if (svViewHolder == null)
//            Log.w("svViewHolder"," is null");
//        else
//            svViewHolder.itemView.setAlpha(0);

//        helper = new ItemTouchHelper(
//                new JigRecycleCallback(activeAdapter, pieceImage));
//        helper.attachToRecyclerView(jigRecyclerView);

//        System.gc();ar
        Log.w("r2m move R"+nowCR,"removed from recycler drag="+dragX+"x"+dragY
        + " pieSZ="+activeJigs.size());
//        ItemTouchHelper helper = new ItemTouchHelper(
//                new JigRecycleCallback(activeAdapter));
//        helper.attachToRecyclerView(jigRecyclerView);
    }

    @Override
    public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {
        Log.w("p24 onMoved", "Pos="+viewHolder.getBindingAdapterPosition());
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
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//        Log.w("p27 onMoved", "Pos="+viewHolder.getBindingAdapterPosition()+" target="+target.getAbsoluteAdapterPosition());
//        int pf = viewHolder.getBindingAdapterPosition();
//        int pt = target.getBindingAdapterPosition();
//
//        Collections.swap(gVal.activeJigs, pf, pt);
//        activeAdapter.notifyItemChanged(pf);
//        activeAdapter.notifyItemChanged(pt);
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//        Log.w("p28 onSwiped", "Swiped rightPosition "+viewHolder.getBindingAdapterPosition());
//        listener.onItemSwiped(viewHolder.getBindingAdapterPosition());
    }

    long helperDrawTime = 0;
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        // -- canvas width = screenX, height = recycler height
        long nowTime = System.currentTimeMillis();
        if (nowTime < helperDrawTime + 50)
            return;

        helperDrawTime = nowTime;

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

//        boolean isCancelled = dX == 0 && !isCurrentlyActive;
//

//        if (!isCurrentlyActive) {
//            clearCanvas(c, pieceView.getRight() + dX, (float) pieceView.getTop(), (float) pieceView.getRight(), (float) pieceView.getBottom());
//            return;
//        }

    }


//    private void clearCanvas(Canvas c, Float left, Float top, Float right, Float bottom) {
//        c.drawRect(left, top, right, bottom, nullPaint);
//        Log.w("p recycleTouchHelper","clearCanvas L"+left+" R"+right+" T"+top+" B"+bottom);
//    }
}