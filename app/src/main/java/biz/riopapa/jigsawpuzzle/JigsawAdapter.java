package biz.riopapa.jigsawpuzzle;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigOLine;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigPic;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.nowCR;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.pieceImage;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;

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

public class JigsawAdapter extends RecyclerView.Adapter<JigsawAdapter.ViewHolder>
            implements ZItemTouchHelperListener {

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
        public void onShowPress(@NonNull MotionEvent e) {}

        @Override
        public boolean onSingleTapUp(@NonNull MotionEvent e) {return true;}

        @Override
        public boolean onScroll(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(@NonNull MotionEvent e) {

        }
        @Override
        public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {         Log.w("onFling", "e1 "+ e1.getY()+" e2 "+e2.getY()+" vel="+velocityY);
            return false;
        }
    }
//
//        @Override
//        public boolean onDown(@NonNull MotionEvent e) {
//            Log.w("onDown"," pos "+e.getX()+"x"+e.getY()+" raw "+e.getRawX()+"x"+e.getRawY()
//
//            );
//
//            return false;   // true or false?
//        }
//
//
//        @Override
//        public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
//
////            Log.w("r14", " onScroll d="+distanceX+":"+distanceY+ " e1="+
////                    e1.getX()+"x"+e1.getY()+" e2="+e2.getX()+"x"+e2.getY());
//            return false;
//        }
//
//
//        @Override
//        public void onLongPress(@NonNull MotionEvent e) {
//            Log.w("r2l long","long press Event="+e);
//
//            doNotUpdate = true;
//            // Todo: resolve jigRecyclePos
//            removeFrmRecycle.sendEmptyMessage(0);
//            add2FloatingPiece();
//        }

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

//    private static void pieceMove(@NonNull View viewP) {
//        viewP.setOnTouchListener((view, evt) -> {
//            float fromX = evt.getX();
//            float fromY = evt.getY();
//            Log.w("onTouch "+evt.getAction(), "DOWN "+fromX+"x"+fromY);
//            int action = evt.getAction();
//            switch (action) {
//                case MotionEvent.ACTION_DOWN:
//                    if (view != null) {
//                        String tag = view.getTag().toString();
//                        nowCR = Integer.parseInt(tag);
//                        nowC = nowCR / 10000;
//                        nowR = nowCR - nowC * 10000;
////                        jigRecyclePos = viewP.getAbsoluteAdapterPosition();
//                        Log.w("onTouch"," DOWN "+jigRecyclePos+" tag="+tag);
////                            ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());
//                        ClipData.Item item = new ClipData.Item(tag);
//                        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
//                        ClipData data = new ClipData(tag, mimeTypes, item);
////                        View.DragShadowBuilder builder = new View.DragShadowBuilder(view);
////                        view.startDragAndDrop(data, new ShadowDraw(), null, 0);
//                        view.startDrag(data, new ShadowDraw(view), view, DRAG_FLAG_GLOBAL);
//                    }
//                    break;
//            }
//            return view.performClick();
//        });
//    }