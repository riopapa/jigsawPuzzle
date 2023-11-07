package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigRecycleAdapter;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigRecyclerView;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.pieceImage;
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
        view.getLayoutParams().width = vars.recySize;
        view.getLayoutParams().height = vars.recySize;
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
            Log.w("r2l long","long press Event="+e.toString());

            vars.doNotUpdate = true;
            removeFrmRecycle.sendEmptyMessage(0);
            add2FloatingPiece();
        }

        private void add2FloatingPiece() {

            vars.jigTables[vars.nowC][vars.nowR].posX = vars.jPosX;
            vars.jigTables[vars.nowC][vars.nowR].posY = vars.jPosY - vars.picOSize;

            FloatPiece fp = new FloatPiece();
            fp.C = vars.nowC; fp.R = vars.nowR;
            fp.oLine = vars.jigTables[vars.nowC][vars.nowR].oLine;
            fp.count = 5;
            fp.mode = vars.aniTO_PAINT;
            fp.uId = System.currentTimeMillis();    // set Unique uId
            fp.anchorId = 0;       // let anchorId to itself
            vars.fps.add(fp);
            vars.doNotUpdate = false;
        }

        @Override
        public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull JigHolder viewHolder, int position) {

        vars.jigCR = vars.activeRecyclerJigs.get(position);
        int jigX = vars.jigCR / 10000;
        int jigY = vars.jigCR - jigX * 10000;
        if (vars.jigTables[jigX][jigY].src == null)
            pieceImage.makeAll(jigX, jigY);
        viewHolder.ivIcon.setImageBitmap(vars.jigTables[jigX][jigY].src);
        viewHolder.ivIcon.setTag(vars.jigCR);
    }

    @Override
    public int getItemCount() {
        return (vars.activeRecyclerJigs.size());
    }

    public final static Handler removeFrmRecycle = new Handler(Looper.getMainLooper()) {
        public void handleMessage(@NonNull Message msg) {
            Log.w("r2m move","removed from recycler vars.jPos="+vars.jPosX+"x"+vars.jPosY);
//            vHolder.itemView.setBackgroundColor(0x000FFFF);
            if (vars.jigRecyclePos < vars.jigTables.length) {
                vars.jigTables[vars.nowC][vars.nowR].outRecycle = true;
                vars.activeRecyclerJigs.remove(vars.jigRecyclePos);
                jigRecycleAdapter.notifyItemRemoved(vars.jigRecyclePos);
            }
//            hangOn = false;
        }
    };
    public final static Handler insert2Recycle = new Handler(Looper.getMainLooper()) {
        public void handleMessage(@NonNull Message msg) {
            // todo: calculate jigRecyclePos
            LinearLayoutManager layoutManager = (LinearLayoutManager) jigRecyclerView.getLayoutManager();

            int i = layoutManager.findFirstVisibleItemPosition();
            vars.jigRecyclePos = i + (vars.jPosX+vars.picISize+ vars.picHSize)/ vars.recySize;
//            Log.w("r2i insert","add to recycler vars.jPos="+vars.jPosX+"x"+vars.jPosY+" i="+i+" pos="+jigRecyclePos);

            vars.jigTables[vars.nowC][vars.nowR].outRecycle = false;
            if (vars.jigRecyclePos < vars.activeRecyclerJigs.size()-1) {
                vars.activeRecyclerJigs.add(vars.jigRecyclePos, vars.nowC * 10000 + vars.nowR);
                jigRecycleAdapter.notifyItemInserted(vars.jigRecyclePos);
            } else {
                vars.activeRecyclerJigs.add(vars.nowC * 10000 + vars.nowR);
                jigRecycleAdapter.notifyItemInserted(vars.activeRecyclerJigs.size()-1);
            }
            vars.doNotUpdate = false;
        }
    };
}
