package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.rnd;
import static com.riopapa.jigsawpuzzle.ActivityMain.mContext;
import static com.riopapa.jigsawpuzzle.Vars.picOSize;
import static com.riopapa.jigsawpuzzle.Vars.possibleImageCount;
import static com.riopapa.jigsawpuzzle.Vars.selectedHeight;
import static com.riopapa.jigsawpuzzle.Vars.selectedImage;
import static com.riopapa.jigsawpuzzle.Vars.selectedImageNbr;
import static com.riopapa.jigsawpuzzle.Vars.selectedWidth;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.riopapa.jigsawpuzzle.func.TargetImage;

public class ImageSelAdapter extends RecyclerView.Adapter<ImageSelAdapter.ViewHolder> {

    static Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iVImage;
        TextView tVInfo;

        ViewHolder(final View itemView) {
            super(itemView);

            iVImage = itemView.findViewById(R.id.image);
            iVImage.setOnClickListener(view -> {
                selectedImageNbr = getBindingAdapterPosition();
                selectedImage = new TargetImage().get(selectedImageNbr);
                selectedWidth = selectedImage.getWidth();
                selectedHeight = selectedImage.getHeight();

                Intent intent = new Intent(context, ActivityJigsaw.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            });
            tVInfo = itemView.findViewById(R.id.info);
        }

    }

    @Override
    public int getItemCount() {
        context = mContext;
        return possibleImageCount;
    }


    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_images, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Bitmap oMap = new TargetImage().get(position);
        Bitmap bitmap = Bitmap.createScaledBitmap(oMap, oMap.getWidth()/5, oMap.getHeight()/5, true);

        holder.iVImage.setImageBitmap(bitmap);
        holder.tVInfo.setText(" "+position);
    }
}
