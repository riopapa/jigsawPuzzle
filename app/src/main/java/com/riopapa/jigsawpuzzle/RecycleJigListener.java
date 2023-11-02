package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.MainActivity.activeRecyclerJigs;
import static com.riopapa.jigsawpuzzle.MainActivity.aniTO_PAINT;
import static com.riopapa.jigsawpuzzle.MainActivity.doNotUpdate;
import static com.riopapa.jigsawpuzzle.MainActivity.fps;
import static com.riopapa.jigsawpuzzle.MainActivity.jPosX;
import static com.riopapa.jigsawpuzzle.MainActivity.jPosY;
import static com.riopapa.jigsawpuzzle.MainActivity.jigCR;
import static com.riopapa.jigsawpuzzle.MainActivity.jigRecycleAdapter;
import static com.riopapa.jigsawpuzzle.MainActivity.jigRecyclePos;
import static com.riopapa.jigsawpuzzle.MainActivity.jigRecyclerView;
import static com.riopapa.jigsawpuzzle.MainActivity.jigTables;
import static com.riopapa.jigsawpuzzle.MainActivity.nowC;
import static com.riopapa.jigsawpuzzle.MainActivity.nowR;
import static com.riopapa.jigsawpuzzle.MainActivity.picHSize;
import static com.riopapa.jigsawpuzzle.MainActivity.picISize;
import static com.riopapa.jigsawpuzzle.MainActivity.picOSize;
import static com.riopapa.jigsawpuzzle.MainActivity.pieceImage;
import static com.riopapa.jigsawpuzzle.MainActivity.recySize;

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
        view.getLayoutParams().width = recySize;
        view.getLayoutParams().height = recySize;
        ImageView iv = view.findViewById(R.id.recycle_jigsaw);
        iv.getLayoutParams().height = picOSize;
        iv.getLayoutParams().width = picOSize;
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
                Collections.swap(activeRecyclerJigs, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(activeRecyclerJigs, i, i - 1);
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
            return false;   // true or false?
        }
        @Override
        public void onShowPress(@NonNull MotionEvent e) {
//            mTouchHelper.startDrag(this);
//            View view = e.
//            Log.w("r27", "Touch onShowPress ");

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

            doNotUpdate = true;
            removeFrmRecycle.sendEmptyMessage(0);
            add2FloatingPiece();
        }

        private void add2FloatingPiece() {

            jigTables[nowC][nowR].posX = jPosX;
            jigTables[nowC][nowR].posY = jPosY - picOSize;

            FloatPiece fp = new FloatPiece();
            fp.C = nowC; fp.R = nowR;
            fp.oLine = jigTables[nowC][nowR].oLine;
            fp.count = 5;
            fp.mode = aniTO_PAINT;
            fp.uId = System.currentTimeMillis();    // set Unique uId
            fp.anchorId = 0;       // let anchorId to itself
            fps.add(fp);
            doNotUpdate = false;
        }

        @Override
        public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull JigHolder viewHolder, int position) {

        jigCR = activeRecyclerJigs.get(position);
        int jigX = jigCR / 10000;
        int jigY = jigCR - jigX * 10000;
        if (jigTables[jigX][jigY].src == null)
            pieceImage.makeAll(jigX, jigY);
        viewHolder.ivIcon.setImageBitmap(jigTables[jigX][jigY].src);
        viewHolder.ivIcon.setTag(jigCR);
    }

    @Override
    public int getItemCount() {
        return (activeRecyclerJigs.size());
    }

    public final static Handler removeFrmRecycle = new Handler(Looper.getMainLooper()) {
        public void handleMessage(@NonNull Message msg) {
//            hangOn = true;
            Log.w("r2m move","removed from recycler jPos="+jPosX+"x"+jPosY);
//            vHolder.itemView.setBackgroundColor(0x000FFFF);
            jigTables[nowC][nowR].outRecycle = true;
            activeRecyclerJigs.remove(jigRecyclePos);
            jigRecycleAdapter.notifyItemRemoved(jigRecyclePos);
//            hangOn = false;
        }
    };
    public final static Handler insert2Recycle = new Handler(Looper.getMainLooper()) {
        public void handleMessage(@NonNull Message msg) {
            // todo: calculate jigRecyclePos
            LinearLayoutManager layoutManager = (LinearLayoutManager) jigRecyclerView.getLayoutManager();

            int i = layoutManager.findFirstVisibleItemPosition();
            jigRecyclePos = i + (jPosX+picISize+ picHSize)/ recySize;
//            Log.w("r2i insert","add to recycler jPos="+jPosX+"x"+jPosY+" i="+i+" pos="+jigRecyclePos);

            jigTables[nowC][nowR].outRecycle = false;
            if (jigRecyclePos < activeRecyclerJigs.size()-1) {
                activeRecyclerJigs.add(jigRecyclePos, nowC * 10000 + nowR);
                jigRecycleAdapter.notifyItemInserted(jigRecyclePos);
            } else {
                activeRecyclerJigs.add(nowC * 10000 + nowR);
                jigRecycleAdapter.notifyItemInserted(activeRecyclerJigs.size()-1);
            }
            doNotUpdate = false;
        }
    };
}
