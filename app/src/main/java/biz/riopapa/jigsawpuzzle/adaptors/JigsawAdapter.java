package biz.riopapa.jigsawpuzzle.adaptors;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.activeJigs;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.itemCR;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigOLine;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigPic;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.moving;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ActivityMain.mContext;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import biz.riopapa.jigsawpuzzle.ItemMoveCallback;
import biz.riopapa.jigsawpuzzle.R;
import biz.riopapa.jigsawpuzzle.func.PieceAlign;
import biz.riopapa.jigsawpuzzle.func.PieceLock;
import biz.riopapa.jigsawpuzzle.images.PieceImage;

public class JigsawAdapter extends RecyclerView.Adapter<JigsawAdapter.MyViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {


    PieceAlign pieceAlign;
    PieceLock pieceLock;
    PieceImage pieceImage;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivIcon;
        View viewLine;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.viewLine = itemView.findViewById(R.id.piece_layout);
            this.ivIcon = itemView.findViewById(R.id.recycler_jigsaw);

        }
    }

    public JigsawAdapter() {}

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_jigsaw, parent, false);
        view.getLayoutParams().width = gVal.recSize;
        view.getLayoutParams().height = gVal.recSize;
        ImageView iv = view.findViewById(R.id.recycler_jigsaw);
        iv.getLayoutParams().height = gVal.picOSize;
        iv.getLayoutParams().width = gVal.picOSize;
        iv.requestLayout();
        pieceAlign = new PieceAlign();
        pieceLock = new PieceLock();
        pieceImage = new PieceImage(mContext, gVal.imgOutSize, gVal.imgInSize);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        itemCR = activeJigs.get(position) - 10000;
        int cc = itemCR / 100;
        int rr = itemCR - cc * 100;
        //        Log.w("onBindViewHolder "+position,jigC+"x"+jigR);
        if (jigPic[cc][rr] == null)
            jigPic[cc][rr] = pieceImage.makePic(cc, rr);

        if (jigOLine[cc][rr] == null)
            jigOLine[cc][rr] = pieceImage.makeOline(jigPic[cc][rr], cc, rr);

        holder.ivIcon.setImageBitmap(jigOLine[cc][rr]);
        holder.ivIcon.setTag(itemCR);
    }


    @Override
    public int getItemCount() {
        return activeJigs.size();
    }


    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(activeJigs, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(activeJigs, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(MyViewHolder myViewHolder) {}
//
//    @Override
//    public void onRowSelected(MyViewHolder myViewHolder) {
//        Log.w("onRowSelected", "getPosition "+myViewHolder.getPosition());
//        Log.w("onRowSelected", "getLeft "+myViewHolder.itemView.getLeft());
//        myViewHolder.itemView.setBackgroundColor(Color.GRAY);
//
//    }

    @Override
    public void onRowClear(MyViewHolder myViewHolder) {

        if (myViewHolder.getAbsoluteAdapterPosition() != -1) {
            Log.w("onRowClear"," pos ="+myViewHolder.getAbsoluteAdapterPosition());
            if (!moving) {
                final String tag1;
                final MyViewHolder svHolder = myViewHolder;
                svHolder.itemView.setAlpha(0);
                tag1 = svHolder.ivIcon.getTag().toString();
                new Timer().schedule(new TimerTask() {
                    public void run() {
                        String tag2 = svHolder.ivIcon.getTag().toString();
                        Log.w("onRowClear " + tag1.equals(tag2), "tag " + tag1 + " vs " + tag2);
                        svHolder.itemView.setAlpha(1);
                    }
                }, 10);
            }
        }
    }
}
