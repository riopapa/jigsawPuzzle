package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.MainActivity.jigC00R;
import static com.riopapa.jigsawpuzzle.MainActivity.jigTables;
import static com.riopapa.jigsawpuzzle.MainActivity.recySize;
import static com.riopapa.jigsawpuzzle.MainActivity.piece;
import static com.riopapa.jigsawpuzzle.MainActivity.recyclerJigs;

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

import com.riopapa.jigsawpuzzle.model.JigTable;

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
    public void onItemMove(int fromPosition, int toPosition) {

    }

    @Override
    public void onItemSwiped(int position) {

        Log.w("r13 Recycler onItemSwiped", "position = "+position);

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
            Log.w("r22 adaptor onShowPress", "Touch onShowPress ");

        }

        @Override
        public boolean onSingleTapUp(@NonNull MotionEvent e) {
            Log.w("r15 onSingleTapUp", "onSingleTapUp UP");
            return false;
        }

        long drawTime;
        @Override
        public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
            long nowTime = System.currentTimeMillis();
            if (nowTime < drawTime + 400)
                return false;
            drawTime = nowTime;

            Log.w("r14 adapter onScroll","distX="+distanceX+", distY="+distanceY+ "e1 X="+e1.getX()+" e1Y="+e1.getY());
            return false;
        }

        @Override
        public void onLongPress(@NonNull MotionEvent e) {
            mTouchHelper.startDrag(this);
            Log.w("r16 adaptor onLong", "getAction  "+e.getAction()
                    +" toString "+e.toString()
                    + " X "+e.getX()+" Y "+e.getY());
            /* e.getX(), e.getY() means relative position within this item */
        }

        @Override
        public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull JigHolder viewHolder, int position) {

        jigC00R = recyclerJigs.get(position);
        jigX = jigC00R / 10000;
        jigY = jigC00R - jigX * 10000;
        JigTable z = jigTables[jigX][jigY];
        if (z.src == null)
            piece.makeAll(jigX, jigY);
        viewHolder.ivIcon.setImageBitmap(jigTables[jigX][jigY].oLine);
        viewHolder.ivIcon.setTag(jigC00R);
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
