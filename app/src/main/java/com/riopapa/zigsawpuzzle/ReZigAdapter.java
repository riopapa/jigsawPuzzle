package com.riopapa.zigsawpuzzle;

import static com.riopapa.zigsawpuzzle.MainActivity.reZigs;
import static com.riopapa.zigsawpuzzle.MainActivity.zigInfo;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReZigAdapter extends RecyclerView.Adapter<ReZigAdapter.ZigHolder>  {

    public ReZigAdapter(ArrayList<Integer> reZigs) {
    }

    static class ZigHolder extends RecyclerView.ViewHolder {

        ImageView ivIcon;
        View viewLine;

        ZigHolder(View view) {
            super(view);
            this.viewLine = itemView.findViewById(R.id.zig_hori_layout);
            this.ivIcon = itemView.findViewById(R.id.re_zigsaw);
            this.viewLine.setOnClickListener(view1 -> {
//                typeNumber = getAbsoluteAdapterPosition();
//                ImageView iv = mActivity.findViewById(R.id.re_zigsaw);
//                iv.setImageBitmap(utils.maskedIcon(typeIcons[typeNumber]));
//                placeHandler.sendEmptyMessage(0);
            });
        }
    }

    @NonNull
    @Override
    public ZigHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycle_zigsaw, viewGroup, false);
        return new ZigHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ZigHolder viewHolder, int position) {

        int x = reZigs.get(position) / 1000;
        int y = reZigs.get(position) - x * 1000;
        viewHolder.ivIcon.setImageBitmap(zigInfo[x][y].src);
        viewHolder.ivIcon.setTag(reZigs.get(position));
    }

    @Override
    public int getItemCount() {
        return (reZigs.size());
    }

}
