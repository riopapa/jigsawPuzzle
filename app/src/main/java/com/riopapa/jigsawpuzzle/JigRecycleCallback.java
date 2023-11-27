package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.doNotUpdate;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.dragX;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.dragY;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.activeAdapter;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.activePos;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowC;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowCR;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowR;
import static com.riopapa.jigsawpuzzle.ActivityMain.ANI_TO_FPS;
import static com.riopapa.jigsawpuzzle.ActivityMain.fPhoneInchX;
import static com.riopapa.jigsawpuzzle.ActivityMain.mContext;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenBottom;
import static com.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static com.riopapa.jigsawpuzzle.ActivityMain.vibrate;
import static com.riopapa.jigsawpuzzle.PaintView.nowFp;
import static com.riopapa.jigsawpuzzle.PaintView.nowIdx;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.riopapa.jigsawpuzzle.databinding.ActivityJigsawBinding;
import com.riopapa.jigsawpuzzle.func.AnchorPiece;
import com.riopapa.jigsawpuzzle.func.NearPieceBind;
import com.riopapa.jigsawpuzzle.func.VibratePhone;
import com.riopapa.jigsawpuzzle.model.FloatPiece;

public class JigRecycleCallback extends ItemTouchHelper.Callback {

    private final ZItemTouchHelperListener listener;
    private final ActivityJigsawBinding binding;
    private final AnchorPiece anchorPiece;
    private final NearPieceBind nearPieceBind;

    public static boolean nowDragging;


    public JigRecycleCallback(ZItemTouchHelperListener listener, ActivityJigsawBinding binding) {
        this.listener = listener;
        this.binding = binding;
        nearPieceBind = new NearPieceBind();
        anchorPiece = new AnchorPiece();
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
        viewHolder.itemView.setBackgroundColor(0x000FFFF);

//        int pos = viewHolder.getAbsoluteAdapterPosition();
//        if (pos < 0)
//            return;
//        Log.w("pc clearView","pos "+pos);
//        int cr = GVal.activeRecyclerJigs.get(pos);
//        int c = cr /10000;
//        int r = cr - c * 10000;

    }

    RecyclerView.ViewHolder svViewHolder;
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if(actionState == ItemTouchHelper.ACTION_STATE_DRAG){
            nowDragging = true;
            // Piece is selected and begun dragging
            svViewHolder = viewHolder;
            recyclerSelected(viewHolder);

        } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
            // Piece dragging is finished
            // if yposition is above recycler then move to fps
            if (dragY < screenBottom - gVal.picHSize) {
                // make visibility to gone for no remaining shadow if tablet
                if (fPhoneInchX > 3f)
                    svViewHolder.itemView.setVisibility(View.GONE);
                Log.w("idle ", "ACTION_STATE_IDLE =" + dragX + " x " + dragY);
                doNotUpdate = true;
                gVal.debugMode = true;
                removeFromRecycle();
                add2FloatingPiece();
                doNotUpdate = false;
                anchorPiece.move();
                nearPieceBind.check();
//                GVal.debugMode = false;
                nowDragging = false;
            }
        } else {
            Log.w("onSelectedChanged", "ItemTouchHelper "+actionState);
        }

    }

    private void recyclerSelected(RecyclerView.ViewHolder viewHolder) {
        nowFp = null;
        activePos = viewHolder.getAbsoluteAdapterPosition();
        nowCR = gVal.activeJigs.get(activePos);
        nowC = nowCR / 10000;
        nowR = nowCR - nowC * 10000;
        dragY = screenBottom;
        dragX = viewHolder.itemView.getLeft();
        Log.w("selected "+ activePos, " CR"+nowCR+ " drag x="+dragX+ " y="+dragY
                +" pieceSz="+gVal.activeJigs.size());
        if (vibrate)
            new VibratePhone(mContext);
    }
    private void add2FloatingPiece() {

        nowFp = new FloatPiece();
        nowFp.C = nowC;
        nowFp.R = nowR;
        nowFp.posX = dragX;
        nowFp.posY = dragY;
        nowFp.count = 5;
        nowFp.mode = ANI_TO_FPS;
        nowFp.uId = System.currentTimeMillis();    // set Unique uId
        nowFp.anchorId = 0;       // let anchorId to itself
        gVal.fps.add(nowFp);
        nowIdx = gVal.fps.size() - 1;
//        Log.w("FPS xy"," info size="+GVal.fps.size());
//        for (int i = 0; i < GVal.fps.size(); i++) {
//            Log.w(GVal.picGap+" gap "+i, (GVal.fps.get(i).posX-dragX)
//                    + " x "+ (GVal.fps.get(i).posY-dragY));
//        }
    }

    private void removeFromRecycle() {
        gVal.jigTables[nowC][nowR].outRecycle = true;
        gVal.activeJigs.remove(activePos);
        activeAdapter.notifyItemRemoved(activePos);
        Log.w("r2m move R"+nowCR,"removed from recycler drag="+dragX+"x"+dragY
        + " pieSZ="+gVal.activeJigs.size());
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
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Log.w("p28 onSwiped", "Swiped rightPosition "+viewHolder.getBindingAdapterPosition());
        listener.onItemSwiped(viewHolder.getBindingAdapterPosition());
    }


//    @Override
//    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//        int holderIdx = viewHolder.getAbsoluteAdapterPosition();
//        int tgtIdx = target.getAbsoluteAdapterPosition();
//        Log.w("p27 onMove","from "+holderIdx+" to "+tgtIdx);
//        Collections.swap(gVal.activeRecyclerJigs, holderIdx, tgtIdx);
//        jigRecycleAdapter.notifyItemMoved(holderIdx, tgtIdx);
////        listener.onItemMove(viewHolder.getBindingAdapterPosition(), target.getBindingAdapterPosition());
//        return true;
//    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        // -- canvas width = screenX, height = recycler height
//        long nowTime = System.currentTimeMillis();
//        if (nowTime < helperDrawTime + 50)
//            return;
//
//        helperDrawTime = nowTime;

        if (gVal.activeJigs.size() == 0)
            return;
        if (activePos == gVal.activeJigs.size()) {
//            Log.e("r tag","activeSize="+GVal.activeRecyclerJigs.size()+" GVal.jigRecyclePos="+GVal.jigRecyclePos);
//            for (int i = 0; i < GVal.activeRecyclerJigs.size(); i++)
//                Log.w("active "+i, "pos "+GVal.activeRecyclerJigs.get(i));
            return;
        }

        // dX, dY is valuable only when piece is dragged from recycler view
        View pieceView = viewHolder.itemView;
//        nowCR = GVal.activeRecyclerJigs.get(jigRecyclePos);
//        nowC = nowCR / 10000;
//        nowR = nowCR - nowC * 10000;
        if (dX != 0 && dY != 0 && nowDragging) {
            dragX = (int) pieceView.getX();
            dragY = screenBottom + (int) pieceView.getY();
            if (nowFp != null) {
                nowFp.posX = dragX;
                nowFp.posY = dragY;
            }
//            String txt = "dxDy "+dX+" x "+dY
//                    + "\n GVal.jPos "+dragX+" x "+dragY + "fps size="+ gVal.fps.size();
//            Log.w("screenbottom", txt);
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