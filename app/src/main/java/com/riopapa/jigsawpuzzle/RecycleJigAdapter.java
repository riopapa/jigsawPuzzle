package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.MainActivity.jPosX;
import static com.riopapa.jigsawpuzzle.MainActivity.jPosY;
import static com.riopapa.jigsawpuzzle.MainActivity.jigRecycleAdapter;
import static com.riopapa.jigsawpuzzle.MainActivity.jigRecyclePos;
import static com.riopapa.jigsawpuzzle.MainActivity.nowC;
import static com.riopapa.jigsawpuzzle.MainActivity.nowR;
import static com.riopapa.jigsawpuzzle.MainActivity.oneItemSelected;
import static com.riopapa.jigsawpuzzle.MainActivity.jigCR;
import static com.riopapa.jigsawpuzzle.MainActivity.jigTables;
import static com.riopapa.jigsawpuzzle.MainActivity.picISize;
import static com.riopapa.jigsawpuzzle.MainActivity.picOSize;
import static com.riopapa.jigsawpuzzle.MainActivity.recySize;
import static com.riopapa.jigsawpuzzle.MainActivity.piece;
import static com.riopapa.jigsawpuzzle.MainActivity.recyclerJigs;
import static com.riopapa.jigsawpuzzle.MainActivity.screenY;
import static com.riopapa.jigsawpuzzle.MainActivity.shouldBeMoved;
import static com.riopapa.jigsawpuzzle.PaintView.fPs;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.riopapa.jigsawpuzzle.model.FloatPiece;

import java.util.Collections;

public class RecycleJigAdapter extends RecyclerView.Adapter<RecycleJigAdapter.JigHolder>
        implements ZItemTouchHelperAdapter {

    int jigX, jigY;
    private ItemTouchHelper mTouchHelper;
    @NonNull
    @Override
    public JigHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycle_jigsaw, viewGroup, false);
        view.getLayoutParams().width = recySize;
        view.getLayoutParams().height = recySize;
        ImageView iv = view.findViewById(R.id.recycle_jigsaw);
        iv.getLayoutParams().height = recySize;
        iv.getLayoutParams().width = recySize;
        iv.requestLayout();
        return new JigHolder(view);
    }

    @Override
    public void onItemSwiped(int position) {

        Log.w("r13 Recycler onItemSwiped", "position = "+position);

    }
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

        Log.w("r33 Recycler on item move", "from = "+fromPosition);
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(recyclerJigs, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(recyclerJigs, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }
    public class JigHolder extends RecyclerView.ViewHolder implements View.OnTouchListener,
            GestureDetector.OnGestureListener {

        ImageView ivIcon;
        View viewLine;
        GestureDetector mGestureDetector;
        public JigHolder(View view) {
            super(view);
            this.viewLine = itemView.findViewById(R.id.jig_hori_layout);
            this.ivIcon = itemView.findViewById(R.id.recycle_jigsaw);
            mGestureDetector = new GestureDetector(itemView.getContext(), this);
            itemView.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mGestureDetector.onTouchEvent(event);
            return true;
        }

        @Override
        public boolean onDown(@NonNull MotionEvent e) {
//            if (oneItemSelected)
//                return true;
//            oneItemSelected = true;
            Log.w("r65 on down","oneItemSelected "+oneItemSelected);
            return false;   // true or false?
        }
        @Override
        public void onShowPress(@NonNull MotionEvent e) {
//            mTouchHelper.startDrag(this);
            Log.w("r27", "Touch onShowPress ");

        }

        @Override
        public boolean onSingleTapUp(@NonNull MotionEvent e) {
            Log.w("r28 on single tab up", "Touch ");
            return true;
        }

        long drawTime;
        @Override
        public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
            long nowTime = System.currentTimeMillis();
            if (nowTime < drawTime + 400)
                return false;
            drawTime = nowTime;

//            Log.w("r14 adapter onScroll","distX="+distanceX+", distY="+distanceY+ "e1 X="+e1.getX()+" e1Y="+e1.getY());
            Log.w("r14", " onScroll");
            return false;
        }


        @Override
        public void onLongPress(@NonNull MotionEvent e) {

            Log.w("rlp ", "on long pressed");
            oneItemSelected = true;
            add2FloatingQue();
            // e.getAction() = ACTION_DOWN
//            mTouchHelper.startDrag(this);
//            Log.w("r16 adaptor onLong"," X "+e.getX()+" Y "+e.getY());
            /* e.getX(), e.getY() means relative position within this item */
        }

        private void add2FloatingQue() {

            jigTables[nowC][nowR].posX = jPosX;
            jigTables[nowC][nowR].posY = jPosY;

            FloatPiece fp = new FloatPiece();
            fp.C = nowC; fp.R = nowR;
            fp.bitmap = jigTables[nowC][nowR].oLine;
            fp.bigMap = piece.makeBigger(fp.bitmap);
            fp.justMoved = true;
            fPs.add(fp);
//            recyclerJigs.remove(jigRecyclePos);
//            jigRecycleAdapter.notifyItemRemoved(jigRecyclePos);

            Log.w("p22 moved "+jigRecyclePos,"item moved to paint "+nowC+"x"+nowR);
            shouldBeMoved = false;
            //        updateViewHandler.sendEmptyMessage(0);
        }



        @Override
        public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull JigHolder viewHolder, int position) {

        jigCR = recyclerJigs.get(position);
        jigX = jigCR / 10000;
        jigY = jigCR - jigX * 10000;
        if (jigTables[jigX][jigY].src == null)
            piece.makeAll(jigX, jigY);
        viewHolder.ivIcon.setImageBitmap(jigTables[jigX][jigY].oLine);
        viewHolder.ivIcon.setTag(jigCR);
    }

    @Override
    public int getItemCount() {
        return (recyclerJigs.size());
    }

    public void setTouchHelper(ItemTouchHelper tHelper){
        this.mTouchHelper = tHelper;
    }

}
