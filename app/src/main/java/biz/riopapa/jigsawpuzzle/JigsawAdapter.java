package biz.riopapa.jigsawpuzzle;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.activeAdapter;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.activePos;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.doNotUpdate;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.dragX;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.dragY;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigOLine;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigPic;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.nowC;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.nowCR;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.nowR;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.pieceImage;
import static biz.riopapa.jigsawpuzzle.ActivityMain.ANI_TO_FPS;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ActivityMain.mContext;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenBottom;
import static biz.riopapa.jigsawpuzzle.ActivityMain.vibrate;
import static biz.riopapa.jigsawpuzzle.JigRecycleCallback.nowDragging;
import static biz.riopapa.jigsawpuzzle.PaintView.nowFp;
import static biz.riopapa.jigsawpuzzle.PaintView.nowIdx;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import biz.riopapa.jigsawpuzzle.func.AnchorPiece;
import biz.riopapa.jigsawpuzzle.func.NearPieceBind;
import biz.riopapa.jigsawpuzzle.func.VibratePhone;
import biz.riopapa.jigsawpuzzle.model.FloatPiece;

public class JigsawAdapter extends RecyclerView.Adapter<JigsawAdapter.ViewHolder>
            implements ZItemTouchHelperListener {

    AnchorPiece anchorPiece;
    NearPieceBind nearPieceBind;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycle_jigsaw, viewGroup, false);
        view.getLayoutParams().width = gVal.recSize;
        view.getLayoutParams().height = gVal.recSize;
        ImageView iv = view.findViewById(R.id.recycle_jigsaw);
        iv.getLayoutParams().height = gVal.picOSize;
        iv.getLayoutParams().width = gVal.picOSize;
        iv.requestLayout();
        anchorPiece = new AnchorPiece();
        nearPieceBind = new NearPieceBind();

        return new ViewHolder(view);
    }


        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            return false;
        }

        @Override
        public void onItemSwiped(int position) {

        }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener,
            GestureDetector.OnGestureListener {
//        public static class ViewHolder extends RecyclerView.ViewHolder {

    ImageView ivIcon;
    View viewLine;
    GestureDetector mGestureDetector;

    public ViewHolder(View view) {
        super(view);
        this.viewLine = itemView.findViewById(R.id.piece_layout);
        this.ivIcon = itemView.findViewById(R.id.recycle_jigsaw);

        mGestureDetector = new GestureDetector(itemView.getContext(), this);
        itemView.setOnTouchListener(this);

    }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mGestureDetector.onTouchEvent(event);
//            Log.w("rq onTouch "+v.getTag(), "e "+event.getX()+";"+event.getY());
            return v.performClick();
        }

        @Override
        public boolean onDown(@NonNull MotionEvent e) {return false;}

        @Override
        public void onShowPress(@NonNull MotionEvent e) {
//            Log.w("jigsawAdapter","onShowPress "+e.getAction());
        }

        @Override
        public boolean onSingleTapUp(@NonNull MotionEvent e) {return true;}

        @Override
        public boolean onScroll(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
            Log.w("jigsawAdapter","onScroll "+e1.getAction());
            return false;
        }

        @Override
        public void onLongPress(@NonNull MotionEvent e) {
//            Log.w("jigsawAdapter","onLongPress "+e.getAction());
        }
        @Override
        public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {         Log.w("onFling", "e1 "+ e1.getY()+" e2 "+e2.getY()+" vel="+velocityY);
            Log.w("jigsawAdapter","onFling "+e1.getAction());
            return false;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        nowCR = gVal.activeJigs.get(position);
        int cc = nowCR / 10000;
        int rr = nowCR - cc * 10000;
//        Log.w("onBindViewHolder "+position,jigC+"x"+jigR);
        if (jigPic[cc][rr] == null) {
            jigPic[cc][rr] = pieceImage.makePic(cc, rr);
        }
        if (jigOLine[cc][rr] == null) {
            jigOLine[cc][rr] = pieceImage.makeOline(jigPic[cc][rr], cc, rr);
        }

        viewHolder.ivIcon.setImageBitmap(jigOLine[cc][rr]);
        viewHolder.ivIcon.setTag(nowCR);
    }

    @Override
    public int getItemCount() {
        return (gVal.activeJigs.size());
    }

}

