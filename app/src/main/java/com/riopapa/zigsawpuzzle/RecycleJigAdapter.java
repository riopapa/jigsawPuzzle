package com.riopapa.zigsawpuzzle;

import static com.riopapa.zigsawpuzzle.MainActivity.jigImageSize;
import static com.riopapa.zigsawpuzzle.MainActivity.jigPos;
import static com.riopapa.zigsawpuzzle.MainActivity.nw;
import static com.riopapa.zigsawpuzzle.MainActivity.pieceBitmap;
import static com.riopapa.zigsawpuzzle.MainActivity.recyclerJigs;
import static com.riopapa.zigsawpuzzle.MainActivity.zigInfo;

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

public class RecycleJigAdapter extends RecyclerView.Adapter<RecycleJigAdapter.JigHolder>
        implements ZItemTouchHelperAdapter {

    int jigX, jigY;
    private ItemTouchHelper mTouchHelper;
    @NonNull
    @Override
    public JigHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycle_zigsaw, viewGroup, false);
        view.getLayoutParams().width = nw + 8;
        view.getLayoutParams().height = nw + 8;
        ImageView iv = view.findViewById(R.id.re_zigsaw);
        iv.getLayoutParams().height = nw;
        iv.getLayoutParams().width = nw;
        iv.requestLayout();
        return new JigHolder(view);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {

    }

    @Override
    public void onItemSwiped(int position) {

        Log.w("Recycler onItemSwiped", "position = "+position);

    }

    public class JigHolder extends RecyclerView.ViewHolder implements View.OnTouchListener,
            GestureDetector.OnGestureListener {

        ImageView ivIcon;
        View viewLine;
        GestureDetector mGestureDetector;
        public JigHolder(View view) {
            super(view);
            this.viewLine = itemView.findViewById(R.id.zig_hori_layout);
            this.ivIcon = itemView.findViewById(R.id.re_zigsaw);
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
            Log.w("adaptor onDown", "Touch Down ");

            return false;
        }
        @Override
        public void onShowPress(@NonNull MotionEvent e) {
            Log.w("adaptor onShowPress", "Touch onShowPress ");

        }

        @Override
        public boolean onSingleTapUp(@NonNull MotionEvent e) {
            Log.w("onSingleTapUp", "onSingleTapUp UP");
            return false;
        }

        long drawTime;
        @Override
        public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
            long nowTime = System.currentTimeMillis();
            if (nowTime < drawTime + 300)
                return false;
            drawTime = nowTime;

            Log.w("adapter onScroll","distX="+distanceX+", distY="+distanceY+ "e1 X="+e1.getX()+" e1Y="+e1.getY());
            return false;
        }

        @Override
        public void onLongPress(@NonNull MotionEvent e) {
            mTouchHelper.startDrag(this);
            Log.w("adaptor onLong", "pressed getActionIndex "+e.getActionIndex()
                    + " X "+e.getX()+" Y "+e.getY()
            );
        }

        @Override
        public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull JigHolder viewHolder, int position) {

        jigPos = recyclerJigs.get(position);
        jigX = jigPos / 10000;
        jigY = jigPos - jigX * 10000;
        ZigInfo z = zigInfo[jigX][jigY];
        if (z.src == null)
            pieceBitmap.make(jigX, jigY);
        viewHolder.ivIcon.setImageBitmap(zigInfo[jigX][jigY].oLine);
        viewHolder.ivIcon.setTag(jigPos);
//        viewHolder.ivIcon.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public int getItemCount() {
        return (recyclerJigs.size());
    }

    public void setTouchHelper(ItemTouchHelper tHelper){
        this.mTouchHelper = tHelper;
    }

}
