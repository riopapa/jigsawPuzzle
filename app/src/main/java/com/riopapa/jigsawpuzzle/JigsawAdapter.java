package com.riopapa.jigsawpuzzle;

import static android.view.View.DRAG_FLAG_GLOBAL;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.doNotUpdate;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jPosX;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jPosY;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowCR;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigOLine;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigPic;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigRecycleAdapter;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigRecyclePos;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigRecyclerView;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowC;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowR;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.pieceImage;
import static com.riopapa.jigsawpuzzle.ActivityMain.vars;

import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.riopapa.jigsawpuzzle.func.ShadowDraw;
import com.riopapa.jigsawpuzzle.model.FloatPiece;

public class JigsawAdapter extends RecyclerView.Adapter<JigsawAdapter.ViewHolder>
            implements ZItemTouchHelperListener {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycle_jigsaw, viewGroup, false);
        view.getLayoutParams().width = vars.recSize;
        view.getLayoutParams().height = vars.recSize;
        ImageView iv = view.findViewById(R.id.recycle_jigsaw);
        iv.getLayoutParams().height = vars.picOSize;
        iv.getLayoutParams().width = vars.picOSize;
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
            Log.w("rq ontouch "+v.getTag(), "e "+event.getX()+";"+event.getY());
            return v.performClick();
        }

        @Override
        public boolean onDown(@NonNull MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(@NonNull MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(@NonNull MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(@NonNull MotionEvent e) {

        }
        @Override
        public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
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
//        @Override
//        public void onShowPress(@NonNull MotionEvent e) {
////            mTouchHelper.startDrag(this);
////            View view = e.
//            Log.w("r27 onShowPress", "Touch onShowPress ");
//
//        }
//
//        @Override
//        public boolean onSingleTapUp(@NonNull MotionEvent e) {
//            Log.w("r28 Recycler", "onSingleTapUp ");
//            return true;
//        }
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

        nowCR = vars.activeRecyclerJigs.get(position);
        int jigC = nowCR / 10000;
        int jigR = nowCR - jigC * 10000;
//        Log.w("onBindViewHolder "+position,jigC+"x"+jigR);
        if (jigPic[jigC][jigR] == null)
            pieceImage.buildPiece(jigC, jigR);
        viewHolder.ivIcon.setImageBitmap(jigOLine[jigC][jigR]);
        viewHolder.ivIcon.setTag(nowCR);
//        startPieceMove(viewHolder);

    }

    @Override
    public int getItemCount() {
        return (vars.activeRecyclerJigs.size());
    }

    public final static Handler removeFrmRecycle = new Handler(Looper.getMainLooper()) {
        public void handleMessage(@NonNull Message msg) {
            Log.w("r2m move "+jigRecyclePos,"removed from recycler jPos="+jPosX+"x"+jPosY);
//            vHolder.itemView.setBackgroundColor(0x000FFFF);
            if (jigRecyclePos < vars.jigTables.length) {
                vars.jigTables[nowC][nowR].outRecycle = true;
                vars.activeRecyclerJigs.remove(jigRecyclePos);
                jigRecycleAdapter.notifyItemRemoved(jigRecyclePos);
            }
//            hangOn = false;
        }
    };
    public final static Handler insert2Recycle = new Handler(Looper.getMainLooper()) {
        public void handleMessage(@NonNull Message msg) {
            // todo: calculate jigRecyclePos
            LinearLayoutManager layoutManager = (LinearLayoutManager) jigRecyclerView.getLayoutManager();

            int i = layoutManager.findFirstVisibleItemPosition();
            jigRecyclePos = i + (jPosX+vars.picISize+ vars.picHSize)/ vars.recSize;
//            Log.w("r2i insert","add to recycler jPos="+jPosX+"x"+jPosY+" i="+i+" pos="+jigRecyclePos);

            vars.jigTables[nowC][nowR].outRecycle = false;
            if (jigRecyclePos < vars.activeRecyclerJigs.size()-1) {
                vars.activeRecyclerJigs.add(jigRecyclePos, nowC * 10000 + nowR);
                jigRecycleAdapter.notifyItemInserted(jigRecyclePos);
            } else {
                vars.activeRecyclerJigs.add(nowC * 10000 + nowR);
                jigRecycleAdapter.notifyItemInserted(vars.activeRecyclerJigs.size()-1);
            }
            doNotUpdate = false;
        }
    };

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