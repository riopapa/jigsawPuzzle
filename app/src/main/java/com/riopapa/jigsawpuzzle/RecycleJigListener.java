package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.doNotUpdate;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jPosX;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jPosY;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigCR;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigPic;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigRecycleAdapter;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigRecyclePos;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigRecyclerView;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowC;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.nowR;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.pieceImage;
import static com.riopapa.jigsawpuzzle.ActivityMain.ANI_TO_PAINT;
import static com.riopapa.jigsawpuzzle.ActivityMain.vars;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.riopapa.jigsawpuzzle.model.FloatPiece;

import java.util.Collections;

public class RecycleJigListener extends RecyclerView.Adapter<RecycleJigListener.JigHolder>
        implements ZItemTouchHelperListener {

    @NonNull
    @Override
    public JigHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycle_jigsaw, viewGroup, false);
        view.getLayoutParams().width = vars.recSize;
        view.getLayoutParams().height = vars.recSize;
        ImageView iv = view.findViewById(R.id.recycle_jigsaw);
        iv.getLayoutParams().height = vars.picOSize;
        iv.getLayoutParams().width = vars.picOSize;
        iv.requestLayout();
        return new JigHolder(view);
    }

    @Override
    public void onItemSwiped(int position) {

        Log.w("r13 Recycler onItemSwiped", "rightPosition = "+position);

    }
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

        Log.w("r33 Recycler on item move", "from = "+fromPosition);
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(vars.activeRecyclerJigs, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(vars.activeRecyclerJigs, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }
    public static class JigHolder extends RecyclerView.ViewHolder implements View.OnTouchListener,
            GestureDetector.OnGestureListener {

        ImageView ivIcon;
        View viewLine;
        GestureDetector mGestureDetector;
        public JigHolder(View view) {
            super(view);
            this.viewLine = itemView.findViewById(R.id.piece_layout);
            this.ivIcon = itemView.findViewById(R.id.recycle_jigsaw);
            mGestureDetector = new GestureDetector(itemView.getContext(), this);
            itemView.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mGestureDetector.onTouchEvent(event);
//            Log.w("rq on touch "+event.getAction(), "e "+event.getX()+";"+event.getY()+" t="+event.getEventTime());
            return true;
        }

        @Override
        public boolean onDown(@NonNull MotionEvent e) {
            Log.w("onDown"," pos "+e.getX()+"x"+e.getY()+" raw "+e.getRawX()+"x"+e.getRawY()

            );

            return false;   // true or false?
        }

        @Override
        public void onShowPress(@NonNull MotionEvent e) {
//            mTouchHelper.startDrag(this);
//            View view = e.
//            Log.w("r27 onShowPress", "Touch onShowPress ");

        }

        @Override
        public boolean onSingleTapUp(@NonNull MotionEvent e) {
//            Log.w("r28 on single tab up", "Touch ");
            return true;
        }

        @Override
        public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {

//            Log.w("r14", " onScroll");
            return false;
        }

        @Override
        public void onLongPress(@NonNull MotionEvent e) {
            Log.w("r2l long","long press Event="+e);

            doNotUpdate = true;
            removeFrmRecycle.sendEmptyMessage(0);
            add2FloatingPiece();
        }

        private void add2FloatingPiece() {

            vars.jigTables[nowC][nowR].posX = jPosX;
            vars.jigTables[nowC][nowR].posY = jPosY - vars.picOSize;

            FloatPiece fp = new FloatPiece();
            fp.C = nowC; fp.R = nowR;
            fp.count = 5;
            fp.mode = ANI_TO_PAINT;
            fp.uId = System.currentTimeMillis();    // set Unique uId
            fp.anchorId = 0;       // let anchorId to itself
            vars.fps.add(fp);
            doNotUpdate = false;
        }

        @Override
        public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull JigHolder viewHolder, int position) {

        jigCR = vars.activeRecyclerJigs.get(position);
        int jigX = jigCR / 10000;
        int jigY = jigCR - jigX * 10000;
//        Log.w("onBindViewHolder "+position,jigX+"x"+jigY);
        if (jigPic[jigX][jigY] == null)
            pieceImage.makeAll(jigX, jigY);
        viewHolder.ivIcon.setImageBitmap(jigPic[jigX][jigY]);
        viewHolder.ivIcon.setTag(jigCR);
    }

    @Override
    public int getItemCount() {
        return (vars.activeRecyclerJigs.size());
    }

    public final static Handler removeFrmRecycle = new Handler(Looper.getMainLooper()) {
        public void handleMessage(@NonNull Message msg) {
            Log.w("r2m move","removed from recycler jPos="+jPosX+"x"+jPosY);
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
