package com.riopapa.zigsawpuzzle;

import static com.riopapa.zigsawpuzzle.MainActivity.jigX00Y;
import static com.riopapa.zigsawpuzzle.MainActivity.jigTables;
import static com.riopapa.zigsawpuzzle.MainActivity.dipSize;
import static com.riopapa.zigsawpuzzle.MainActivity.piece;
import static com.riopapa.zigsawpuzzle.MainActivity.recyclerJigs;

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

import com.riopapa.zigsawpuzzle.model.JigTable;

public class RecycleJigAdapter extends RecyclerView.Adapter<RecycleJigAdapter.JigHolder>
        implements ZItemTouchHelperAdapter {

    int jigX, jigY;
    private ItemTouchHelper mTouchHelper;
    @NonNull
    @Override
    public JigHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycle_zigsaw, viewGroup, false);
        view.getLayoutParams().width = dipSize;
        view.getLayoutParams().height = dipSize;
        ImageView iv = view.findViewById(R.id.recycle_jigsaw);
        iv.getLayoutParams().height = dipSize;
        iv.getLayoutParams().width = dipSize;
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

        jigX00Y = recyclerJigs.get(position);
        jigX = jigX00Y / 10000;
        jigY = jigX00Y - jigX * 10000;
        JigTable z = jigTables[jigX][jigY];
        if (z.src == null)
            piece.make(jigX, jigY);
        viewHolder.ivIcon.setImageBitmap(jigTables[jigX][jigY].oLine);
        viewHolder.ivIcon.setTag(jigX00Y);
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
